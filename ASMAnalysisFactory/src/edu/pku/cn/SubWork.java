/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-7-20 ����09:01:43
 * @modifier: Liuxizhiyi
 * @time 2008-7-20 ����09:01:43
 * @reviewer: Liuxizhiyi
 * @time 2008-7-20 ����09:01:43
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn;

import java.util.concurrent.Callable;

import edu.pku.cn.classfile.PackageResource;
import edu.pku.cn.detector.DetectorCategory;

/**
 * 
 * @author Liuxizhiyi
 */
public class SubWork implements Callable<Integer> {
	PackageResource packageResource;
	DetectorCategory category;

	public SubWork(PackageResource resource, DetectorCategory category) {
		packageResource = resource;
		this.category = category;
	}


	int bugCount=0;
	public int getBugCount() {
		return bugCount;
	}
	public void run() {
		packageResource.accept(category);
		// System.out.println(packageResource.getPackageName());
		category.report();
		bugCount+=category.getBugCount();
	}
	public Integer call() {
		// TODO Auto-generated method stub
		packageResource.accept(category);
		// System.out.println(packageResource.getPackageName());
		category.report();
		bugCount+=category.getBugCount();
		return bugCount;
	}

}

// end
