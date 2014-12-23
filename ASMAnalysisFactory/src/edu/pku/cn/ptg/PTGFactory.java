/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author liuxi
 * @time 2010-3-16
 * @modifier: liuxi
 * @time 2010-3-16
 * @reviewer: liuxi
 * @time 2010-3-16
 * (C) Copyright PKU Software Lab. 2010
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.ptg;

import org.objectweb.asm.tree.analysis.AnalyzerException;

import edu.pku.cn.analysis.AnalysisFactory;
import edu.pku.cn.classfile.MethodNode;

/**
 * @author liuxi
 */
public class PTGFactory extends AnalysisFactory<MethodNode, PointToGraph> {
	public final static String NAME = "PTGFactory";
	PTGBuilder builder=new PTGBuilder();
	@Override
	protected PointToGraph analyze(MethodNode target) throws AnalyzerException {
		// TODO Auto-generated method stub
		return builder.analyze(target);
	}

	@Override
	public AnalysisFactory clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new PTGFactory();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

}

// end
