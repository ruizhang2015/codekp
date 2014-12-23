/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author zhouzhiyi
 * @time 2009-12-24 下午03:44:07
 * @modifier: root
 * @time 2009-12-24 下午03:44:07
 * @reviewer: root
 * @time 2009-12-24 下午03:44:07
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.analysis.dataflow;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import edu.pku.cn.graph.Graph;
import edu.pku.cn.graph.GraphVertex;
import edu.pku.cn.graph.visit.BlockOrder;

/**
 * Abstract class that provides the fixed point iteration functionality required
 * by all ForwardFlowAnalyses.
 * 
 */
public class FowardFlow<Fact, AnalysisType extends DataflowAnalysis<Fact, GraphVertex>> {
	/** Maps graph nodes to OUT sets. */
	protected Map<GraphVertex, Fact> unitToAfterFlow;
	/** Maps graph nodes to IN sets. */
	protected Map<GraphVertex, Fact> unitToBeforeFlow;

	protected Collection<GraphVertex> constructWorklist(final Map<GraphVertex, Integer> numbers) {
		return new TreeSet<GraphVertex>(new Comparator<GraphVertex>() {
			public int compare(GraphVertex o1, GraphVertex o2) {
				Integer i1 = numbers.get(o1);
				Integer i2 = numbers.get(o2);
				return (i1.intValue() - i2.intValue());
			}
		});
	}

	protected void doAnalysis(Graph graph, AnalysisType analysis) {
		// TODO Auto-generated method stub
		int numNodes = graph.getVerticeSize();
		unitToBeforeFlow = new HashMap<GraphVertex, Fact>(numNodes * 2 + 1, 0.7f);
		unitToAfterFlow = new HashMap<GraphVertex, Fact>(numNodes * 2 + 1, 0.7f);

		final Map<GraphVertex, Integer> numbers = new HashMap<GraphVertex, Integer>();

		// List<GraphVertex> orderedUnits = analysis.getBlockOrder();
		BlockOrder order = analysis.getBlockOrder();
		// Timers.v().orderComputation.end();
		int i = 1;
		for (Iterator<GraphVertex> uIt = order.blockIterator(); uIt.hasNext();) {
			final GraphVertex u = uIt.next();
			numbers.put(u, new Integer(i));
			i++;
		}
		Collection<GraphVertex> changedUnits = constructWorklist(numbers);
		int numComputations = 0;
		// Set initial values and nodes to visit.
		{
			Iterator<GraphVertex> it = graph.vertexIterator();

			while (it.hasNext()) {
				GraphVertex s = it.next();

				changedUnits.add(s);

				unitToBeforeFlow.put(s, analysis.newInitialFlow());
				unitToAfterFlow.put(s, analysis.newInitialFlow());
			}
			GraphVertex head = order.getEntry();

			// Set initial values for entry points
			unitToBeforeFlow.put(head, analysis.entryInitialFlow());

			// Perform fixed point flow analysis

			Fact previousAfterFlow = analysis.newInitialFlow();
			while (!changedUnits.isEmpty()) {
				Fact beforeFlow;
				Fact afterFlow;

				// get the first object
				GraphVertex s = changedUnits.iterator().next();
				changedUnits.remove(s);
				boolean isHead = head.equals(s);

				analysis.copy(unitToAfterFlow.get(s), previousAfterFlow);

				// Compute and store beforeFlow
				beforeFlow = unitToBeforeFlow.get(s);
				Iterator<GraphVertex> iter = s.predecessorIterator();
				int vertexSize = 0;
				while (iter.hasNext()) {
					vertexSize++;
					if (vertexSize == 1)
						analysis.copy(unitToAfterFlow.get(iter.next()), beforeFlow);
					else
						analysis.merge(beforeFlow, unitToAfterFlow.get(iter.next()));
				}
				// Compute afterFlow and store it.
				afterFlow = unitToAfterFlow.get(s);
				analysis.flowThrough(beforeFlow, s, afterFlow);
				numComputations++;
				// Update queue appropriately
				if (!analysis.same(previousAfterFlow, afterFlow)) {
					Iterator<GraphVertex> succ = s.successorIterator();
					while (succ.hasNext()) {
						changedUnits.add(succ.next());
					}
				}
			}
		}
	}
}

// end
