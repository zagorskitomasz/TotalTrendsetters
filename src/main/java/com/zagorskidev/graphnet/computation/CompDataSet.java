package com.zagorskidev.graphnet.computation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * Model argument, covers data sent from view to computation engine.
 * 
 * Validated with Hibernate Validator
 * 
 * @author Tomasz Zagorski
 *
 */
@Component
public class CompDataSet {

	@NotNull(message="is required")
	@NotEmpty(message="is required")
	private String graphName;
	
	@Max(value=100, message="must be less than or equal to 100%")
	@Min(value=1, message="must be greater than or equal to 1%")
	@NotNull(message="is required")
	private int threshold;
	
	@Max(value=20, message="must be less than or equal to 20")
	@Min(value=1, message="must be greater than or equal to 1")
	@NotNull(message="is required")
	private int attractivityTrue;
	
	@Max(value=20, message="must be less than or equal to 20")
	@Min(value=1, message="must be greater than or equal to 1")
	@NotNull(message="is required")
	private int attractivityFalse;
	
	public CompDataSet() {}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getAttractivityTrue() {
		return attractivityTrue;
	}

	public void setAttractivityTrue(int attractivityTrue) {
		this.attractivityTrue = attractivityTrue;
	}

	public int getAttractivityFalse() {
		return attractivityFalse;
	}

	public void setAttractivityFalse(int attractivityFalse) {
		this.attractivityFalse = attractivityFalse;
	}
}
