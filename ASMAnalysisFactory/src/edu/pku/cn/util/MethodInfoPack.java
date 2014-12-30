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

import java.util.Map;

/**
 * @author ZR-Private
 */
public class MethodInfoPack {
	String className;
	String methodName;
	String commitInfo;
	String issueInfo;
	
	public MethodInfoPack(String cname, String ci, String tmp){
		className = cname;
		methodName = null;
		commitInfo = ci;
		issueInfo = tmp; //for short long descs
	}
	
	public String getCommitInfo(){
		return commitInfo;
	}
	
	public String getIssueInfo(){
		return issueInfo;
	}
	
	public String toString(){
		return ("[pathNmae] " + className + "\n" + "[commitInfo] " + commitInfo + "\n" + "[issueInfo] " + issueInfo);
	}
}

// end
