package edu.pku.cn;
/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-24
 * @modifier: liuxi
 * @time 2010-3-24
 * @reviewer: liuxi
 * @time 2010-3-24
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
/**
 * @author liuxi
 */
import java.io.File;

/* 
 * To change this template, choose Tools | Templates 
 * and open the template in the editor. 
 */

public class DeleteFile {

	public static String deleteFileName = ".svn";
	public static String deleteDirectory = System.getProperty("user.dir");

	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteFile(f);
			}
		}
		System.out.println("delete file: " + file.getPath() + "/" + file.getName());
		file.delete();
	}

	public static void deleteDestFile(File file) {
		if (file.isDirectory()) {
			if (file.getName().equals(deleteFileName)) {
				deleteFile(file);
			} else {
				File[] files = file.listFiles();
				for (File f : files) {
					deleteDestFile(f);
				}
			}
		}
	}

	public static void deleteClassFile(File file){
		if(file.isDirectory()){
			File[] files=file.listFiles();
			for(File f:files)
				deleteClassFile(f);
		}
		else{
			if(file.getName().endsWith(".class")){
				System.out.println("delete file: " + file.getPath() + "/" + file.getName());
				file.delete();
			}
		}
	}
	public static void main(String[] args) {
		File file = new File(deleteDirectory);
		deleteDestFile(file);
		file=new File(deleteDirectory+"/src/");
		deleteClassFile(file);
	}
}
// end
