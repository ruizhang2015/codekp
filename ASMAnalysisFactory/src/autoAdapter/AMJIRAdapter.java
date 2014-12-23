/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-25
 * @modifier: liuxi
 * @time 2010-3-25
 * @reviewer: liuxi
 * @time 2010-3-25
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package autoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;

import automachine.AutoMachine;
import automachine.AutoMachineException;
import automachine.AutomaUtil;
import automachine.VisitMethodInsnEdge;

import edu.pku.cn.bugreport.BugInstance;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.JIRDetector;
import edu.pku.cn.jir.AbstractJIRVisitor;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.util.HASelect;

/**
 * automachine java intrenet
 * 
 * @author liuxi
 * 
 */
public class AMJIRAdapter extends JIRDetector {
	
	public boolean Debug = false;
	AutoMachine sampleAutomachine;
	String specificFile;
	// database id
	int automachineId;
	boolean applyForChild = false;
	Type[] applyType;
	HashMap<JIRValue, AutoMachine> nodeAm = new HashMap<JIRValue, AutoMachine>();
	
	int currentLine;
	String desc;
	boolean isInConstructor = false;
	Set<LocalVariableNode> param = new HashSet<LocalVariableNode>();
	
	public AMJIRAdapter() {
		super();
	}

	
	public AMJIRAdapter(AutoMachine am) {
		this.automachineId = am.automachineId;
		this.sampleAutomachine = am;
		// if(am.getOnlyConcernClass()!=null){
		// applyForChild=true;
		// String[] name=am.getOnlyConcernClass().split(";");
		// applyType=new Type[name.length];
		// for(int i=0;i<name.length;i++){
		// applyType[i]=Type.getObjectType(name[i]);
		// }
		// }
		am.initial();
	}

	public AMJIRAdapter(String specificFile) {
		this.specificFile = specificFile;
		this.sampleAutomachine = AutomaUtil.generateAutoMachineFromXML(specificFile);
		if (sampleAutomachine.getOnlyConcernClass() != null && !"".equals(sampleAutomachine.getOnlyConcernClass())) {
			applyForChild = true;
			String[] name = sampleAutomachine.getOnlyConcernClass().split(";");
			applyType = new Type[name.length];
			for (int i = 0; i < name.length; i++) {
				applyType[i] = Type.getObjectType(name[i]);
			}
		}
	}

	

	private boolean isAssignableFrom(Type type) {
		for (int i = 0; i < applyType.length; i++) {
			if (HASelect.isAssignableFrom(applyType[i], type))
				return true;
		}
		return false;
	}

	private AutoMachine getAM(JIRValue key) {
		Type keyType = key.getType();
		if (keyType == null || keyType.getSort() < Type.ARRAY)
			return null;// / 当前我们只处理对象时序关系
		if (key instanceof LocalRef) {
			LocalRef l = (LocalRef) key;
			if (l.getName().equals("this"))
				return null;
		}
		if (applyForChild) {
			if (!isAssignableFrom(keyType))
				return null;
		}

		AutoMachine am = nodeAm.get(key);
		try {
			if (am == null) {
				am = sampleAutomachine.getCopy();
				am.automachineName = keyType.getClassName();
				nodeAm.put(key, am);
			}
		} catch (Exception e) {
			System.err.println(specificFile);
			throw new RuntimeException(e);
		}
		return am;
	}

	

	public void visitStart(String name, String desc, String signature) {
		// System.out.println(name+desc);
		this.setName(name);
		this.desc = desc;
		if (name.equals("<init>"))
			isInConstructor = true;
	}

	public void visitParameters(List<LocalVariableNode> params) {
		if (params != null)
			for (int i = 0; i < params.size(); i++)
				param.add(params.get(i));
	}

	public void visit(LineStmt as) {
		currentLine = as.line;
	}

	public void visit(ReturnStmt as) {
		if (as.value != null) {
			AutoMachine am = getAM(as.value);
			if (am != null)
				am.currentState = am.getOriginState();
		}
	}

	private void visit(InvokeExpr ie, JIRValue leftValue) {
		if (Debug)
			System.out.println(ie.invoker.getType() + "  " + ie.node.name + ie.node.desc);

		// ie.node.owner 调用者所属类型
		// ie.node.name 调用方法的名字
		VisitMethodInsnEdge e = new VisitMethodInsnEdge(null, null, ie.node.getOpcode(), ie.node.owner, ie.node.name,
				ie.node.desc);
		e.setApplyForChild(applyForChild);

		// ie.invoker 调用者实例的名字
		AutoMachine am = getAM(ie.invoker);
		if (am != null)
			try {
				// nparam 调用者是否是传入参数？
				boolean nparam = true;
				if (ie.invoker instanceof LocalRef) {
					LocalRef ref = (LocalRef) ie.invoker;

					// param 传入参数
					if (param.contains(ref.nodeRef))
						nparam = false;
				}

				if (nparam && !(ie.invoker instanceof TempRef || ie.invoker instanceof FieldRef))
					am.goOneStep(e, currentLine);
			} catch (AutoMachineException e1) {
				// TODO Auto-generated catch block
				reportAmBug(e1, am.automachineName, currentLine);
			}
		if (leftValue != null) {
			am = getAM(leftValue);
			if (am != null)
				try {
					if (!(leftValue instanceof FieldRef || leftValue instanceof TempRef))
						am.goOneStep(e, currentLine);
				} catch (AutoMachineException e1) {
					// TODO Auto-generated catch block
					reportAmBug(e1, am.automachineName, am.getCurrentState().getLastLine());
					// e1.printStackTrace();
				}
		}
		for (JIRValue jir : ie.params) {
			// if(jir instanceof LocalRef){
			am = getAM(jir);
			if (am != null)
				am.initial();
			// }
		}
	}

	@Override
	public void visit(AssignStmt as) {
		// TODO Auto-generated method stub
		if (as.right instanceof InvokeExpr)
			visit((InvokeExpr) as.right, as.left);
	}

	@Override
	public void visit(InvokeStmt as) {
		// TODO Auto-generated method stub
		visit((InvokeExpr) as.invoke, null);
	}

	private void reportAmBug(AutoMachineException e, String className, int line) {
		reportor.report(getClassName(), new BugInstance(BugInstance.format(e.getMessage(), new String[] { className,
				name })
				+ " in method " + this.getName() + desc + "\n" + " by am:" + this.automachineId, line));
	}

	@Override
	public void visitEnd() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (!this.sampleAutomachine.isEndMeanViolateDefect())
			for (AutoMachine am : this.nodeAm.values()) {
				try {
					am.checkOutWhenVistEnd();
				} catch (AutoMachineException e) {
					// TODO Auto-generated catch block

					reportAmBug(e, am.automachineName, am.getCurrentState().getLastLine());
					// System.out.println(e.getMessage()+" in method
					// "+this.getName()+""+am.getCurrentState().getLastLine());
				}
				am.initial();
			}
		nodeAm.clear();
		param.clear();
		isInConstructor = false;
	}

	@Override
	public ArrayList<Detector> getInstances() {
		// TODO Auto-generated method stub
		ArrayList<Detector> detectors;
		// boolean generateFromXml =
		// !System.getProperty("AutoMethodAdapter.usexmltoconfig",
		// "yes").equals("no");
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
					detectors.add(new AMJIRAdapter(file.getAbsolutePath()));
				}
			}
		}
		// generate automachines form database
		else {
			System.out.println("use database to config ams");
			detectors = new ArrayList<Detector>();
			System.out.println("accessing database to generate automachines...");
			ArrayList<AutoMachine> ams = AutomaUtil.generateAllAutoMachineFromDatabase();
			for (AutoMachine am : ams) {
				detectors.add(new AMJIRAdapter(am));
			}
			System.out.println("generate " + ams.size() + " automachines.");
		}
		return detectors;
	}

	public String getClassName() {
		return node.owner;
	}
}

// end
