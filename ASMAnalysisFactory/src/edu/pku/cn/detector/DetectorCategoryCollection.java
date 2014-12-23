/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-6-30 ����02:14:55
 * @modifier: Liuxizhiyi
 * @time 2008-6-30 ����02:14:55
 * @reviewer: Liuxizhiyi
 * @time 2008-6-30 ����02:14:55
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.detector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pku.cn.xml.detectorConfiger.CategoryType;
import edu.pku.cn.xml.detectorConfiger.DocumentRoot;
import edu.pku.cn.xml.detectorConfiger.util.DetectorConfigerResourceUtil;

/**
 * 
 * @author Liuxizhiyi
 */
public class DetectorCategoryCollection {
	public static final String configerFileName = "DetectorConfiger.xml";
	public static final String configerpath = "etc";

	DocumentRoot root;
	String path;

	List<DetectorCategory> categorys = new ArrayList<DetectorCategory>();

	public int getCategorySize(){
		return categorys.size();
	}
	public void clear() {
		if (categorys != null) {
			for (DetectorCategory dc : categorys) {
				dc.clear();
			}
			categorys.clear();
		}
	}

	public DetectorCategoryCollection() {
		path = System.getProperty("user.dir");
		// path="E:\\mass\\workspace\\ASMAnalysisFactory\\";
		String filepath = "file:/" + path + "/" + configerpath + "/";
		if (path != null) {
			try {
				root = DetectorConfigerResourceUtil.getInstance().load(filepath + configerFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (root != null) {
			List<CategoryType> categoryTypes = root.getDectectorFactory().getCategory();
			Iterator<CategoryType> iter = categoryTypes.iterator();
			while (iter.hasNext()) {
				categorys.add(new DetectorCategory(iter.next(), path));
			}
		}
	}

	public Iterator<DetectorCategory> categoryIterator() {
		return categorys.iterator();
	}
}

// end
