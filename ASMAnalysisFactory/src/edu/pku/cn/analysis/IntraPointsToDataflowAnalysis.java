/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-25 锟斤拷锟斤拷04:31:40
 * @modifier: Administrator
 * @time 2009-3-25 锟斤拷锟斤拷04:31:40
 * @reviewer: Administrator
 * @time 2009-3-25 锟斤拷锟斤拷04:31:40
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import automachine.AutoMachine;
import automachine.AutoMachineException;
import automachine.AutomaUtil;
import automachine.VisitMethodInsnEdge;
import edu.pku.cn.analysis.RefactoredBasicDataflowAnalysis;
import edu.pku.cn.analysis.RefactoredDataflow;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.detector.Detector;
import edu.pku.cn.detector.RefactoredDataflowTestDriver;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.jir.AnyNewExpr;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.CastExpr;
import edu.pku.cn.jir.CaughtExceptionRef;
import edu.pku.cn.jir.Constant;
import edu.pku.cn.jir.FieldRef;
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
import edu.pku.cn.jir.TempRef;
import edu.pku.cn.point.PointsToAnalysis;
import edu.pku.cn.point.PointsToSet;
import edu.pku.cn.ptg.AllocEdge;
import edu.pku.cn.ptg.AllocateNode;
import edu.pku.cn.ptg.ArrayNode;
import edu.pku.cn.ptg.AssignEdge;
import edu.pku.cn.ptg.Edge;
import edu.pku.cn.ptg.FieldNode;
import edu.pku.cn.ptg.InvokeNode;
import edu.pku.cn.ptg.LocalNode;
import edu.pku.cn.ptg.Node;
import edu.pku.cn.ptg.ParameterNode;
import edu.pku.cn.ptg.PointToGraph;
import edu.pku.cn.util.HASelect;

public class IntraPointsToDataflowAnalysis extends
		RefactoredBasicDataflowAnalysis<HashMap<JIRValue, HashSet<AutoMachine>>> {

	public boolean Debug = false;

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;

	HashMap<JIRValue, RefVarInfo> pointsToInfos = new HashMap<JIRValue, RefVarInfo>();

	public IntraPointsToDataflowAnalysis(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;		 
	}

	@Override
	public HashMap<JIRValue, HashSet<AutoMachine>> createFact() {
		HashMap<JIRValue, HashSet<AutoMachine>> fact = new HashMap<JIRValue, HashSet<AutoMachine>>();
		return fact;
	}

	@Override
	public HashMap<JIRValue, HashSet<AutoMachine>> createFact(HashMap<JIRValue, HashSet<AutoMachine>> fact) {
		return fact;
	}

	// @Override
	// public HashSet<String> getNewStartFact(BasicBlock block) {
	// return facts[block.startStmt];
	// }

	@Override
	public void initEntryFact() {
		HashMap rdFact = createFact();
		startFactMap.put(blockOrder.getEntry(), rdFact);
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
	}

	@Override
	public HashMap merge(HashMap start, HashMap pred) {
		HashMap result = createFact(start);
		return result;
	}

	@Override
	public boolean same(HashMap fact1, HashMap fact2) {
		return true;
	}

	@Override
	public HashMap<JIRValue, HashSet<AutoMachine>> transferVertex(BasicBlock block) {

		HashMap<JIRValue, HashSet<AutoMachine>> fact = this.createFact(getStartFact(block));

		if (fact == null)
			fact = new HashMap<JIRValue, HashSet<AutoMachine>>();

		for (int i = block.startStmt; i <= block.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			// fact = facts[stmt.getIndex()];
			System.out.println("i: " + i);

			if (stmt instanceof LineStmt) {
				LineStmt ls = (LineStmt) stmt;
				currentLine = ls.line;

			} else if (stmt instanceof AssignStmt) {
				// a = x
				// a.f = x
				// a = x.f

				AssignStmt as = (AssignStmt) stmt;
				Type type = as.left.getType();

				// 确保赋值语句是引用类型
				if (type.getSort() >= Type.ARRAY) {

					if (as.left instanceof LocalRef) {
						// e.g. a = x; a = x.f;

						if (as.right instanceof AnyNewExpr) {
							// e.g.: a = new B();
							// e.g.: a = new B[]();

							if (as.right instanceof NewExpr) {
								// a = new B();
								NewExpr newExpr = (NewExpr) as.right;

								// new created object type
								Type objectType = newExpr.getType();
								// new created object naming
								String objectName = "O[" + newExpr.getType().toString() + "][" + currentLine + "]";

								RefVarInfo newPointsToInfo = new RefVarInfo();
								newPointsToInfo.types.add(objectType);
								newPointsToInfo.objects.add(objectName);

								this.pointsToInfos.put(as.left, newPointsToInfo);

							} else if (as.right instanceof NewArrayExpr) {
								// a = new B[]();
								NewArrayExpr newExpr = (NewArrayExpr) as.right;

								// new created object type
								Type objectType = newExpr.getType();
								// new created object naming
								String objectName = "O[" + newExpr.getType().toString() + "][" + currentLine + "]["
										+ newExpr.sizes.length + "]";

								RefVarInfo newPointsToInfo = new RefVarInfo();
								newPointsToInfo.types.add(objectType);
								newPointsToInfo.objects.add(objectName);

								this.pointsToInfos.put(as.left, newPointsToInfo);
							}

						} else if (as.right instanceof StringConstant) {
							// e.g.: a = "string"

							Type objectType;
							String objectName = "";

							// 获取a的别名信息，将a从与其别名的ref的alias删除
							if (this.pointsToInfos.get(as.left) != null) {
								for (JIRValue alias : this.pointsToInfos.get(as.left).alias) {
									this.pointsToInfos.get(alias).alias.remove(as.left);
								}
							}

							StringConstant stringConstant = (StringConstant) as.right;
							objectType = stringConstant.getType();
							objectName = "O[" + objectType.toString() + "][" + currentLine + "]:"
									+ stringConstant.toString();

							RefVarInfo newPointsToInfo = new RefVarInfo();
							newPointsToInfo.types.add(objectType);
							newPointsToInfo.objects.add(objectName);
							this.pointsToInfos.put(as.left, newPointsToInfo);

						} else if (as.right instanceof Ref) {
							// e.g.: a = b; a = b.c; a = @CaughException; a
							// ="string";

							// new created object type
							Type objectType;
							String objectName = "";

							// e.g.: e=@CaughException
							if (as.right instanceof CaughtExceptionRef) {
								CaughtExceptionRef ref = (CaughtExceptionRef) as.right;
								objectType = ref.getType();
								objectName = "O[" + ref.getType().toString() + "][" + currentLine + "]";
								RefVarInfo newPointsToInfo = new RefVarInfo();
								newPointsToInfo.types.add(objectType);
								newPointsToInfo.objects.add(objectName);
								this.pointsToInfos.put(as.left, newPointsToInfo);
							} else if (as.right instanceof FieldRef) {
								// e.g.: a = b.c;

							} else if (as.right instanceof LocalRef) {
								// e.g.: a = b;

								// 删除a的所有关联信息
								RefVarInfo leftPointsToInfo = new RefVarInfo();
								RefVarInfo rightNodePointsToInfo = this.pointsToInfos.get(as.right);

								// 获取a的别名信息，将a从与其别名的ref的alias删除
								if (this.pointsToInfos.get(as.left) != null) {
									for (JIRValue alias : this.pointsToInfos.get(as.left).alias) {
										this.pointsToInfos.get(alias).alias.remove(as.left);
									}
								}

								// 将b所指的信息拷贝一份给a
								if (rightNodePointsToInfo != null) {
									leftPointsToInfo = RefVarInfo.clone(rightNodePointsToInfo);
								} else {
									rightNodePointsToInfo = new RefVarInfo();
								}
								
								// 在a的别名信息中增加b
								leftPointsToInfo.alias.add(as.right);

								// 将与b别名的各个ref的别名信息中增加a
								for (JIRValue alias : rightNodePointsToInfo.alias) {
									this.pointsToInfos.get(alias).alias.add(as.left);
								}

//								 在b的别名信息中增加a
								rightNodePointsToInfo.alias.add(as.left);
								
								this.pointsToInfos.put(as.left, leftPointsToInfo);
								this.pointsToInfos.put(as.right, rightNodePointsToInfo);

							} else if (as.right instanceof ArrayRef) {
								// a = b[1]; a = b[i];

							} else if (as.right instanceof Null) {
								// e.g.: a = null;

								// 获取a的别名信息，将a从与其别名的ref的alias删除
								if (this.pointsToInfos.get(as.left) != null) {
									for (JIRValue alias : this.pointsToInfos.get(as.left).alias) {
										this.pointsToInfos.get(alias).alias.remove(as.left);
									}
								}

								// 重新建立a的指向信息
								RefVarInfo leftPointsToInfo = new RefVarInfo();
								leftPointsToInfo.types.add(as.left.getType());

								this.pointsToInfos.put(as.left, leftPointsToInfo);

							}
						} else if (as.right instanceof InvokeExpr) {
							// e.g.: a = b.f();

							InvokeExpr invoke = (InvokeExpr) as.right;

							// 获取a的别名信息，将a从与其别名的ref的alias删除
							RefVarInfo leftPointsToInfo = this.pointsToInfos.get(as.left);
							if (leftPointsToInfo != null) {
								for (JIRValue alias : this.pointsToInfos.get(as.left).alias) {
									this.pointsToInfos.get(alias).alias.remove(as.left);
								}
							}

							// 重新建立a的指向信息
							leftPointsToInfo = new RefVarInfo();
							leftPointsToInfo.types.add(invoke.getType());
							leftPointsToInfo.objects.add("O[" + invoke.getType().toString() + "][" + currentLine + "]");
							this.pointsToInfos.put(as.left, leftPointsToInfo);

						} else if (as.right instanceof CastExpr) {
							// e.g.: a = (A)b;

							CastExpr castExpr = (CastExpr) as.right;

							// 删除a的所有关联信息
							RefVarInfo leftPointsToInfo = new RefVarInfo();
							RefVarInfo rightNodePointsToInfo = this.pointsToInfos.get(castExpr.value);

							// 获取a的别名信息，将a从与其别名的ref的alias删除
							if (this.pointsToInfos.get(as.left) != null) {
								for (JIRValue alias : this.pointsToInfos.get(as.left).alias) {
									this.pointsToInfos.get(alias).alias.remove(as.left);
								}
							}

							// 将b所指的信息拷贝一份给a
							if (rightNodePointsToInfo != null) {
								leftPointsToInfo = RefVarInfo.clone(rightNodePointsToInfo);
							} else {
								rightNodePointsToInfo = new RefVarInfo();
							}

							// 在a的别名信息中增加b
							leftPointsToInfo.alias.add(castExpr.value);
							// 将a的类型信息更改为castExpr.type
							leftPointsToInfo.types.clear();
							leftPointsToInfo.types.add(castExpr.type);

							// 将与b别名的各个ref的别名信息中增加a
							for (JIRValue alias : rightNodePointsToInfo.alias) {
								this.pointsToInfos.get(alias).alias.add(as.left);
							}

							// 在b的别名信息中增加a
							rightNodePointsToInfo.alias.add(as.left);

							this.pointsToInfos.put(as.left, leftPointsToInfo);
							this.pointsToInfos.put(as.right, rightNodePointsToInfo);

						}

					} else if (as.left instanceof FieldRef) {
						// // e.g. a.b = x; a.b = x.f;
						//
						// if (as.right instanceof AnyNewExpr) {
						// // e.g.: a = new B();
						// // e.g.: a = new B[]();
						//
						// if (as.right instanceof NewExpr) {
						// // a = new B();
						// NewExpr newExpr = (NewExpr) as.right;
						//
						// // new created object type
						// Type objectType = newExpr.getType();
						// // new created object naming
						// String objectName = "O[" +
						// newExpr.getType().toString() + "][" + currentLine +
						// "]";
						//
						// IntraPointsToInfo newPointsToInfo = new
						// IntraPointsToInfo();
						// newPointsToInfo.types.add(objectType);
						// newPointsToInfo.objects.add(objectName);
						//
						// this.pointsToInfos.put(as.left, newPointsToInfo);
						//
						// } else if (as.right instanceof NewArrayExpr) {
						// // a = new B[]();
						// NewArrayExpr newExpr = (NewArrayExpr) as.right;
						//
						// // new created object type
						// Type objectType = newExpr.getType();
						// // new created object naming
						// String objectName = "O[" +
						// newExpr.getType().toString() + "][" + currentLine +
						// "]["
						// + newExpr.sizes.length + "]";
						//
						// IntraPointsToInfo newPointsToInfo = new
						// IntraPointsToInfo();
						// newPointsToInfo.types.add(objectType);
						// newPointsToInfo.objects.add(objectName);
						//
						// this.pointsToInfos.put(as.left, newPointsToInfo);
						// }
						//
						// } else if (as.right instanceof StringConstant) {
						// // e.g.: a = "string"
						//							
						// Type objectType;
						// String objectName = "";
						//							
						// StringConstant stringConstant = (StringConstant)
						// as.right;
						// objectType = stringConstant.getType();
						// objectName = "O[" + objectType.toString() + "][" +
						// currentLine + "]:"
						// + stringConstant.toString();
						//
						// IntraPointsToInfo newPointsToInfo = new
						// IntraPointsToInfo();
						// newPointsToInfo.types.add(objectType);
						// newPointsToInfo.objects.add(objectName);
						// this.pointsToInfos.put(as.left, newPointsToInfo);
						//
						// } else if (as.right instanceof Ref) {
						// // e.g.: a = b; a = b.c; a = @CaughException; a
						// // ="string";
						//
						// // new created object type
						// Type objectType;
						// String objectName = "";
						//
						// // e.g.: e=@CaughException
						// if (as.right instanceof CaughtExceptionRef) {
						// CaughtExceptionRef ref = (CaughtExceptionRef)
						// as.right;
						// objectType = ref.getType();
						// objectName = "O[" + ref.getType().toString() + "][" +
						// currentLine + "]";
						// IntraPointsToInfo newPointsToInfo = new
						// IntraPointsToInfo();
						// newPointsToInfo.types.add(objectType);
						// newPointsToInfo.objects.add(objectName);
						// this.pointsToInfos.put(as.left, newPointsToInfo);
						// } else if (as.right instanceof FieldRef) {
						// // e.g.: a = b.c;
						//
						// } else if (as.right instanceof LocalRef) {
						// // e.g.: a = b;
						//
						// // 删除a的所有关联信息
						// IntraPointsToInfo leftPointsToInfo = new
						// IntraPointsToInfo();
						// IntraPointsToInfo rightNodePointsToInfo =
						// this.pointsToInfos.get(as.right);
						// // 将b所指的信息拷贝一份给a
						// if (rightNodePointsToInfo != null) {
						// leftPointsToInfo =
						// IntraPointsToInfo.clone(rightNodePointsToInfo);
						// } else {
						// rightNodePointsToInfo = new IntraPointsToInfo();
						// }
						//
						// // 在a的别名信息中增加b
						// leftPointsToInfo.alias.add(as.right);
						//
						// // 将与b别名的各个ref的别名信息中增加a
						// for (JIRValue alias : rightNodePointsToInfo.alias) {
						// this.pointsToInfos.get(alias).alias.add(as.left);
						// }
						//
						// // 在b的别名信息中增加a
						// rightNodePointsToInfo.alias.add(as.left);
						//								
						// this.pointsToInfos.put(as.left, leftPointsToInfo);
						// this.pointsToInfos.put(as.right,
						// rightNodePointsToInfo);
						//								
						// } else if (as.right instanceof ArrayRef){
						// // a = b[1]; a = b[i];
						//								
						// } else if (as.right instanceof Null) {
						// // e.g.: a = null;
						//								
						// // 获取a的别名信息，将a从与其别名的ref的alias删除
						// for(JIRValue alias:
						// this.pointsToInfos.get(as.left).alias){
						// this.pointsToInfos.get(alias).alias.remove(as.left);
						// }
						//								
						// // 重新建立a的指向信息
						// IntraPointsToInfo leftPointsToInfo = new
						// IntraPointsToInfo();
						// leftPointsToInfo.types.add(as.left.getType());
						//								
						// this.pointsToInfos.put(as.left, leftPointsToInfo);
						//
						// }
						// } else if (as.right instanceof InvokeExpr) {
						// // e.g.: a = b.f();
						//
						// InvokeExpr invoke = (InvokeExpr) as.right;
						//							
						// // 获取a的别名信息，将a从与其别名的ref的alias删除
						// IntraPointsToInfo leftPointsToInfo =
						// this.pointsToInfos.get(as.left);
						// if(leftPointsToInfo != null){
						// for(JIRValue alias:
						// this.pointsToInfos.get(as.left).alias){
						// this.pointsToInfos.get(alias).alias.remove(as.left);
						// }
						// }
						//							
						// // 重新建立a的指向信息
						// leftPointsToInfo = new IntraPointsToInfo();
						// leftPointsToInfo.types.add(invoke.getType());
						// leftPointsToInfo.objects.add("O[" +
						// invoke.getType().toString() + "][" + currentLine +
						// "]");
						// this.pointsToInfos.put(as.left, leftPointsToInfo);
						//							
						// } else if (as.right instanceof CastExpr) {
						// // e.g.: a = (A)b;
						//							
						// CastExpr castExpr = (CastExpr)as.right;
						//							
						// // 删除a的所有关联信息
						// IntraPointsToInfo leftPointsToInfo = new
						// IntraPointsToInfo();
						// IntraPointsToInfo rightNodePointsToInfo =
						// this.pointsToInfos.get(castExpr.value);
						//							
						// // 将b所指的信息拷贝一份给a
						// if (rightNodePointsToInfo != null) {
						// leftPointsToInfo =
						// IntraPointsToInfo.clone(rightNodePointsToInfo);
						// } else {
						// rightNodePointsToInfo = new IntraPointsToInfo();
						// }
						//
						// // 在a的别名信息中增加b
						// leftPointsToInfo.alias.add(castExpr.value);
						// // 将a的类型信息更改为castExpr.type
						// leftPointsToInfo.types.clear();
						// leftPointsToInfo.types.add(castExpr.type);
						//							
						// // 将与b别名的各个ref的别名信息中增加a
						// for (JIRValue alias : rightNodePointsToInfo.alias) {
						// this.pointsToInfos.get(alias).alias.add(as.left);
						// }
						//
						// // 在b的别名信息中增加a
						// rightNodePointsToInfo.alias.add(as.left);
						//							
						// this.pointsToInfos.put(as.left, leftPointsToInfo);
						// this.pointsToInfos.put(as.right,
						// rightNodePointsToInfo);
						//							
						// }
						//
					} else if (as.left instanceof ArrayRef) {

					}

				}
			} else if (stmt instanceof ReturnStmt) {

			}
		}

		printTransferResult();
		return fact;

	}

	private void printTransferResult() {

		for (JIRValue jirValue : this.pointsToInfos.keySet()) {
			System.out.println(jirValue.toString() + "->");
			System.out.println(this.pointsToInfos.get(jirValue).toString());
		}

	}

	public static void main(String[] args) {
		RefactoredDataflowTestDriver<HashMap<JIRValue, HashSet<AutoMachine>>, IntraPointsToDataflowAnalysis> driver = new RefactoredDataflowTestDriver<HashMap<JIRValue, HashSet<AutoMachine>>, IntraPointsToDataflowAnalysis>() {
			public RefactoredDataflow<HashMap<JIRValue, HashSet<AutoMachine>>, IntraPointsToDataflowAnalysis> createDataflow(
					ClassNode cc, MethodNode method) {
				// TODO Auto-generated method stub
				try {
					CFG cfg = method.getCFG();

					PointToGraph ptg = new PointToGraph();
					ptg.setParams(method.params);
					Node returnNode = new LocalNode();
					returnNode.type = Type.getReturnType(method.desc);

					IntraPointsToDataflowAnalysis analysis = new IntraPointsToDataflowAnalysis(cfg);

					RefactoredDataflow<HashMap<JIRValue, HashSet<AutoMachine>>, IntraPointsToDataflowAnalysis> dataflow = new RefactoredDataflow<HashMap<JIRValue, HashSet<AutoMachine>>, IntraPointsToDataflowAnalysis>(
							cfg, analysis);

					dataflow.execute();

					return dataflow;
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void examineResults(
					RefactoredDataflow<HashMap<JIRValue, HashSet<AutoMachine>>, IntraPointsToDataflowAnalysis> dataflow) {
				// TODO Auto-generated method stub
				// HashSet<String>[] facts = dataflow.getAnalysis().facts;
				// List<Stmt> stmts =
				// dataflow.getAnalysis().cfg.getMethod().getStmts();
				// for (int i = 0; i < facts.length; i++) {
				// System.out.println(i + ": " + stmts.get(i).toString() + " ->
				// " + facts[i]);
				// }
				// Iterator<BasicBlock> i =
				// dataflow.getAnalysis().cfg.blockIterator();
				// while (i.hasNext()) {
				// BasicBlock block = i.next();
				// HashMap<JIRValue, HashSet<AutoMachine>> start =
				// dataflow.getAnalysis().getStartFact(block);
				//
				// // result 有问题，但下一个block的start是没有问题的
				// HashMap<JIRValue, HashSet<AutoMachine>> result =
				// dataflow.getAnalysis().getResultFact(block);
				//
				// System.out.println("________________________________");
				// Set<JIRValue> keys = start.keySet();
				//
				// for (JIRValue key : keys) {
				// System.out.println(key.toString());
				// HashSet<AutoMachine> machines = (HashSet<AutoMachine>)
				// start.get(key);
				//
				// for (AutoMachine machine : machines) {
				// System.out.println(" " + machine.automachineName + ";"
				// + machine.currentState.getStateNumber() + " " +
				// machine.lastStateChangingLine);
				// }
				// }
				// System.out.println("");
				// for (int m = block.startStmt; m <= block.endStmt; m++) {
				// System.out.println(m + ": " + block.stmts.get(m).toString());
				// }
				// System.out.println("");
				//
				// keys = result.keySet();
				// for (JIRValue key : keys) {
				// System.out.println(key.toString());
				// HashSet<AutoMachine> machines = (HashSet<AutoMachine>)
				// result.get(key);
				//
				// for (AutoMachine machine : machines) {
				// System.out.println(" " + machine.automachineName + ";"
				// + machine.currentState.getStateNumber() + " " +
				// machine.lastStateChangingLine);
				// }
				// }
				//
				// System.out.println("________________________________");

				// }

			}
		};
		driver.execute("TestPointsToInfo");
	}
}

// end
