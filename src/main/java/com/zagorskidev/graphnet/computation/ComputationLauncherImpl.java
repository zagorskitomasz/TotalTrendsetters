package com.zagorskidev.graphnet.computation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zagorskidev.graphnet.graph.Graph;
import com.zagorskidev.graphnet.graph.GraphLoader;

/**
 * Starts and controls computation
 * 
 * @author Tomasz Zagorski
 *
 */
@Component
public class ComputationLauncherImpl implements ComputationLauncher {

	@Autowired
	private ComputationEngine computationEngine;
	
	/**
	 * It will be run in other thread
	 * Since project algorithm is very heavy, it won't be launched more than one in time
	 * 
	 * Uses http session to transfer computation result directly to view layer.
	 */
	@Override
	@Async
	public void launchAsyncComputation(HttpSession session, CompDataSet compDataSet) {
		
		fillSessionWithZeros(session);
		
		Graph graph = new Graph();
		GraphLoader.loadGraph(graph, "src/main/resources/"+compDataSet.getGraphName()+".txt");
		
		int truesPercent = compDataSet.getThreshold();
		compDataSet.setThreshold((1-truesPercent/100)*graph.numVertices());
		System.out.println(compDataSet.getThreshold());
		int result = computationEngine.findTrendsettersNumber(graph, compDataSet);
		
		if(result>=0) {
			session.setAttribute("graphName", compDataSet.getGraphName());
			session.setAttribute("graphSize", graph.numVertices());
			session.setAttribute("convPercent", truesPercent);
			session.setAttribute("convByTrues", result);
			session.setAttribute("attTrue", compDataSet.getAttractivityTrue());
			session.setAttribute("attFalse", compDataSet.getAttractivityFalse());
		}
		
		session.setAttribute("computing", "false");
	}

	private void fillSessionWithZeros(HttpSession session) {
		
		session.setAttribute("computing", "true");
		session.setAttribute("graphName", "");
		session.setAttribute("graphSize", 0);
		session.setAttribute("convPercent", 0);
		session.setAttribute("convByTrues", 0);
		session.setAttribute("attTrue", 0);
		session.setAttribute("attFalse", 0);
	}

	@Override
	public boolean isAlreadyComputing() {
		
		return computationEngine.isAlreadyComputing();
	}

	@Override
	public void blockEngine() {
		
		computationEngine.blockEngine();
	}

	@Override
	public void cancelComputation() {
		
		computationEngine.cancelComputation();
	}
}
