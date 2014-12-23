package automachine;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.objectweb.asm.Type;

public class AutomaUtil {
	static HashMap<String, Integer> opcodeStringMap = new HashMap<String, Integer>();
	static final String DBUrl = "jdbc:mysql://127.0.0.1:3306/jcdlib";// "jdbc:mysql://192.168.4.131:3306/jcdlib";
	static final String DBUser = "root";
	static final String DBPwd = "zhouzhiyi";// "massjcdlib";

	public static boolean stringEqual(String a, String b) {
		if (a == null) {
			if (b == null)
				return true;
			else
				return false;
		} else {
			if (b == null)
				return false;
			return a.equals(b);
		}

	}

	public static boolean stringMatch(String toMatchStr, String strPattern) {
		if (toMatchStr == null) {
			if (strPattern == null)
				return true;
			else
				return false;
		} else {
			if (strPattern == null)
				return false;
			else {
				Pattern pat = Pattern.compile(strPattern);
				Matcher mat = pat.matcher(toMatchStr);
				if(mat.find() == true){
					return true;
				}
				else
					return false;
			}
		}
	}

	public static boolean stringEqualWithReturnValue(String a, String desc) {
		if (a == null || desc == null)
			return a == null && desc == null;
		Type type = Type.getReturnType(desc);
		return a.replace('/', '.').equalsIgnoreCase(type.getClassName());
	}

	// stringEqualIgnoreDotSlash("java.lang.String","java/lang/String") === true
	public static boolean stringEqualIgnoreDotSlash(String a, String b) {
		if (a == null) {
			if (b == null)
				return true;
			else
				return false;
		} else {
			if (b == null)
				return false;

			return a.replace('/', '.').equals(b.replace('/', '.'));
		}

	}

	// add by zhouzhiyi
	public static boolean convertStringToBoolean(String s) {
		if (s == null)
			return false;
		return Boolean.parseBoolean(s);
	}

	public static Integer convertStringToInteger(String s) {
		if (s == null)
			return null;
		else
			return new Integer(s);
	}

	public static int convertOpcodeToInt(String op) {
		int iop = 0;
		if(op != null){
			iop = opcodeStringMap.get(op.toUpperCase());
		}
		return iop;
	}

	public static String convertOpcodeToString(int opcode) {
		for (Entry<String, Integer> e : opcodeStringMap.entrySet()) {
			if (e.getValue() == opcode)
				return e.getKey();
		}
		return "";
	}

	public static State getStateFromListByNumber(ArrayList<State> states, int sn) {
		for (State s : states) {
			if (s.stateNumber == sn)
				return s;
		}
		return null;
	}

	public static AutoMachine generateOneAutoMachineFromDatabase(int automachineId) {

		Connection amConnection, stateConnection, edgeConnection;
		Statement amStatement, stateStatement, edgeStatement;
		ResultSet amResultset, stateResultset, edgeResultset;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			amConnection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			stateConnection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			edgeConnection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);

			String amSql = "select * from automachine where automachineId = " + automachineId;
			amStatement = amConnection.createStatement();
			amResultset = amStatement.executeQuery(amSql);

			if (amResultset.next()) {
				AutoMachine am = new AutoMachine();
				am.automachineId = amResultset.getInt("automachineId");
				am.description = amResultset.getString("description");
				am.errorMessage = amResultset.getString("errorMessage");
				am.endMeanViolateDefect = amResultset.getBoolean("endMeanViolateDefect");
				am.needCFG = amResultset.getBoolean("objectSensitive");
				am.onlyConcernClass = amResultset.getString("onlyConcernClass");
				am.allOpcodePushOjectAutomachine = amResultset.getBoolean("allOpcodePushOjectAutomachine");
				am.severeLevel = amResultset.getInt("severeLevel");
				am.automachineName = amResultset.getString("automachineName");
				// System.out.println(amResultset.getString("automachineName"));

				String stateSql = "select * from amstate where automachineId = " + am.automachineId;
				stateStatement = stateConnection.createStatement();
				stateResultset = stateStatement.executeQuery(stateSql);

				while (stateResultset.next()) {
					State state = new State();
					state.stateNumber = stateResultset.getInt("stateNumber");
					state.stateId = stateResultset.getInt("stateId");
					state.isEndState = stateResultset.getBoolean("isEndState");
					state.isOriginState = stateResultset.getBoolean("isOriginState");
					am.states.add(state);
				}
				stateResultset.beforeFirst();
				while (stateResultset.next()) {
					int sn = stateResultset.getInt("stateNumber");
					int esn = stateResultset.getInt("exceptionState");
					State state = getStateFromListByNumber(am.states, sn);
					State estate = getStateFromListByNumber(am.states, esn);
					state.exceptionState = estate;

					String edgeSql = "select * from amedge where stateId = " + state.stateId;
					edgeStatement = edgeConnection.createStatement();
					edgeResultset = edgeStatement.executeQuery(edgeSql);

					while (edgeResultset.next()) {
						State toState = getStateFromListByNumber(am.states, edgeResultset.getInt("toState"));
						String str_class = edgeResultset.getString("class");

						int opcode = convertOpcodeToInt(edgeResultset.getString("opcode"));
						int operand = edgeResultset.getInt("operand");
						int var = edgeResultset.getInt("var");
						;
						int index = edgeResultset.getInt("index");
						int increment = edgeResultset.getInt("increment");
						int dims = edgeResultset.getInt("dims");
						int min = edgeResultset.getInt("min");
						int max = edgeResultset.getInt("max");
						String str_owner = edgeResultset.getString("owner");
						String str_name = edgeResultset.getString("name");
						String str_desc = edgeResultset.getString("desc");
						String str_signature = edgeResultset.getString("signature");
						String str_type = edgeResultset.getString("type");
						// add by zhouzhiyi
						boolean b_bindToReturnValue = edgeResultset.getBoolean("bindToReturnValue");
						// edit by zhouzhiyi
						Edge edge = AutomaUtil.addAnEdgeBetweenTwoStates(state, toState, str_class, opcode, operand,
								var, index, increment, dims, min, max, str_owner, str_name, str_desc, str_signature,
								str_type, b_bindToReturnValue);
						edge.edgeId = edgeResultset.getInt("edgeId");

					}// while edge
				}// while state
				am.initial();
				return am;
			}// if am

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	// generate automachine from a database
	public static ArrayList<AutoMachine> generateAllAutoMachineFromDatabase() {

		ArrayList<AutoMachine> automachines = new ArrayList<AutoMachine>();

		Connection amConnection, stateConnection, edgeConnection;
		Statement amStatement, stateStatement, edgeStatement;
		ResultSet amResultset, stateResultset, edgeResultset;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			amConnection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			stateConnection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			edgeConnection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);

			String amSql = "select * from automachine where isValid = 1 and alreadyincodas=2";
			amStatement = amConnection.createStatement();
			amResultset = amStatement.executeQuery(amSql);

			while (amResultset.next()) {
				AutoMachine am = new AutoMachine();
				am.automachineId = amResultset.getInt("automachineId");
				am.description = amResultset.getString("description");
				am.errorMessage = amResultset.getString("errorMessage");
				am.endMeanViolateDefect = amResultset.getBoolean("endMeanViolateDefect");
				am.needCFG = amResultset.getBoolean("objectSensitive");
				am.onlyConcernClass = amResultset.getString("onlyConcernClass");
				am.allOpcodePushOjectAutomachine = amResultset.getBoolean("allOpcodePushOjectAutomachine");
				am.severeLevel = amResultset.getInt("severeLevel");
				am.automachineName = amResultset.getString("automachineName");
				// System.out.println(amResultset.getString("automachineName"));

				String stateSql = "select * from amstate where automachineId = " + am.automachineId;
				stateStatement = stateConnection.createStatement();
				stateResultset = stateStatement.executeQuery(stateSql);

				while (stateResultset.next()) {
					State state = new State();
					state.stateNumber = stateResultset.getInt("stateNumber");
					state.stateId = stateResultset.getInt("stateId");
					state.isEndState = stateResultset.getBoolean("isEndState");
					state.isOriginState = stateResultset.getBoolean("isOriginState");
					am.states.add(state);
				}
				stateResultset.beforeFirst();
				while (stateResultset.next()) {
					int sn = stateResultset.getInt("stateNumber");
					int esn = stateResultset.getInt("exceptionState");
					State state = getStateFromListByNumber(am.states, sn);
					State estate = getStateFromListByNumber(am.states, esn);
					state.exceptionState = estate;

					String edgeSql = "select * from amedge where stateId = " + state.stateId;
					edgeStatement = edgeConnection.createStatement();
					edgeResultset = edgeStatement.executeQuery(edgeSql);

					while (edgeResultset.next()) {
						State toState = getStateFromListByNumber(am.states, edgeResultset.getInt("toState"));
						String str_class = edgeResultset.getString("class");

						int opcode = convertOpcodeToInt(edgeResultset.getString("opcode"));
						int operand = edgeResultset.getInt("operand");
						int var = edgeResultset.getInt("var");
						;
						int index = edgeResultset.getInt("index");
						int increment = edgeResultset.getInt("increment");
						int dims = edgeResultset.getInt("dims");
						int min = edgeResultset.getInt("min");
						int max = edgeResultset.getInt("max");
						String str_owner = edgeResultset.getString("owner");
						String str_name = edgeResultset.getString("name");
						String str_desc = edgeResultset.getString("desc");
						String str_signature = edgeResultset.getString("signature");
						String str_type = edgeResultset.getString("type");
						// add by zhouzhiyi
						// boolean
						// b_bindToReturnValue=edgeResultset.getBoolean("bindToReturnValue");
						boolean b_bindToReturnValue = false;
						// edit by zhouzhiyi, add param b_leftvalue
						Edge edge = AutomaUtil.addAnEdgeBetweenTwoStates(state, toState, str_class, opcode, operand,
								var, index, increment, dims, min, max, str_owner, str_name, str_desc, str_signature,
								str_type, b_bindToReturnValue);
						// end
						edge.edgeId = edgeResultset.getInt("edgeId");

					}// while edge
				}// while state
				am.initial();
				automachines.add(am);
			}// while am

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return automachines;
	}

	// generate automachine from a xml file
	public static AutoMachine generateAutoMachineFromXML(String filePath) {

		AutoMachine am = new AutoMachine();
		try {

			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(new FileInputStream(filePath));

			Element eleAutoMachine = doc.getRootElement();

			// List<Element> l=eleAutoMachine.getChildren();
			// for(Element e:l)
			// {
			// System.out.println(e.getName()+":"+e.getNamespace());
			// }
			//			
			// System.out.println(eleAutoMachine.getChild("description").getText());
			String description = eleAutoMachine.getChildText("description");
			String errorMessage = eleAutoMachine.getChildText("errorMessage");
			// boolean endMeanViolateDefect =
			// eleAutoMachine.getChild("endMeanViolateDefect").getText().equals("true");
			int severeLevel = new Integer(eleAutoMachine.getChild("severeLevel").getText());
			String needCFGstr = eleAutoMachine.getChildText("objectSensitive");
			if (needCFGstr != null)
				am.setNeedCFG(needCFGstr.trim().toLowerCase().equals("true"));

			String concernClass = eleAutoMachine.getChildText("onlyConcernClass");
			am.setOnlyConcernClass(concernClass);

			String allOpcodePushOjectAutomachine = eleAutoMachine.getChildText("allOpcodePushOjectAutomachine");
			if (allOpcodePushOjectAutomachine != null)
				am.setAllOpcodePushOjectAutomachine(allOpcodePushOjectAutomachine.toLowerCase().equals("true"));

			// am.setEndMeanViolateDefect(endMeanViolateDefect);
			am.setDescription(description);
			am.setErrorMessage(errorMessage);
			am.setSevereLevel(severeLevel);
			List<Element> eleStates = (List<Element>) eleAutoMachine.getChildren("state");

			for (Element eState : eleStates) {
				int number = new Integer(eState.getChildText("number"));
				boolean isEndState = eState.getChildText("endable") != null ? eState.getChildText("endable").equals("true"): false;
				boolean isFixed = eState.getChildText("fixed") != null ? eState.getChildText("fixed").equals("true"): false;
				
				boolean isOriginState = false;
				if (eState.getChildText("isOriginalState") != null)
				{
					isOriginState = eState.getChildText("isOriginalState").equals("true");
				}
				int exceptionState = new Integer(eState.getChildText("exceptionState"));

				State st = am.getStateByNumber(number);
				State est = am.getStateByNumber(exceptionState);
				st.setEndState(isEndState);
				st.setFixedState(isFixed);
				st.setOriginState(isOriginState);
				st.setExceptionState(est);

				// add edges
				List<Element> eleEdges = (List<Element>) eState.getChildren("edge");

				for (Element eleEdge : eleEdges) {
					
					// new code
					String str_isConditional = eleEdge.getAttributeValue("conditional");
					String str_conditionsRegExp = eleEdge.getChildText("conditions");
					
					String str_toState = eleEdge.getChildText("toState");
					String str_class = eleEdge.getChildText("class");
					
					// VisitMethodInsnEdge
					String str_opcode = eleEdge.getChildText("opcode");
					String str_owner = eleEdge.getChildText("owner");
					String str_name = eleEdge.getChildText("name");
					String str_desc = eleEdge.getChildText("desc");
					
					String str_operand = eleEdge.getChildText("operand");
					String str_var = eleEdge.getChildText("var");
					String str_index = eleEdge.getChildText("index");
					String str_increment = eleEdge.getChildText("increment");
					String str_dims = eleEdge.getChildText("dims");
					String str_min = eleEdge.getChildText("min");
					String str_max = eleEdge.getChildText("max");
					String str_signature = eleEdge.getChildText("signature");
					String str_type = eleEdge.getChildText("type");
					
					// add by zhouzhiyi
					String str_bindToReturnValue = eleEdge.getChildText("bindToReturnValue");

					// VisitNullCheckIfStmtEdge
					String str_opType = eleEdge.getChildText("opType");
					
					// end
					if (str_type != null)
						str_type = str_type.replace('.', '/');
					if (str_desc != null)
						str_desc = str_desc.replace('.', '/');
					if (str_owner != null)
						str_owner = str_owner.replace('.', '/');

					addAnEdgeBetweenTwoStates(st, am.getStateByNumber((new Integer(str_toState)).intValue()),
							str_isConditional, str_conditionsRegExp,
							str_class, convertOpcodeToInt(str_opcode), convertStringToInteger(str_operand),
							convertStringToInteger(str_var), convertStringToInteger(str_index),
							convertStringToInteger(str_increment), convertStringToInteger(str_dims),
							convertStringToInteger(str_min), convertStringToInteger(str_max), str_owner, str_name,
							str_desc, str_signature, str_type, convertStringToBoolean(str_bindToReturnValue), str_opType);
				}
			}
			am.initial();
			return am;
		} catch (Exception e) {
			System.err.println(filePath);
			e.printStackTrace();
		}
		return null;
	}

	// new added method @2011-01-30
	public static Edge addAnEdgeBetweenTwoStates(State fromState, State toState, 
			String str_isConditional, String str_conditionsRegExp,
			String str_class, Integer opcode,
			Integer operand, Integer var, Integer index, Integer increment, Integer dims, Integer min, Integer max,
			String str_owner, String str_name, String str_desc, String str_signature, String str_type,
			boolean b_bindToReturnValue, String str_opType) {

		Edge bEdge = null;
		// generate edge class
		if (str_class.equals("VisitNewInsnEdge")) {
			VisitNewInsnEdge aEdge = new VisitNewInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setType(str_type);
			bEdge = aEdge;
		} else if (str_class.equals("VisitFieldInsnEdge")) {
			VisitFieldInsnEdge aEdge = new VisitFieldInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(str_desc);
			aEdge.setName(str_name);
			aEdge.setOpcode(opcode);
			aEdge.setOwner(str_owner);
			bEdge = aEdge;
		} else if (str_class.equals("VisitIincInsnEdge")) {
			VisitIincInsnEdge aEdge = new VisitIincInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setIncrement(increment);
			aEdge.setVar(var);
			bEdge = aEdge;
		} else if (str_class.equals("VisitInsnEdge")) {
			VisitInsnEdge aEdge = new VisitInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			bEdge = aEdge;
		} else if (str_class.equals("VisitIntInsnEdge")) {
			VisitIntInsnEdge aEdge = new VisitIntInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			aEdge.setOperand(operand);
			bEdge = aEdge;
		} else if (str_class.equals("VisitJumpInsnEdge")) {
			VisitJumpInsnEdge aEdge = new VisitJumpInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			bEdge = aEdge;
		} else if (str_class.equals("VisitLdcInsnEdge")) {
			VisitLdcInsnEdge aEdge = new VisitLdcInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			bEdge = aEdge;
		} else if (str_class.equals("VisitLocalVariableEdge")) {
			VisitLocalVariableEdge aEdge = new VisitLocalVariableEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(str_desc);

			aEdge.setIndex(index);
			aEdge.setName(str_name);
			aEdge.setSignature(str_signature);
			bEdge = aEdge;
		} else if (str_class.equals("VisitMethodInsnEdge")) {
			VisitMethodInsnEdge aEdge = new VisitMethodInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(str_desc);
			aEdge.setBindToReturnValue(b_bindToReturnValue);

			String miniClassName;
			if (str_owner != null) {
				miniClassName = str_owner;
			} else
				miniClassName = "";

			if (miniClassName.indexOf('.') >= 0)
				miniClassName = miniClassName.substring(miniClassName.lastIndexOf('.') + 1);

			if (miniClassName.indexOf('/') >= 0)
				miniClassName = miniClassName.substring(miniClassName.lastIndexOf('/') + 1);

			// System.out.println("miniClassName = "+miniClassName);

			if (miniClassName.equals(str_name))
				aEdge.setName("<init>");
			else
				aEdge.setName(str_name);

			aEdge.setOpcode(opcode);
			aEdge.setOwner(str_owner);
			bEdge = aEdge;
		} else if (str_class.equals("VisitMultiANewArrayInsnEdge")) {
			VisitMultiANewArrayInsnEdge aEdge = new VisitMultiANewArrayInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(str_desc);
			aEdge.setDims(dims);
			bEdge = aEdge;
		} else if (str_class.equals("VisitTableSwitchInsnEdge")) {
			VisitTableSwitchInsnEdge aEdge = new VisitTableSwitchInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setMax(max);
			aEdge.setMin(min);
			bEdge = aEdge;
		} else if (str_class.equals("VisitTryCatchBlockEdge")) {
			VisitTryCatchBlockEdge aEdge = new VisitTryCatchBlockEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setType(str_type);
			bEdge = aEdge;
		} else if (str_class.equals("VisitTypeInsnEdge")) {
			VisitTypeInsnEdge aEdge = new VisitTypeInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			aEdge.setType(str_type);
			bEdge = aEdge;
		} else if (str_class.equals("VisitVarInsnEdge")) {
			VisitVarInsnEdge aEdge = new VisitVarInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			aEdge.setVar(var);
			bEdge = aEdge;
		} else if (str_class.equals("VisitMethodEndEdge")) {
			VisitMethodEndEdge aEdge = new VisitMethodEndEdge(fromState, toState);
			fromState.addEdge(aEdge);
			bEdge = aEdge;
		} else if (str_class.equals("VisitNullCheckIfStmtEdge")) {
			VisitNullCheckIfStmtEdge aEdge = new VisitNullCheckIfStmtEdge(fromState, toState);
			fromState.addEdge(aEdge);
			aEdge.opType = str_opType;
			bEdge = aEdge;
		} else if (str_class.equals("VisitVarDeRefStmtEdge")) {
			VisitVarDerefStmtEdge aEdge = new VisitVarDerefStmtEdge(fromState, toState);
			fromState.addEdge(aEdge);
			bEdge = aEdge;
		}
		
		if (bEdge != null) {
			if (str_isConditional != null && str_isConditional.equals("true")) {
				bEdge.setConditional(true);
				if (str_conditionsRegExp != null && str_conditionsRegExp.length() > 0) {
					bEdge.setConditionsRegExp(str_conditionsRegExp);
				}
			}
		}
		
		// end of generate edge class
		return bEdge;
	}
	
	// edit by zhouzhiyi to adept new element leftvalue
	// public static Edge addAnEdgeBetweenTwoStates(State fromState, State
	// toState, String str_class,
	// Integer opcode, Integer operand,
	// Integer var, Integer index, Integer increment,
	// Integer dims, Integer min, Integer max, String str_owner,
	// String str_name, String str_desc, String str_signature,
	// String str_type){
	// return addAnEdgeBetweenTwoStates(fromState, toState, str_class, opcode,
	// operand, var, index, increment, dims, min, max, str_owner, str_name,
	// str_desc, str_signature, str_type
	// ,false);
	// }
	// add end
	
	public static Edge addAnEdgeBetweenTwoStates(State fromState, State toState, 
			String str_class, Integer opcode,
			Integer operand, Integer var, Integer index, Integer increment, Integer dims, Integer min, Integer max,
			String str_owner, String str_name, String str_desc, String str_signature, String str_type,
			boolean b_bindToReturnValue) {

		// generate edge class
		if (str_class.equals("VisitNewInsnEdge")) {
			VisitNewInsnEdge aEdge = new VisitNewInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setType(str_type);
			return aEdge;
		} else if (str_class.equals("VisitFieldInsnEdge")) {
			VisitFieldInsnEdge aEdge = new VisitFieldInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(str_desc);
			aEdge.setName(str_name);
			aEdge.setOpcode(opcode);
			aEdge.setOwner(str_owner);
			return aEdge;
		} else if (str_class.equals("VisitIincInsnEdge")) {
			VisitIincInsnEdge aEdge = new VisitIincInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setIncrement(increment);
			aEdge.setVar(var);
			return aEdge;
		} else if (str_class.equals("VisitInsnEdge")) {
			VisitInsnEdge aEdge = new VisitInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			return aEdge;
		} else if (str_class.equals("VisitIntInsnEdge")) {
			VisitIntInsnEdge aEdge = new VisitIntInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			aEdge.setOperand(operand);
			return aEdge;
		} else if (str_class.equals("VisitJumpInsnEdge")) {
			VisitJumpInsnEdge aEdge = new VisitJumpInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			return aEdge;
		} else if (str_class.equals("VisitLdcInsnEdge")) {
			VisitLdcInsnEdge aEdge = new VisitLdcInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			return aEdge;
		} else if (str_class.equals("VisitLocalVariableEdge")) {
			VisitLocalVariableEdge aEdge = new VisitLocalVariableEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(str_desc);

			aEdge.setIndex(index);
			aEdge.setName(str_name);
			aEdge.setSignature(str_signature);
			return aEdge;
		} else if (str_class.equals("VisitMethodInsnEdge")) {
			VisitMethodInsnEdge aEdge = new VisitMethodInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(str_desc);
			aEdge.setBindToReturnValue(b_bindToReturnValue);

			String miniClassName;
			if (str_owner != null) {
				miniClassName = str_owner;
			} else
				miniClassName = "";

			if (miniClassName.indexOf('.') >= 0)
				miniClassName = miniClassName.substring(miniClassName.lastIndexOf('.') + 1);

			if (miniClassName.indexOf('/') >= 0)
				miniClassName = miniClassName.substring(miniClassName.lastIndexOf('/') + 1);

			// System.out.println("miniClassName = "+miniClassName);

			if (miniClassName.equals(str_name))
				aEdge.setName("<init>");
			else
				aEdge.setName(str_name);

			aEdge.setOpcode(opcode);
			aEdge.setOwner(str_owner);
			return aEdge;
		} else if (str_class.equals("VisitMultiANewArrayInsnEdge")) {
			VisitMultiANewArrayInsnEdge aEdge = new VisitMultiANewArrayInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(str_desc);
			aEdge.setDims(dims);
			return aEdge;
		} else if (str_class.equals("VisitTableSwitchInsnEdge")) {
			VisitTableSwitchInsnEdge aEdge = new VisitTableSwitchInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setMax(max);
			aEdge.setMin(min);
			return aEdge;
		} else if (str_class.equals("VisitTryCatchBlockEdge")) {
			VisitTryCatchBlockEdge aEdge = new VisitTryCatchBlockEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setType(str_type);
			return aEdge;
		} else if (str_class.equals("VisitTypeInsnEdge")) {
			VisitTypeInsnEdge aEdge = new VisitTypeInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			aEdge.setType(str_type);
			return aEdge;
		} else if (str_class.equals("VisitVarInsnEdge")) {
			VisitVarInsnEdge aEdge = new VisitVarInsnEdge(null, null);
			aEdge.setFromState(fromState);
			aEdge.setToState(toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(opcode);
			aEdge.setVar(var);
			return aEdge;
		}
		// end of generate edge class

		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(stringMatch("null==this || this.b = true", ".*this==null.*|.*null==this.*"));
		// generateAutoMachineFromXML("bin/automachine/detector_i2d_ceilround.xml");
//		for (AutoMachine am : generateAllAutoMachineFromDatabase()) {
//			System.out.println(am);
//		}
		// buildAutomachie("bin/automachine/test.xml");
	}

	static {

		// opcodes // visit method (- = idem)
		// opcodes // visit method (- = idem)

		opcodeStringMap.put("NOP", 0); // visitInsn
		opcodeStringMap.put("ACONST_NULL", 1); // -
		opcodeStringMap.put("ICONST_M1", 2); // -
		opcodeStringMap.put("ICONST_0", 3); // -
		opcodeStringMap.put("ICONST_1", 4); // -
		opcodeStringMap.put("ICONST_2", 5); // -
		opcodeStringMap.put("ICONST_3", 6); // -
		opcodeStringMap.put("ICONST_4", 7); // -
		opcodeStringMap.put("ICONST_5", 8); // -
		opcodeStringMap.put("LCONST_0", 9); // -
		opcodeStringMap.put("LCONST_1", 10); // -
		opcodeStringMap.put("FCONST_0", 11); // -
		opcodeStringMap.put("FCONST_1", 12); // -
		opcodeStringMap.put("FCONST_2", 13); // -
		opcodeStringMap.put("DCONST_0", 14); // -
		opcodeStringMap.put("DCONST_1", 15); // -
		opcodeStringMap.put("BIPUSH", 16); // visitIntInsn
		opcodeStringMap.put("SIPUSH", 17); // -
		opcodeStringMap.put("LDC", 18); // visitLdcInsn
		opcodeStringMap.put("LDC_W", 19); // -
		opcodeStringMap.put("LDC2_W", 20); // -
		opcodeStringMap.put("ILOAD", 21); // visitVarInsn
		opcodeStringMap.put("LLOAD", 22); // -
		opcodeStringMap.put("FLOAD", 23); // -
		opcodeStringMap.put("DLOAD", 24); // -
		opcodeStringMap.put("ALOAD", 25); // -
		opcodeStringMap.put("ILOAD_0", 26); // -
		opcodeStringMap.put("ILOAD_1", 27); // -
		opcodeStringMap.put("ILOAD_2", 28); // -
		opcodeStringMap.put("ILOAD_3", 29); // -
		opcodeStringMap.put("LLOAD_0", 30); // -
		opcodeStringMap.put("LLOAD_1", 31); // -
		opcodeStringMap.put("LLOAD_2", 32); // -
		opcodeStringMap.put("LLOAD_3", 33); // -
		opcodeStringMap.put("FLOAD_0", 34); // -
		opcodeStringMap.put("FLOAD_1", 35); // -
		opcodeStringMap.put("FLOAD_2", 36); // -
		opcodeStringMap.put("FLOAD_3", 37); // -
		opcodeStringMap.put("DLOAD_0", 38); // -
		opcodeStringMap.put("DLOAD_1", 39); // -
		opcodeStringMap.put("DLOAD_2", 40); // -
		opcodeStringMap.put("DLOAD_3", 41); // -
		opcodeStringMap.put("ALOAD_0", 42); // -
		opcodeStringMap.put("ALOAD_1", 43); // -
		opcodeStringMap.put("ALOAD_2", 44); // -
		opcodeStringMap.put("ALOAD_3", 45); // -
		opcodeStringMap.put("IALOAD", 46); // visitInsn
		opcodeStringMap.put("LALOAD", 47); // -
		opcodeStringMap.put("FALOAD", 48); // -
		opcodeStringMap.put("DALOAD", 49); // -
		opcodeStringMap.put("AALOAD", 50); // -
		opcodeStringMap.put("BALOAD", 51); // -
		opcodeStringMap.put("CALOAD", 52); // -
		opcodeStringMap.put("SALOAD", 53); // -
		opcodeStringMap.put("ISTORE", 54); // visitVarInsn
		opcodeStringMap.put("LSTORE", 55); // -
		opcodeStringMap.put("FSTORE", 56); // -
		opcodeStringMap.put("DSTORE", 57); // -
		opcodeStringMap.put("ASTORE", 58); // -
		opcodeStringMap.put("ISTORE_0", 59); // -
		opcodeStringMap.put("ISTORE_1", 60); // -
		opcodeStringMap.put("ISTORE_2", 61); // -
		opcodeStringMap.put("ISTORE_3", 62); // -
		opcodeStringMap.put("LSTORE_0", 63); // -
		opcodeStringMap.put("LSTORE_1", 64); // -
		opcodeStringMap.put("LSTORE_2", 65); // -
		opcodeStringMap.put("LSTORE_3", 66); // -
		opcodeStringMap.put("FSTORE_0", 67); // -
		opcodeStringMap.put("FSTORE_1", 68); // -
		opcodeStringMap.put("FSTORE_2", 69); // -
		opcodeStringMap.put("FSTORE_3", 70); // -
		opcodeStringMap.put("DSTORE_0", 71); // -
		opcodeStringMap.put("DSTORE_1", 72); // -
		opcodeStringMap.put("DSTORE_2", 73); // -
		opcodeStringMap.put("DSTORE_3", 74); // -
		opcodeStringMap.put("ASTORE_0", 75); // -
		opcodeStringMap.put("ASTORE_1", 76); // -
		opcodeStringMap.put("ASTORE_2", 77); // -
		opcodeStringMap.put("ASTORE_3", 78); // -
		opcodeStringMap.put("IASTORE", 79); // visitInsn
		opcodeStringMap.put("LASTORE", 80); // -
		opcodeStringMap.put("FASTORE", 81); // -
		opcodeStringMap.put("DASTORE", 82); // -
		opcodeStringMap.put("AASTORE", 83); // -
		opcodeStringMap.put("BASTORE", 84); // -
		opcodeStringMap.put("CASTORE", 85); // -
		opcodeStringMap.put("SASTORE", 86); // -
		opcodeStringMap.put("POP", 87); // -
		opcodeStringMap.put("POP2", 88); // -
		opcodeStringMap.put("DUP", 89); // -
		opcodeStringMap.put("DUP_X1", 90); // -
		opcodeStringMap.put("DUP_X2", 91); // -
		opcodeStringMap.put("DUP2", 92); // -
		opcodeStringMap.put("DUP2_X1", 93); // -
		opcodeStringMap.put("DUP2_X2", 94); // -
		opcodeStringMap.put("SWAP", 95); // -
		opcodeStringMap.put("IADD", 96); // -
		opcodeStringMap.put("LADD", 97); // -
		opcodeStringMap.put("FADD", 98); // -
		opcodeStringMap.put("DADD", 99); // -
		opcodeStringMap.put("ISUB", 100); // -
		opcodeStringMap.put("LSUB", 101); // -
		opcodeStringMap.put("FSUB", 102); // -
		opcodeStringMap.put("DSUB", 103); // -
		opcodeStringMap.put("IMUL", 104); // -
		opcodeStringMap.put("LMUL", 105); // -
		opcodeStringMap.put("FMUL", 106); // -
		opcodeStringMap.put("DMUL", 107); // -
		opcodeStringMap.put("IDIV", 108); // -
		opcodeStringMap.put("LDIV", 109); // -
		opcodeStringMap.put("FDIV", 110); // -
		opcodeStringMap.put("DDIV", 111); // -
		opcodeStringMap.put("IREM", 112); // -
		opcodeStringMap.put("LREM", 113); // -
		opcodeStringMap.put("FREM", 114); // -
		opcodeStringMap.put("DREM", 115); // -
		opcodeStringMap.put("INEG", 116); // -
		opcodeStringMap.put("LNEG", 117); // -
		opcodeStringMap.put("FNEG", 118); // -
		opcodeStringMap.put("DNEG", 119); // -
		opcodeStringMap.put("ISHL", 120); // -
		opcodeStringMap.put("LSHL", 121); // -
		opcodeStringMap.put("ISHR", 122); // -
		opcodeStringMap.put("LSHR", 123); // -
		opcodeStringMap.put("IUSHR", 124); // -
		opcodeStringMap.put("LUSHR", 125); // -
		opcodeStringMap.put("IAND", 126); // -
		opcodeStringMap.put("LAND", 127); // -
		opcodeStringMap.put("IOR", 128); // -
		opcodeStringMap.put("LOR", 129); // -
		opcodeStringMap.put("IXOR", 130); // -
		opcodeStringMap.put("LXOR", 131); // -
		opcodeStringMap.put("IINC", 132); // visitIincInsn
		opcodeStringMap.put("I2L", 133); // visitInsn
		opcodeStringMap.put("I2F", 134); // -
		opcodeStringMap.put("I2D", 135); // -
		opcodeStringMap.put("L2I", 136); // -
		opcodeStringMap.put("L2F", 137); // -
		opcodeStringMap.put("L2D", 138); // -
		opcodeStringMap.put("F2I", 139); // -
		opcodeStringMap.put("F2L", 140); // -
		opcodeStringMap.put("F2D", 141); // -
		opcodeStringMap.put("D2I", 142); // -
		opcodeStringMap.put("D2L", 143); // -
		opcodeStringMap.put("D2F", 144); // -
		opcodeStringMap.put("I2B", 145); // -
		opcodeStringMap.put("I2C", 146); // -
		opcodeStringMap.put("I2S", 147); // -
		opcodeStringMap.put("LCMP", 148); // -
		opcodeStringMap.put("FCMPL", 149); // -
		opcodeStringMap.put("FCMPG", 150); // -
		opcodeStringMap.put("DCMPL", 151); // -
		opcodeStringMap.put("DCMPG", 152); // -
		opcodeStringMap.put("IFEQ", 153); // visitJumpInsn
		opcodeStringMap.put("IFNE", 154); // -
		opcodeStringMap.put("IFLT", 155); // -
		opcodeStringMap.put("IFGE", 156); // -
		opcodeStringMap.put("IFGT", 157); // -
		opcodeStringMap.put("IFLE", 158); // -
		opcodeStringMap.put("IF_ICMPEQ", 159); // -
		opcodeStringMap.put("IF_ICMPNE", 160); // -
		opcodeStringMap.put("IF_ICMPLT", 161); // -
		opcodeStringMap.put("IF_ICMPGE", 162); // -
		opcodeStringMap.put("IF_ICMPGT", 163); // -
		opcodeStringMap.put("IF_ICMPLE", 164); // -
		opcodeStringMap.put("IF_ACMPEQ", 165); // -
		opcodeStringMap.put("IF_ACMPNE", 166); // -
		opcodeStringMap.put("GOTO", 167); // -
		opcodeStringMap.put("JSR", 168); // -
		opcodeStringMap.put("RET", 169); // visitVarInsn
		opcodeStringMap.put("TABLESWITCH", 170); // visiTableSwitchInsn
		opcodeStringMap.put("LOOKUPSWITCH", 171); // visitLookupSwitch
		opcodeStringMap.put("IRETURN", 172); // visitInsn
		opcodeStringMap.put("LRETURN", 173); // -
		opcodeStringMap.put("FRETURN", 174); // -
		opcodeStringMap.put("DRETURN", 175); // -
		opcodeStringMap.put("ARETURN", 176); // -
		opcodeStringMap.put("RETURN", 177); // -
		opcodeStringMap.put("GETSTATIC", 178); // visitFieldInsn
		opcodeStringMap.put("PUTSTATIC", 179); // -
		opcodeStringMap.put("GETFIELD", 180); // -
		opcodeStringMap.put("PUTFIELD", 181); // -
		opcodeStringMap.put("INVOKEVIRTUAL", 182); // visitMethodInsn
		opcodeStringMap.put("INVOKESPECIAL", 183); // -
		opcodeStringMap.put("INVOKESTATIC", 184); // -
		opcodeStringMap.put("INVOKEINTERFACE", 185); // -
		opcodeStringMap.put("UNUSED", 186); // NOT VISITED
		opcodeStringMap.put("NEW", 187); // visitTypeInsn
		opcodeStringMap.put("NEWARRAY", 188); // visitIntInsn
		opcodeStringMap.put("ANEWARRAY", 189); // visitTypeInsn
		opcodeStringMap.put("ARRAYLENGTH", 190); // visitInsn
		opcodeStringMap.put("ATHROW", 191); // -
		opcodeStringMap.put("CHECKCAST", 192); // visitTypeInsn
		opcodeStringMap.put("INSTANCEOF", 193); // -
		opcodeStringMap.put("MONITORENTER", 194); // visitInsn
		opcodeStringMap.put("MONITOREXIT", 195); // -
		opcodeStringMap.put("WIDE", 196); // NOT VISITED
		opcodeStringMap.put("MULTIANEWARRAY", 197); // visitMultiANewArrayInsn
		opcodeStringMap.put("IFNULL", 198); // visitJumpInsn
		opcodeStringMap.put("IFNONNULL", 199); // -
		opcodeStringMap.put("GOTO_W", 200); // -
		opcodeStringMap.put("JSR_W", 201); // -

	}

	public static void addAnEdgeBetweenTwoStates(State fromState, State toState, Edge e) {
		// generate edge class

		Edge bEdge = null;
		
		if (e instanceof VisitNewInsnEdge) {
			VisitNewInsnEdge oEdge = (VisitNewInsnEdge) e;
			VisitNewInsnEdge aEdge = new VisitNewInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);

			aEdge.setType(oEdge.getType());
			
			bEdge = aEdge;
		} else if (e instanceof VisitFieldInsnEdge) {
			VisitFieldInsnEdge oEdge = (VisitFieldInsnEdge) e;
			VisitFieldInsnEdge aEdge = new VisitFieldInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);

			aEdge.setDesc(oEdge.desc);
			aEdge.setName(oEdge.name);
			aEdge.setOpcode(oEdge.opcode);
			aEdge.setOwner(oEdge.owner);
			
			bEdge = aEdge;
		} else if (e instanceof VisitIincInsnEdge) {
			VisitIincInsnEdge oEdge = (VisitIincInsnEdge) e;
			VisitIincInsnEdge aEdge = new VisitIincInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);
			aEdge.setIncrement(oEdge.increment);
			aEdge.setVar(oEdge.var);
			
			bEdge = aEdge;
			
		} else if (e instanceof VisitInsnEdge) {
			VisitInsnEdge oEdge = (VisitInsnEdge) e;
			VisitInsnEdge aEdge = new VisitInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(oEdge.opcode);
			bEdge = aEdge;
			
		} else if (e instanceof VisitIntInsnEdge) {
			VisitIntInsnEdge oEdge = (VisitIntInsnEdge) e;
			VisitIntInsnEdge aEdge = new VisitIntInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);

			aEdge.setOpcode(oEdge.opcode);
			aEdge.setOperand(oEdge.operand);
			
			bEdge = aEdge;
		} else if (e instanceof VisitJumpInsnEdge) {
			VisitJumpInsnEdge oEdge = (VisitJumpInsnEdge) e;
			VisitJumpInsnEdge aEdge = new VisitJumpInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(oEdge.opcode);
			bEdge = aEdge;
		} else if (e instanceof VisitLdcInsnEdge) {
			VisitLdcInsnEdge oEdge = (VisitLdcInsnEdge) e;
			VisitLdcInsnEdge aEdge = new VisitLdcInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);
			bEdge = aEdge;
		} else if (e instanceof VisitLocalVariableEdge) {
			VisitLocalVariableEdge oEdge = (VisitLocalVariableEdge) e;
			VisitLocalVariableEdge aEdge = new VisitLocalVariableEdge(fromState, toState);
			fromState.addEdge(aEdge);

			aEdge.setDesc(oEdge.desc);
			aEdge.setIndex(oEdge.index);
			aEdge.setName(oEdge.name);
			aEdge.setSignature(oEdge.signature);
			
			bEdge = aEdge;
		} else if (e instanceof VisitMethodInsnEdge) {
			VisitMethodInsnEdge oEdge = (VisitMethodInsnEdge) e;
			VisitMethodInsnEdge aEdge = new VisitMethodInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);
			aEdge.setDesc(oEdge.desc);
			aEdge.setBindToReturnValue(oEdge.bindToReturnValue);
			aEdge.setName(oEdge.name);
			aEdge.setOpcode(oEdge.opcode);
			aEdge.setOwner(oEdge.owner);
			
			bEdge = aEdge;
		} else if (e instanceof VisitMultiANewArrayInsnEdge) {
			VisitMultiANewArrayInsnEdge oEdge = (VisitMultiANewArrayInsnEdge) e;
			VisitMultiANewArrayInsnEdge aEdge = new VisitMultiANewArrayInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);

			aEdge.setDesc(oEdge.desc);
			aEdge.setDims(oEdge.dims);
			
			bEdge = aEdge;
		} else if (e instanceof VisitTableSwitchInsnEdge) {
			VisitTableSwitchInsnEdge oEdge = (VisitTableSwitchInsnEdge) e;
			VisitTableSwitchInsnEdge aEdge = new VisitTableSwitchInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);

			aEdge.setMax(oEdge.max);
			aEdge.setMin(oEdge.min);
			
			bEdge = aEdge;
			
		} else if (e instanceof VisitTryCatchBlockEdge) {
			VisitTryCatchBlockEdge oEdge = (VisitTryCatchBlockEdge) e;
			VisitTryCatchBlockEdge aEdge = new VisitTryCatchBlockEdge(fromState, toState);
			fromState.addEdge(aEdge);

			aEdge.setType(oEdge.type);
			
			bEdge = aEdge;
		} else if (e instanceof VisitTypeInsnEdge) {
			VisitTypeInsnEdge oEdge = (VisitTypeInsnEdge) e;
			VisitTypeInsnEdge aEdge = new VisitTypeInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);
			aEdge.setOpcode(oEdge.opcode);
			aEdge.setType(oEdge.type);
			
			bEdge = aEdge;
		} else if (e instanceof VisitVarInsnEdge) {
			VisitVarInsnEdge oEdge = (VisitVarInsnEdge) e;
			VisitVarInsnEdge aEdge = new VisitVarInsnEdge(fromState, toState);
			fromState.addEdge(aEdge);

			aEdge.setOpcode(oEdge.opcode);
			aEdge.setVar(oEdge.var);
			
			bEdge = aEdge;
		} else if (e instanceof VisitMethodEndEdge){
			VisitMethodEndEdge aEdge = new VisitMethodEndEdge(fromState, toState);
			fromState.addEdge(aEdge);
			bEdge = aEdge;
		} else if (e instanceof VisitNullCheckIfStmtEdge) {
			VisitNullCheckIfStmtEdge oEdge =	(VisitNullCheckIfStmtEdge)e;
			VisitNullCheckIfStmtEdge aEdge = new VisitNullCheckIfStmtEdge(fromState, toState);
			fromState.addEdge(aEdge);
			aEdge.opType = oEdge.opType;
			bEdge = aEdge;
		} else if (e instanceof VisitVarDerefStmtEdge) {
			VisitVarDerefStmtEdge aEdge = new VisitVarDerefStmtEdge(fromState, toState);
			fromState.addEdge(aEdge);
			bEdge = aEdge;
		}
		
		
		if(bEdge != null){
			bEdge.setConditional(e.isConditional);
			bEdge.setConditionsRegExp(e.getConditionsRegExp());
		}
		// end of generate edge class
	}
}
