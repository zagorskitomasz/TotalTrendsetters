package com.zagorskidev.graphnet.computation;

import com.zagorskidev.graphnet.graph.Graph;

/**
 * Abstract computation engine for Spring IoC use.
 * 
 * @author Tomasz Zagorski
 * 
 */
public interface ComputationEngine {

	public int findTrendsettersNumber(Graph graph, CompDataSet compDataSet);
	public boolean isAlreadyComputing();
	public void blockEngine();
	public void cancelComputation();
}
