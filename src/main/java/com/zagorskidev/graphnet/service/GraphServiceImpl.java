package com.zagorskidev.graphnet.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zagorskidev.graphnet.computation.CompDataSet;
import com.zagorskidev.graphnet.computation.ComputationLauncher;

/**
 * Implements graph service. Transfer (and select) tasks for deeper app layers.
 * 
 * @author Tomasz Zagórski
 *
 */
@Service
public class GraphServiceImpl implements GraphService{
	
	@Autowired
	private ComputationLauncher computationLauncher;

	/**
	 * Checks if engine is free and, if yes, creates new async computation task.
	 * 
	 * @param session
	 * @param compDataSet
	 * @return it's not computation result! this boolean tells if computation was started
	 */
	@Override
	public boolean tryCompute(HttpSession session, CompDataSet compDataSet) {
		
		synchronized(this) {
			
			if(computationLauncher.isAlreadyComputing()) {
				return false;
			}
			else {
				computationLauncher.blockEngine();
				session.setAttribute("computing", "true");
				computationLauncher.launchAsyncComputation(session, compDataSet);
				return true;
			}
		}
	}
	
	@Override
	public void cancelComputation() {
		
		computationLauncher.cancelComputation();
	}
}
