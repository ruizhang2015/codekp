package edu.pku.cn.testcase;

import junit.framework.*;

public class GeneratedTest0 extends TestCase {

  // Runs all the tests in this file.
  public static void main(String[] args) {
    junit.textui.TestRunner.run(GeneratedTest0.class);
  }

  public void test1() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "";
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var3 = var0.connect(var2);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test2() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "hi!";
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var2 = var0.connect(var1);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test3() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    var1.close();

  }

  public void test4() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();

  }

  public void test5() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    var7.reopen();

  }

  public void test6() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.DBConnection var6 = new database.DBConnection(var0);

  }

  public void test7() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();

  }

  public void test8() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    database.Driver var2 = new database.Driver();

  }

  public void test9() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    java.lang.String var7 = "DROP TABLE";
    var6.execute(var7);
    database.Statement var9 = new database.Statement();

  }

  public void test10() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();

  }

  public void test11() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    database.Statement var7 = var1.createStmt();
    var1.reopen();

  }

  public void test12() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    var2.lookAtDriver(var3);
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var3.connect(var6);
    database.DBConnection var8 = var0.connect(var6);

  }

  public void test13() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var1);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test14() throws Throwable {

    database.Statement var0 = new database.Statement();
    database.Driver var1 = new database.Driver();
    var1.init();
    java.lang.String var3 = "jdbc:tinysql xxxx";
    database.DBConnection var4 = var1.connect(var3);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.executeUpdate(var3);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test15() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    var0.init();

  }

  public void test16() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    var3.lookAtDriver(var4);
    database.Statement var7 = var3.createStmt();
    database.Statement var8 = var3.createStmt();
    java.lang.String var9 = "DROP TABLE";
    var8.execute(var9);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var11 = var0.connect(var9);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test17() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    var1.close();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.Statement var8 = var1.createStmt();
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test18() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    java.lang.String var7 = "DROP TABLE";
    var6.execute(var7);
    var6.close();
    var6.reopen();

  }

  public void test19() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    var2.lookAtDriver(var3);
    database.Statement var6 = var2.createStmt();
    database.Statement var7 = var2.createStmt();
    java.lang.String var8 = "DROP TABLE";
    var7.execute(var8);
    var0.execute(var8);

  }

  public void test20() throws Throwable {

    database.Statement var0 = new database.Statement();
    database.Driver var1 = new database.Driver();
    var1.init();
    java.lang.String var3 = "jdbc:tinysql xxxx";
    database.DBConnection var4 = var1.connect(var3);
    database.Driver var5 = new database.Driver();
    var5.init();
    java.lang.String var7 = "jdbc:tinysql xxxx";
    database.DBConnection var8 = var5.connect(var7);
    var4.lookAtDriver(var5);
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var5.connect(var10);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var10);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test21() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "DROP TABLE";
    var0.execute(var1);
    database.Statement var3 = new database.Statement();

  }

  public void test22() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.DBConnection var14 = new database.DBConnection(var11);

  }

  public void test23() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    database.Statement var7 = var1.createStmt();
    java.lang.String var8 = "UPDATE";
    var7.executeUpdate(var8);
    var7.reopen();

  }

  public void test24() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    var12.close();

  }

  public void test25() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    var1.close();

  }

  public void test26() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    var2.lookAtDriver(var3);
    database.Statement var6 = var2.createStmt();
    database.Statement var7 = var2.createStmt();
    database.Statement var8 = var2.createStmt();
    java.lang.String var9 = "UPDATE";
    var8.executeUpdate(var9);
    java.lang.String var11 = "INSERT";
    var8.execute(var11);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var13 = var0.connect(var11);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test27() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    var1.close();
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var1.lookAtDriver(var8);
    var11.close();

  }

  public void test28() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    var12.close();

  }

  public void test29() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var7.createStmt();

  }

  public void test30() throws Throwable {

    database.Driver var0 = new database.Driver();
    database.Driver var1 = new database.Driver();
    var1.init();
    java.lang.String var3 = "jdbc:tinysql xxxx";
    database.DBConnection var4 = var1.connect(var3);
    database.DBConnection var5 = var0.connect(var3);

  }

  public void test31() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    var3.lookAtDriver(var4);
    database.Statement var7 = var3.createStmt();
    database.Statement var8 = var3.createStmt();
    database.Statement var9 = var3.createStmt();
    java.lang.String var10 = "UPDATE";
    var9.executeUpdate(var10);
    java.lang.String var12 = "INSERT";
    var9.execute(var12);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var14 = var0.connect(var12);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test32() throws Throwable {

    database.Statement var0 = new database.Statement();
    var0.close();

  }

  public void test33() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    java.lang.String var6 = "UPDATE";
    var5.executeUpdate(var6);
    var5.reopen();

  }

  public void test34() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    database.Statement var7 = var1.createStmt();
    java.lang.String var8 = "UPDATE";
    var7.executeUpdate(var8);
    java.lang.String var10 = "INSERT";
    var7.execute(var10);
    var7.close();
    database.Statement var13 = new database.Statement();

  }

  public void test35() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    var10.reopen();

  }

  public void test36() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "DROP TABLE";
    var0.execute(var1);
    var0.close();
    database.Statement var4 = new database.Statement();

  }

  public void test37() throws Throwable {

    database.Statement var0 = new database.Statement();
    database.Driver var1 = new database.Driver();
    var1.init();
    java.lang.String var3 = "jdbc:tinysql xxxx";
    database.DBConnection var4 = var1.connect(var3);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var3);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test38() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    java.lang.String var6 = "UPDATE";
    var5.executeUpdate(var6);
    java.lang.String var8 = "INSERT";
    var5.execute(var8);
    var5.reopen();

  }

  public void test39() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    var2.lookAtDriver(var3);
    database.Statement var6 = var2.createStmt();
    java.lang.String var7 = "UPDATE";
    var6.executeUpdate(var7);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var9 = var0.connect(var7);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test40() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    var1.close();
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var1.lookAtDriver(var8);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var1.lookAtDriver(var13);
    database.Statement var18 = var16.createStmt();

  }

  public void test41() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.executeUpdate(var1);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test42() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = var0.connect(var1);
    var0.init();

  }

  public void test43() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.DBConnection var10 = new database.DBConnection(var6);

  }

  public void test44() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    var1.close();
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var1.lookAtDriver(var8);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var1.lookAtDriver(var13);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.Statement var18 = var1.createStmt();
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test45() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    java.lang.String var6 = "UPDATE";
    var5.executeUpdate(var6);
    java.lang.String var8 = "INSERT";
    var5.execute(var8);
    var5.close();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var5.executeUpdate(var8);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test46() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    var2.lookAtDriver(var3);
    database.Statement var6 = var2.createStmt();
    database.Statement var7 = var2.createStmt();
    database.Statement var8 = var2.createStmt();
    java.lang.String var9 = "UPDATE";
    var8.executeUpdate(var9);
    java.lang.String var11 = "INSERT";
    var8.execute(var11);
    var0.execute(var11);

  }

  public void test47() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    var14.close();

  }

  public void test48() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    var7.close();

  }

  public void test49() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.DBConnection var13 = new database.DBConnection(var9);

  }

  public void test50() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    database.Statement var3 = new database.Statement();

  }

  public void test51() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Statement var2 = var1.createStmt();
    var1.close();

  }

  public void test52() throws Throwable {

    database.Statement var0 = new database.Statement();
    database.Driver var1 = new database.Driver();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var1.connect(var2);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var2);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test53() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    database.Statement var16 = var10.createStmt();
    var14.lookAtDriver(var11);

  }

  public void test54() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    database.DBConnection var3 = var0.connect(var1);

  }

  public void test55() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "";
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var2 = var0.connect(var1);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test56() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    database.Statement var15 = var1.createStmt();
    database.DBConnection var16 = new database.DBConnection(var11);

  }

  public void test57() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    var4.init();

  }

  public void test58() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    var2.lookAtDriver(var3);
    database.Statement var6 = var2.createStmt();
    database.Statement var7 = var2.createStmt();
    java.lang.String var8 = "DROP TABLE";
    var7.execute(var8);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var10 = var0.connect(var8);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test59() throws Throwable {

    database.Driver var0 = new database.Driver();
    database.Driver var1 = new database.Driver();
    var1.init();
    java.lang.String var3 = "jdbc:tinysql xxxx";
    database.DBConnection var4 = var1.connect(var3);
    database.Driver var5 = new database.Driver();
    var5.init();
    java.lang.String var7 = "jdbc:tinysql xxxx";
    database.DBConnection var8 = var5.connect(var7);
    var4.lookAtDriver(var5);
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var5.connect(var10);
    database.DBConnection var12 = var0.connect(var10);

  }

  public void test60() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = var0.connect(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var3.connect(var5);
    var2.lookAtDriver(var3);
    database.DBConnection var8 = new database.DBConnection(var5);

  }

  public void test61() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = new database.DBConnection(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    var2.lookAtDriver(var3);
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var3.connect(var6);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var6);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test62() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    database.Statement var15 = var1.createStmt();
    var1.close();
    var12.reopen();

  }

  public void test63() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.Statement var10 = var3.createStmt();
    var7.close();

  }

  public void test64() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    java.lang.String var3 = "INSERT";
    var0.execute(var3);
    var0.reopen();

  }

  public void test65() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    var5.lookAtDriver(var10);
    var3.lookAtDriver(var10);
    var16.reopen();

  }

  public void test66() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    database.Statement var16 = var10.createStmt();
    database.Statement var17 = var10.createStmt();
    var14.close();

  }

  public void test67() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    var1.close();
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var1.lookAtDriver(var8);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var1.lookAtDriver(var13);
    database.Driver var18 = new database.Driver();
    var18.init();
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var18.connect(var20);
    var1.lookAtDriver(var18);
    database.Statement var23 = var21.createStmt();

  }

  public void test68() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    database.Statement var15 = var1.createStmt();
    var1.close();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.Statement var17 = var1.createStmt();
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test69() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    var12.reopen();

  }

  public void test70() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.Statement var10 = var3.createStmt();
    var3.close();
    var7.reopen();

  }

  public void test71() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    var5.lookAtDriver(var10);
    var3.lookAtDriver(var10);
    database.Statement var19 = var3.createStmt();
    var16.reopen();

  }

  public void test72() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.Statement var10 = var3.createStmt();
    var3.close();
    database.Driver var12 = new database.Driver();
    var12.init();
    java.lang.String var14 = "jdbc:tinysql xxxx";
    database.DBConnection var15 = var12.connect(var14);
    var3.lookAtDriver(var12);
    var15.reopen();

  }

  public void test73() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    database.Statement var16 = var14.createStmt();

  }

  public void test74() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "DROP TABLE";
    var0.execute(var1);
    var0.close();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.executeUpdate(var1);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test75() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    var5.lookAtDriver(var10);
    var3.lookAtDriver(var10);
    database.Statement var19 = var3.createStmt();
    database.Statement var20 = var3.createStmt();
    var16.close();

  }

  public void test76() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var14.reopen();

  }

  public void test77() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    database.Statement var2 = new database.Statement();
    java.lang.String var3 = "DROP TABLE";
    var2.execute(var3);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var5 = var0.connect(var3);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test78() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    var10.lookAtDriver(var7);

  }

  public void test79() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    var1.lookAtDriver(var2);
    database.DBConnection var7 = new database.DBConnection(var4);

  }

  public void test80() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    var1.lookAtDriver(var6);

  }

  public void test81() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var4.connect(var13);
    database.Driver var15 = new database.Driver();

  }

  public void test82() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var4.connect(var13);

  }

  public void test83() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    var5.lookAtDriver(var10);
    var3.lookAtDriver(var10);
    database.Statement var19 = var3.createStmt();
    database.Statement var20 = var3.createStmt();
    var3.close();
    database.DBConnection var22 = new database.DBConnection(var15);

  }

  public void test84() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    database.Statement var16 = var3.createStmt();
    var14.close();

  }

  public void test85() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    java.lang.String var3 = "INSERT";
    var0.execute(var3);
    var0.close();
    database.Statement var6 = new database.Statement();

  }

  public void test86() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    var5.lookAtDriver(var10);
    var3.lookAtDriver(var10);
    database.Statement var19 = var3.createStmt();
    database.Statement var20 = var3.createStmt();
    var3.close();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.Statement var22 = var3.createStmt();
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test87() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    database.Statement var16 = var10.createStmt();
    database.Statement var17 = var10.createStmt();
    var10.close();
    var14.reopen();

  }

  public void test88() throws Throwable {

    database.Driver var0 = new database.Driver();
    database.Statement var1 = new database.Statement();
    java.lang.String var2 = "DROP TABLE";
    var1.execute(var2);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var4 = var0.connect(var2);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test89() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    database.Statement var15 = var1.createStmt();
    var12.close();

  }

  public void test90() throws Throwable {

    database.Statement var0 = new database.Statement();
    database.Statement var1 = new database.Statement();
    java.lang.String var2 = "DROP TABLE";
    var1.execute(var2);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.executeUpdate(var2);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test91() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    database.DBConnection var14 = new database.DBConnection(var9);

  }

  public void test92() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = var0.connect(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var3.connect(var5);
    var2.lookAtDriver(var3);
    database.Statement var8 = var2.createStmt();
    var6.lookAtDriver(var3);

  }

  public void test93() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var6.close();
    database.Statement var15 = var10.createStmt();

  }

  public void test94() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var1.lookAtDriver(var8);
    var14.reopen();

  }

  public void test95() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    database.Statement var2 = new database.Statement();
    java.lang.String var3 = "UPDATE";
    var2.executeUpdate(var3);
    java.lang.String var5 = "INSERT";
    var2.execute(var5);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var7 = var0.connect(var5);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test96() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var6.connect(var13);
    var1.lookAtDriver(var6);

  }

  public void test97() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    java.lang.String var3 = "jdbc:tinysql xxxx";
    database.DBConnection var4 = var2.connect(var3);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var1.lookAtDriver(var2);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test98() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    database.Statement var16 = var3.createStmt();
    var14.lookAtDriver(var8);

  }

  public void test99() throws Throwable {

    database.Driver var0 = new database.Driver();
    database.Driver var1 = new database.Driver();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var1.connect(var2);
    database.DBConnection var4 = var0.connect(var2);

  }

  public void test100() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    var1.lookAtDriver(var2);
    var5.close();

  }

  public void test101() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var3.lookAtDriver(var4);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test102() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    database.Statement var16 = var3.createStmt();
    database.Statement var17 = var3.createStmt();
    var14.lookAtDriver(var8);

  }

  public void test103() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var6.close();
    database.Driver var15 = new database.Driver();
    var15.init();
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = var15.connect(var17);
    var6.lookAtDriver(var15);
    var18.reopen();

  }

  public void test104() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var1);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test105() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    database.Statement var16 = var3.createStmt();
    database.Statement var17 = var3.createStmt();
    var3.close();
    database.DBConnection var19 = new database.DBConnection(var13);

  }

  public void test106() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    database.Statement var16 = var3.createStmt();
    database.Statement var17 = var3.createStmt();
    var3.close();

  }

  public void test107() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var12.lookAtDriver(var13);
    var16.reopen();

  }

  public void test108() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.DBConnection var4 = var0.connect(var2);

  }

  public void test109() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    database.Statement var16 = var10.createStmt();
    database.Statement var17 = var10.createStmt();
    var10.close();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.Statement var19 = var10.createStmt();
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test110() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var6.close();
    database.Driver var15 = new database.Driver();
    var15.init();
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = var15.connect(var17);
    var6.lookAtDriver(var15);
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var15.connect(var20);
    var15.init();

  }

  public void test111() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    var5.lookAtDriver(var10);
    var3.lookAtDriver(var10);
    database.Statement var19 = var3.createStmt();
    database.Statement var20 = var3.createStmt();
    var3.close();
    database.Driver var22 = new database.Driver();
    var22.init();
    java.lang.String var24 = "jdbc:tinysql xxxx";
    database.DBConnection var25 = var22.connect(var24);
    var3.lookAtDriver(var22);
    var25.close();

  }

  public void test112() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var1.lookAtDriver(var8);
    database.Statement var17 = var1.createStmt();
    var14.close();

  }

  public void test113() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    java.lang.String var3 = "INSERT";
    var0.execute(var3);
    var0.close();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var3);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test114() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var1.lookAtDriver(var8);
    database.Statement var17 = var1.createStmt();
    database.Statement var18 = var1.createStmt();
    var14.lookAtDriver(var8);

  }

  public void test115() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var1.lookAtDriver(var8);
    database.Statement var17 = var1.createStmt();
    database.Statement var18 = var1.createStmt();

  }

  public void test116() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    var1.lookAtDriver(var2);
    database.Statement var7 = var1.createStmt();
    var5.close();

  }

  public void test117() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var12.lookAtDriver(var13);
    database.Statement var18 = var12.createStmt();
    var16.reopen();

  }

  public void test118() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var12.lookAtDriver(var13);
    database.Statement var18 = var12.createStmt();

  }

  public void test119() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    database.Statement var16 = var10.createStmt();
    var14.reopen();

  }

  public void test120() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var6.close();
    database.Driver var15 = new database.Driver();
    var15.init();
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = var15.connect(var17);
    var6.lookAtDriver(var15);
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var15.connect(var20);
    database.Driver var22 = new database.Driver();
    var22.init();
    java.lang.String var24 = "jdbc:tinysql xxxx";
    database.DBConnection var25 = var22.connect(var24);
    var21.lookAtDriver(var22);
    var25.reopen();

  }

  public void test121() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var12.lookAtDriver(var13);
    database.Driver var18 = new database.Driver();
    var18.init();
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var18.connect(var20);
    java.lang.String var22 = "jdbc:tinysql xxxx";
    database.DBConnection var23 = new database.DBConnection(var22);
    database.Driver var24 = new database.Driver();
    var24.init();
    java.lang.String var26 = "jdbc:tinysql xxxx";
    database.DBConnection var27 = var24.connect(var26);
    database.Driver var28 = new database.Driver();
    var28.init();
    java.lang.String var30 = "jdbc:tinysql xxxx";
    database.DBConnection var31 = var28.connect(var30);
    var27.lookAtDriver(var28);
    java.lang.String var33 = "jdbc:tinysql xxxx";
    database.DBConnection var34 = var28.connect(var33);
    var23.lookAtDriver(var28);
    var21.lookAtDriver(var28);
    var12.lookAtDriver(var28);

  }

  public void test122() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var1.lookAtDriver(var8);
    database.Statement var17 = var1.createStmt();
    var1.close();

  }

  public void test123() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var6.lookAtDriver(var7);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test124() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    java.lang.String var3 = "INSERT";
    var0.execute(var3);
    var0.close();
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var15);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test125() throws Throwable {

    database.Main var0 = new database.Main();

  }

  public void test126() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    database.Statement var16 = var10.createStmt();
    var10.close();

  }

  public void test127() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    database.Statement var16 = var3.createStmt();
    var16.close();

  }

  public void test128() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    var1.lookAtDriver(var2);
    var2.init();

  }

  public void test129() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    java.lang.String var7 = "jdbc:tinysql xxxx";
    database.DBConnection var8 = new database.DBConnection(var7);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var6.executeUpdate(var7);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test130() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = var0.connect(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var3.connect(var5);
    var2.lookAtDriver(var3);
    var2.reopen();

  }

  public void test131() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.DBConnection var6 = var0.connect(var4);
    database.Statement var7 = var6.createStmt();

  }

  public void test132() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    database.Statement var7 = var1.createStmt();
    java.lang.String var8 = "UPDATE";
    var7.executeUpdate(var8);
    java.lang.String var10 = "INSERT";
    var7.execute(var10);
    var7.close();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = new database.DBConnection(var13);
    database.Driver var15 = new database.Driver();
    var15.init();
    var14.lookAtDriver(var15);
    database.Statement var18 = var14.createStmt();
    java.lang.String var19 = "UPDATE";
    var18.executeUpdate(var19);
    java.lang.String var21 = "INSERT";
    var18.execute(var21);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var7.executeUpdate(var21);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test133() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    database.Statement var15 = var1.createStmt();
    var1.reopen();

  }

  public void test134() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = var0.connect(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var3.connect(var5);
    var2.lookAtDriver(var3);
    database.Statement var8 = var2.createStmt();
    var8.reopen();

  }

  public void test135() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    database.Statement var7 = var1.createStmt();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = new database.DBConnection(var8);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var7.executeUpdate(var8);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test136() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var6.close();
    database.Driver var15 = new database.Driver();
    var15.init();
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = var15.connect(var17);
    var6.lookAtDriver(var15);
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var15.connect(var20);
    database.Driver var22 = new database.Driver();
    var22.init();
    java.lang.String var24 = "jdbc:tinysql xxxx";
    database.DBConnection var25 = var22.connect(var24);
    var21.lookAtDriver(var22);
    database.Driver var27 = new database.Driver();
    var27.init();
    java.lang.String var29 = "jdbc:tinysql xxxx";
    database.DBConnection var30 = var27.connect(var29);
    var21.lookAtDriver(var27);

  }

  public void test137() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    var0.init();

  }

  public void test138() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.Statement var10 = var3.createStmt();
    var3.close();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.Statement var12 = var3.createStmt();
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test139() throws Throwable {

    database.Driver var0 = new database.Driver();
    java.lang.String var1 = "jdbc:tinysql xxxx";
    database.DBConnection var2 = var0.connect(var1);
    database.Driver var3 = new database.Driver();
    var3.init();
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var3.connect(var5);
    var2.lookAtDriver(var3);
    database.Statement var8 = var2.createStmt();
    var8.close();

  }

  public void test140() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    java.lang.String var7 = "jdbc:tinysql xxxx";
    database.DBConnection var8 = new database.DBConnection(var7);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var6.execute(var7);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test141() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    var6.close();

  }

  public void test142() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "DROP TABLE";
    var0.execute(var1);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var3 = new database.DBConnection(var1);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test143() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    database.Driver var14 = new database.Driver();
    var14.init();
    java.lang.String var16 = "jdbc:tinysql xxxx";
    database.DBConnection var17 = var14.connect(var16);
    var13.lookAtDriver(var14);
    database.Driver var19 = new database.Driver();
    var19.init();
    java.lang.String var21 = "jdbc:tinysql xxxx";
    database.DBConnection var22 = var19.connect(var21);
    database.DBConnection var23 = var14.connect(var21);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var9.executeUpdate(var21);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test144() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    var3.close();

  }

  public void test145() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    var3.reopen();

  }

  public void test146() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var6.close();
    database.Driver var15 = new database.Driver();
    var15.init();
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = var15.connect(var17);
    var6.lookAtDriver(var15);
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var15.connect(var20);
    database.Driver var22 = new database.Driver();
    var22.init();
    java.lang.String var24 = "jdbc:tinysql xxxx";
    database.DBConnection var25 = var22.connect(var24);
    var21.lookAtDriver(var22);
    database.Driver var27 = new database.Driver();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var21.lookAtDriver(var27);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test147() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "DROP TABLE";
    var0.execute(var1);
    java.lang.String var3 = "jdbc:tinysql xxxx";
    database.DBConnection var4 = new database.DBConnection(var3);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = new database.DBConnection(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    database.Driver var11 = new database.Driver();
    var11.init();
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var11.connect(var13);
    var10.lookAtDriver(var11);
    java.lang.String var16 = "jdbc:tinysql xxxx";
    database.DBConnection var17 = var11.connect(var16);
    var6.lookAtDriver(var11);
    var4.lookAtDriver(var11);
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = new database.DBConnection(var20);
    database.DBConnection var22 = new database.DBConnection(var20);
    database.DBConnection var23 = var11.connect(var20);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var20);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test148() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Statement var4 = var3.createStmt();

  }

  public void test149() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    database.Statement var15 = var1.createStmt();
    database.Statement var16 = new database.Statement();
    java.lang.String var17 = "DROP TABLE";
    var16.execute(var17);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var15.executeUpdate(var17);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test150() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    database.DBConnection var17 = var4.connect(var15);
    var17.reopen();

  }

  public void test151() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    var12.close();

  }

  public void test152() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.DBConnection var10 = var4.connect(var8);
    database.DBConnection var11 = var0.connect(var8);

  }

  public void test153() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var6.close();
    database.Driver var15 = new database.Driver();
    var15.init();
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = var15.connect(var17);
    var6.lookAtDriver(var15);
    var15.init();

  }

  public void test154() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = new database.DBConnection(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    database.Driver var12 = new database.Driver();
    var12.init();
    java.lang.String var14 = "jdbc:tinysql xxxx";
    database.DBConnection var15 = var12.connect(var14);
    var11.lookAtDriver(var12);
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = var12.connect(var17);
    var7.lookAtDriver(var12);
    var5.lookAtDriver(var12);
    database.Statement var21 = var5.createStmt();
    database.Statement var22 = var5.createStmt();
    var5.close();
    database.Driver var24 = new database.Driver();
    var24.init();
    java.lang.String var26 = "jdbc:tinysql xxxx";
    database.DBConnection var27 = var24.connect(var26);
    var5.lookAtDriver(var24);
    database.Driver var29 = new database.Driver();
    var29.init();
    java.lang.String var31 = "jdbc:tinysql xxxx";
    database.DBConnection var32 = var29.connect(var31);
    database.Driver var33 = new database.Driver();
    var33.init();
    java.lang.String var35 = "jdbc:tinysql xxxx";
    database.DBConnection var36 = var33.connect(var35);
    var32.lookAtDriver(var33);
    java.lang.String var38 = "jdbc:tinysql xxxx";
    database.DBConnection var39 = var33.connect(var38);
    java.lang.String var40 = "jdbc:tinysql xxxx";
    database.DBConnection var41 = var33.connect(var40);
    database.DBConnection var42 = var24.connect(var40);
    var1.lookAtDriver(var24);

  }

  public void test155() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Driver var9 = new database.Driver();
    var9.init();
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var9.connect(var11);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var12.lookAtDriver(var13);
    java.lang.String var18 = "jdbc:tinysql xxxx";
    database.DBConnection var19 = var13.connect(var18);
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var13.connect(var20);
    var3.lookAtDriver(var13);
    var3.close();
    var3.close();

  }

  public void test156() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    var1.close();
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var1.lookAtDriver(var8);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var1.lookAtDriver(var13);
    database.Driver var18 = new database.Driver();
    var18.init();
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var18.connect(var20);
    var1.lookAtDriver(var18);
    database.Driver var23 = new database.Driver();
    java.lang.String var24 = "jdbc:tinysql xxxx";
    database.DBConnection var25 = var23.connect(var24);
    database.DBConnection var26 = var18.connect(var24);

  }

  public void test157() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.Statement var10 = var3.createStmt();
    database.Statement var11 = var3.createStmt();

  }

  public void test158() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    var3.lookAtDriver(var8);

  }

  public void test159() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    var1.lookAtDriver(var2);
    database.Statement var7 = var1.createStmt();
    var7.reopen();

  }

  public void test160() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    java.lang.String var3 = "INSERT";
    var0.execute(var3);
    var0.close();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = new database.DBConnection(var6);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.execute(var6);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test161() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var13.close();
    database.Driver var15 = new database.Driver();
    java.lang.String var16 = "jdbc:tinysql xxxx";
    database.DBConnection var17 = var15.connect(var16);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var13.execute(var16);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test162() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.Statement var10 = var3.createStmt();
    var10.reopen();

  }

  public void test163() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    database.Statement var7 = var1.createStmt();
    java.lang.String var8 = "UPDATE";
    var7.executeUpdate(var8);
    java.lang.String var10 = "INSERT";
    var7.execute(var10);
    var7.reopen();
    database.Statement var13 = new database.Statement();
    java.lang.String var14 = "UPDATE";
    var13.executeUpdate(var14);
    java.lang.String var16 = "INSERT";
    var13.execute(var16);
    var7.execute(var16);

  }

  public void test164() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    var5.lookAtDriver(var10);
    var3.lookAtDriver(var10);
    database.Statement var19 = var3.createStmt();
    database.Statement var20 = var3.createStmt();
    java.lang.String var21 = "jdbc:tinysql xxxx";
    database.DBConnection var22 = new database.DBConnection(var21);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var20.execute(var21);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test165() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    database.Statement var9 = var3.createStmt();
    database.Statement var10 = var3.createStmt();
    var3.close();
    database.Driver var12 = new database.Driver();
    var12.init();
    java.lang.String var14 = "jdbc:tinysql xxxx";
    database.DBConnection var15 = var12.connect(var14);
    var3.lookAtDriver(var12);
    database.Driver var17 = new database.Driver();
    var17.init();
    java.lang.String var19 = "jdbc:tinysql xxxx";
    database.DBConnection var20 = var17.connect(var19);
    database.DBConnection var21 = var12.connect(var19);

  }

  public void test166() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Statement var13 = var6.createStmt();
    var6.close();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = new database.DBConnection(var15);
    database.Driver var17 = new database.Driver();
    var17.init();
    var16.lookAtDriver(var17);
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var17.connect(var20);
    database.Driver var22 = new database.Driver();
    var22.init();
    java.lang.String var24 = "jdbc:tinysql xxxx";
    database.DBConnection var25 = var22.connect(var24);
    var21.lookAtDriver(var22);
    database.Statement var27 = var21.createStmt();
    database.Statement var28 = var21.createStmt();
    var21.close();
    database.Driver var30 = new database.Driver();
    var30.init();
    java.lang.String var32 = "jdbc:tinysql xxxx";
    database.DBConnection var33 = var30.connect(var32);
    var21.lookAtDriver(var30);
    java.lang.String var35 = "jdbc:tinysql xxxx";
    database.DBConnection var36 = var30.connect(var35);
    var6.lookAtDriver(var30);

  }

  public void test167() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    var12.reopen();

  }

  public void test168() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    java.lang.String var3 = "INSERT";
    var0.execute(var3);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var5 = new database.DBConnection(var3);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test169() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    var4.init();

  }

  public void test170() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    database.Statement var16 = var3.createStmt();
    var16.reopen();
    var16.reopen();

  }

  public void test171() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    var14.reopen();

  }

  public void test172() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    var0.close();

  }

  public void test173() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Statement var12 = var6.createStmt();
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var12.execute(var15);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test174() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.DBConnection var4 = new database.DBConnection(var2);
    database.DBConnection var5 = var0.connect(var2);

  }

  public void test175() throws Throwable {

    database.Statement var0 = new database.Statement();
    var0.reopen();
    var0.close();

  }

  public void test176() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    database.Statement var16 = var3.createStmt();
    database.Statement var17 = var3.createStmt();
    database.Statement var18 = var3.createStmt();
    var3.reopen();

  }

  public void test177() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    var5.reopen();

  }

  public void test178() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    database.DBConnection var11 = new database.DBConnection(var9);

  }

  public void test179() throws Throwable {

    database.Statement var0 = new database.Statement();
    database.Driver var1 = new database.Driver();
    var1.init();
    database.Driver var3 = new database.Driver();
    var3.init();
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var3.connect(var5);
    database.DBConnection var7 = var1.connect(var5);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.executeUpdate(var5);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test180() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var1.lookAtDriver(var8);
    var1.close();

  }

  public void test181() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Driver var5 = new database.Driver();
    var5.init();
    java.lang.String var7 = "jdbc:tinysql xxxx";
    database.DBConnection var8 = var5.connect(var7);
    database.Driver var9 = new database.Driver();
    var9.init();
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var9.connect(var11);
    var8.lookAtDriver(var9);
    java.lang.String var14 = "jdbc:tinysql xxxx";
    database.DBConnection var15 = var9.connect(var14);
    database.DBConnection var16 = var2.connect(var14);

  }

  public void test182() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "DROP TABLE";
    var0.execute(var1);
    var0.close();
    var0.reopen();
    database.Driver var5 = new database.Driver();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var5.connect(var6);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.executeUpdate(var6);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test183() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    var3.reopen();
    var3.close();

  }

  public void test184() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    database.Statement var7 = var1.createStmt();
    database.Statement var8 = var1.createStmt();

  }

  public void test185() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var1.lookAtDriver(var8);
    database.Statement var17 = var1.createStmt();
    var17.close();
    database.Driver var19 = new database.Driver();
    var19.init();
    java.lang.String var21 = "jdbc:tinysql xxxx";
    database.DBConnection var22 = var19.connect(var21);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var17.executeUpdate(var21);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test186() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    java.lang.String var3 = "INSERT";
    var0.execute(var3);
    var0.close();
    var0.reopen();

  }

  public void test187() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    database.Statement var7 = var1.createStmt();
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var7.execute(var10);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test188() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.DBConnection var6 = var0.connect(var4);
    database.Driver var7 = new database.Driver();
    var7.init();
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var7.connect(var9);
    var6.lookAtDriver(var7);
    database.Driver var12 = new database.Driver();
    var12.init();
    java.lang.String var14 = "jdbc:tinysql xxxx";
    database.DBConnection var15 = var12.connect(var14);
    database.Driver var16 = new database.Driver();
    var16.init();
    java.lang.String var18 = "jdbc:tinysql xxxx";
    database.DBConnection var19 = var16.connect(var18);
    var15.lookAtDriver(var16);
    database.Driver var21 = new database.Driver();
    var21.init();
    java.lang.String var23 = "jdbc:tinysql xxxx";
    database.DBConnection var24 = var21.connect(var23);
    database.DBConnection var25 = var16.connect(var23);
    database.DBConnection var26 = var7.connect(var23);

  }

  public void test189() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = var2.connect(var5);
    var6.reopen();

  }

  public void test190() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    database.Statement var15 = var1.createStmt();
    var15.close();

  }

  public void test191() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = new database.DBConnection(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    var5.lookAtDriver(var10);
    var3.lookAtDriver(var10);
    var3.reopen();

  }

  public void test192() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    java.lang.String var4 = "jdbc:tinysql xxxx";
    database.DBConnection var5 = var2.connect(var4);
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    var5.lookAtDriver(var6);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var6.connect(var11);
    var1.lookAtDriver(var6);
    database.Statement var14 = var1.createStmt();
    database.Statement var15 = var1.createStmt();
    var15.reopen();
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = new database.DBConnection(var17);
    database.Driver var19 = new database.Driver();
    var19.init();
    var18.lookAtDriver(var19);
    java.lang.String var22 = "jdbc:tinysql xxxx";
    database.DBConnection var23 = var19.connect(var22);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var15.executeUpdate(var22);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test193() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Statement var2 = var1.createStmt();
    var2.reopen();

  }

  public void test194() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    var3.lookAtDriver(var4);
    java.lang.String var7 = "jdbc:tinysql xxxx";
    database.DBConnection var8 = var4.connect(var7);
    database.Driver var9 = new database.Driver();
    var9.init();
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var9.connect(var11);
    var8.lookAtDriver(var9);
    database.Statement var14 = var8.createStmt();
    database.Statement var15 = var8.createStmt();
    var8.close();
    database.Driver var17 = new database.Driver();
    var17.init();
    java.lang.String var19 = "jdbc:tinysql xxxx";
    database.DBConnection var20 = var17.connect(var19);
    var8.lookAtDriver(var17);
    java.lang.String var22 = "jdbc:tinysql xxxx";
    database.DBConnection var23 = var17.connect(var22);
    database.DBConnection var24 = var0.connect(var22);

  }

  public void test195() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = new database.DBConnection(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    database.Driver var8 = new database.Driver();
    var8.init();
    java.lang.String var10 = "jdbc:tinysql xxxx";
    database.DBConnection var11 = var8.connect(var10);
    var7.lookAtDriver(var8);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = var8.connect(var13);
    var3.lookAtDriver(var8);
    var1.lookAtDriver(var8);
    java.lang.String var17 = "jdbc:tinysql xxxx";
    database.DBConnection var18 = new database.DBConnection(var17);
    database.DBConnection var19 = new database.DBConnection(var17);
    database.DBConnection var20 = var8.connect(var17);
    var20.reopen();

  }

  public void test196() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    java.lang.String var7 = "DROP TABLE";
    var6.execute(var7);
    var6.close();
    var6.close();

  }

  public void test197() throws Throwable {

    database.Statement var0 = new database.Statement();
    java.lang.String var1 = "UPDATE";
    var0.executeUpdate(var1);
    java.lang.String var3 = "INSERT";
    var0.execute(var3);
    var0.close();
    database.Driver var6 = new database.Driver();
    var6.init();
    java.lang.String var8 = "jdbc:tinysql xxxx";
    database.DBConnection var9 = var6.connect(var8);
    database.Driver var10 = new database.Driver();
    var10.init();
    java.lang.String var12 = "jdbc:tinysql xxxx";
    database.DBConnection var13 = var10.connect(var12);
    var9.lookAtDriver(var10);
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var10.connect(var15);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var0.executeUpdate(var15);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test198() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    var1.reopen();
    var1.reopen();
    var1.close();

  }

  public void test199() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    java.lang.String var13 = "jdbc:tinysql xxxx";
    database.DBConnection var14 = new database.DBConnection(var13);
    database.Driver var15 = new database.Driver();
    var15.init();
    var14.lookAtDriver(var15);
    database.Statement var18 = var14.createStmt();
    database.Statement var19 = var14.createStmt();
    database.Statement var20 = var14.createStmt();
    java.lang.String var21 = "UPDATE";
    var20.executeUpdate(var21);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      database.DBConnection var23 = var4.connect(var21);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test200() throws Throwable {

    database.Driver var0 = new database.Driver();
    database.Driver var1 = new database.Driver();
    var1.init();
    java.lang.String var3 = "jdbc:tinysql xxxx";
    database.DBConnection var4 = var1.connect(var3);
    java.lang.String var5 = "jdbc:tinysql xxxx";
    database.DBConnection var6 = new database.DBConnection(var5);
    database.DBConnection var7 = var1.connect(var5);
    database.DBConnection var8 = var0.connect(var5);

  }

  public void test201() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    var12.lookAtDriver(var13);
    var12.close();
    database.Driver var19 = new database.Driver();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var12.lookAtDriver(var19);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test202() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.DBConnection var2 = new database.DBConnection(var0);
    database.Driver var3 = new database.Driver();
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var2.lookAtDriver(var3);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test203() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    database.Driver var13 = new database.Driver();
    var13.init();
    java.lang.String var15 = "jdbc:tinysql xxxx";
    database.DBConnection var16 = var13.connect(var15);
    database.DBConnection var17 = var4.connect(var15);
    database.Driver var18 = new database.Driver();
    var18.init();
    java.lang.String var20 = "jdbc:tinysql xxxx";
    database.DBConnection var21 = var18.connect(var20);
    java.lang.String var22 = "jdbc:tinysql xxxx";
    database.DBConnection var23 = new database.DBConnection(var22);
    database.Driver var24 = new database.Driver();
    var24.init();
    java.lang.String var26 = "jdbc:tinysql xxxx";
    database.DBConnection var27 = var24.connect(var26);
    database.Driver var28 = new database.Driver();
    var28.init();
    java.lang.String var30 = "jdbc:tinysql xxxx";
    database.DBConnection var31 = var28.connect(var30);
    var27.lookAtDriver(var28);
    java.lang.String var33 = "jdbc:tinysql xxxx";
    database.DBConnection var34 = var28.connect(var33);
    var23.lookAtDriver(var28);
    var21.lookAtDriver(var28);
    database.Statement var37 = var21.createStmt();
    database.Statement var38 = var21.createStmt();
    var21.close();
    database.Driver var40 = new database.Driver();
    var40.init();
    java.lang.String var42 = "jdbc:tinysql xxxx";
    database.DBConnection var43 = var40.connect(var42);
    var21.lookAtDriver(var40);
    database.Driver var45 = new database.Driver();
    var45.init();
    java.lang.String var47 = "jdbc:tinysql xxxx";
    database.DBConnection var48 = var45.connect(var47);
    database.Driver var49 = new database.Driver();
    var49.init();
    java.lang.String var51 = "jdbc:tinysql xxxx";
    database.DBConnection var52 = var49.connect(var51);
    var48.lookAtDriver(var49);
    java.lang.String var54 = "jdbc:tinysql xxxx";
    database.DBConnection var55 = var49.connect(var54);
    java.lang.String var56 = "jdbc:tinysql xxxx";
    database.DBConnection var57 = var49.connect(var56);
    database.DBConnection var58 = var40.connect(var56);
    database.DBConnection var59 = var4.connect(var56);
    database.Driver var60 = new database.Driver();
    var60.init();
    java.lang.String var62 = "jdbc:tinysql xxxx";
    database.DBConnection var63 = var60.connect(var62);
    database.DBConnection var64 = var4.connect(var62);

  }

  public void test204() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.DBConnection var4 = new database.DBConnection(var2);

  }

  public void test205() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    database.Statement var6 = var1.createStmt();
    java.lang.String var7 = "DROP TABLE";
    var6.execute(var7);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = new database.DBConnection(var9);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var6.execute(var9);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test206() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.DBConnection var2 = new database.DBConnection(var0);
    database.DBConnection var3 = new database.DBConnection(var0);

  }

  public void test207() throws Throwable {

    java.lang.String var0 = "jdbc:tinysql xxxx";
    database.DBConnection var1 = new database.DBConnection(var0);
    database.Driver var2 = new database.Driver();
    var2.init();
    var1.lookAtDriver(var2);
    database.Statement var5 = var1.createStmt();
    java.lang.String var6 = "UPDATE";
    var5.executeUpdate(var6);
    java.lang.String var8 = "INSERT";
    var5.execute(var8);
    var5.close();
    database.Statement var11 = new database.Statement();
    java.lang.String var12 = "DROP TABLE";
    var11.execute(var12);
    var11.close();
    var11.reopen();
    database.Statement var16 = new database.Statement();
    java.lang.String var17 = "DROP TABLE";
    var16.execute(var17);
    var11.execute(var17);
    // The following exception was thrown during execution.
    // This behavior will recorded for regression testing.
    try {
      var5.executeUpdate(var17);
      fail("Expected exception of type java.lang.RuntimeException");
    } catch (java.lang.RuntimeException e) {
      // Expected exception.
    }

  }

  public void test208() throws Throwable {

    database.Driver var0 = new database.Driver();
    var0.init();
    java.lang.String var2 = "jdbc:tinysql xxxx";
    database.DBConnection var3 = var0.connect(var2);
    database.Driver var4 = new database.Driver();
    var4.init();
    java.lang.String var6 = "jdbc:tinysql xxxx";
    database.DBConnection var7 = var4.connect(var6);
    var3.lookAtDriver(var4);
    java.lang.String var9 = "jdbc:tinysql xxxx";
    database.DBConnection var10 = var4.connect(var9);
    java.lang.String var11 = "jdbc:tinysql xxxx";
    database.DBConnection var12 = var4.connect(var11);
    database.Statement var13 = var12.createStmt();
    database.Statement var14 = var12.createStmt();

  }

}
