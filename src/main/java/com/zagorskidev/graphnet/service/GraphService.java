package com.zagorskidev.graphnet.service;

import javax.servlet.http.HttpSession;

import com.zagorskidev.graphnet.computation.CompDataSet;

/**
 * Abstract graph service for Spring IoC use.
 * 
 * @author Tomasz Zagórski
 *
 */
public interface GraphService {

	public boolean tryCompute(HttpSession session, CompDataSet compDataSet);
	public void cancelComputation();
}
