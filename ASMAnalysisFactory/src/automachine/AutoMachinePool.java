/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-2 下午03:05:33
 * @modifier: root
 * @time 2009-12-2 下午03:05:33
 * @reviewer: root
 * @time 2009-12-2 下午03:05:33
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package automachine;

import java.io.File;
import java.util.ArrayList;

import autoAdapter.AutoMethodAdapter;

public class AutoMachinePool {
	private static AutoMachinePool instance = null;
	private ArrayList<AutoMethodAdapter> autoMachines;

	private AutoMachinePool() {
		boolean generateFromXml = !System.getProperty("AutoMethodAdapter.usexmltoconfig", "yes").equals("no");
		// generate automachines from xml specfic files
		if (generateFromXml) {
			System.out.println("use xml files to config ams");
			autoMachines = new ArrayList<AutoMethodAdapter>();

			String specDir = "AutomachineSpecs";
			File dir = new File(specDir);
			if (dir == null || !dir.exists()) {
				System.out.println(specDir + "not exist!");
			}
			if (!dir.isDirectory()) {
				System.out.println(specDir + "is not a directory!");
			}
			File files[] = dir.listFiles();
			for (File file : files) {
				if (file.getName().toLowerCase().startsWith("detector")) {
					autoMachines.add(new AutoMethodAdapter(file.getAbsolutePath()));
				}
			}
		} else {
			System.out.println("use database to config ams");
			autoMachines = new ArrayList<AutoMethodAdapter>();
			System.out.println("accessing database to generate automachines...");
			ArrayList<AutoMachine> ams = AutomaUtil.generateAllAutoMachineFromDatabase();
			for (AutoMachine am : ams) {
				autoMachines.add(new AutoMethodAdapter(am));
			}
		}
	}

	public static synchronized AutoMachinePool getInstance() {
		if (instance == null)
			instance = new AutoMachinePool();
		return instance;
	}

}

// end
