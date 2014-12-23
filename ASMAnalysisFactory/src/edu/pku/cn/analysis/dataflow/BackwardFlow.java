/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author root
 * @time 2009-12-25 下午01:06:38
 * @modifier: root
 * @time 2009-12-25 下午01:06:38
 * @reviewer: root
 * @time 2009-12-25 下午01:06:38
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
 * by all BackwardFlowAnalyses.
 * 
 */
public class BackwardFlow<Fact, AnalysisType extends DataflowAnalysis<Fact, GraphVertex>> {
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
			GraphVertex exit = order.getExit();
			// Set initial values for entry points
			unitToAfterFlow.put(exit, analysis.entryInitialFlow());

			// Perform fixed point flow analysis

			Fact previousBeforeFlow = analysis.newInitialFlow();
			while (!changedUnits.isEmpty()) {
				Fact beforeFlow;
				Fact afterFlow;

				// get the first object
				GraphVertex s = changedUnits.iterator().next();
				changedUnits.remove(s);
				// boolean isHead = exit.equals(s);

				analysis.copy(unitToBeforeFlow.get(s), previousBeforeFlow);

				// Compute and store beforeFlow
				afterFlow = unitToAfterFlow.get(s);
				Iterator<GraphVertex> iter = s.successorIterator();
				int vertexSize = 0;
				while (iter.hasNext()) {
					vertexSize++;
					if (vertexSize == 1)
						analysis.copy(unitToBeforeFlow.get(iter.next()), afterFlow);
					else
						analysis.merge(afterFlow, unitToBeforeFlow.get(iter.next()));
				}
				// Compute afterFlow and store it.
				beforeFlow = unitToBeforeFlow.get(s);
				analysis.flowThrough(afterFlow, s, beforeFlow);
				numComputations++;
				// Update queue appropriately
				if (!analysis.same(previousBeforeFlow, beforeFlow)) {
					Iterator<GraphVertex> pred = s.predecessorIterator();
					while (pred.hasNext()) {
						changedUnits.add(pred.next());
					}
				}
			}

		}
	}
}

// end
