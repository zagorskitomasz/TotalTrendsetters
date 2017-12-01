package com.zagorskidev.graphnet.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zagorskidev.graphnet.computation.CompDataSet;
import com.zagorskidev.graphnet.service.GraphService;

/**
 * Spring controller for web app. Resolves http requests.
 * 
 * @author Tomasz Zagórski
 *
 */
@Controller
public class GraphController {
	
	@Autowired
	private GraphService graphService;
	
	private String[] graphNameOptions = {"facebook_1000", "facebook_2000", "facebook_ucsd"};
	
	@RequestMapping({"/", "home"})
	public String showHome() {
		
		return "home";
	}
	
	/**
	 * Prepares form for computation parameters.
	 * 
	 * @param model
	 * @return view name
	 */
	@RequestMapping("/form")
	public String showForm(Model model) {
		
		CompDataSet compDataSet = createInitDataSet();
		
		model.addAttribute("compDataSet", compDataSet);
		model.addAttribute("graphNameOptions", graphNameOptions);
		
		return "data-form";
	}
	
	/**
	 * Validates form, try starting computation service and deal with result.
	 * 
	 * @param compDataSet
	 * @param bindingResult
	 * @param session
	 * @param model
	 * @return view name
	 */
	@PostMapping("/compute")
	public String compute(@Valid @ModelAttribute("compDataSet") CompDataSet compDataSet, 
			BindingResult bindingResult, HttpSession session, Model model) {
		
		if(bindingResult.hasErrors()) {
			
			model.addAttribute("compDataSet", compDataSet);
			model.addAttribute("graphNameOptions", graphNameOptions);
			return "data-form";
		}
		else {
			boolean success = graphService.tryCompute(session, compDataSet);
			
			if(success)
				return "redirect:/result";
			else 
				return "server-busy";
		}
	}
	
	@RequestMapping("/result")
	public String showResult() {
		
		return "result-page";
	}
	
	@RequestMapping("/cancel")
	public String cancelComputation() {
		
		graphService.cancelComputation();
		return "canceled";
	}

	private CompDataSet createInitDataSet() {
		
		CompDataSet compDataSet = new CompDataSet();
		
		compDataSet.setGraphName("");
		compDataSet.setThreshold(100);
		compDataSet.setAttractivityFalse(2);
		compDataSet.setAttractivityTrue(3);
		
		return compDataSet;
	}
}
