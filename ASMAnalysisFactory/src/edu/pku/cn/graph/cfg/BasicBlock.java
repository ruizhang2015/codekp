/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-22 ����12:47:26
 * @modifier: Liuxizhiyi
 * @time 2008-5-22 ����12:47:26
 * @reviewer: Liuxizhiyi
 * @time 2008-5-22 ����12:47:26
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.BasicVertex;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.CaughtExceptionRef;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LabelStmt;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.Stmt;

/**
 * 
 * @author Liuxizhiyi
 */
public class BasicBlock extends BasicVertex<Edge, BasicBlock> {

	// private List<Node> node=null;
	private int startInc = 0;
	private int endInc = 0;
	protected Frame[] frames;
	protected InsnList insnList;

	public int startStmt = 0;
	public int endStmt = 0;

	public int startPPG = 0;
	public int endPPG = 0;

	public List<Stmt> stmts;

	// added by guangtai Liang @2011-12-29
	public int startSrcLine = 0;
	public int endSrcLine = 0;

	public int getStartSrcLine() {
		if (this.startSrcLine == 0) {
			setSrcLines();
		}
		return this.startSrcLine;
	}

	public int getEndSrcLine() {
		if (this.endSrcLine == 0) {
			setSrcLines();
		}
		return this.endSrcLine;
	}

	private void setSrcLines() {
		boolean beginLine = true;
		for (int i = this.startStmt; i <= this.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			if (stmt instanceof LineStmt) {
				LineStmt lineStmt = (LineStmt) stmt;
				if (beginLine == true) {
					this.startSrcLine = lineStmt.line;
					beginLine = false;
				}
				this.endSrcLine = lineStmt.line;
			}
		}
	}

	public void printBlockBasicInfo() {
		System.out.println("Block ID: " + this.getLabel());
	}

	public void printBlockStructure() {
		System.out.println(this.toString());
	}

	public void printBlockStmts() {
		System.out.println("	  SrcLine [" + this.startSrcLine + "," + this.endSrcLine + "]");
		for (int m = this.startStmt; m <= this.endStmt; m++) {
			System.out.println("	" + m + ": " + this.stmts.get(m).toString());
		}
		System.out.println("");
	}

	public void printBlockFullInfo() {
		this.printBlockBasicInfo();
		this.printBlockStructure();
		this.printBlockStmts();
	}

	// end of the addition

	protected BasicBlock() {
		// node=new LinkedList<Node>();
		type = BlockType.NORMAL_BLOCK;
	}

	protected BasicBlock(int start) {
		type = BlockType.NORMAL_BLOCK;
		startInc = start;
		endInc = start;
	}

	protected BasicBlock(int start, int end) {
		type = BlockType.NORMAL_BLOCK;
		startInc = start;
		endInc = end;
	}

	// public BasicBlock(BasicBlock block){
	// super(block, block.type);
	// frameList.addAll(block.frameList);
	// // insnList.addAll(block.insnList);
	// setLabel(block.getLabel());
	// setStartInc(block.getStartInc());
	// setEndInc(block.getEndInc());
	// }

	protected BasicBlock(BasicBlock block, int type) {
		super(block, type);
		insnList = block.insnList;
		setLabel(block.getLabel());
		setStartInc(block.getStartInc());
		setEndInc(block.getEndInc());
	}

	public int getStartInc() {
		return startInc;
	}

	public void setStartInc(int inc) {
		startInc = inc;
	}

	public int getEndInc() {
		return endInc;
	}

	public void setEndInc(int inc) {
		endInc = inc;
	}

	public Frame getFrame(int index) {
		return frames[index];
	}

	// public void addNode(AbstractInsnNode insn, Frame frame) {
	// insnList.add(insn);
	// }

	public InsnList getInsnList() {
		return insnList;
	}

	@Override
	public int compareTo(BasicBlock o) {
		// TODO Auto-generated method stub
		return super.compareTo(o);
	}

	/**
	 * return whether this block has method invocation statements
	 * 
	 * @return
	 */
	public boolean hasInvokeStmt() {
		for (int i = this.startStmt; i <= this.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			if (stmt instanceof InvokeStmt) {
				return true;
			}
			if (stmt instanceof AssignStmt) {
				JIRValue jir = ((AssignStmt) stmt).getRight();
				if (jir instanceof InvokeExpr) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * return the ClassNode List representing exceptions thrown from method
	 * invocations of this block
	 * 
	 * @return
	 */
	public ArrayList<ClassNode> getExceptions() {
		ArrayList<ClassNode> exceptions = new ArrayList<ClassNode>();

		for (int i = this.startStmt; i <= this.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			if (stmt instanceof InvokeStmt) {
				InvokeExpr expr = (InvokeExpr) ((InvokeStmt) stmt).getInvoke();
				MethodNode mn = expr.getMethodNode();
				if (mn != null) {
					exceptions.addAll(mn.getExceptionNodes());
				} else {
					System.out.println("can't find method: " + expr.getMethodNodeName());
				}

			}

			if (stmt instanceof AssignStmt) {
				JIRValue jir = ((AssignStmt) stmt).getRight();
				if (jir instanceof InvokeExpr) {
					MethodNode mn = ((InvokeExpr) jir).getMethodNode();
					if (mn != null)
						exceptions.addAll(mn.getExceptionNodes());
				}
			}
		}

		return exceptions;
	}

	/**
	 * For catch-block, returns the classNode of the exception that this block
	 * handles; For other blocks, returns null
	 * 
	 * @return
	 */
	public ClassNode handleException() {

		Repository cha = Repository.getInstance();

		for (int i = this.startStmt; i <= this.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			if (stmt instanceof AssignStmt) {
				if (((AssignStmt) stmt).getRight() instanceof CaughtExceptionRef) {
					String classNm = ((AssignStmt) stmt).getLeft().getType().getInternalName();
					ClassNode handleCn = cha.findClassNode(classNm);
					if (handleCn != null)
						return handleCn;
					else {
						System.out.println("can't find handle exception class:" + classNm);
					}
				}
			}
		}

		return null;

	}

	public String toString() {

		StringBuffer buf = new StringBuffer();
		buf.append("Block ").append(this.getLabel()).append("\n");
		buf.append(super.toString());
		// buf.append("from ").append(startInc).append(" to ").append(endInc);
		// for(int i=0;i<insnList.size();i++){
		// int len=buf.length();
		// buf.append(startInc+i).append(" ").append(insnList.get(i));
		// len=9-(buf.length()-len)/8;
		// while(len-->0)buf.append("\t");
		// buf.append(frameList.get(i).toString());
		// buf.append("\n");
		// }
		// buf.append("
		// Stmt:[").append(startStmt).append(",").append(endStmt).append("]");
		try {
			for (int i = this.startStmt; i <= this.endStmt; i++) {
				Stmt stmt = this.stmts.get(i);
				if (stmt instanceof InvokeStmt)
					buf.append(stmt.toString());
				if (stmt instanceof AssignStmt)
					buf.append(stmt.toString());
			}
		} catch (Exception e) {
			System.out.println("[!!Exception Caught]" + e);
			e.printStackTrace();
		}
		return buf.toString();
	}

	// public String toString(){
	//
	// StringBuffer buf = new StringBuffer();
	// buf.append("Block ").append(this.getLabel()).append(" Type:
	// ").append(BlockType.Type[getType()]).append("\n");
	// buf.append(super.toString());
	// buf.append("from ").append(startInc).append(" to ").append(endInc);
	// // for(int i=0;i<insnList.size();i++){
	// // int len=buf.length();
	// // buf.append(startInc+i).append(" ").append(insnList.get(i));
	// // len=9-(buf.length()-len)/8;
	// // while(len-->0)buf.append("\t");
	// // buf.append(frameList.get(i).toString());
	// // buf.append("\n");
	// // }
	// buf.append("Stmt:[").append(startStmt).append(",").append(endStmt).append("]");
	// return buf.toString();
	//
	// // StringBuffer buf = new StringBuffer();
	// // for(int i=this.startStmt;i<=this.endStmt;i++){
	// // Stmt stmt=this.stmts.get(i);
	// // if(stmt instanceof InvokeStmt||(stmt instanceof
	// AssignStmt&&((AssignStmt)stmt).getRight() instanceof InvokeExpr))
	// // buf.append(stmt.toString());
	// //
	// // }
	// // return buf.toString();
	// }
	/**
	 * @see edu.pku.cn.graph.BasicVertex#getValue(int)
	 */
	@Override
	@Deprecated
	public Frame getValue(int index) {
		// TODO Auto-generated method stub
		return null;// frameList.get(index);
	}

	private final class InsnListIterator implements Iterator<AbstractInsnNode> {
		int index;
		int step;
		int start;
		int end;

		public InsnListIterator(int step) {
			this.start = startInc;
			this.end = endInc;
			this.step = step;
			index = step > 0 ? start : end;
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			index += step;
			return index >= start && index <= end;
		}

		@Override
		public AbstractInsnNode next() {
			// TODO Auto-generated method stub
			return insnList.get(index);
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
		}

	}

	/**
	 * @see edu.pku.cn.graph.BasicVertex#nodeIterator()
	 */
	@Override
	public Iterator<AbstractInsnNode> nodeIterator(boolean reverse) {
		if (reverse)
			return new InsnListIterator(-1);
		return new InsnListIterator(1);
	}

	public List<BasicBlock> getIncomingExceptionBlocks() {
		List<BasicBlock> backwardBlocks = new ArrayList<BasicBlock>();
		Iterator<Edge> inEdges = this.inEdgeIterator();
		while (inEdges.hasNext()) {
			Edge inEdge = inEdges.next();
			if (EdgeType.Type[inEdge.getType()].equals("UNHANDLED_EXCEPTION_EDGE")
					|| EdgeType.Type[inEdge.getType()].equals("HANDLED_EXCEPTION_EDGE")) {
				backwardBlocks.add(inEdge.getSource());
				break;
			}
		}
		return backwardBlocks;
	}

	public Integer getExceptionalObjectCreationStmt() {
		Integer stmtIndex = this.endStmt;
		Stmt lastStmt = this.stmts.get(this.endStmt);

		if (lastStmt != null && lastStmt instanceof AssignStmt) {
			return stmtIndex;
		}

		stmtIndex = this.endStmt - 1;
		lastStmt = this.stmts.get(stmtIndex);
		if (stmtIndex >= this.startStmt && lastStmt != null && lastStmt instanceof AssignStmt) {
			return stmtIndex;
		}
		return null;
	}

	public BasicBlock getItsTrueBlock() {
		Iterator<Edge> outEdges = this.outEdgeIterator();
		while (outEdges.hasNext()) {
			Edge outEdge = outEdges.next();
			if (EdgeType.Type[outEdge.getType()].equals("IFCMP_EDGE_JUMP")) {
				return outEdge.getTarget();
			}
		}
		return null;
	}

	public boolean isReturnBlock() {
		for (int i = this.startStmt; i <= this.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			if (stmt instanceof ReturnStmt) {
				return true;
			}
		}
		return false;
	}

	public BasicBlock getItsFalseBlock() {
		Iterator<Edge> outEdges = this.outEdgeIterator();
		while (outEdges.hasNext()) {
			Edge outEdge = outEdges.next();
			if (EdgeType.Type[outEdge.getType()].equals("IFCMP_EDGE_FALLTHROUGH")) {
				return outEdge.getTarget();
			}
		}
		return null;
	}
}

// end
