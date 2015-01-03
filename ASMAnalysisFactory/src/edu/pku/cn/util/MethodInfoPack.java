/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author ZR-Private
 * @time Dec 30, 2014
 * @modifier: ZR-Private
 * @time Dec 30, 2014
 * @reviewer: ZR-Private
 * @time Dec 30, 2014
 * (C) Copyright PKU Software Lab. 2014
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.util;

import java.util.List;
import java.util.Map;

/**
 * @author ZR-Private
 */
public class MethodInfoPack {
	String className;
	String methodName;
	Map<String, String[]> commitInfo;
	Map<String, String[]> issueInfo;
	int begin;
	int end;
	
	public MethodInfoPack(String cname, String mname, Map<String, String[]> commitInfoList, Map<String, String[]> issueInfoList, int b, int e){
		className = cname;
		methodName = mname;
		commitInfo = commitInfoList;
		issueInfo = issueInfoList; //for short long descs
		begin =b ;
		end = e;
	}
	
	
	public String toString(){
		return ("[pathNmae] " + className + "\n" + "[commitInfo] " + commitInfo + "\n" + "[issueInfo] " + issueInfo);
	}
}

// end
