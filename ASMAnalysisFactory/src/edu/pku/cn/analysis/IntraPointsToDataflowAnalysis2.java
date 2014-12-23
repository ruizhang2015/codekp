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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.cfg.BasicBlock;
import edu.pku.cn.graph.cfg.CFG;
import edu.pku.cn.graph.cfg.Edge;
import edu.pku.cn.graph.cfg.EdgeType;
import edu.pku.cn.graph.visit.ReversePostOrder;
import edu.pku.cn.jir.AnyNewExpr;
import edu.pku.cn.jir.ArrayRef;
import edu.pku.cn.jir.AssignStmt;
import edu.pku.cn.jir.BinopExpr;
import edu.pku.cn.jir.CastExpr;
import edu.pku.cn.jir.CaughtExceptionRef;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.InvokeExpr;
import edu.pku.cn.jir.JIR;
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

// 该版本与IntraPointsToDataflowAnalysis的过程内的指向分析不同之处在于：对FieldRef 进行了分析，如 a.b = c.d 之类的语句，该版本的分析更加准确

public class IntraPointsToDataflowAnalysis2 extends
		RefactoredBasicDataflowAnalysis<HashMap<JIRValue, RefVarInfo>> {

	public boolean Debug = false;

	int currentLine;
	String desc;
	boolean isInConstructor = false;

	private InsnList insns;
	private List<Stmt> stmts;
	private CFG cfg;

	// HashMap<JIRValue, IntraPointsToInfo> pointsToInfos = new
	// HashMap<JIRValue, IntraPointsToInfo>();

	public IntraPointsToDataflowAnalysis2(CFG cfg) {
		this.blockOrder = new ReversePostOrder(cfg);
		this.isForwards = true;
		this.cfg = cfg;
		this.stmts = cfg.getJIRStmt();
		this.insns = cfg.getMethod().instructions;
	}

	@Override
	public HashMap<JIRValue, RefVarInfo> createFact() {
		HashMap<JIRValue, RefVarInfo> fact = new HashMap<JIRValue, RefVarInfo>();
		return fact;
	}

	@Override
	public HashMap<JIRValue, RefVarInfo> createFact(HashMap<JIRValue, RefVarInfo> fact) {
		HashMap<JIRValue, RefVarInfo> newFact = new HashMap<JIRValue, RefVarInfo>();
		for (JIRValue jirValue : fact.keySet()) {
			newFact.put(jirValue, fact.get(jirValue).clone());
		}
		return newFact;
	}

	// @Override
	// public HashSet<String> getNewStartFact(BasicBlock block) {
	// return facts[block.startStmt];
	// }

	@Override
	public void initEntryFact() {
		HashMap<JIRValue, RefVarInfo> rdFact = createFact();
		startFactMap.put(blockOrder.getEntry(), rdFact);
	}

	@Override
	public boolean isEndBlock(BasicBlock block) {
		return block.getStartInc() >= insns.size();
	}

	@Override
	public HashMap<JIRValue, RefVarInfo> merge(HashMap<JIRValue, RefVarInfo> start,
			HashMap<JIRValue, RefVarInfo> pred) {
		HashMap<JIRValue, RefVarInfo> result = this.createFact(start);

		for (JIRValue jirValue : pred.keySet()) {

			RefVarInfo info = pred.get(jirValue);
			if (result.get(jirValue) != null) {
				result.put(jirValue, result.get(jirValue).mergeWith(info));
			} else
				result.put(jirValue, info.clone());
		}
		return result;
	}

	@Override
	public boolean same(HashMap<JIRValue, RefVarInfo> fact1, HashMap<JIRValue, RefVarInfo> fact2) {
		if (fact1.keySet().size() != fact2.keySet().size())
			return false;

		for (JIRValue jirValue : fact1.keySet()) {
			if (fact2.get(jirValue) == null) {
				return false;
			}

			if (!fact1.get(jirValue).equals(fact2.get(jirValue))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public HashMap<JIRValue, RefVarInfo> transferVertex(BasicBlock block) {

		HashMap<JIRValue, RefVarInfo> fact = this.createFact(getStartFact(block));

		if (fact == null)
			fact = new HashMap<JIRValue, RefVarInfo>();

		for (int i = block.startStmt; i <= block.endStmt; i++) {
			Stmt stmt = this.stmts.get(i);
			// fact = facts[stmt.getIndex()];
			// System.out.println("i: " + i);

			if (stmt instanceof LineStmt) {
				LineStmt ls = (LineStmt) stmt;
				currentLine = ls.line;

			} else if (stmt instanceof IfStmt) {

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

								fact.put(as.left, newPointsToInfo);

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

								fact.put(as.left, newPointsToInfo);
							}

						} else if (as.right instanceof StringConstant) {
							// e.g.: a = "string"

							Type objectType;
							String objectName = "";

							// 获取a的别名信息，将a从与其别名的ref的alias删除
							if (fact.get(as.left) != null) {
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
								}
							}

							StringConstant stringConstant = (StringConstant) as.right;
							objectType = stringConstant.getType();
							objectName = "O[" + objectType.toString() + "][" + currentLine + "]:"
									+ stringConstant.toString();

							RefVarInfo newPointsToInfo = new RefVarInfo();
							newPointsToInfo.types.add(objectType);
							newPointsToInfo.objects.add(objectName);
							fact.put(as.left, newPointsToInfo);

						} else if (as.right instanceof Null) {
							// e.g.: a = null;

							// 获取a的别名信息，将a从与其别名的ref的alias删除
							if (fact.get(as.left) != null) {
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
								}
							}

							// 重新建立a的指向信息
							RefVarInfo leftPointsToInfo = new RefVarInfo();
							leftPointsToInfo.types.add(as.left.getType());

							fact.put(as.left, leftPointsToInfo);

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
								fact.put(as.left, newPointsToInfo);
							} else if (as.right instanceof LocalRef || as.right instanceof FieldRef
									|| as.right instanceof ArrayRef) {
								// e.g.: a = b;

								// 删除a的所有关联信息
								RefVarInfo leftPointsToInfo = new RefVarInfo();
								RefVarInfo rightNodePointsToInfo = fact.get(as.right);

								// 获取a的别名信息，将a从与其别名的ref的alias删除
								if (fact.get(as.left) != null) {
									for (JIRValue alias : fact.get(as.left).alias) {
										fact.get(alias).alias.remove(as.left);
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
									fact.get(alias).alias.add(as.left);
								}

								// 在b的别名信息中增加a
								rightNodePointsToInfo.alias.add(as.left);

								fact.put(as.left, leftPointsToInfo);
								fact.put(as.right, rightNodePointsToInfo);

							}
						} else if (as.right instanceof InvokeExpr) {
							// e.g.: a = b.f();

							InvokeExpr invoke = (InvokeExpr) as.right;

							// 获取a的别名信息，将a从与其别名的ref的alias删除
							RefVarInfo leftPointsToInfo = fact.get(as.left);
							if (leftPointsToInfo != null) {
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
								}
							}

							// 重新建立a的指向信息
							leftPointsToInfo = new RefVarInfo();
							leftPointsToInfo.types.add(invoke.getType());
							leftPointsToInfo.objects.add("O[" + invoke.getType().toString() + "][" + currentLine + "]");
							fact.put(as.left, leftPointsToInfo);

						} else if (as.right instanceof CastExpr) {
							// e.g.: a = (A)b;

							CastExpr castExpr = (CastExpr) as.right;

							// 删除a的所有关联信息
							RefVarInfo leftPointsToInfo = new RefVarInfo();
							RefVarInfo rightNodePointsToInfo = fact.get(castExpr.value);

							// 获取a的别名信息，将a从与其别名的ref的alias删除
							if (fact.get(as.left) != null) {
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
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
								fact.get(alias).alias.add(as.left);
							}

							// 在b的别名信息中增加a
							rightNodePointsToInfo.alias.add(as.left);

							fact.put(as.left, leftPointsToInfo);
							fact.put(as.right, rightNodePointsToInfo);

						}

					} else if (as.left instanceof FieldRef) {
						// e.g. a.b = x; a.b = x.f;
						FieldRef leftFieldRef = (FieldRef) as.left;

						if (as.right instanceof AnyNewExpr) {
							// e.g.: a.b = new B();
							// e.g.: a.b = new B[]();

							if (as.right instanceof NewExpr) {
								// a.b = new B();
								NewExpr newExpr = (NewExpr) as.right;

								// new created object type
								Type objectType = newExpr.getType();
								// new created object naming
								String objectName = "O[" + newExpr.getType().toString() + "][" + currentLine + "]";

								RefVarInfo newPointsToInfo = new RefVarInfo();
								newPointsToInfo.types.add(objectType);
								newPointsToInfo.objects.add(objectName);

								// 获取a.b的别名信息，将a.b从与其别名的ref的alias删除
								if (fact.get(as.left) != null) {
									for (JIRValue alias : fact.get(as.left).alias) {
										fact.get(alias).alias.remove(as.left);
										// 获取a别名信息，并相应删除a.b的别名信息
										RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
										if (baseIntraPointsToInfo != null) {
											for (JIRValue alia : baseIntraPointsToInfo.alias) {
												FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
												newfieldRef.base = alia;
												fact.get(alias).alias.remove(newfieldRef);
											}
										}
									}
								}

								// 获取a别名信息，并相应增加a.b的别名信息
								RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
								if (baseIntraPointsToInfo != null) {
									for (JIRValue alias : baseIntraPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alias;
										newPointsToInfo.alias.add(newfieldRef);
									}
								}

								// 保存a.b的指向信息
								fact.put(as.left, newPointsToInfo);

								// 计算a.b的别名的指向信息并保存
								for (JIRValue alias : newPointsToInfo.alias) {
									RefVarInfo aliasPointsToInfo = newPointsToInfo.clone();
									// 别名信息中去除本身
									aliasPointsToInfo.alias.remove(alias);
									// 别名信息增加as.left
									aliasPointsToInfo.alias.add(as.left);
									// 为alias增加别名信息
									fact.put(alias, aliasPointsToInfo);
								}

							} else if (as.right instanceof NewArrayExpr) {
								// a.b = new B[]();
								NewArrayExpr newExpr = (NewArrayExpr) as.right;

								// new created object type
								Type objectType = newExpr.getType();
								// new created object naming
								String objectName = "O[" + newExpr.getType().toString() + "][" + currentLine + "]["
										+ newExpr.sizes.length + "]";

								RefVarInfo newPointsToInfo = new RefVarInfo();
								newPointsToInfo.types.add(objectType);
								newPointsToInfo.objects.add(objectName);

								// 获取a.b的别名信息，将a.b从与其别名的ref的alias删除
								if (fact.get(as.left) != null) {
									for (JIRValue alias : fact.get(as.left).alias) {
										fact.get(alias).alias.remove(as.left);
										// 获取a别名信息，并相应删除a.b的别名信息
										RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
										if (baseIntraPointsToInfo != null) {
											for (JIRValue alia : baseIntraPointsToInfo.alias) {
												FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
												newfieldRef.base = alia;
												fact.get(alias).alias.remove(newfieldRef);
											}
										}
									}
								}

								// 获取a别名信息，并相应增加a.b的别名信息
								RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
								if (baseIntraPointsToInfo != null) {
									for (JIRValue alias : baseIntraPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alias;
										newPointsToInfo.alias.add(newfieldRef);
									}
								}

								// 保存a.b的指向信息
								fact.put(as.left, newPointsToInfo);

								// 计算a.b的别名的指向信息并保存
								for (JIRValue alias : newPointsToInfo.alias) {
									RefVarInfo aliasPointsToInfo = newPointsToInfo.clone();
									// 别名信息中去除本身
									aliasPointsToInfo.alias.remove(alias);
									// 别名信息增加as.left
									aliasPointsToInfo.alias.add(as.left);
									// 为alias增加别名信息
									fact.put(alias, aliasPointsToInfo);
								}
							}

						} else if (as.right instanceof StringConstant) {
							// e.g.: a.b = "string"

							Type objectType;
							String objectName = "";

							StringConstant stringConstant = (StringConstant) as.right;
							objectType = stringConstant.getType();
							objectName = "O[" + objectType.toString() + "][" + currentLine + "]:"
									+ stringConstant.toString();

							RefVarInfo newPointsToInfo = new RefVarInfo();
							newPointsToInfo.types.add(objectType);
							newPointsToInfo.objects.add(objectName);

							// 获取a.b的别名信息，将a.b从与其别名的ref的alias删除
							if (fact.get(as.left) != null) {
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
									// 获取a别名信息，并相应删除a.b的别名信息
									RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
									if (baseIntraPointsToInfo != null) {
										for (JIRValue alia : baseIntraPointsToInfo.alias) {
											FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
											newfieldRef.base = alia;
											fact.get(alias).alias.remove(newfieldRef);
										}
									}
								}
							}

							// 获取a别名信息，并相应增加a.b的别名信息
							RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
							if (baseIntraPointsToInfo != null) {
								for (JIRValue alias : baseIntraPointsToInfo.alias) {
									FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
									newfieldRef.base = alias;
									newPointsToInfo.alias.add(newfieldRef);
								}
							}

							// 保存a.b的指向信息
							fact.put(as.left, newPointsToInfo);

							// 计算a.b的别名的指向信息并保存
							for (JIRValue alias : newPointsToInfo.alias) {
								RefVarInfo aliasPointsToInfo = newPointsToInfo.clone();
								// 别名信息中去除本身
								aliasPointsToInfo.alias.remove(alias);
								// 别名信息增加as.left
								aliasPointsToInfo.alias.add(as.left);
								// 为alias增加别名信息
								fact.put(alias, aliasPointsToInfo);
							}

						} else if (as.right instanceof Null) {
							// e.g.: a.b = null;

							// 获取a.b的别名信息，将a.b从与其别名的ref的alias删除
							if (fact.get(as.left) != null) {
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
									// 获取a别名信息，并相应删除a.b的别名信息
									RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
									if (baseIntraPointsToInfo != null) {
										for (JIRValue alia : baseIntraPointsToInfo.alias) {
											FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
											newfieldRef.base = alia;
											fact.get(alias).alias.remove(newfieldRef);
										}
									}
								}
							}

							// 重新建立a.b的指向信息
							RefVarInfo leftPointsToInfo = new RefVarInfo();
							leftPointsToInfo.types.add(as.left.getType());

							// 找出a的别名信息，并将相应的别名信息增加到 a.b的别名信息中
							RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
							if (baseIntraPointsToInfo != null) {
								for (JIRValue alias : baseIntraPointsToInfo.alias) {
									FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
									newfieldRef.base = alias;
									leftPointsToInfo.alias.add(newfieldRef);
								}
							}

							// 保存a.b的指向信息
							fact.put(as.left, leftPointsToInfo);

							// 计算a.b的别名的指向信息并保存
							for (JIRValue alias : leftPointsToInfo.alias) {
								RefVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
								// 别名信息中去除本身
								aliasPointsToInfo.alias.remove(alias);
								// 别名信息增加as.left
								aliasPointsToInfo.alias.add(as.left);
								// 为alias增加别名信息
								fact.put(alias, aliasPointsToInfo);
							}

						} else if (as.right instanceof Ref) {
							// e.g.: a = b; a = b.c; a = @CaughException; a
							// ="string";

							// new created object type
							Type objectType;
							String objectName = "";

							// e.g.: e=@CaughException
							if (as.right instanceof CaughtExceptionRef) {
								// CaughtExceptionRef ref = (CaughtExceptionRef)
								// as.right;
								// objectType = ref.getType();
								// objectName = "O[" + ref.getType().toString()
								// + "][" + currentLine + "]";
								// IntraPointsToInfo newPointsToInfo = new
								// IntraPointsToInfo();
								// newPointsToInfo.types.add(objectType);
								// newPointsToInfo.objects.add(objectName);
								// fact.put(as.left, newPointsToInfo);
							} else if (as.right instanceof FieldRef) {
								// e.g.: a.b = c.d;

								// 获取a.b的别名信息，将a.b从与其别名的ref的alias删除
								if (fact.get(as.left) != null) {
									for (JIRValue alias : fact.get(as.left).alias) {
										fact.get(alias).alias.remove(as.left);
										// 获取a别名信息，并相应删除a.b对应的别名信息（如：a2.b）
										RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
										if (baseIntraPointsToInfo != null) {
											for (JIRValue alia : baseIntraPointsToInfo.alias) {
												FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
												newfieldRef.base = alia;
												fact.get(alias).alias.remove(newfieldRef);
											}
										}
									}
								}

								// 删除a.b的所有关联信息
								RefVarInfo leftPointsToInfo = new RefVarInfo();
								RefVarInfo rightNodePointsToInfo = fact.get(as.right);

								// 将c.d所指的信息拷贝一份给a.b
								if (rightNodePointsToInfo != null) {
									leftPointsToInfo = RefVarInfo.clone(rightNodePointsToInfo);
								} else {
									rightNodePointsToInfo = new RefVarInfo();
								}

								// 在a.b的别名信息中增加c.d
								leftPointsToInfo.alias.add(as.right);

								// 找出c的别名，并相应增加c.d对应的未传播的别名信息（如：c2.d）(当把某个类对象c2赋值给c1时，其field的数量可能是无极限的，处理策略是此时不进行传播，
								// 当使用到具体field时进行传播,可能在某些时刻会导致误差)
								FieldRef rightFieldRef = (FieldRef) as.right;
								RefVarInfo baseRightIntraPointsToInfo = fact.get(rightFieldRef.base);
								if (baseRightIntraPointsToInfo != null) {
									for (JIRValue alias : baseRightIntraPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) rightFieldRef.clone();
										newfieldRef.base = alias;
										leftPointsToInfo.alias.add(newfieldRef);

									}
								}

								// 找出a的别名信息，并将相应的别名信息增加到 a.b的别名信息中
								RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
								if (baseIntraPointsToInfo != null) {
									for (JIRValue alias : baseIntraPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alias;
										leftPointsToInfo.alias.add(newfieldRef);

									}
								}

								// 保存a.b的指向信息
								fact.put(as.left, leftPointsToInfo);

								// 计算a.b的别名的指向信息并保存
								for (JIRValue alias : leftPointsToInfo.alias) {
									RefVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
									// 别名信息中去除本身
									aliasPointsToInfo.alias.remove(alias);
									// 别名信息增加as.left
									aliasPointsToInfo.alias.add(as.left);
									// 为alias增加别名信息
									fact.put(alias, aliasPointsToInfo);
								}

							} else if (as.right instanceof LocalRef) {
								// e.g.: a.b = b;

								// 获取a.b的别名信息，将a.b从与其别名的ref的alias删除
								if (fact.get(as.left) != null) {
									for (JIRValue alias : fact.get(as.left).alias) {
										fact.get(alias).alias.remove(as.left);
										// 获取a别名信息，并相应删除a.b的别名信息
										RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
										if (baseIntraPointsToInfo != null) {
											for (JIRValue alia : baseIntraPointsToInfo.alias) {
												FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
												newfieldRef.base = alia;
												fact.get(alias).alias.remove(newfieldRef);
											}
										}
									}
								}

								// 删除a.b的所有关联信息
								RefVarInfo leftPointsToInfo = new RefVarInfo();
								RefVarInfo rightNodePointsToInfo = fact.get(as.right);
								// 将b所指的信息拷贝一份给a.b
								if (rightNodePointsToInfo != null) {
									leftPointsToInfo = RefVarInfo.clone(rightNodePointsToInfo);
								} else {
									rightNodePointsToInfo = new RefVarInfo();
								}

								// 在a.b的别名信息中增加b
								leftPointsToInfo.alias.add(as.right);

								// 找出a的别名信息，并将相应的别名信息增加到 a.b的别名信息中
								RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
								if (baseIntraPointsToInfo != null) {
									for (JIRValue alias : baseIntraPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alias;
										leftPointsToInfo.alias.add(newfieldRef);
									}
								}

								// 保存a.b的指向信息
								fact.put(as.left, leftPointsToInfo);

								// 计算a.b的别名的指向信息并保存
								for (JIRValue alias : leftPointsToInfo.alias) {
									RefVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
									// 别名信息中去除本身
									aliasPointsToInfo.alias.remove(alias);
									// 别名信息增加as.left
									aliasPointsToInfo.alias.add(as.left);
									// 为alias增加别名信息
									fact.put(alias, aliasPointsToInfo);
								}

							} else if (as.right instanceof ArrayRef) {
								// a.b = b[1]; a.b = b[i];

								// 获取a.b的别名信息，将a.b从与其别名的ref的alias删除
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
									// 获取a别名信息，并相应删除a.b的别名信息
									RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
									if (baseIntraPointsToInfo != null) {
										for (JIRValue alia : baseIntraPointsToInfo.alias) {
											FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
											newfieldRef.base = alia;
											fact.get(alias).alias.remove(newfieldRef);
										}
									}
								}

								// 删除a.b的所有关联信息
								RefVarInfo leftPointsToInfo = new RefVarInfo();
								RefVarInfo rightNodePointsToInfo = fact.get(as.right);

								// 将b[i]所指的信息拷贝一份给a.b
								if (rightNodePointsToInfo != null) {
									leftPointsToInfo = RefVarInfo.clone(rightNodePointsToInfo);
								} else {
									rightNodePointsToInfo = new RefVarInfo();
								}

								// 在a.b的别名信息中增加b[i]
								leftPointsToInfo.alias.add(as.right);

								// 找出a的别名信息，并将相应的别名信息增加到 a.b的别名信息中
								RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
								if (baseIntraPointsToInfo != null) {
									for (JIRValue alias : baseIntraPointsToInfo.alias) {
										FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
										newfieldRef.base = alias;
										leftPointsToInfo.alias.add(newfieldRef);
									}
								}

								// 保存a.b的指向信息
								fact.put(as.left, leftPointsToInfo);

								// 计算a.b的别名的指向信息并保存
								for (JIRValue alias : leftPointsToInfo.alias) {
									RefVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
									// 别名信息中去除本身
									aliasPointsToInfo.alias.remove(alias);
									// 别名信息增加as.left
									aliasPointsToInfo.alias.add(as.left);
									// 为alias增加别名信息
									fact.put(alias, aliasPointsToInfo);
								}

							}
						} else if (as.right instanceof InvokeExpr) {
							// e.g.: a.b = b.f();

							InvokeExpr invoke = (InvokeExpr) as.right;

							// 获取a.b的别名信息，将a.b从与其别名的ref的alias中删除
							RefVarInfo leftPointsToInfo = fact.get(as.left);
							if (leftPointsToInfo != null) {
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
									// 获取a别名信息，并相应删除a.b的别名信息
									RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
									if (baseIntraPointsToInfo != null) {
										for (JIRValue alia : baseIntraPointsToInfo.alias) {
											FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
											newfieldRef.base = alia;
											fact.get(alias).alias.remove(newfieldRef);
										}
									}
								}
							}

							// 重新建立a的指向信息
							leftPointsToInfo = new RefVarInfo();
							leftPointsToInfo.types.add(invoke.getType());
							leftPointsToInfo.objects.add("O[" + invoke.getType().toString() + "][" + currentLine + "]");

							// 找出a的别名信息，并将相应的别名信息增加到 a.b的别名信息中
							RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
							if (baseIntraPointsToInfo != null) {
								for (JIRValue alias : baseIntraPointsToInfo.alias) {
									FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
									newfieldRef.base = alias;
									leftPointsToInfo.alias.add(newfieldRef);
								}
							}

							// 保存a.b的指向信息
							fact.put(as.left, leftPointsToInfo);

							// 计算a.b的别名的指向信息并保存
							for (JIRValue alias : leftPointsToInfo.alias) {
								RefVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
								// 别名信息中去除本身
								aliasPointsToInfo.alias.remove(alias);
								// 别名信息增加as.left
								aliasPointsToInfo.alias.add(as.left);
								// 为alias增加别名信息
								fact.put(alias, aliasPointsToInfo);
							}

						} else if (as.right instanceof CastExpr) {
							// e.g.: a.b = (A)b;

							CastExpr castExpr = (CastExpr) as.right;

							// 获取a.b的别名信息，将a.b从与其别名的ref的alias中删除
							RefVarInfo leftPointsToInfo = fact.get(as.left);
							if (leftPointsToInfo != null) {
								for (JIRValue alias : fact.get(as.left).alias) {
									fact.get(alias).alias.remove(as.left);
									// 获取a别名信息，并相应删除a.b的别名信息
									RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
									if (baseIntraPointsToInfo != null) {
										for (JIRValue alia : baseIntraPointsToInfo.alias) {
											FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
											newfieldRef.base = alia;
											fact.get(alias).alias.remove(newfieldRef);
										}
									}
								}
							}

							// 删除a.b的所有关联信息
							leftPointsToInfo = new RefVarInfo();
							RefVarInfo rightNodePointsToInfo = fact.get(castExpr.value);

							// 将b所指的信息拷贝一份给a.b
							if (rightNodePointsToInfo != null) {
								leftPointsToInfo = RefVarInfo.clone(rightNodePointsToInfo);
							} else {
								rightNodePointsToInfo = new RefVarInfo();
							}

							// 在a.b的别名信息中增加b
							leftPointsToInfo.alias.add(castExpr.value);

							// 将a的类型信息更改为castExpr.type
							leftPointsToInfo.types.clear();
							leftPointsToInfo.types.add(castExpr.type);

							// 找出a的别名信息，并将相应的别名信息增加到 a.b的别名信息中
							RefVarInfo baseIntraPointsToInfo = fact.get(leftFieldRef.base);
							if (baseIntraPointsToInfo != null) {
								for (JIRValue alias : baseIntraPointsToInfo.alias) {
									FieldRef newfieldRef = (FieldRef) leftFieldRef.clone();
									newfieldRef.base = alias;
									leftPointsToInfo.alias.add(newfieldRef);
								}
							}

							// 保存a.b的指向信息
							fact.put(as.left, leftPointsToInfo);

							// 计算a.b的别名的指向信息并保存
							for (JIRValue alias : leftPointsToInfo.alias) {
								RefVarInfo aliasPointsToInfo = leftPointsToInfo.clone();
								// 别名信息中去除本身
								aliasPointsToInfo.alias.remove(alias);
								// 别名信息增加as.left
								aliasPointsToInfo.alias.add(as.left);
								// 为alias增加别名信息
								fact.put(alias, aliasPointsToInfo);
							}

						}

					} else if (as.left instanceof ArrayRef) {
						// a[1] = new A(); a[i] = "String";

					}

				}
			} else if (stmt instanceof ReturnStmt) {

			}
		}

		// printTransferResult();
		return fact;

	}

	private void printTransferResult(HashMap<JIRValue, RefVarInfo> fact) {
		for (JIRValue jirValue : fact.keySet()) {
			System.out.println(jirValue.toString() + "->");
			System.out.println(fact.get(jirValue).toString());
		}
	}

	protected void initEntryBlock() {
		this.initEntryFact();
		HashMap<JIRValue, RefVarInfo> result = this.transferVertex(logicalEntryBlock());
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
				HashMap<JIRValue, RefVarInfo> start = getStartFact(block);
				HashMap<JIRValue, RefVarInfo> result = getResultFact(block);

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
					HashMap<JIRValue, RefVarInfo> newStart = new HashMap<JIRValue, RefVarInfo>();
					// get all predecessors' result fact and merge them with the
					// new start fact
					int preEdgeNumber = 0;
					while (predEdgeIter.hasNext()) {
						preEdgeNumber++;
						Edge edge = predEdgeIter.next();
						BasicBlock logicalPred = isForwards ? edge.getSource() : edge.getTarget();
						HashMap<JIRValue, RefVarInfo> pred = null;
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

					HashMap<JIRValue, RefVarInfo> newResult = transferVertex(block);

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
			HashMap<JIRValue, RefVarInfo> start = this.getStartFact(block);

			// result 有问题，但下一个block的start是没有问题的
			HashMap<JIRValue, RefVarInfo> result = this.getResultFact(block);

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

	public static void main(String[] args) {

		AnalysisFactoryManager.initial();
		ClassNodeLoader loader = new ClassNodeLoader("bin/edu/pku/cn/testcase/");
		ClassNode cc = loader.loadClassNode("PrintWriter", 0);

		for (MethodNode method : cc.methods) {

			method.getStmts();
			CFG cfg = method.getCFG();

			IntraPointsToDataflowAnalysis2 anlysis = new IntraPointsToDataflowAnalysis2(cfg);
			try {
				anlysis.execute();
				anlysis.examineResults();
			} catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

// end
