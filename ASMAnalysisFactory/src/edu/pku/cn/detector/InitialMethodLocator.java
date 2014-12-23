/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2010-7-3 04:31:40
 * @modifier: Administrator
 * @time 2010-7-3 04:31:40
 * @reviewer: Administrator
 * @time 2010-7-3 04:31:40
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import edu.pku.cn.util.DBUtil;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import autoAdapter.CalledMethodsCollector;

import edu.pku.cn.Project;
import edu.pku.cn.analysis.InterValueDataflowAnalysis;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.ClassNodeLoader;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.classfile.PackageResource;
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
import edu.pku.cn.jir.Constant;
import edu.pku.cn.jir.Expr;
import edu.pku.cn.jir.FieldRef;
import edu.pku.cn.jir.IfStmt;
import edu.pku.cn.jir.IntConstant;
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
import edu.pku.cn.jir.ThrowStmt;
import edu.pku.cn.util.AnalysisFactoryManager;
import edu.pku.cn.util.CodaProperties;

public class InitialMethodLocator {

	static final String DBUrl = "jdbc:mysql://127.0.0.1:3306/ruleverification";// "jdbc:mysql://192.168.4.131:3306/jcdlib";
	static final String DBUser = "root";
	static final String DBPwd = "111111";// "massjcdlib";

	public static void main(String[] args) {

		// locateInitialMethods();
		// classifyClassWithInterface();
		//
		// // String projectName =
		// "D:\\Study\\@PKU\\Research\\SelfPapers\\20100517
		// // 约束验证\\SubjectProjects\\all";
		String projectName = "D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\all\\";
		// extractInheritanceRelation(projectName);

		// 约束验证\\SubjectProjects\\all\\"
		// getCalledInitMethods(projectName);
		// getCalledCleanupMethods(projectName);

		// locateInitialMethods();
		//InitialMethodLocator.locatedInitialAndCleanupMethods(projectName);
		InitialMethodLocator.getInterfaceMethods(projectName);
	}

	
	private static void extractInheritanceRelation(String projectPathName) {
		AnalysisFactoryManager.initial();
		Project project = new Project(projectPathName);
		CodaProperties.isLibExpland = false;
		// project.addLibPath("lib/");

		String className;
		ClassNodeLoader loader = new ClassNodeLoader(projectPathName);
		ClassNode cc;

		// worklist algorithm
		HashSet<PackageResource> worklist = new HashSet<PackageResource>();
		worklist.add(project.getPackageResource());
		String sql = "";

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			statement = connection.createStatement();

			int packageNumber = 0;
			while (worklist.size() > 0) {
				packageNumber++;
				PackageResource subjectPR = (PackageResource) worklist.toArray()[0];
				System.out.println("开始分析" + subjectPR.getPackageName() + " node number:"
						+ subjectPR.getClassNodeList().size());
				worklist.remove(subjectPR);
				worklist.addAll(subjectPR.getPackageList());

				for (ClassNode classNode : subjectPR.getClassNodeList()) {
					if (!classNode.isInterface()) {
						List<ClassNode> interfaceNodes = classNode.getInterfaces();
						for (ClassNode superInterface : interfaceNodes) {
							StringBuilder sb = new StringBuilder();
							sb.append("insert into classinterface(ClassName, InterfaceName) values (").append(
									"'" + classNode.name + "', ").append("'" + superInterface.name + "')");
							System.out.println(sb.toString());
							statement.execute(sb.toString());
							// DBUtil.executeSQLStmt(sb.toString());

						}

						for (ClassNode superClass : classNode.getSuperClasses()) {
							if (superClass != null && superClass.name != null && !superClass.name.equals("")) {

								StringBuilder sb = new StringBuilder();
								sb.append("insert into classparent(ClassName, parentName) values (").append(
										"'" + classNode.name + "', ").append("'" + superClass.name + "')");
								System.out.println(sb.toString());
								statement.execute(sb.toString());
								// DBUtil.executeSQLStmt(sb.toString());
							}
						}
					}
				}
			}
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

	}

	private static void getCalledCleanupMethods(String projectPathName) {
		AnalysisFactoryManager.initial();
		Project project = new Project(projectPathName);
		CodaProperties.isLibExpland = false;
		// project.addLibPath("lib/");
		String className, methodName;
		ClassNodeLoader loader = new ClassNodeLoader(projectPathName);
		ClassNode cc;

		Connection connection;
		Statement statement;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			statement = connection.createStatement();

			// 获取called init方法列表
			// HashSet<String> initMethodNames = new HashSet<String>();
			ResultSet resultSet = DBUtil
					.executeQuery("select id, subjectClassName, cleanupMethodName from subjectclasses_pair_cleanup");
			try {
				while (resultSet.next()) {
					className = resultSet.getString("subjectClassName");
					methodName = resultSet.getString("cleanupMethodName");

					cc = loader.loadClassNode(className, 0);
					if (cc != null) {
						if (!cc.isInterface()) {
							String sql = "insert into initcleanupcallees(subjectClassName, methodName, role) values ("
									+ "'" + cc.name + "'," + "'" + cc.name.replace('.', '/') + "::" + methodName + "',"
									+ "'cleanup-class')";
							System.out.println(sql);
							statement.execute(sql);
						} else {
							// 获取所有实现了该接口的类,寻找出实现了该cleanup方法的方法
							String sql = "insert into initcleanupcallees(subjectClassName, methodName, role) values ("
									+ "'" + cc.name + "'," + "'" + cc.name.replace('.', '/') + "::" + methodName + "',"
									+ "'cleanup-interface')";
							System.out.println(sql);
							statement.execute(sql);

							sql = "select className from classinterface where interfacename = '" + cc.name + "'";
							ResultSet implClasses = DBUtil.executeQuery(sql);
							while (implClasses.next()) {
								String implClassName = implClasses.getString("className");
								cc = loader.loadClassNode(implClassName, 0);
								for (MethodNode method : cc.methods) {
									if (method.getFullName().contains(methodName)) {
										// save the init method
										sql = "insert into initcleanupcallees(subjectClassName, methodName, role) values ("
												+ "'"
												+ cc.name
												+ "',"
												+ "'"
												+ method.getFullName()
												+ "',"
												+ "'cleanup-interface-impl')";
										System.out.println("接口：" + className + "的实现类：" + implClassName + "实现了方法"
												+ methodName);
										System.out.println(sql);
										statement.execute(sql);
									}
								}
							}
						}
					} else
						System.out.println("无法load " + className);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

	}

	private static void getCalledInitMethods(String projectPathName) {
		AnalysisFactoryManager.initial();
		Project project = new Project(projectPathName);
		CodaProperties.isLibExpland = false;
		// project.addLibPath("lib/");
		String className;
		ClassNodeLoader loader = new ClassNodeLoader(projectPathName);
		ClassNode cc;

		Connection connection;
		Statement statement;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			statement = connection.createStatement();

			// 获取called init方法列表
			// HashSet<String> initMethodNames = new HashSet<String>();
			ResultSet resultSet = DBUtil.executeQuery("select id, subjectClassName from subjectclasses_pair");
			try {
				while (resultSet.next()) {
					className = resultSet.getString("subjectClassName");
					cc = loader.loadClassNode(className, 0);
					if (cc != null) {
						if (!cc.isInterface()) {
							for (MethodNode method : cc.methods) {
								if (method.name.equals("<init>")) {
									// save the init method
									String sql = "insert into initcleanupcallees(subjectClassName, methodName, role) values ("
											+ "'" + cc.name + "'," + "'" + method.getFullName() + "'," + "'initial-class')";
									System.out.println(sql);
									statement.execute(sql);
								}
							}
						} else {
							String sql = "select className from classinterface where interfacename = '" + cc.name + "'";
							ResultSet implClasses = DBUtil.executeQuery(sql);
							while (implClasses.next()) {
								String implClassName = implClasses.getString("className");
								cc = loader.loadClassNode(implClassName, 0);
								for (MethodNode method : cc.methods) {
									if (method.name.equals("<init>")) {
										// save the init method
										sql = "insert into initcleanupcallees(subjectClassName, methodName, role) values ("
												+ "'" + cc.name + "'," + "'" + method.getFullName() + "'," + "'initial-interface-impl')";
										System.out.println(sql);
										statement.execute(sql);
									}
								}
							}
						}
					} else
						System.out.println("无法load " + className);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

	}

	
	private static void getInterfaceMethods(String projectPathName) {
		AnalysisFactoryManager.initial();
		Project project = new Project(projectPathName);
		CodaProperties.isLibExpland = false;
		// project.addLibPath("lib/");
		String className;
		ClassNodeLoader loader = new ClassNodeLoader(projectPathName);
		ClassNode cc;

		Connection connection;
		Statement statement;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			statement = connection.createStatement();

			// 获取called init方法列表
			// HashSet<String> initMethodNames = new HashSet<String>();
			ResultSet resultSet = DBUtil.executeQuery("select distinct(interfacename) from classinterface");
			try {
				while (resultSet.next()) {
					className = resultSet.getString("interfacename");
					cc = loader.loadClassNode(className, 0);
					if (cc != null) {
						if (!cc.isInterface()) {
							System.out.println(className + "is not an interface!");
						} else {
							String sql = "select className from classinterface where interfacename = '" + cc.name + "'";
							ResultSet implClasses = DBUtil.executeQuery(sql);
							for (MethodNode method: cc.methods){
								sql = "insert into interfacemethods(MethodName, InterfaceName) values ("
										+ "'" + method.getFullName() + "','" + className + "')";
								System.out.println(sql);
								statement.execute(sql);
							}
						}
					} else
						System.out.println("无法load " + className);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

	}
	
	private static void locatedInitialAndCleanupMethods(String projectPathName) {
		AnalysisFactoryManager.initial();
		Project project = new Project(projectPathName);
		CodaProperties.isLibExpland = false;
		// project.addLibPath("lib/");

		String className, methodName;
		ClassNodeLoader loader = new ClassNodeLoader(projectPathName);
		ClassNode cc;

		// worklist algorithm
		HashSet<PackageResource> worklist = new HashSet<PackageResource>();
		worklist.add(project.getPackageResource());
		String sql = "";

		Connection connection;
		Statement statement;
		boolean succeed = true;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			statement = connection.createStatement();

			// 获取called init方法列表
			HashSet<String> initMethodNames = new HashSet<String>();
			sql = "select id, methodName from initcleanupcallees where role like '%initial%'";
			ResultSet resultSet = DBUtil.executeQuery(sql);
			
			try {
				while (resultSet.next()) {
					methodName = resultSet.getString("methodName");
					initMethodNames.add(methodName);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 获取called close方法列表
			HashSet<String> cleanupMethodNames = new HashSet<String>();
			sql = "select id, methodName from initcleanupcallees where role like '%cleanup%'";
			resultSet = DBUtil.executeQuery(sql);
			try {
				while (resultSet.next()) {
					methodName = resultSet.getString("methodName");
					cleanupMethodNames.add(methodName);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("init methods 个数：" + initMethodNames.size());
			System.out.println("cleanup methods 个数：" + cleanupMethodNames.size());
			
			// 依次判断每个一个项目文件是否调用了initMethodNames和cleanupMethodNames中的方法
			int packageNumber = 0;
			CalledMethodsCollector calleesCollector = new CalledMethodsCollector();
			while (worklist.size() > 0) {
				packageNumber++;
				PackageResource subjectPR = (PackageResource) worklist.toArray()[0];
				System.out.println("开始分析Package:" + subjectPR.getPackageName() + "-> node number:"
						+ subjectPR.getClassNodeList().size());
				worklist.remove(subjectPR);
				worklist.addAll(subjectPR.getPackageList());

				
				for (ClassNode classNode : subjectPR.getClassNodeList()) {
					if (!classNode.isInterface()) {
						
						System.out.println("开始分析Class：" + classNode.name);
						
						for (MethodNode method : classNode.methods) {
							System.out.println("开始分析method：" + method.getFullName());
							CalledMethodsCollector.callees.clear();
							try {
								method.accept(calleesCollector);
							} catch (Exception e) {
								System.out.println("cannot analyze the method:" + method.name);
								continue;
							}
							
							// 判断是否调用了init方法
							for (String calledInitMethod : initMethodNames) {
								for (String callingMethod : CalledMethodsCollector.callees) {
									if (callingMethod.contains(calledInitMethod)){
										StringBuilder sb = new StringBuilder();
										sb.append("insert into initcleanupmethods(providedClassName, invokeMethodName, initMethodName, " +
												"relationship) values (")
												.append("'" + classNode.name + "', ")
												.append("'" + method.getFullName() + "', ")
												.append("'" + calledInitMethod + "', ")
												.append("'Invoke_Init')");
										System.out.println(sb.toString());
										statement.execute(sb.toString());
									}
								}
							}
							
							// 判断是否调用了cleanup方法
							for (String cleanupMethodName : cleanupMethodNames) {
								for (String callingMethod : CalledMethodsCollector.callees) {
									if (callingMethod.contains(cleanupMethodName)){
										StringBuilder sb = new StringBuilder();
										sb.append("insert into initcleanupmethods(providedClassName, invokeMethodName, initMethodName, " +
												"relationship) values (")
												.append("'" + classNode.name + "', ")
												.append("'" + method.getFullName() + "', ")
												.append("'" + cleanupMethodName + "', ")
												.append("'Invoke_Cleanup')");
										System.out.println(sb.toString());
										statement.execute(sb.toString());
									}
								}
							}
							
							System.out.println("结束分析method：" + method.getFullName());
							
						}
						System.out.println("结束分析Class:" + classNode.name);
						System.out.println("");
					}
					
					System.out.println("结束分析Package:" + subjectPR.getPackageName());
				}
			}
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

	}

	private static void classifyClassWithInterface() {
		AnalysisFactoryManager.initial();
		Project project = new Project("D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\all\\");
		CodaProperties.isLibExpland = false;
		// project.addLibPath("lib/");

		String className;
		ClassNodeLoader loader = new ClassNodeLoader(
				"D:\\Study\\@PKU\\Research\\SelfPapers\\20100517 约束验证\\SubjectProjects\\all\\");
		ClassNode cc;
		ResultSet resultSet = DBUtil.executeQuery("select id, subjectClassName from subjectnodes_pair");
		HashSet<ClassNode> interfaces = new HashSet();
		HashSet<ClassNode> classes = new HashSet();

		try {
			while (resultSet.next()) {
				className = resultSet.getString("subjectClassName");
				cc = loader.loadClassNode(className, 0);

				if (cc != null) {
					// System.out.println(cc.name + " (isInterface==" +
					// cc.isInterface() + ")");
					if (cc.isInterface()) {
						interfaces.add(cc);
					} else {
						classes.add(cc);
					}
				} else
					System.out.println("无法load " + className);
			}

			System.out.println("subject classes: ");
			for (ClassNode subjectClass : classes) {
				System.out.println(subjectClass.name);
			}

			System.out.println("subject interfaces: ");
			for (ClassNode subjectInter : interfaces) {
				System.out.println(subjectInter.name);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void locateInitialMethods() {
		AnalysisFactoryManager.initial();
		Project project = new Project("bin/edu/pku/cn/testcase/");
		CodaProperties.isLibExpland = true;
		project.addLibPath("lib/");
		String toFindClassName = "edu.pku.cn.testcase.PrintWriter";
		// String className = "edu.pku.cn.testcase.TestPointsToInfo";
		ClassNodeLoader loader = new ClassNodeLoader("bin/");
		// ClassNode providedClassNode = loader.loadClassNode(className, 0);
		ClassNode requiredClassNode = loader.loadClassNode(toFindClassName, 0);

		PackageResource packageResource = project.getPackageResource();
		List<ClassNode> allClassNodes = packageResource.getClassNodeList();

		// 先将所有的class进行分析
		for (ClassNode providedClassNode : allClassNodes) {
			if (!providedClassNode.isInterface()) {
				// 过滤出"需配代码"的初始化方法
				HashSet<String> initMethodNames = new HashSet<String>();
				for (MethodNode method : requiredClassNode.methods) {
					if (method.name.equals("<init>")) {
						initMethodNames.add(method.getFullName());
					}
				}

				int methodsNumber = providedClassNode.methods.size();
				boolean exist = false;

				for (MethodNode method : providedClassNode.methods) {

					System.out.println("******************************");
					System.out.println("——》开始分析方法" + method.name);

					CalledMethodsCollector analysis = new CalledMethodsCollector();
					CalledMethodsCollector.callees.clear();
					method.accept(analysis);

					for (String initMethodName : initMethodNames) {
						if (CalledMethodsCollector.callees.contains(initMethodName)) {
							System.out.println("函数（" + method.getFullName() + ") 会调用init方法（" + initMethodName + ")");
							DBUtil.saveInitMethods(providedClassNode.name, method.getFullName(),
									requiredClassNode.name, initMethodName);
							exist = true;
							break;
						}
					}

					System.out.println("******************************");
					// anlysis.examineResults();

				}

				// 保存其接口信息
				// for (String classInterface : providedClassNode.interfaces) {
				// DBUtil.saveMethodInterface(providedClassNode.name,
				// classInterface);
				// }

			}
		}

		// 利用之前生成的所有信息对Interface进行归总
		// for (ClassNode providedClassNode : allClassNodes) {
		// if (providedClassNode.isInterface()) {
		// // 获取数据库表classInterface中该interface的实现类的个数
		// String sql = "select * from MethodInterface where InterfaceName = '"
		// + providedClassNode.name + "'";
		// System.out.println(sql);
		// if (DBUtil.executeQuery(sql) != null) {
		// int implCount = DBUtil.getResultSetSize(DBUtil.executeQuery(sql));
		// System.out.println(DBUtil.executeQuery(sql).toString());
		// System.out.println("实现接口(" + providedClassNode.name + ")的类数量：" +
		// implCount);
		// for (MethodNode method : providedClassNode.methods) {
		// // CFG cfg = method.getCFG();
		// System.out.println("******************************");
		// System.out.println("——》开始分析方法" + method.getFullName());
		// sql = "select * from MethodInterface i, InitMethods m where
		// i.ClassName = m.providedClassName "
		// + "and i.InterfaceName = '" + providedClassNode.name + "' "
		// + "and m.invokeMethodName like '%" + method.name + method.desc + "' "
		// + "and m.subjectClassName = '" + requiredClassNode.name + "' ";
		// System.out.println(sql);
		//
		// int implAndInitCount = 0;
		// if (DBUtil.executeQuery(sql) != null) {
		// System.out.println(DBUtil.executeQuery(sql).toString());
		// implAndInitCount = DBUtil.getResultSetSize(DBUtil.executeQuery(sql));
		// }
		//
		// System.out.println("实现接口(" + providedClassNode.name + ")的方法（" +
		// method.getFullName() + ")的个数："
		// + implAndInitCount);
		// System.out.println("该接口(" + providedClassNode.name + ")的方法（" +
		// method.getFullName()
		// + ")为initMethod的概率为：" + (implAndInitCount / (implCount * 1.0)));
		//
		// // anlysis.examineResults();
		//
		// }
		//
		// }
		//
		// }
		// }
	}
}

// end
