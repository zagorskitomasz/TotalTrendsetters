package com.zagorskidev.graphnet.computation;

import javax.servlet.http.HttpSession;

import org.springframework.scheduling.annotation.Async;

/**
 * Abstract computation launcher for Spring IoC use.
 * 
 * @author Tomasz Zagorski
 *
 */
public interface ComputationLauncher {

	@Async
	public void launchAsyncComputation(HttpSession session, CompDataSet compDataSet);
	
	public boolean isAlreadyComputing();
	public void blockEngine();
	public void cancelComputation();
}
