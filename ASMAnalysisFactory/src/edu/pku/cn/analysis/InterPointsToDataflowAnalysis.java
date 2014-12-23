/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-25 ����04:31:40
 * @modifier: Administrator
 * @time 2009-3-25 ����04:31:40
 * @reviewer: Administrator
 * @time 2009-3-25 ����04:31:40
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.Project;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.EdgeType;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.AnyNewExpr;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.CastExpr;
import edu.pku.cn.jir.CaughtExceptionRef;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.InvokeStmt;
import edu.pku.cn.jir.JIRValue;
import edu.pku.cn.jir.LineStmt;
import edu.pku.cn.jir.LocalRef;
import edu.pku.cn.jir.NewArrayExpr;
import edu.pku.cn.jir.NewExpr;
import edu.pku.cn.jir.Null;
import edu.pku.cn.jir.Ref;
import edu.pku.cn.jir.ReturnStmt;
import edu.pku.cn.jir.Stmt;
import edu.pku.cn.jir.StringConstant;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;

// ����̵ı�����������[������type��Ϣ���������ͱ�����object�������������ͱ�����value����]���ð汾�ڿ���̷��������в�δ���� �������ȿ��ƣ�

public class InterPointsToDataflowAnalysis extends
		RefactoredBasicDataflowAnalysis<HashMap<InterJIRValue, InterVarInfo>> {

	public boolean Debug = false;

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;
	private HashMap<InterJIRValue, InterVarInfo> initFact;
	private int level = 0;
	private CallingContext callingContext;

	private InterJIRValue returnInterJIRValue;

	static public HashSet<String> callees = new HashSet<String>();
	
	static public ClassNodeLoader loader;

	// HashMap<JIRValue, IntraPointsToInfo> pointsToInfos = new
	// HashMap<JIRValue, IntraPointsToInfo>();

	public InterPointsToDataflowAnalysis(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
	}

	public InterPointsToDataflowAnalysis(CFG cfg, HashMap<InterJIRValue, InterVarInfo> initFact, int level,
			CallingContext callingContext) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
		this.initFact = initFact;
		this.level = level;
		this.callingContext = callingContext;
	}

	@Override
	public HashMap<InterJIRValue, InterVarInfo> createFact() {
		HashMap<InterJIRValue, InterVarInfo> fact = new HashMap<InterJIRValue, InterVarInfo>();
		return fact;
	}

	@Override
	public HashMap<InterJIRValue, InterVarInfo> createFact(HashMap<InterJIRValue, InterVarInfo> fact) {
		HashMap<InterJIRValue, InterVarInfo> newFact = new HashMap<InterJIRValue, InterVarInfo>();
		for (InterJIRValue interJIRValue : fact.keySet()) {
			newFact.put(interJIRValue, fact.get(interJIRValue).clone());
		}
		return newFact;
	}

	// @Override
	// public HashSet<String> getNewStartFact(BasicBlock block) {
	// return facts[block.startStmt];
	// }

	@Override
	public void initEntryFact() {
		if (this.initFact == null) {
			this.initFact = createFact();
		}
		startFactMap.put(blockOrder.getEntry(), this.initFact);
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
	}

	@Override
	public HashMap<InterJIRValue, InterVarInfo> merge(HashMap<InterJIRValue, InterVarInfo> start,
			HashMap<InterJIRValue, InterVarInfo> pred) {
		HashMap<InterJIRValue, InterVarInfo> result = this.createFact(start);

		for (InterJIRValue interJIRValue : pred.keySet()) {

			InterVarInfo info = pred.get(interJIRValue);
			if (result.get(interJIRValue) != null) {
				result.put(interJIRValue, result.get(interJIRValue).mergeWith(info));
			} else
				result.put(interJIRValue, info.clone());
		}
		return result;
	}

	@Override
	public boolean same(HashMap<InterJIRValue, InterVarInfo> fact1, HashMap<InterJIRValue, InterVarInfo> fact2) {
		if (fact1.keySet().size() != fact2.keySet().size())
			return false;

		for (InterJIRValue interJIRValue : fact1.keySet()) {
			if (fact2.get(interJIRValue) == null) {
				return false;
			}

			InterVarInfo fact1Info = fact1.get(interJIRValue);
			InterVarInfo fact2Info = fact2.get(interJIRValue);

			if (!fact1Info.equals(fact2Info)) {
				return false;
			}
		}

		return true;
	}

	private ClassNode getClassNode(String className) {
		Repository cha = Repository.getInstance();
		return cha.findClassNode(className);
	}

	private MethodNode getMethod(InvokeExpr in, ClassNode cn) {
		String meName = in.getMethodName();
		String meDesc = in.getMethodDesc();
		do {
			if (cn.containMethod(meName, meDesc))
				return cn.getMethod(meName, meDesc);
			else
				cn = cn.getSuperClass();
		} while (cn != null);
		return null;// �����Ŀ�û�м���
		// throw new RuntimeException("some error in static invoke");
	}

	@Override
	public HashMap<InterJIRValue, InterVarInfo> transferVertex(BasicBlock block) {

		HashMap<InterJIRValue, InterVarInfo> fact = this.createFact(getStartFact(block));

		if (fact == null)
			fact = new HashMap<InterJIRValue, InterVarInfo>();

		for (int i = block.startStmt; i <= block.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			// fact = facts[stmt.getIndex()];
			// System.out.println("i: " + i);

			if (stmt instanceof LineStmt) {
				LineStmt ls = (LineStmt) stmt;
				currentLine = ls.line;

			} else if (stmt instanceof IfStmt) {

			} else if (stmt instanceof AssignStmt) {
				
				AssignStmt assignStmt = (AssignStmt) stmt;
				fact = transferAssignmentStmt(fact, assignStmt.left, this.level, assignStmt.right, this.level);
				
			} else if (stmt instanceof InvokeStmt){
				
				InvokeStmt invokeStmt = (InvokeStmt)stmt;
				InterPointsToDataflowAnalysis.callees.add(((InvokeExpr)invokeStmt.invoke).toString());
				fact = interProceduralAnalysis(fact, null, this.level, invokeStmt.invoke, this.level, false);
				
			} else if (stmt instanceof ReturnStmt) {
				ReturnStmt returnStmt = (ReturnStmt) stmt;
				this.returnInterJIRValue = new InterJIRValue(returnStmt.value, this.level);
			}
		}

		// printTransferResult();
		return fact;

	}

	private HashMap<InterJIRValue, InterVarInfo> transferAssignmentStmt(HashMap<InterJIRValue, InterVarInfo> fact, 
			JIRValue leftValue, int leftValueLevel, 
			JIRValue rightValue, int rightValueLevel) {
		// a = x
		// a.f = x
		// a = x.f
		Type type = leftValue.getType();

		// ȷ����ֵ�������������
		if (type.getSort() >= Type.ARRAY) {

			if (leftValue instanceof LocalRef) {
				// e.g. a = x; a = x.f;

				if (rightValue instanceof AnyNewExpr) {
					// e.g.: a = new B();
					// e.g.: a = new B[]();

					if (rightValue instanceof NewExpr) {
						// a = new B();
						NewExpr newExpr = (NewExpr) rightValue;

						// new created object type
						Type objectType = newExpr.getType();
						// new created object naming with different
						// context
						String objectName = "O[" + newExpr.getType().toString() + "][" + currentLine + "]";

						if (this.callingContext != null)
							objectName += "[" + this.callingContext.toString() + "]";

						InterVarInfo newPointsToInfo = new InterVarInfo();
						newPointsToInfo.types.add(objectType);
						newPointsToInfo.objects.add(objectName);

						fact.put(new InterJIRValue(leftValue, leftValueLevel), newPointsToInfo);

					} else if (rightValue instanceof NewArrayExpr) {
						// a = new B[]();
						NewArrayExpr newExpr = (NewArrayExpr) rightValue;

						// new created object type
						Type objectType = newExpr.getType();
						// new created object naming
						String objectName = "O[" + newExpr.getType().toString() + "][" + currentLine + "]["
								+ newExpr.sizes.length + "]";

						if (this.callingContext != null)
							objectName += "[" + this.callingContext.toString() + "]";

						InterVarInfo newPointsToInfo = new InterVarInfo();
						newPointsToInfo.types.add(objectType);
						newPointsToInfo.objects.add(objectName);

						fact.put(new InterJIRValue(leftValue, leftValueLevel), newPointsToInfo);
					}

				} else if (rightValue instanceof StringConstant) {
					// e.g.: a = "string"

					Type objectType;
					String objectName = "";

					// ��ȡa�ı�����Ϣ����a�����������ref��aliasɾ��
					if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
						for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
							fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
						}
					}

					StringConstant stringConstant = (StringConstant) rightValue;
					objectType = stringConstant.getType();
					objectName = "O[" + objectType.toString() + "][" + currentLine + "]:"
							+ stringConstant.toString();

					InterVarInfo newPointsToInfo = new InterVarInfo();
					newPointsToInfo.types.add(objectType);
					newPointsToInfo.objects.add(objectName);
					fact.put(new InterJIRValue(leftValue, leftValueLevel), newPointsToInfo);

				} else if (rightValue instanceof Null) {
					// e.g.: a = null;

					// ��ȡa�ı�����Ϣ����a�����������ref��aliasɾ��
					if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
						for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
							fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
						}
					}

					// ���½���a��ָ����Ϣ
					InterVarInfo leftPointsToInfo = new InterVarInfo();
					leftPointsToInfo.types.add(leftValue.getType());

					fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);

				} else if (rightValue instanceof Ref) {
					// e.g.: a = b; a = b.c; a = @CaughException; a
					// ="string";

					// new created object type
					Type objectType;
					String objectName = "";

					// e.g.: e=@CaughException
					if (rightValue instanceof CaughtExceptionRef) {
						CaughtExceptionRef ref = (CaughtExceptionRef) rightValue;
						objectType = ref.getType();
						objectName = "O[" + ref.getType().toString() + "][" + currentLine + "]";
						InterVarInfo newPointsToInfo = new InterVarInfo();
						newPointsToInfo.types.add(objectType);
						newPointsToInfo.objects.add(objectName);
						fact.put(new InterJIRValue(leftValue, leftValueLevel), newPointsToInfo);

					} else if (rightValue instanceof LocalRef || rightValue instanceof FieldRef
							|| rightValue instanceof ArrayRef) {
						// e.g.: a = b;

						// ɾ��a�����й�����Ϣ
						InterVarInfo leftPointsToInfo = new InterVarInfo();
						InterVarInfo rightNodePointsToInfo = fact.get(new InterJIRValue(rightValue,
								rightValueLevel));

						// ��ȡa�ı�����Ϣ����a�����������ref��aliasɾ��
						if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
							for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
								fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
							}
						}

						// ��b��ָ����Ϣ����һ�ݸ�a
						if (rightNodePointsToInfo != null) {
							leftPointsToInfo = InterVarInfo.clone(rightNodePointsToInfo);
						} else {
							rightNodePointsToInfo = new InterVarInfo();
						}

						// ��a�ı�����Ϣ������b
						leftPointsToInfo.alias.add(new InterJIRValue(rightValue, rightValueLevel));

						// ����b�����ĸ���ref�ı�����Ϣ������a
						for (InterJIRValue alias : rightNodePointsToInfo.alias) {
							fact.get(alias).alias.add(new InterJIRValue(leftValue, leftValueLevel));
						}

						// ��b�ı�����Ϣ������a
						rightNodePointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
						fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);
						fact.put(new InterJIRValue(rightValue, rightValueLevel), rightNodePointsToInfo);

					}

				} else if (rightValue instanceof InvokeExpr) {
					
					InterPointsToDataflowAnalysis.callees.add(((InvokeExpr)rightValue).toString());
					fact = interProceduralAnalysis(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);

				} else if (rightValue instanceof CastExpr) {
					// e.g.: a = (A)b;
					CastExpr castExpr = (CastExpr) rightValue;

					// ɾ��a�����й�����Ϣ
					InterVarInfo leftPointsToInfo = new InterVarInfo();
					InterVarInfo rightNodePointsToInfo = fact.get(new InterJIRValue(castExpr.value,
							rightValueLevel));

					// ��ȡa�ı�����Ϣ����a�����������ref��aliasɾ��
					if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
						for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
							fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
						}
					}

					// ��b��ָ����Ϣ����һ�ݸ�a
					if (rightNodePointsToInfo != null) {
						leftPointsToInfo = InterVarInfo.clone(rightNodePointsToInfo);
					} else {
						rightNodePointsToInfo = new InterVarInfo();
					}

					// ��a�ı�����Ϣ������b
					leftPointsToInfo.alias.add(new InterJIRValue(castExpr.value, rightValueLevel));
					// ��a��������Ϣ����ΪcastExpr.type
					leftPointsToInfo.types.clear();
					leftPointsToInfo.types.add(castExpr.type);

					// ����b�����ĸ���ref�ı�����Ϣ������a
					for (InterJIRValue alias : rightNodePointsToInfo.alias) {
						fact.get(alias).alias.add(new InterJIRValue(leftValue, leftValueLevel));
					}

					// ��b�ı�����Ϣ������a
					rightNodePointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));

					fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);
					fact.put(new InterJIRValue(rightValue, rightValueLevel), rightNodePointsToInfo);

				}

			} else if (leftValue instanceof FieldRef) {
				// e.g. a.b = x; a.b = x.f;
				FieldRef leftFieldRef = (FieldRef) leftValue;

				if (rightValue instanceof AnyNewExpr) {
					// e.g.: a.b = new B();
					// e.g.: a.b = new B[]();

					if (rightValue instanceof NewExpr) {
						// a.b = new B();
						NewExpr newExpr = (NewExpr) rightValue;

						// new created object type
						Type objectType = newExpr.getType();
						// new created object naming
						String objectName = "O[" + newExpr.getType().toString() + "][" + currentLine + "]";

						if (this.callingContext != null)
							objectName += "[" + this.callingContext.toString() + "]";

						InterVarInfo newPointsToInfo = new InterVarInfo();
						newPointsToInfo.types.add(objectType);
						newPointsToInfo.objects.add(objectName);

						// ��ȡa.b�ı�����Ϣ����a.b�����������ref��aliasɾ��
						if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
							for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
								fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
								// ��ȡa������Ϣ������Ӧɾ��a.b�ı�����Ϣ
								InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
										leftFieldRef.base, leftValueLevel));
								if (baseInterPointsToInfo != null) {
									for (InterJIRValue alia : baseInterPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alia.intraJIRValue;
										fact.get(alias).alias.remove(new InterJIRValue(newfieldRef,
												alia.scopeLevel));
									}
								}
							}
						}

						// ��ȡa������Ϣ������Ӧ����a.b�ı�����Ϣ
						InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(leftFieldRef.base,
								leftValueLevel));
						if (baseInterPointsToInfo != null) {
							for (InterJIRValue alias : baseInterPointsToInfo.alias) {
								FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
								newfieldRef.base = alias.intraJIRValue;
								newPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
							}
						}

						// ����a.b��ָ����Ϣ
						fact.put(new InterJIRValue(leftValue, leftValueLevel), newPointsToInfo);

						// ����a.b�ı�����ָ����Ϣ������
						for (InterJIRValue alias : newPointsToInfo.alias) {
							InterVarInfo aliasPointsToInfo = newPointsToInfo.clone();
							// ������Ϣ��ȥ������
							aliasPointsToInfo.alias.remove(alias);
							// ������Ϣ����leftValue
							aliasPointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
							// Ϊalias���ӱ�����Ϣ
							fact.put(alias, aliasPointsToInfo);
						}

					} else if (rightValue instanceof NewArrayExpr) {
						// a.b = new B[]();
						NewArrayExpr newExpr = (NewArrayExpr) rightValue;

						// new created object type
						Type objectType = newExpr.getType();
						// new created object naming
						String objectName = "O[" + newExpr.getType().toString() + "][" + currentLine + "]["
								+ newExpr.sizes.length + "]";

						if (this.callingContext != null)
							objectName += "[" + this.callingContext.toString() + "]";

						InterVarInfo newPointsToInfo = new InterVarInfo();
						newPointsToInfo.types.add(objectType);
						newPointsToInfo.objects.add(objectName);

						// ��ȡa.b�ı�����Ϣ����a.b�����������ref��aliasɾ��
						if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
							for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
								fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
								// ��ȡa������Ϣ������Ӧɾ��a.b�ı�����Ϣ
								InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
										leftFieldRef.base, leftValueLevel));
								if (baseInterPointsToInfo != null) {
									for (InterJIRValue alia : baseInterPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alia.intraJIRValue;
										fact.get(alias).alias.remove(new InterJIRValue(newfieldRef,
												alia.scopeLevel));
									}
								}
							}
						}

						// ��ȡa������Ϣ������Ӧ����a.b�ı�����Ϣ
						InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(leftFieldRef.base,
								leftValueLevel));
						if (baseInterPointsToInfo != null) {
							for (InterJIRValue alias : baseInterPointsToInfo.alias) {
								FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
								newfieldRef.base = alias.intraJIRValue;
								newPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
							}
						}

						// ����a.b��ָ����Ϣ
						fact.put(new InterJIRValue(leftValue, leftValueLevel), newPointsToInfo);

						// ����a.b�ı�����ָ����Ϣ������
						for (InterJIRValue alias : newPointsToInfo.alias) {
							InterVarInfo aliasPointsToInfo = newPointsToInfo.clone();
							// ������Ϣ��ȥ������
							aliasPointsToInfo.alias.remove(alias);
							// ������Ϣ����leftValue
							aliasPointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
							// Ϊalias���ӱ�����Ϣ
							fact.put(alias, aliasPointsToInfo);
						}
					}

				} else if (rightValue instanceof StringConstant) {
					// e.g.: a.b = "string"

					Type objectType;
					String objectName = "";

					StringConstant stringConstant = (StringConstant) rightValue;
					objectType = stringConstant.getType();
					objectName = "O[" + objectType.toString() + "][" + currentLine + "]:"
							+ stringConstant.toString();

					if (this.callingContext != null)
						objectName = "[" + this.callingContext.toString() + "]";

					InterVarInfo newPointsToInfo = new InterVarInfo();
					newPointsToInfo.types.add(objectType);
					newPointsToInfo.objects.add(objectName);

					// ��ȡa.b�ı�����Ϣ����a.b�����������ref��aliasɾ��
					if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
						for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
							fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
							// ��ȡa������Ϣ������Ӧɾ��a.b�ı�����Ϣ
							InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
									leftFieldRef.base, leftValueLevel));
							if (baseInterPointsToInfo != null) {
								for (InterJIRValue alia : baseInterPointsToInfo.alias) {
									FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
									newfieldRef.base = alia.intraJIRValue;
									fact.get(alias).alias
											.remove(new InterJIRValue(newfieldRef, alia.scopeLevel));
								}
							}
						}
					}

					// ��ȡa������Ϣ������Ӧ����a.b�ı�����Ϣ
					InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(leftFieldRef.base,
							leftValueLevel));
					if (baseInterPointsToInfo != null) {
						for (InterJIRValue alias : baseInterPointsToInfo.alias) {
							FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
							newfieldRef.base = alias.intraJIRValue;
							newPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
						}
					}

					// ����a.b��ָ����Ϣ
					fact.put(new InterJIRValue(leftValue, leftValueLevel), newPointsToInfo);

					// ����a.b�ı�����ָ����Ϣ������
					for (InterJIRValue alias : newPointsToInfo.alias) {
						InterVarInfo aliasPointsToInfo = newPointsToInfo.clone();
						// ������Ϣ��ȥ������
						aliasPointsToInfo.alias.remove(alias);
						// ������Ϣ����leftValue
						aliasPointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
						// Ϊalias���ӱ�����Ϣ
						fact.put(alias, aliasPointsToInfo);
					}

				} else if (rightValue instanceof Null) {
					// e.g.: a.b = null;

					// ��ȡa.b�ı�����Ϣ����a.b�����������ref��aliasɾ��
					if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
						for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
							fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
							// ��ȡa������Ϣ������Ӧɾ��a.b�ı�����Ϣ
							InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
									leftFieldRef.base, leftValueLevel));
							if (baseInterPointsToInfo != null) {
								for (InterJIRValue alia : baseInterPointsToInfo.alias) {
									FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
									newfieldRef.base = alia.intraJIRValue;
									fact.get(alias).alias
											.remove(new InterJIRValue(newfieldRef, alia.scopeLevel));
								}
							}
						}
					}

					// ���½���a.b��ָ����Ϣ
					InterVarInfo leftPointsToInfo = new InterVarInfo();
					leftPointsToInfo.types.add(leftValue.getType());

					// �ҳ�a�ı�����Ϣ��������Ӧ�ı�����Ϣ���ӵ� a.b�ı�����Ϣ��
					InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(leftFieldRef.base,
							leftValueLevel));
					if (baseInterPointsToInfo != null) {
						for (InterJIRValue alias : baseInterPointsToInfo.alias) {
							FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
							newfieldRef.base = alias.intraJIRValue;
							leftPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
						}
					}

					// ����a.b��ָ����Ϣ
					fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);

					// ����a.b�ı�����ָ����Ϣ������
					for (InterJIRValue alias : leftPointsToInfo.alias) {
						InterVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
						// ������Ϣ��ȥ������
						aliasPointsToInfo.alias.remove(alias);
						// ������Ϣ����leftValue
						aliasPointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
						// Ϊalias���ӱ�����Ϣ
						fact.put(alias, aliasPointsToInfo);
					}

				} else if (rightValue instanceof Ref) {
					// e.g.: a = b; a = b.c; a = @CaughException; a
					// ="string";

					// new created object type
					Type objectType;
					String objectName = "";

					// e.g.: e=@CaughException
					if (rightValue instanceof CaughtExceptionRef) {
						// CaughtExceptionRef ref = (CaughtExceptionRef)
						// rightValue;
						// objectType = ref.getType();
						// objectName = "O[" + ref.getType().toString()
						// + "][" + currentLine + "]";
						// InterPointsToInfo newPointsToInfo = new
						// InterPointsToInfo();
						// newPointsToInfo.types.add(objectType);
						// newPointsToInfo.objects.add(objectName);
						// fact.put(leftValue, newPointsToInfo);
					} else if (rightValue instanceof FieldRef) {
						// e.g.: a.b = c.d;

						// ��ȡa.b�ı�����Ϣ����a.b�����������ref��aliasɾ��
						if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
							for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
								fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
								// ��ȡa������Ϣ������Ӧɾ��a.b�ı�����Ϣ
								InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
										leftFieldRef.base, leftValueLevel));
								if (baseInterPointsToInfo != null) {
									for (InterJIRValue alia : baseInterPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alia.intraJIRValue;
										fact.get(alias).alias.remove(new InterJIRValue(newfieldRef,
												alia.scopeLevel));
									}
								}
							}
						}

						// ɾ��a.b�����й�����Ϣ
						InterVarInfo leftPointsToInfo = new InterVarInfo();
						InterVarInfo rightNodePointsToInfo = fact.get(new InterJIRValue(rightValue,
								rightValueLevel));

						// ��c.d��ָ����Ϣ����һ�ݸ�a.b
						if (rightNodePointsToInfo != null) {
							leftPointsToInfo = InterVarInfo.clone(rightNodePointsToInfo);
						} else {
							rightNodePointsToInfo = new InterVarInfo();
						}

						// ��a.b�ı�����Ϣ������c.d
						leftPointsToInfo.alias.add(new InterJIRValue(rightValue, rightValueLevel));

						// �ҳ�c�ı���������Ӧ����c.d��Ӧ��δ�����ı�����Ϣ���磺c2.d��(����ĳ�������c2��ֵ��c1ʱ����field�������������޼��޵ģ���������Ǵ�ʱ�����д�����
						// ��ʹ�õ�����fieldʱ���д���,������ĳЩʱ�̻ᵼ�����)
						FieldRef rightFieldRef = (FieldRef) rightValue;
						InterVarInfo baseRightInterPointsToInfo = fact.get(new InterJIRValue(
								rightFieldRef.base, rightValueLevel));
						if (baseRightInterPointsToInfo != null) {
							for (InterJIRValue alias : baseRightInterPointsToInfo.alias) {
								FieldRef newfieldRef = (FieldRef) rightFieldRef.clone();
								newfieldRef.base = alias.intraJIRValue;
								leftPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
							}
						}

						// �ҳ�a�ı�����Ϣ��������Ӧ�ı�����Ϣ���ӵ� a.b�ı�����Ϣ��
						InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
								rightFieldRef.base, rightValueLevel));
						if (baseInterPointsToInfo != null) {
							for (InterJIRValue alias : baseInterPointsToInfo.alias) {
								FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
								newfieldRef.base = alias.intraJIRValue;
								leftPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
							}
						}

						// ����a.b��ָ����Ϣ
						fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);

						// ����a.b�ı�����ָ����Ϣ������
						for (InterJIRValue alias : leftPointsToInfo.alias) {
							InterVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
							// ������Ϣ��ȥ������
							aliasPointsToInfo.alias.remove(alias);
							// ������Ϣ����leftValue
							aliasPointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
							// Ϊalias���ӱ�����Ϣ
							fact.put(alias, aliasPointsToInfo);
						}

					} else if (rightValue instanceof LocalRef) {
						// e.g.: a.b = b;

						// ��ȡa.b�ı�����Ϣ����a.b�����������ref��aliasɾ��
						if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
							for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
								fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));

								// ��ȡa������Ϣ������Ӧɾ��a.b�ı�����Ϣ
								InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
										leftFieldRef.base, leftValueLevel));
								if (baseInterPointsToInfo != null) {
									for (InterJIRValue alia : baseInterPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alia.intraJIRValue;
										fact.get(alias).alias.remove(new InterJIRValue(newfieldRef,
												alia.scopeLevel));
									}
								}
							}
						}

						// ɾ��a.b�����й�����Ϣ
						InterVarInfo leftPointsToInfo = new InterVarInfo();
						InterVarInfo rightNodePointsToInfo = fact.get(new InterJIRValue(rightValue,
								rightValueLevel));

						// ��b��ָ����Ϣ����һ�ݸ�a.b
						if (rightNodePointsToInfo != null) {
							leftPointsToInfo = InterVarInfo.clone(rightNodePointsToInfo);
						} else {
							rightNodePointsToInfo = new InterVarInfo();
						}

						// ��a.b�ı�����Ϣ������b
						leftPointsToInfo.alias.add(new InterJIRValue(rightValue, rightValueLevel));

						// �ҳ�a�ı�����Ϣ��������Ӧ�ı�����Ϣ���ӵ� a.b�ı�����Ϣ��
						InterJIRValue baseInterJIRValue = new InterJIRValue(leftFieldRef.base, leftValueLevel);
						InterVarInfo baseInterPointsToInfo = fact.get(baseInterJIRValue);

						if (baseInterPointsToInfo != null) {
							for (InterJIRValue alias : baseInterPointsToInfo.alias) {
								FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
								newfieldRef.base = alias.intraJIRValue;
								leftPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
							}
						}

						// ����a.b��ָ����Ϣ
						fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);

						// ����a.b�ı�����ָ����Ϣ������
						for (InterJIRValue alias : leftPointsToInfo.alias) {
							InterVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
							// ������Ϣ��ȥ������
							aliasPointsToInfo.alias.remove(alias);
							// ������Ϣ����leftValue
							aliasPointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
							// Ϊalias���ӱ�����Ϣ
							fact.put(alias, aliasPointsToInfo);
						}

					} else if (rightValue instanceof ArrayRef) {
						// a.b = b[1]; a.b = b[i];

						// ��ȡa.b�ı�����Ϣ����a.b�����������ref��aliasɾ��
						if (fact.get(new InterJIRValue(leftValue, leftValueLevel)) != null) {
							for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
								fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
								// ��ȡa������Ϣ������Ӧɾ��a.b�ı�����Ϣ
								InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
										leftFieldRef.base, leftValueLevel));
								if (baseInterPointsToInfo != null) {
									for (InterJIRValue alia : baseInterPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alia.intraJIRValue;
										fact.get(alias).alias.remove(new InterJIRValue(newfieldRef,
												alia.scopeLevel));
									}
								}
							}
						}

						// ɾ��a.b�����й�����Ϣ
						InterVarInfo leftPointsToInfo = new InterVarInfo();
						InterVarInfo rightNodePointsToInfo = fact.get(new InterJIRValue(rightValue,
								rightValueLevel));

						// ��b[i]��ָ����Ϣ����һ�ݸ�a.b
						if (rightNodePointsToInfo != null) {
							leftPointsToInfo = InterVarInfo.clone(rightNodePointsToInfo);
						} else {
							rightNodePointsToInfo = new InterVarInfo();
						}

						// ��a.b�ı�����Ϣ������b[i]
						leftPointsToInfo.alias.add(new InterJIRValue(rightValue, rightValueLevel));

						// �ҳ�a�ı�����Ϣ��������Ӧ�ı�����Ϣ���ӵ� a.b�ı�����Ϣ��
						InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(leftFieldRef.base,
								leftValueLevel));
						if (baseInterPointsToInfo != null) {
							for (InterJIRValue alias : baseInterPointsToInfo.alias) {
								FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
								newfieldRef.base = alias.intraJIRValue;
								leftPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
							}
						}

						// ����a.b��ָ����Ϣ
						fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);

						// ����a.b�ı�����ָ����Ϣ������
						for (InterJIRValue alias : leftPointsToInfo.alias) {
							InterVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
							// ������Ϣ��ȥ������
							aliasPointsToInfo.alias.remove(alias);
							// ������Ϣ����leftValue
							aliasPointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
							// Ϊalias���ӱ�����Ϣ
							fact.put(alias, aliasPointsToInfo);
						}
					}

				} else if (rightValue instanceof InvokeExpr) {
					// e.g.: a.d = b.f(c);
					// A f(d){ return e}
					InterPointsToDataflowAnalysis.callees.add(((InvokeExpr)rightValue).toString());
					fact = interProceduralAnalysis(fact, leftValue, leftValueLevel, rightValue, rightValueLevel, true);
					
				} else if (rightValue instanceof CastExpr) {
					// e.g.: a.b = (A)b;

					CastExpr castExpr = (CastExpr) rightValue;

					// ��ȡa.b�ı�����Ϣ����a.b�����������ref��alias��ɾ��
					InterVarInfo leftPointsToInfo = fact.get(new InterJIRValue(leftValue, leftValueLevel));
					if (leftPointsToInfo != null) {
						for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
							fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
							// ��ȡa������Ϣ������Ӧɾ��a.b�ı�����Ϣ
							InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(
									leftFieldRef.base, leftValueLevel));
							if (baseInterPointsToInfo != null) {
								for (InterJIRValue alia : baseInterPointsToInfo.alias) {
									FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
									newfieldRef.base = alia.intraJIRValue;
									fact.get(alias).alias
											.remove(new InterJIRValue(newfieldRef, alia.scopeLevel));
								}
							}
						}
					}

					// ɾ��a.b�����й�����Ϣ
					leftPointsToInfo = new InterVarInfo();
					InterVarInfo rightNodePointsToInfo = fact.get(new InterJIRValue(castExpr.value,
							rightValueLevel));

					// ��b��ָ����Ϣ����һ�ݸ�a.b
					if (rightNodePointsToInfo != null) {
						leftPointsToInfo = InterVarInfo.clone(rightNodePointsToInfo);
					} else {
						rightNodePointsToInfo = new InterVarInfo();
					}

					// ��a.b�ı�����Ϣ������b
					leftPointsToInfo.alias.add(new InterJIRValue(castExpr.value, rightValueLevel));

					// ��a��������Ϣ����ΪcastExpr.type
					leftPointsToInfo.types.clear();
					leftPointsToInfo.types.add(castExpr.type);

					// �ҳ�a�ı�����Ϣ��������Ӧ�ı�����Ϣ���ӵ� a.b�ı�����Ϣ��
					InterVarInfo baseInterPointsToInfo = fact.get(new InterJIRValue(leftFieldRef.base,
							leftValueLevel));
					if (baseInterPointsToInfo != null) {
						for (InterJIRValue alias : baseInterPointsToInfo.alias) {
							FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
							newfieldRef.base = alias.intraJIRValue;
							leftPointsToInfo.alias.add(new InterJIRValue(newfieldRef, alias.scopeLevel));
						}
					}

					// ����a.b��ָ����Ϣ
					fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);

					// ����a.b�ı�����ָ����Ϣ������
					for (InterJIRValue alias : leftPointsToInfo.alias) {
						InterVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
						// ������Ϣ��ȥ������
						aliasPointsToInfo.alias.remove(alias);
						// ������Ϣ����leftValue
						aliasPointsToInfo.alias.add(new InterJIRValue(leftValue, leftValueLevel));
						// Ϊalias���ӱ�����Ϣ
						fact.put(alias, aliasPointsToInfo);
					}

				}

			} else if (leftValue instanceof ArrayRef) {
				// a[1] = new A(); a[i] = "String";

			}

		}
		return fact;
	}

	private HashMap<InterJIRValue, InterVarInfo> interProceduralAnalysis(HashMap<InterJIRValue, InterVarInfo> fact, 
			JIRValue leftValue, int leftValueLevel, JIRValue rightValue, int rightValueLevel, boolean handleResturn) {
		// e.g.: a = b.f(c);
		// A f(d){ return e}

		InvokeExpr invoke = (InvokeExpr) rightValue;

		// invoke.node.ower ����������
		// todo ͨ��ָ���������е�type��Ϣ�����м���
		String classNameToAnalyze = "";
		InterVarInfo interPointsToInfo = fact
				.get(new InterJIRValue(invoke.invoker, rightValueLevel));
		if (interPointsToInfo != null) {
			Set<Type> types = interPointsToInfo.types;
			if (types.size() > 1) {
				// ģ������
				classNameToAnalyze = ((Type) types.toArray()[0]).getClassName();

			} else if (types.size() == 1) {
				// ��ȷ����
				classNameToAnalyze = ((Type) types.toArray()[0]).getClassName();

			} else {
				// �޷��������ģ������

			}
		}

		if ("".equals(classNameToAnalyze)) {
			classNameToAnalyze = invoke.node.owner;
		}

		ClassNode cn = getClassNode(classNameToAnalyze);

		boolean coarseAnalysis = false;

		if (cn == null) {
			Type invokeType = Type.getObjectType(invoke.node.owner);
			if (invokeType != null && invokeType.getSort() == Type.ARRAY)
				cn = getClassNode("java.lang.Object");
			else {
				//System.err.println("can not find class " + invoke.node.owner);
				coarseAnalysis = true;
			}
		}

		if (cn != null) {
			MethodNode me = getMethod(invoke, cn);
			if (me != null) {
				// �ɹ�����callee���������п���̷�������

				if (invoke.isStatic() || !me.isAbstract()) {// �ݹ����

					CFG calleeCFG = me.getCFG();
					//System.out.println("-> step into callee:" + calleeCFG.getMethod().name);

					int calleeLevel = leftValueLevel + 1;
					CallingContext callingContext = new CallingContext(cfg.getMethod(),
							this.currentLine);
					HashMap<InterJIRValue, InterVarInfo> calleeInitFact = fact;

					// ���� this = b
					JIRValue actualParam = invoke.invoker;
					InterJIRValue interActualParam = new InterJIRValue(actualParam, leftValueLevel);
					LocalRef formalParamJIRValue = new LocalRef((LocalVariableNode) calleeCFG
							.getMethod().localVariables.get(0));
					InterJIRValue interFormalParam = new InterJIRValue(formalParamJIRValue,
							calleeLevel);
					
					transferAssignmentStmt(calleeInitFact, interFormalParam.intraJIRValue, interFormalParam.scopeLevel,
							interActualParam.intraJIRValue, interActualParam.scopeLevel);
					
					// ���δ������������� d = c

					List<LocalVariableNode> formalParams = calleeCFG.getMethod().params;
					List<JIRValue> actualParams = invoke.params;

					for (int j = 0; j < actualParams.size(); j++) {
						actualParam = actualParams.get(j);
						interActualParam = new InterJIRValue(actualParam, leftValueLevel);

						formalParamJIRValue = new LocalRef(formalParams.get(j));
						interFormalParam = new InterJIRValue(formalParamJIRValue, calleeLevel);

						transferAssignmentStmt(calleeInitFact, interFormalParam.intraJIRValue, interFormalParam.scopeLevel,
								interActualParam.intraJIRValue, interActualParam.scopeLevel);

					}

					// ��calleeִ�еݹ����
					InterPointsToDataflowAnalysis calleeAnlysis = new InterPointsToDataflowAnalysis(
							calleeCFG, calleeInitFact, calleeLevel, callingContext);
					try {

						calleeAnlysis.execute();

						// �����ز���
						// a = e;
						
						HashMap<InterJIRValue, InterVarInfo> calleeResultFact = calleeAnlysis
								.getResultFact(calleeCFG.getExit());
						
						if (handleResturn == true && leftValue != null) {
							transferAssignmentStmt(calleeResultFact, leftValue, leftValueLevel,
									calleeAnlysis.returnInterJIRValue.intraJIRValue, calleeAnlysis.returnInterJIRValue.scopeLevel);
						}

						// ɾ��callee����
						filterUnreachableVariables(calleeResultFact);
						
						fact = calleeResultFact;

						// anlysis.examineResults();
					} catch (AnalyzerException e) {

						e.printStackTrace();
					}

					//System.out.println("<- step out callee:" + calleeCFG.getMethod().name);

				} else {
					// �ǳ��󷽷����ӿڷ��������麯��������ʱ����ģ������
					coarseAnalysis = true;
				}
			} else {
				// �޷�����calleeԴ�룬����ģ������
				// ָ����Ϣ�������κα仯�����Է�����Ϣ���д���
				// ��ȡa�ı�����Ϣ����a�����������ref��aliasɾ��
				coarseAnalysis = true;

			}

		} else {
			coarseAnalysis = true;
		}

		// ����ģ������
		if (coarseAnalysis == true && leftValue != null) {
			InterVarInfo leftPointsToInfo = fact.get(new InterJIRValue(leftValue, leftValueLevel));
			if (leftPointsToInfo != null) {
				for (InterJIRValue alias : fact.get(new InterJIRValue(leftValue, leftValueLevel)).alias) {
					fact.get(alias).alias.remove(new InterJIRValue(leftValue, leftValueLevel));
				}
			}

			// ���½���a��ָ����Ϣ
			leftPointsToInfo = new InterVarInfo();
			leftPointsToInfo.types.add(invoke.getType());

			String callingContext = "";
			if (this.callingContext != null) {
				callingContext = this.callingContext.toString();
			}

			leftPointsToInfo.objects.add("O[" + invoke.getType().toString() + "][" + currentLine
					+ "][" + callingContext + "]");
			fact.put(new InterJIRValue(leftValue, leftValueLevel), leftPointsToInfo);

		}
		return fact;
	}

	private void filterUnreachableVariables(HashMap<InterJIRValue, InterVarInfo> calleeResultFact) {
		HashMap<InterJIRValue, InterVarInfo> newFact = this.createFact(calleeResultFact);
		for (InterJIRValue key : newFact.keySet()) {
			if (key.scopeLevel > this.level) {
				calleeResultFact.remove(key);
			} else {
				Set<InterJIRValue> alias = newFact.get(key).alias;
				for (InterJIRValue alia : alias) {
					if (alia.scopeLevel > this.level) {
						calleeResultFact.get(key).alias.remove(alia);
					}
				}
			}
		}
	}

	private void propagateSideEffects(HashMap<InterJIRValue, InterVarInfo> fact, InterJIRValue leftValue,
			InterJIRValue rightValue) {

		if (leftValue.intraJIRValue instanceof LocalRef) {
			propagateSideEffectsForLocalRef(fact, leftValue, rightValue);
		} else if (leftValue.intraJIRValue instanceof FieldRef) {
			
		}
		

	}

	private void propagateSideEffectsForFieldRef(HashMap<InterJIRValue, InterVarInfo> fact, InterJIRValue leftValue,
			InterJIRValue rightValue){
		
	}
	
	// a = b; a = b.c; a = b[i];
	private void propagateSideEffectsForLocalRef(HashMap<InterJIRValue, InterVarInfo> fact, InterJIRValue leftValue,
			InterJIRValue rightValue){
//		 e.g.: a (leftValue) = b (rightValue); a = b.c;

		// ɾ��a�����й�����Ϣ
		InterVarInfo leftPointsToInfo = new InterVarInfo();
		InterVarInfo rightNodePointsToInfo = fact.get(rightValue);

		// ��ȡa�ı�����Ϣ����a�����������ref��aliasɾ��
		if (fact.get(leftValue) != null) {
			for (InterJIRValue alias : fact.get(leftValue).alias) {
				fact.get(alias).alias.remove(leftValue);
			}
		}

		// ��b��ָ����Ϣ����һ�ݸ�a
		if (rightNodePointsToInfo == null) {
			rightNodePointsToInfo = new InterVarInfo();
			rightNodePointsToInfo.types.add(rightValue.intraJIRValue.getType());
		}
		leftPointsToInfo = InterVarInfo.clone(rightNodePointsToInfo);

		// ��a�ı�����Ϣ������b
		leftPointsToInfo.alias.add(rightValue);

		// ����b�����ĸ���ref�ı�����Ϣ������a
		for (InterJIRValue alias : rightNodePointsToInfo.alias) {
			fact.get(alias).alias.add(leftValue);
		}

		// ��b�ı�����Ϣ������a
		rightNodePointsToInfo.alias.add(leftValue);
		fact.put(leftValue, leftPointsToInfo);
		fact.put(rightValue, rightNodePointsToInfo);
	}
	
	private void printTransferResult(HashMap<InterJIRValue, InterVarInfo> fact) {
		for (InterJIRValue interJIRValue : fact.keySet()) {
			System.out.println(interJIRValue.toString() + "->");
			System.out.println(fact.get(interJIRValue).toString());
		}
	}

	protected void initEntryBlock() {
		this.initEntryFact();
		HashMap<InterJIRValue, InterVarInfo> result = this.transferVertex(logicalEntryBlock());
		this.setResultFact(logicalEntryBlock(), result);
	}

	protected BasicBlock logicalEntryBlock() {
		if (blockOrder instanceof ReversePostOrder)
			return cfg.getRoot();
		else
			return cfg.getExit();
	}

	private static final int MAX_ITERS = 10;
	private int numIterations = 0;
	private final boolean DEBUG = false;

	protected String getFullyQualifiedMethodName() {
		return cfg.getMethod().name;
	}

	protected Iterator<Edge> logicalPredecessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.inEdgeIterator() : block.outEdgeIterator();
	}

	protected Iterator<Edge> logicalSuccessorEdgeIterator(BasicBlock block) {
		return isForwards ? block.outEdgeIterator() : block.inEdgeIterator();
	}

	public synchronized void execute() throws AnalyzerException {
		boolean change;
		// OpcodeUtil.printInsnList(insns, cfg.getMethod().name +
		// cfg.getMethod().desc);
		initEntryBlock();

		int timestamp = 0;
		do {
			change = false;
			// if(numIterations >= 10)
			// break;
			if (numIterations >= MAX_ITERS) {
				System.out.println("Too many iterations (" + numIterations + ") in dataflow when analyzing "
						+ getFullyQualifiedMethodName());
				break;
			}

			startIteration();

			Iterator<BasicBlock> i = blockOrder.blockIterator();
			while (i.hasNext()) {
				BasicBlock block = i.next();
				HashMap<InterJIRValue, InterVarInfo> start = getStartFact(block);
				HashMap<InterJIRValue, InterVarInfo> result = getResultFact(block);

				boolean isEndBlock = isEndBlock(block);
				boolean needToRecompute = false;

				if (!isEndBlock && start != null) {// if the original start is
					// not equal to new start,
					// need to recompute
					if (getNewStartFact(block) == null) {
						needToRecompute = true;
						setNewStartFact(block, createFact(start));
					}

					Iterator<Edge> predEdgeIter = logicalPredecessorEdgeIterator(block);
					HashMap<InterJIRValue, InterVarInfo> newStart = new HashMap<InterJIRValue, InterVarInfo>();
					if (block.equals(logicalEntryBlock())) {
						newStart = this.initFact;
					}

					// get all predecessors' result fact and merge them with the
					// new start fact
					int preEdgeNumber = 0;
					while (predEdgeIter.hasNext()) {
						preEdgeNumber++;
						Edge edge = predEdgeIter.next();
						BasicBlock logicalPred = isForwards ? edge.getSource() : edge.getTarget();
						HashMap<InterJIRValue, InterVarInfo> pred = null;
						// ////////////perhaps we need to distinguish different
						// handlers
						if (edge.getType() != EdgeType.HANDLED_EXCEPTION_EDGE) {
							if (this.blockOrder instanceof ReversePostOrder) {
								pred = createFact(getResultFact(logicalPred));
								if (preEdgeNumber == 1) {
									newStart = this.createFact(pred);
								} else {
									newStart = merge(newStart, pred);
								}
							}
						}
					}

					// if the generated startBack is different from start, need
					// to recompute
					if (!same(newStart, start)) {
						needToRecompute = true;
						start = newStart;
					}
				}
				// if really need to recompute, recompute the current block's
				// frames
				if (needToRecompute && !isEndBlock) {
					setLastUpdateTimestamp(start, timestamp);
					setStartFact(block, createFact(start)); // set
					// start
					// facts
					// in
					// startFactMap
					setNewStartFact(block, createFact(start));// set
					// start
					// facts
					// in
					// blocks

					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number before calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key + "    value = " + map.get(key).toString());
							}
						}
					}

					HashMap<InterJIRValue, InterVarInfo> newResult = transferVertex(block);

					if (newResult != null && !same(newResult, result)) {
						setLastUpdateTimestamp(newResult, timestamp);
						setResultFact(block, createFact(newResult));
						change = true;
					}
					if (DEBUG) {
						if (Thread.activeCount() > 1) {
							Map map = Thread.getAllStackTraces();
							System.out.println("The thread number after calling transferVertex");
							for (Object key : map.keySet()) {
								System.out.println("key = " + key);
								StackTraceElement[] stack = (StackTraceElement[]) (map.get(key));
								for (int ii = 0; ii < stack.length; ii++) {
									System.out.println(stack[ii]);
								}
							}
						}
					}
				}
				if (DEBUG) {
					// System.out.println(block.toString());
				}
			}
			finishIteration();

			//examineResults();

			++numIterations;
			++timestamp;
		} while (change);

		return;
	}

	public void examineResults() {
		System.out.println("*****************************************");
		System.out.println(this.cfg.getMethod().name);
		System.out.println(this.numIterations);
		Iterator<BasicBlock> i = this.cfg.blockIterator();
		while (i.hasNext()) {
			BasicBlock block = i.next();
			HashMap<InterJIRValue, InterVarInfo> start = this.getStartFact(block);

			// result �����⣬����һ��block��start��û�������
			HashMap<InterJIRValue, InterVarInfo> result = this.getResultFact(block);

			System.out.println("________________________________");

			this.printTransferResult(start);

			System.out.println("");

			for (int m = block.startStmt; m <= block.endStmt; m++) {
				System.out.println(m + ": " + block.stmts.get(m).toString());
			}

			System.out.println("");

			this.printTransferResult(result);

			System.out.println("");

			System.out.println("________________________________");
		}	
	}

	public void printCallees() {
		System.out.println("*****************************************");
		System.out.println(this.cfg.getMethod().name);
		System.out.println(this.numIterations);
		
		for (String callee : callees) {
			System.out.println(callee);
		}	
	}
	
	public static void main(String[] args) {

		AnalysisFactoryManager.initial();

		Project project = new Project("bin/");
		CodaProperties.isLibExpland = true;
		project.addLibPath("lib/");

		ClassNodeLoader loader = new ClassNodeLoader("bin/");
		ClassNode cc = loader.loadClassNode("edu.pku.cn.testcase.TestInterPointsToInfo", 0);

		InterPointsToDataflowAnalysis.loader = loader;

		for (MethodNode method : cc.methods) {

			method.getStmts();
			CFG cfg = method.getCFG();

			InterPointsToDataflowAnalysis analysis = new InterPointsToDataflowAnalysis(cfg);
			try {
				analysis.execute();
				analysis.printCallees();
				// anlysis.examineResults();
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

// end
