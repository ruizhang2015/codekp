package constraintVerifier;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import classParse.Converter;

import constraintVerifier.DFFilter;

import docParse.CommentExtractor;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class xlsParser {

	
	public static void readAll(String xlsLoc){
		
				
		try {
			  
			  
			  
			  InputStream is = new FileInputStream(xlsLoc);
			  Workbook rwb = Workbook.getWorkbook(is);
			  
			  Sheet rs = rwb.getSheet(0);
			  int rows = rs.getRows();

			  int count_all=0;
			  int count_false=0;
			  int count_true=0;
			  int count_true_positive=0;
			  int count_false_negative=0;
			  int count_true_negative=0;
			  int count_false_positive=0;
			  int count_recog_false=0;
			  int count_recog_true=0;
			  
			  ArrayList<String> skipped=new ArrayList<String>();
		      loop1: for (int i = 1; i < rows; ++i) {		
		    	       
//		    	       HashMap<String,String> mHasDep=new HashMap<String,String>();
		    	       
		    	       ArrayList<HashSet<String> >cHasDep=new ArrayList<HashSet<String> >();//每个元素是一个HashSet，其中的元素彼此间存在数据流关系。
		    	  	
		    	   	   String id=rs.getCell(0,i).getContents().trim();
		    	   	   String category=rs.getCell(5,i).getContents().trim();
		    	   	   if(category.equalsIgnoreCase("Duplicate")){
		    	   		   skipped.add(id);
	    				   continue loop1;
		    	   	   }
		    	   		  
		    	   	   if(category.equalsIgnoreCase("rule"))
		    	   		   category="1";
		    	   	   else
		    	   		   category="0";
		    	   	   String normal=rs.getCell(1,i).getContents().trim();
		    	   	   String FCA=rs.getCell(2, i).getContents().trim();
		    	   	   
		    	   	   if(FCA.trim().equals(""))
		    	   		   continue loop1;
		    	   	   
		    	   	   //先解析FCA
		    	   	   String methodFCA=FCA.substring(0, FCA.indexOf(")")+1);
	    			   String fullClassNmFCA=methodFCA.substring(0, methodFCA.indexOf(":"));
	    			   String methodNmFCA=methodFCA.substring(methodFCA.indexOf(":")+1, methodFCA.length());
	    			   methodNmFCA=methodNmFCA.replaceAll("::", ",");
	    			   methodNmFCA=methodNmFCA.replace(",)", ")");
	    			   String nameFCA=methodNmFCA.substring(0, methodNmFCA.indexOf("("));
	    			   		    			 
	    			   if(!(fullClassNmFCA.startsWith("java")||fullClassNmFCA.startsWith("ptolemy"))||fullClassNmFCA.startsWith("javax.transaction"))	{
	    				   skipped.add(id);
	    				   continue loop1;
	    			   }
	    			   
//	    			   if(!(fullClassNmFCA.startsWith("java"))||fullClassNmFCA.startsWith("javax.transaction"))	{
//	    				   skipped.add(id);
//	    				   continue loop1;
//	    			   }
	    				   
	    			   
	    			 //寻找返回值类型
	    			   String projectPath="F:\\benchmark\\CAR-Miner\\ptolemy_bin";

    				   	    			   
	    			   if(nameFCA.equals("CONSTRUCTOR"))
	    				   nameFCA="<init>";
	    			   String argFCA=methodNmFCA.substring(methodNmFCA.indexOf("(")+1, methodNmFCA.indexOf(")"));
	    			   
	    			   String[] argsFCA;
	    			   if(!argFCA.trim().equals("")){
	    				   if(argFCA.contains(",")){
	    					   argsFCA=argFCA.split("\\s*,\\s*");
	    				   }
	    				   else{
	    					   argsFCA=new String[]{argFCA};
	    				   }
	    			   }
	    			   else{
	    				   argsFCA=new String[]{};
	    			   }
	    			   
	    			   
	    			   ArrayList<String> tempFCA=new ArrayList<String>();
	    			   tempFCA.add(fullClassNmFCA);//首先要增加方法所在的类
	    			   for(int j=0;j<argsFCA.length;j++){
	    				   if(argsFCA[j].contains(".")&&(argsFCA[j].startsWith("java")||argsFCA[j].startsWith("ptolemy")))//忽略简单类型
	    					   tempFCA.add(argsFCA[j]);
//	    				   if(argsFCA[j].contains(".")&&(argsFCA[j].startsWith("java")))//忽略简单类型
//	    					   tempFCA.add(argsFCA[j]);
	    			   }
	    			   
	    			   String returnTFCA=DFFilter.getReturnType(projectPath, fullClassNmFCA, nameFCA, argsFCA);
	    			   if(returnTFCA==null){
	    				   System.out.println("can't find method "+fullClassNmFCA+"."+methodNmFCA);
	    			   }
	    			   else{
	    				   if(returnTFCA.contains("."))
		    				   tempFCA.add(returnTFCA);
	    			   }
	    			   		    			   
	    			   
	    			   String ArgsFCA[]=new String[tempFCA.size()];
	    			   for(int n=0;n<ArgsFCA.length;n++)
	    				   ArgsFCA[n]=tempFCA.get(n);
	    			   
		    	   	  //开始分析异常部分的方法
		    		   String content=rs.getCell(3, i).getContents().trim();
		    		   if(content.trim().equals(""))
		    			   continue;
		    		   	
		    		   //key为方法名,value为这方法的参数列表
		    		   HashMap<String, String[]> normals=new HashMap<String, String[]>();
		    		   HashMap<String, String[]> exceptions=new HashMap<String, String[]>();
		    		   
		    		   String[] methods=content.split("#");		    		   
		    		   for(int k=0;k<methods.length;k++){
		    			   String method=methods[k].substring(0, methods[k].indexOf(")")+1);
		    			   String fullClassNm=method.substring(0, method.indexOf(":"));
		    			   String methodNm=method.substring(method.indexOf(":")+1, method.length());
		    			   methodNm=methodNm.replaceAll("::", ",");
		    			   methodNm=methodNm.replace(",)", ")");
		    			   String name=methodNm.substring(0, methodNm.indexOf("("));
		    			   		    			 
		    			   if(!(fullClassNm.startsWith("java")||fullClassNm.startsWith("ptolemy"))||fullClassNm.startsWith("javax.transaction")){
		    				   skipped.add(id);
		    				   continue loop1;
		    			   }
//		    			   if(!(fullClassNm.startsWith("java"))||fullClassNm.startsWith("javax.transaction")){
//		    				   skipped.add(id);
//		    				   continue loop1;
//		    			   }
		    				   
		    			   
		    			   //异常部分的方法不需要返回值
		    			   String arg=methodNm.substring(methodNm.indexOf("(")+1, methodNm.indexOf(")"));
		    			   
		    			   ArrayList<String> temp=new ArrayList<String>();
		    			   temp.add(fullClassNm);//首先要增加方法所在的类
		    			   
		    			   if(!arg.trim().equals("")){
		    				   if(arg.contains(",")){
		    					   String[] args=arg.split(",");
				    			   for(int j=0;j<args.length;j++){
				    				   if(args[j].contains(".")&&(args[j].startsWith("java")||args[j].startsWith("ptolemy")))//忽略简单类型
				    					   temp.add(args[j]);
//				    				   if(args[j].contains(".")&&(args[j].startsWith("java")))//忽略简单类型
//				    					   temp.add(args[j]);
				    			   }
		    				   }
		    				   else{
		    					   if(arg.contains("."))
		    						   temp.add(arg);
		    				   }
		    					   
		    			   }
		    			   
		    			   
		    			   String[]  exArgs=new String[temp.size()];
		    			   for(int m=0;m<exArgs.length;m++)
		    				   exArgs[m]=temp.get(m);
		    			   exceptions.put(name,exArgs);		    			   
		    			   
		    		   }
		    		   
		    		   //分析normal部分的方法
		    		   if(!normal.trim().equals("")){
		    			   methods=normal.split("#");
			    		   for(int k=0;k<methods.length;k++){
			    			   String method=methods[k].substring(0, methods[k].indexOf(")")+1);
			    			   String fullClassNm=method.substring(0, method.indexOf(":"));
			    			   String classNm=fullClassNm.substring(fullClassNm.lastIndexOf(".")+1, fullClassNm.length());
			    			   String methodNm=method.substring(method.indexOf(":")+1, method.length());
			    			   methodNm=methodNm.replaceAll("::", ",");
			    			   methodNm=methodNm.replace(",)", ")");
			    			   		    			 
			    			   if(!(fullClassNm.startsWith("java")||fullClassNm.startsWith("ptolemy"))||fullClassNm.startsWith("javax.transaction")){
			    				   skipped.add(id);
			    				   continue loop1;
			    			   }
//			    			   if(!(fullClassNm.startsWith("java"))||fullClassNm.startsWith("javax.transaction")){
//			    				   skipped.add(id);
//			    				   continue loop1;
//			    			   }
			    				   
			    			   //寻找返回值类型
			    			   projectPath="F:\\benchmark\\CAR-Miner\\ptolemy_bin";
		    				   
			    			   String name=methodNm.substring(0, methodNm.indexOf("("));
			    			   if(name.equals("CONSTRUCTOR"))
			    				   name="<init>";
			    			   String arg=methodNm.substring(methodNm.indexOf("(")+1, methodNm.indexOf(")"));
			    			   
			    			   String[] args;
			    			   if(!arg.trim().equals("")){
			    				   if(arg.contains(",")){
			    					   args=arg.split("\\s*,\\s*");
			    				   }
			    				   else{
			    					   args=new String[]{arg};
			    				   }
			    			   }
			    			   else{
			    				   args=new String[]{};
			    			   }
			    			   
			    			   
			    			   ArrayList<String> temp=new ArrayList<String>();
			    			   temp.add(fullClassNm);//首先要增加方法所在的类
			    			   for(int j=0;j<args.length;j++){
			    				   if(args[j].contains(".")&&(args[j].startsWith("java")||args[j].startsWith("ptolemy")))//忽略简单类型
			    					   temp.add(args[j]);
//			    				   if(args[j].contains(".")&&(args[j].startsWith("java")))//忽略简单类型
//			    					   temp.add(args[j]);
			    			   }
			    			   
			    			   String returnT=DFFilter.getReturnType(projectPath, fullClassNm, name, args);
			    			   if(returnT==null){
			    				   System.out.println("can't find method "+fullClassNm+"."+methodNm);
			    			   }
			    			   else{
			    				   if(returnT.contains(".")){
			    					   temp.add(returnT);
//			    					   mHasDep.put(returnT, fullClassNm);			    					   
			    				   }
				    				   
			    			   }
			    			   		    			   
			    			   Iterator<HashSet<String>> ite=cHasDep.iterator();
	    					   boolean found=false;
	    					   while(ite.hasNext()){
	    						   HashSet<String>cs=ite.next();
	    						   HashSet<String> tempcs=new HashSet<String>(cs);
	    						   tempcs.addAll(temp);
	    						   if(tempcs.size()<cs.size()+temp.size()){
	    							   cs.addAll(temp);			    							   
	    							   found=true;
	    						   }
	    						  
	    					   }
	    					   if(!found){
	    						   HashSet<String>cs=new HashSet<String>();
	    						   cs.addAll(temp);
    							   cHasDep.add(cs);
	    					   }
	    					   
			    			   String norArgs[]=new String[temp.size()];
			    			   for(int n=0;n<norArgs.length;n++)
			    				   norArgs[n]=temp.get(n);
			    			   normals.put(name,norArgs);		    			   
			    			   
			    		   }
		    		   }
		    		   
		    		   count_all++;
		    		   if(category.equals("1"))
		    			   count_true++;
		    		   if(category.equals("0"))
		    			   count_false++;
		     
		    		   //判断normals中和exceptions中的方法的数据流依赖关系
		    		   projectPath="F:\\benchmark\\CAR-Miner\\ptolemy_bin";//这个路径中包含了j2se和hibernate中的class，以防止两个方法的参数中有同时涉及到这两个项目的
		    		   
		    		   boolean hasDepFCA=false;
		    		   boolean hasDep=true;
		    		   Iterator<String> exIte=exceptions.keySet().iterator();
		    		   while(exIte.hasNext()){//异常部分中的每个方法都必须与前面的方法有数据流依赖关系
		    			   											   //且至少有一个异常部分的方法与FCA方法有数据流依赖
		    			   String name=exIte.next();
		    			   String[] exArgs=exceptions.get(name);
		    			   boolean dep=false;
		    			   
		    			   HashSet<String> adds=new HashSet<String>();
		    			   for(int k=0;k<ArgsFCA.length;k++){
		    				   String fca=ArgsFCA[k];
//		    				   add=mHasDep.get(fca);
		    				   
		    				   Iterator<HashSet<String>> ite=cHasDep.iterator();
		    				   while(ite.hasNext()){
		    					   HashSet<String> cs=ite.next();
		    					   if(cs.contains(fca)){
		    						   adds.addAll(cs);
		    						   adds.remove(fca);
		    					   }
		    				   }
		    				   
//		    				   if(add!=null)
//		    					   break;
		    			   }
		    			   
		    			   if(adds.size()!=0){
		    				   String[] temp=new String[ArgsFCA.length+adds.size()];
		    				   for(int a=0;a<ArgsFCA.length;a++){
		    					   temp[a]=ArgsFCA[a];
		    				   }
		    				   int b=ArgsFCA.length;
		    				   Iterator<String>ite_add=adds.iterator();
		    				   while(ite_add.hasNext()){
		    					   temp[b]=ite_add.next();
		    					   b++;
		    				   }
		    				   ArgsFCA=temp;
		    			   }
		    			   
		    			   if(DFFilter.hasDep(projectPath, ArgsFCA, exArgs)){
	    					   dep=true;
	    					   hasDepFCA=true;
	    					   continue;
	    				   }
		    			   
		    			   Iterator<String> norIte=normals.keySet().iterator();
		    			   while(norIte.hasNext()){
		    				   String norName=norIte.next();
		    				   String[] norTypes=normals.get(norName);
		    				   if(DFFilter.hasDep(projectPath, norTypes, exArgs)){
		    					   dep=true;
		    					   break;
		    				   }
		    			   }
		    			   if(!dep){//这个异常部分的方法与前面的方法都没有数据流依赖关系
//		    				   System.out.println(id+" "+name);
		    				   hasDep=false;
		    			   }
		    		   }
		    		   if(!hasDepFCA){//或FCA与异常部分的方法没有依赖关系
//		    			   System.out.println(id+" "+nameFCA);
		    			   hasDep=false;
		    		   }
		    		   
		    		   if(hasDep){
		    			   count_recog_true++;
		    			   if(category.equals("0")){
		    				   count_false_positive++;
		    				   System.out.println("false positive: "+id);
		    			   }
		    				   
		    			   if(category.equals("1"))
		    				   count_true_positive++;
		    		   }		    			   
		    		   else{
		    			   count_recog_false++;
		    			   if(category.equals("0"))
		    				   count_true_negative++;
		    			   if(category.equals("1")){
		    				   System.out.println("false negative: "+id);
		    				   count_false_negative++;
		    			   }
		    				   
		    		   }
		    			   
		    		  
		   }
			  
			 
			  
			  System.out.println("count_all: "+count_all);
			  System.out.println("count_false: "+count_false);
			  System.out.println("count_true: "+count_true);
			  System.out.println("count_true_positive: "+count_true_positive);
			  System.out.println("count_false_negative: "+count_false_negative);
			  System.out.println("count_true_negative: "+count_true_negative);
			  System.out.println("count_false_positive: "+count_false_positive);
			  System.out.println("count_recog_false: "+count_recog_false);
			  System.out.println("count_recog_true: "+count_recog_true);
			  
			  System.out.println("skip: "+skipped.toString());
			  
		       rwb.close();
		  }
		  	catch (Exception ex) {
		   ex.printStackTrace();
		  }
	}
	
	public static void readExcel(String xlsLoc) {
		  try {
			  
			  String javaDoc="F:\\workspace\\j2se_1.4\\doc\\";
			  String ptolemyDoc="F:\\benchmark\\Claire\\ptII3.0.2\\javaDoc\\";
			  String dest="F:\\benchmark\\CAR-Miner\\docs\\Ptolemy_cleanup_summary\\";
			  
			  
			  InputStream is = new FileInputStream(xlsLoc);
			  Workbook rwb = Workbook.getWorkbook(is);
			  Sheet rs = rwb.getSheet(0);
			  int rows = rs.getRows();

		       for (int i = 1; i < rows; ++i) {		    	 
		    	   	   String id=rs.getCell(0,i).getContents().trim();
		    	   	   String category=rs.getCell(5,i).getContents().trim();
		    	   	   if(category.equalsIgnoreCase("Duplicate")){
	    				   continue;
		    	   	   }
		    	   		   
		    	   	   if(category.equalsIgnoreCase("rule"))
		    	   		   category="1";
		    	   	   else
		    	   		   category="0";
		    		   String content=rs.getCell(3, i).getContents().trim();
		    		   if(content.trim().equals(""))
		    			   continue;
		    		   String[] methods=content.split("#");
		    		   
		    		   for(int k=0;k<methods.length;k++){
		    			   String method=methods[k].substring(0, methods[k].indexOf(")")+1);
		    			   String fullClassNm=method.substring(0, method.indexOf(":"));
//		    			   String pack=fullClassNm.substring(0, fullClassNm.lastIndexOf("."));
		    			   String classNm=fullClassNm.substring(fullClassNm.lastIndexOf(".")+1, fullClassNm.length());
		    			   String methodNm=method.substring(method.indexOf(":")+1, method.length());
		    			   methodNm=methodNm.replaceAll("::", ",");
		    			   methodNm=methodNm.replace(",)", ")");
		    			   
		    			   if(methodNm.contains("CONSTRUCTOR")){
		    				   methodNm=methodNm.replace("CONSTRUCTOR", classNm);
		    			   }
		    				  
		    			   
		    			   String docLoc="";
		    			   if(fullClassNm.startsWith("java"))
		    				   docLoc=javaDoc+fullClassNm.replaceAll("\\.", "\\\\")+".html";
		    			   else if(fullClassNm.startsWith("ptolemy"))
		    				   docLoc=ptolemyDoc+fullClassNm.replaceAll("\\.", "\\\\")+".html";
		    			   else
		    				   continue;
		    			   
		    			   boolean findMethod=false;
		    			   CommentExtractor extractor= CommentExtractor.extractSummary(docLoc, id+"_"+category+"_"+fullClassNm, methodNm, dest);
		    			   if(extractor.noClass){//没找到这个类
		    				   System.out.println("can't find the class: "+fullClassNm);
		    				   continue;
		    			   }
		    			   if(extractor.noSuchMethod){//尝试从父类找方法
		    				   String projectPath="F:\\benchmark\\CAR-Miner\\ptolemy_bin";
		    				  
//		    				   System.out.println(projectPath);
//		    				   System.out.println(fullClassNm);
		    				   Iterator<String> ite=DFFilter.findFarthers(projectPath, fullClassNm).iterator();
		    				   while(ite.hasNext()){
		    					   String farther=ite.next();
		    					   if(fullClassNm.startsWith("java")){
			    					   docLoc=javaDoc+farther.replaceAll("\\.", "\\\\")+".html";
			    				   }
			    				   if(fullClassNm.startsWith("ptolemy")){
			    					   docLoc=ptolemyDoc+farther.replaceAll("\\.", "\\\\")+".html";
			    				   }
			    				   extractor= CommentExtractor.extractSummary(docLoc, id+"_"+category+"_"+fullClassNm, methodNm, dest);
			    				   if(!extractor.noSuchMethod&&(!extractor.noClass)){//找到了这个方法
			    					   findMethod=true;
			    					   break;
			    				   }
			    					   
		    				   }
    				   
		    			   }
		    			   else
		    				   findMethod=true;
		    			   
		    			   if(!findMethod){
		    				   System.out.println("can't find the doc of Method: "+id+"_"+method);
		    			   }
		    			   
		    		   }
		     

		   }
		       rwb.close();
		  }
		  	catch (Exception ex) {
		   ex.printStackTrace();
		  }
		 }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		readAll("F:\\benchmark\\CAR-Miner\\Ptolemy_MinedPatterns_Processed.xls");

	}

}
