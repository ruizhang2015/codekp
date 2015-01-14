/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * @modifier: Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * @reviewer: Administrator
 * @time Oct 20, 2008 4:22:59 PM
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.sql.*;

import org.apache.catalina.valves.StuckThreadDetectionValve;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;


public class TestCloseDbConnection2 {
	//String str;
	private static final Log log = LogFactory.getLog(StuckThreadDetectionValve.class);
	
	public TestCloseDbConnection2(){
		//str = "test";
		//System.out.println("print sth: " 
				//+ str);
        if (log.isDebugEnabled()) {
            log.debug("Monitoring stuck threads with threshold = "
                    + threshold
                    + " sec");
        }
	}


	void wrong(Connection con3) {
		//String str0 = "strinit";
		try {

			//str0 = "str0test";
			//System.out.println(str);

		} catch (Exception e) {
			//System.out.println(str0);
		} finally {

			
		}
	}


}

// end
