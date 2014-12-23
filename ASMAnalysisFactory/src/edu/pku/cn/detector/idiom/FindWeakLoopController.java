/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-4 ����08:40:44
 * @modifier: Administrator
 * @time 2009-3-4 ����08:40:44
 * @reviewer: Administrator
 * @time 2009-3-4 ����08:40:44
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector.idiom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.MethodDetector;
import edu.pku.cn.util.OpcodeUtil;

/**
 * Find weak loop controller. For example, for(int i = 0; i < n; i++) { for(int
 * j = 0; i < n; i ++) //����������j { j = j + i; } } ���� for(int i = 0; i < n;
 * i++) { for(int j = 0; j < n; i ++)//����������j { j = j + i; } }
 * ��Ӧ��insn����Ϊ XSTORE m LABEL GOTO n g: . IINC m . n: LABEL FRAME XLOAD x
 * certain insn //�����invokeXXXָ�����Դ�ƥ�䣬��Ϊ��ʱ���ܻ�ƥ��for(it =
 * words.Iterator; it.hasNext;){word = it.next()}����ѭ�� IFXXX g
 * 
 * @author Administrator
 */
public class FindWeakLoopController extends MethodDetector {

	private boolean DEBUG = false;

	private Set<Integer> indexSet = new HashSet<Integer>();

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new FindWeakLoopController());
		return detectors;
	}

	@Override
	public void visitCode() {
		super.visitCode();
		InsnList insns = node.instructions;
		int listSize = insns.size();
		if (DEBUG)
			OpcodeUtil.printInsnList(insns, name);

		int gotoNext = -1; // ��¼goto����һ��insn֮index
		int target = -1; // ��¼goto��Ŀ��insn֮index
		int var = -1; // ��¼ѭ�����Ʊ���֮var
		int back = -1; // ��¼ifXXX��Ŀ��insn֮index
		boolean hasIinc = false;// ��¼�Ƿ����iinc
		boolean successMatch = false; // ��¼�Ƿ�ɹ�ƥ��iinc֮������ѭ�����Ʊ���
		indexSet.clear();// �������п����������ѭ�����Ʊ�����Ӧ��XSTORE insn֮index

		for (int i = 0; i < listSize; i++) {
			AbstractInsnNode storeNode = insns.get(i);
			if (storeNode instanceof VarInsnNode && storeNode.getOpcode() >= Opcodes.ISTORE
					&& storeNode.getOpcode() <= Opcodes.ASTORE) {// this is an
				// XSTORE insn
				var = ((VarInsnNode) storeNode).var;

				AbstractInsnNode gotoNode = insns.get(i + 2);// ���GOTO��STORE֮���LABEL
				if (gotoNode instanceof JumpInsnNode && gotoNode.getOpcode() == Opcodes.GOTO) { // this
																								// is
																								// a
					// GOTO insn
					target = ((JumpInsnNode) gotoNode).label.index;
					gotoNext = gotoNode.index + 1;

					AbstractInsnNode loadNode = insns.get(target + 2);// ���target����LABEL����һ��insn��Ӧ��FRAME
					if (loadNode instanceof VarInsnNode && loadNode.getOpcode() >= Opcodes.ILOAD
							&& loadNode.getOpcode() <= Opcodes.ALOAD) { // this
						// is a
						// LOAD
						// insn
						AbstractInsnNode invokeNode = insns.get(target + 3);

						if (invokeNode.getOpcode() >= Opcodes.INVOKEVIRTUAL
								&& invokeNode.getOpcode() <= Opcodes.INVOKEINTERFACE)
							return; // ����˴�ƥ�䣬��Ϊ���п����Ƕ�Iterator��ƥ��

						AbstractInsnNode compareNode = insns.get(target + 4);// ���һ��ָ��
						if (compareNode instanceof JumpInsnNode && compareNode.getOpcode() != Opcodes.GOTO
								&& compareNode.getOpcode() != Opcodes.JSR) {// ����IFXXXϵ�е�insn
							back = ((JumpInsnNode) compareNode).label.index;

							if (back == gotoNext) {// ����һ��ѭ���ṹ
								hasIinc = false;
								successMatch = false;
								for (int j = gotoNext; j < target; j++) {
									if (insns.get(j) instanceof IincInsnNode) {
										hasIinc = true;
										if (var == ((IincInsnNode) insns.get(j)).var) {
											successMatch = true;
											break;
										}
									}
								}
								if (hasIinc && !successMatch) {// ����������������ѭ�����Ʊ���
									indexSet.add(storeNode.index);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void visitVarInsn(final int opcode, final int var) {
		super.visitVarInsn(opcode, var);
		if (indexSet.contains(this.currInsnNumber)) {// �ڱ�ָ�����漰���Ŀ��Ʊ����п���û������
			BugInstance instance = new BugInstance(BugInstance.format(getBugPattern("WEAK_LOOP_CONTROLLER")
					.getLongDescription(), new String[] {}), getLineNumber());
			reportor.report(node.owner, instance);
			indexSet.remove(this.currInsnNumber);
		}
	}

}

// end
