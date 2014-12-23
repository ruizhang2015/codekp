package autoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.MethodDetector;

import automachine.*;

public class AutoMethodAdapter extends MethodDetector {

	/*
	 * edit by zhouzhiyi. add automachine to detect field in class
	 */
	HashMap<String, AutoMachine> fieldMachine;

	/*
	 * sample Automachine to record some flag endMeanViolateDefect needCFG
	 * allOpcodePushOjectAutomachine onlyConcernClass
	 */
	private boolean Debug = false;
	AutoMachine sampleAutomachine;

	String specificFile;
	// database id
	int automachineId;

	// the index of
	// int topStackIndex=-1;
	// these 15 functions will add instruction in MethodNode.java
	// visitFrame, visitInsn, visitIntInsn, visitVarInsn,
	// visitTypeInsn, visitFieldInsn, visitMethodInsn,
	// visitJumpInsn, visitLabel, visitLdcInsn, visitIincInsn
	// visitTableSwitchInsn, visitLookupSwitchInsn, visitMultiANewArayInsn
	// visitLineNumber
	// start from 0
	int currentInstructionNumber;
	Frame[] instructionFrame;
	HashMap<Integer, Integer> pcIndexMap = new HashMap<Integer, Integer>();

	// map: index of local stack to the object's state
	HashMap<Integer, AutoMachine> indexAutoMachineMap = new HashMap<Integer, AutoMachine>();

	// opcode sequence not concern objects
	// opcode sequence in indexStateMap
	static final int opcodeSequenceIndex = -1;

	Edge preEdge = null;
	boolean needCFG;

	public void setNeedCFG(boolean need) {
		this.needCFG = need;
	}

	// private static ArrayList<Detector> detectors=null;
	// add by zhouzhiyi. clear the state of automachineadapter
	public void clear() {
		fieldMachine.clear();
		// sampleAutomachine.initial();
		instructionFrame = null;
		// pcIndexMap.clear();
		// indexAutoMachineMap.clear();
		// preEdge=null;
	}

	synchronized AutoMachine addAutoMachine(int varAllIndex, String name) {
		if (!indexAutoMachineMap.containsKey(varAllIndex)) {
			// indexAutoMachineMap.put(varAllIndex,
			// AutomaUtil.generateAutoMachineFromXML(this.specificFile));
			// indexAutoMachineMap.put(varAllIndex,
			// AutomaUtil.generateOneAutoMachineFromDatabase(this.automachineId));
			AutoMachine am = sampleAutomachine.getCopy();
			am.automachineName = name;
			indexAutoMachineMap.put(varAllIndex, am);
		}

		return indexAutoMachineMap.get(varAllIndex);
	}

	public void allGoOneStep(Edge e, int currentLine) throws AutoMachineException {
		// if(this.specificFile.indexOf("detector_i2d_ceilround")>0)
		// System.out.println("specfile="+this.specificFile+" "+this.needCFG+this.sampleAutomachine.isNeedCFG());
		// System.out.println(e);
		// System.out.println("line="+currentLine+" method="+this.cfg.getMethod().name+"()");
		// System.out.println("\n\n");

		if (this.sampleAutomachine.isAllOpcodePushOjectAutomachine()) {

			for (AutoMachine am : indexAutoMachineMap.values()) {
				am.goOneStep(e, currentLine);
			}
		} else {
			// System.out.println(this.isNeedCFG());
			if (this.sampleAutomachine.isNeedCFG()) {
				if (e instanceof VisitMethodInsnEdge) {
					VisitMethodInsnEdge me = (VisitMethodInsnEdge) e;
					if (Debug)
						System.out.println("line=" + currentLine + " method=" + node.name + "()");

					int paramlen = Type.getArgumentTypes(me.getDesc()).length;
					if (true)
						System.out.println("invoke method:" + me.getOwner() + me.getName() + " " + me.getDesc() + " "
								+ paramlen);
					Frame currentFrame = this.instructionFrame[this.currentInstructionNumber];
					if (currentFrame != null) {
						int currentStackSize = currentFrame.getStackSize();
						int objectPc = this.currentInstructionNumber;
						while (objectPc >= 0
								&& this.instructionFrame[objectPc].getStackSize() >= currentStackSize - paramlen)
							objectPc--;
						if (objectPc >= 0 && this.pcIndexMap.get(objectPc) != null) {
							if (Debug) {
								// System.out.println("go one step in ");
								System.out.println("line=" + currentLine + " method=" + node.getCFG().getMethod().name
										+ "()");
								System.out.println("invoke method:" + me.getOwner() + me.getName() + " " + me.getDesc()
										+ " " + paramlen);
							}
							int objectPcIndex = this.pcIndexMap.get(objectPc);

							LocalVariableNode lvn = node.getCFG().localVariableTable.getNode(objectPc, objectPcIndex);
							if (lvn != null) {
								int objectAllIndex = lvn.allIndex;
								if (indexAutoMachineMap.containsKey(objectAllIndex))
									this.indexAutoMachineMap.get(objectAllIndex).goOneStep(e, currentLine);
							}
						}
					}

				}

			} else {
				this.indexAutoMachineMap.get(opcodeSequenceIndex).goOneStep(e, currentLine);
				// System.out.println(this.specificFile);
				// System.out.println("line="+currentLine+" method="+this.cfg.getMethod().name+"() e:"+e);
			}
		}
		this.preEdge = e;
	}

	public AutoMethodAdapter() {
		super();
	}

	public AutoMethodAdapter(String specificFile) {
		this.specificFile = specificFile;
		this.sampleAutomachine = AutomaUtil.generateAutoMachineFromXML(specificFile);

		this.setNeedCFG(sampleAutomachine.isNeedCFG());
		// System.out.println(this.needCFG);

	}

	public AutoMethodAdapter(int automachineId) {
		this.automachineId = automachineId;
		this.sampleAutomachine = AutomaUtil.generateOneAutoMachineFromDatabase(automachineId);

		this.setNeedCFG(sampleAutomachine.isNeedCFG());
		// System.out.println(this.needCFG);

	}

	public AutoMethodAdapter(AutoMachine am) {
		this.automachineId = am.automachineId;

		this.sampleAutomachine = am;

		this.setNeedCFG(sampleAutomachine.isNeedCFG());
		am.initial();
		// System.out.println(this.needCFG);

	}

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		ArrayList<Detector> detectors;
//		boolean generateFromXml = !System.getProperty("AutoMethodAdapter.usexmltoconfig", "yes").equals("no");
		boolean generateFromXml = true;
		// generate automachines from xml specfic files
		if (generateFromXml) {
			System.out.println("use xml files to config ams");
			detectors = new ArrayList<Detector>();

			String specDir = "AutomachineSpecs";
			File dir = new File(specDir);
			if (dir == null || !dir.exists()) {
				System.out.println(specDir + "not exist!");
			}
			if (!dir.isDirectory()) {
				System.out.println(specDir + "is not a directory!");
			}
			File files[] = dir.listFiles();
			for (File file : files) {
				if (file.getName().toLowerCase().startsWith("detector")) {
					detectors.add(new AutoMethodAdapter(file.getAbsolutePath()));
					if (file.getName().contains("jboss"))
						System.out.println(file.getName());
				}
			}
		}
		// generate automachines form database
		else {
			System.out.println("use database to config ams");

			// if(detectors!=null)
			// return detectors;
			detectors = new ArrayList<Detector>();
			System.out.println("accessing database to generate automachines...");
			ArrayList<AutoMachine> ams = AutomaUtil.generateAllAutoMachineFromDatabase();
			for (AutoMachine am : ams) {
				detectors.add(new AutoMethodAdapter(am));

				if (Debug) {
					System.out.println("generate:");
					System.out.println(am);
				}
			}

			System.out.println("generate " + ams.size() + " automachines.");

		}
		return detectors;
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		// TODO Auto-generated method stub
		return null;
	}

	public AnnotationVisitor visitAnnotationDefault() {
		// TODO Auto-generated method stub
		return null;
	}

	public void visitAttribute(Attribute attr) {
		// TODO Auto-generated method stub

	}

	public void visitCode() {

		this.currentInstructionNumber = -1;
		this.pcIndexMap.clear();
		this.indexAutoMachineMap.clear();
		this.addAutoMachine(opcodeSequenceIndex, "this");
		if (Debug) {
			System.out.println("am: " + this.specificFile);
			System.out.println("method = " + this.name);
		}
		try {
			// this.instructionFrame=new Analyzer(new
			// BasicInterpreter()).analyze(this.cfg.getClass().getName(),
			// this.cfg.getMethod());
			this.instructionFrame = new Analyzer(new BasicInterpreter()).analyze(node.name, node);
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void visitEnd() {
		// TODO Auto-generated method stub
		if (!this.sampleAutomachine.isEndMeanViolateDefect())
			for (AutoMachine am : this.indexAutoMachineMap.values()) {

				try {

					am.checkOutWhenVistEnd();
				} catch (AutoMachineException e) {
					// TODO Auto-generated catch block

					reportAmBug(e.getMessage() + " in method " + this.getName() + "\n", am.getCurrentState()
							.getLastLine());
					// System.out.println(e.getMessage()+" in method "+this.getName()+""+am.getCurrentState().getLastLine());
				}
				am.initial();
			}
	}

	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		try {
			allGoOneStep(new VisitFieldInsnEdge(null, null, opcode, owner, name, desc), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public void visitFrame(int type, int local, Object[] local2, int stack, Object[] stack2) {
		// TODO Auto-generated method stub
		// no opcode
		this.currentInstructionNumber++;
		int opcode = -1;
	}

	public void visitIincInsn(int var, int increment) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		int opcode = Opcodes.IINC;
		try {
			allGoOneStep(new VisitIincInsnEdge(null, null, var, increment), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public void visitInsn(int opcode) {
		// TODO Auto-generated method stub
		// System.out.println("line="+currentLine);
		this.currentInstructionNumber++;
		// edit by zhouzhiyi
		if (opcode >= Opcodes.ALOAD_0 && opcode <= Opcodes.ALOAD_3)
			pcIndexMap.put(currentInstructionNumber - 1, opcode - Opcodes.ALOAD_0);
		else if (opcode >= Opcodes.ASTORE_0 && opcode <= Opcodes.ASTORE_3) {
			pcIndexMap.put(currentInstructionNumber - 1, opcode - Opcodes.ASTORE_0);
			AstoreInsn(opcode - Opcodes.ASTORE_0);
		}
		// edit end
		try {
			allGoOneStep(new VisitInsnEdge(null, null, opcode), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public void visitIntInsn(int opcode, int operand) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		try {
			allGoOneStep(new VisitIntInsnEdge(null, null, opcode, operand), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public void visitJumpInsn(int opcode, Label label) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		try {
			allGoOneStep(new VisitJumpInsnEdge(null, null, opcode, label), currentLine);
		} catch (AutoMachineException e) {
			reportAmBug(e, currentLine);
		}
	}

	private void reportAmBug(AutoMachineException e, int line) {
		reportor.report(getClassName(), new BugInstance(e.getMessage() + " by am:" + this.automachineId, line));
	}

	private void reportAmBug(String bugMsg, int line) {
		reportor.report(getClassName(), new BugInstance(bugMsg + " by am:" + this.automachineId, line));
	}

	public void visitLabel(Label label) {
		// TODO Auto-generated method stub
		// no opcode
		this.currentInstructionNumber++;
		// System.out.println("current label=");
		int opcode = -1;
	}

	public void visitLdcInsn(Object cst) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		int opcode = Opcodes.LDC;
	}

	// public void visitLineNumber(int line, Label start) {
	// TODO Auto-generated method stub

	// }

	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		// TODO Auto-generated method stub
		// no opcode
		int opcode = -1;
		try {
			allGoOneStep(new VisitLocalVariableEdge(null, null, name, desc, signature, start, end, index), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		int opcode = Opcodes.LOOKUPSWITCH;
		try {
			allGoOneStep(new VisitLookupSwitchInsnEdge(null, null, dflt, keys, labels), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	// public void visitMaxs(int maxStack, int maxLocals) {
	// // TODO Auto-generated method stub
	//		
	// }

	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		try {
			// if(sampleAutomachine.isNeedCFG())
			// {
			// int paramlen=Type.getArgumentTypes(desc).length;
			// Frame currentFrame =
			// this.instructionFrame[this.currentInstructionNumber];
			// if (currentFrame!=null) {
			// int currentStackSize = currentFrame.getStackSize();
			// int objectPc = this.currentInstructionNumber;
			// while (objectPc >= 0 &&
			// this.instructionFrame[objectPc].getStackSize() >=
			// currentStackSize- paramlen)
			// objectPc--;
			// if (objectPc >= 0 && this.pcIndexMap.get(objectPc) != null) {
			// int objectPcIndex = this.pcIndexMap.get(objectPc);
			// LocalVariableNode lvn =
			// this.cfg.localVariableTable.getNode(objectPc, objectPcIndex);
			// if (lvn != null) {
			// int objectAllIndex = lvn.allIndex;
			// if (indexAutoMachineMap.containsKey(objectAllIndex)){
			// AutoMachine a=indexAutoMachineMap.get(objectAllIndex);
			// State state=a.currentState;
			// for(Edge e:state.getOutEdges()){
			// if( e instanceof VisitMethodInsnEdge){
			// VisitMethodInsnEdge me=(VisitMethodInsnEdge)e;
			// if(!me.getIsLeftValue()){
			//										
			// }
			// }
			// }
			// }
			// //.goOneStep(e,currentLine);
			// }
			// }
			// }
			// }
			//			
			allGoOneStep(new VisitMethodInsnEdge(null, null, opcode, owner, name, desc), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public void visitMultiANewArrayInsn(String desc, int dims) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		int opcode = Opcodes.MULTIANEWARRAY;
		try {
			allGoOneStep(new VisitMultiANewArrayInsnEdge(null, null, desc, dims), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
		// TODO Auto-generated method stub
		return null;
	}

	public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		int opcode = Opcodes.TABLESWITCH;
		try {
			allGoOneStep(new VisitTableSwitchInsnEdge(null, null, min, max, dflt, labels), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		// TODO Auto-generated method stub
		// no opcode
		int opcode = -1;
		try {
			allGoOneStep(new VisitTryCatchBlockEdge(null, null, start, end, handler, type), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	public void visitTypeInsn(int opcode, String type) {
		// TODO Auto-generated method stub
		// System.out.println("type="+type);
		this.currentInstructionNumber++;
		try {
			allGoOneStep(new VisitTypeInsnEdge(null, null, opcode, type), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	private void AstoreInsn(int var) {
		if (this.preEdge instanceof VisitMethodInsnEdge) {
			VisitMethodInsnEdge vmEdge = (VisitMethodInsnEdge) preEdge;
			if (vmEdge.getOpcode() >= Opcodes.INVOKEVIRTUAL && vmEdge.getOpcode() <= Opcodes.INVOKEINTERFACE) {
				this.pcIndexMap.put(currentInstructionNumber, var);
				LocalVariableNode lNode = node.getCFG().localVariableTable.getNode(this.currentInstructionNumber, var);
				if (lNode != null) {
					try {
						vmEdge.setOwner(lNode.getType().getClassName());
						addAutoMachine(lNode.allIndex, lNode.name).goOneStep(preEdge, currentLine);
					} catch (AutoMachineException e) {
						reportAmBug(e, currentLine);
					}
				}
			}
		}
	}

	public void visitVarInsn(int opcode, int var) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;

		if (this.needCFG) {
			if (opcode == AutomaUtil.convertOpcodeToInt("ALOAD")) {
				this.pcIndexMap.put(currentInstructionNumber, var);
				// System.out.println("aload in method "+this.cfg.getMethod().name);
				LocalVariableNode lNode = node.getCFG().localVariableTable.getNode(this.currentInstructionNumber, var);
				if (lNode != null) {
					this.addAutoMachine(lNode.allIndex, lNode.name);
					// System.out.println("load class name:"+lNode.name
					// +" type: "+lNode.type.getClassName());
					// TODO
					// add some filter such as class name ()
					// System.out.println(this.sampleAutomachine.getOnlyConcernClass());
				}

			}
			// scenario: called a function previously and store its return value
			// to current object
			// eg. String a = new java.Lang.String();
			// eg. Connection conn=DriverManager.getConnection();
			else if (opcode == AutomaUtil.convertOpcodeToInt("ASTORE")) {
				AstoreInsn(var);
			}
		}
		try {
			// System.out.println(AutomaUtil.convertOpcodeToString(opcode)+" "+var);
			allGoOneStep(new VisitVarInsnEdge(null, null, opcode, var), currentLine);
		} catch (AutoMachineException e) {
			// TODO Auto-generated catch block
			reportAmBug(e, currentLine);
		}
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		// TODO Auto-generated method stub
		this.currentInstructionNumber++;
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		// TODO Auto-generated method stub
		super.visitMaxs(maxStack, maxLocals);
	}

}
