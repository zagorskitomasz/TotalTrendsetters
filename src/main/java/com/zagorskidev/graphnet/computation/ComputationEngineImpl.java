package com.zagorskidev.graphnet.computation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.zagorskidev.graphnet.graph.Graph;
import com.zagorskidev.graphnet.graph.Vertex;

/**
 * This algorithm merges best parts of different approaches tested during project.
 * 
 * @author Tomasz Zagórski
 *
 */
@Component
public class ComputationEngineImpl implements ComputationEngine {

	private static final int THRESHOLD_CAP = 3000;
	
	private AtomicBoolean alreadyComputing;
	
	private Graph graph;
	
	private double attractivityTrue;
	private double attractivityFalse;
	
	private List<Vertex> initialList;
	private List<Vertex> stubbornGroup;
	private boolean[] preferencesMap;
	private boolean[] resultMap;
	
	private AtomicInteger changesCount;
	private AtomicInteger trueCount;
	private AtomicInteger falseCount;
	private boolean done;
	
	private int numToChange;
	private int nextIndexToChange;
	
	private int totalTrues;
	private int resultTrues;
	
	public ComputationEngineImpl() {
		
		alreadyComputing = new AtomicBoolean(false);
		changesCount = new AtomicInteger(0); 
		trueCount = new AtomicInteger(0); 
		falseCount = new AtomicInteger(0);
	}
	
	/**
	 * Find smallest possible group of change initializers 
	 * to achieve breakpoint and spread new preference in major part of graph
	 * It uses heuristics that vertices with more neighbors are more influential. 
	 * 
	 * Then use single adding approach to deal with stubborn vertices (if needed).
	 */
	public int findTrendsettersNumber(Graph graph, 
			CompDataSet compDataSet) {
	
		blockEngine();
	
		setComputationParams(graph, compDataSet);
		initializeAlgorithm();

		int r2Threshold = compDataSet.getThreshold();
		int r1Threshold = Math.min(initialList.size()/2, THRESHOLD_CAP);
		
		countPreferences();
		
		System.out.println("\tInit: "+printChangesResume()+"\n\tThreshold: "+r2Threshold);
		
		compute(r2Threshold, r1Threshold);
		
		System.out.println("\n\n\nResult: "+totalTrues+" initial trues.");
		
		int result = takeResult();
		releaseEngine();
		
		return result;
	}

	private void compute(int r2Threshold, int r1Threshold) {
		
		computeRoundOne(r1Threshold);
		prepareForRoundTwo(r1Threshold);
		
		if(falseCount.get()>r2Threshold) {
			computeRoundTwo(r2Threshold);
		}
	}

	private void setComputationParams(Graph graph, CompDataSet compDataSet) {
		
		this.graph = graph;
		this.attractivityTrue = compDataSet.getAttractivityTrue();
		this.attractivityFalse = compDataSet.getAttractivityFalse();
		done = false;
	}
	
	private void initializeAlgorithm() {
		populateList();
		populateMap();
		orderList();
		
		initializeVariables();

		forceChangeToTrue();
		setPreferencesFromMap();
	}
	
	private void initializeVariables() {
		totalTrues = 0;
		resetCounters();
		numToChange = initialList.size()/2;
		nextIndexToChange = 0;
	}

	private void populateList() {
		
		initialList = new ArrayList<>();
		initialList.addAll(graph.getVertices());
	}
	
	private void populateMap() {
		
		preferencesMap = new boolean[initialList.size()];
		resultMap = new boolean[initialList.size()];
	}
	
	private void orderList() {
		
		Collections.sort(initialList, (a, b) -> 
				b.getNeighbors().size() - a.getNeighbors().size());
		
		for(int i=0; i<initialList.size(); i++) {
			initialList.get(i).setPrefsListIndex(i);
		}
	}
	
	private void setPreferencesFromMap() {
		
		for(int i = 0; i < preferencesMap.length; i++)
			initialList.get(i).setPreference(preferencesMap[i]);
	}
	
	/**
	 * Adding more trues to initial list 
	 */
	private void forceChangeToTrue() {
		
		for(int i = nextIndexToChange; (i < nextIndexToChange + numToChange && i < preferencesMap.length); i++) {
			preferencesMap[i] = true;
		}
		
		totalTrues += numToChange;
		nextIndexToChange = nextIndexToChange + numToChange;
		descNumToChange();
	}
	
	/**
	 * Removing some trues from initial list
	 */
	private void forceChangeToFalse() {
		
		for(int i = nextIndexToChange - numToChange; (i < nextIndexToChange && i>=0); i++) {
			preferencesMap[i] = false;
		}
		
		totalTrues -= numToChange;
		nextIndexToChange = nextIndexToChange - numToChange;
		descNumToChange();
	}

	/**
	 * Dividing next add/remove parts by 2
	 */
	private void descNumToChange() {
		
		if(numToChange%2 != 0)
			numToChange++;
		numToChange = numToChange/2;
	}
	
	private void countPreferences() {
		
		for(Vertex vertex : initialList) {
			
			if(vertex.getPreference()==true)
				trueCount.incrementAndGet();
			else
				falseCount.incrementAndGet();
		}
	}
	
	private String printChangesResume() {
		
		return "Changes: "+changesCount.get()+", trues: "+trueCount.get()+", falses: "+falseCount.get();
	}

	/**
	 * Converting major part of vertices.
	 * It's efficient and precise, but leaves "stubborn group"
	 * 
	 * @param threshold
	 */
	private void computeRoundOne(int threshold) {
		int round = 0;
		
		while(numToChange>1 && alreadyComputing.get()) {
			
			round++;
			resetCounters();
			
			countPreferences();
			System.out.println("\n\n\tRound "+round+" trues: "+totalTrues);
			System.out.println(printChangesResume());
			
			computeSingleRound(threshold);

			interpretRoundResult(threshold);
		}
	}

	/**
	 * Very slow, but precisely deals with stubborn group after round one.
	 * 
	 * @param mainThreshold
	 */
	private void computeRoundTwo(int mainThreshold) {
		
		while(!done && alreadyComputing.get()) {
			convertNextVertex();
			setPreferencesFromMapRound2();
			resetCounters();
			countPreferences();
			
			System.out.println("\n\tNext round: "+printChangesResume());
			
			computeSingleRound(mainThreshold);
			
			interpretRoundResultRound2(mainThreshold);
		}
	}

	/**
	 * Loads local variables with last saved best result from round one
	 * 
	 * @param threshold
	 */
	private void prepareForRoundTwo(int threshold) {
		
		for(int i=0; i<resultMap.length; i++) {
			initialList.get(i).setPreference(resultMap[i]);
		}
		totalTrues = resultTrues;
		System.out.println("\n\n\tPreparing for round two...");
		computeSingleRound(threshold);
	}
	
	/**
	 * Data flow algorithm, ends with equilibrium or domination of one option.
	 * 
	 * @param threshold
	 */
	protected void computeSingleRound(int threshold) {
		
		changesCount.set(1);
		
		int[] periodChecker = new int[100];
		
		for(int i = 0; i<100 && isLoopNotInPeriod(periodChecker, i) && alreadyComputing.get(); i++) {
			System.out.print("Loop "+i+": ");
			computeRoundWithStreams();
			
			periodChecker[i]=changesCount.get();
		}
		
		setStubbornGroup();
	}

	/**
	 * One step of data flow algorithm
	 */
	private void computeRoundWithStreams() {
		
		resetCounters();
		
		initialList.parallelStream()
				.forEach(vertex -> makeDecission(vertex));
		initialList.parallelStream()
				.forEach(vertex -> vertex.switchPreference());

		System.out.println(printChangesResume());
	}

	/**
	 * Checks if it's time to stop algorithm
	 * 
	 * @param periodChecker
	 * @param i
	 * @return
	 */
	private boolean isLoopNotInPeriod(int[] periodChecker, int i) {
		
		return (i>=3 && (periodChecker[i-1]!=periodChecker[i-2] || 
		periodChecker[i-1]!=periodChecker[i-3])) || i<3;
	}

	/**
	 * 
	 * Creates list of vertices that didn't changed preference at the end data flow
	 */
	private void setStubbornGroup() {
		
		stubbornGroup = new LinkedList<>();
		
		for(Vertex vertex : initialList) {
			if(!vertex.getPreference()) {
				stubbornGroup.add(vertex);
			}
		}
	}
	
	/**
	 * Decides if vertex should change preference
	 * 
	 * @param vertex
	 * @return
	 */
	private Vertex makeDecission(Vertex vertex) {
		
		double optionTrue = 0;
		double optionFalse = 0;
		
		for(Vertex neighbor : vertex.getNeighbors()) {
			
			if(neighbor.getPreference()==true)
				optionTrue += attractivityTrue;
			else
				optionFalse += attractivityFalse;
		}
		
		executeDecission(vertex, optionTrue, optionFalse);
		
		return vertex;
	}

	/**
	 * There are some helper methods for data flow algorithm
	 * 
	 * @param vertex
	 * @param optionTrue
	 * @param optionFalse
	 */
	private void executeDecission(Vertex vertex, double optionTrue, double optionFalse) {
		
		if(vertex.getPreference()==true) {
			
			if(optionFalse>optionTrue) {
				switchTrueToFalse(vertex);
			}
			else {
				stayTrue(vertex);
			}
		}
		else {
			
			if(optionTrue>optionFalse) {
				switchFalseToTrue(vertex);
			}
			else {
				stayFalse(vertex);
			}
		}
	}

	private void switchFalseToTrue(Vertex vertex) {
		vertex.setNewPreference(true);
		changesCount.incrementAndGet();
		trueCount.incrementAndGet();
	}

	private void switchTrueToFalse(Vertex vertex) {
		vertex.setNewPreference(false);
		changesCount.incrementAndGet();
		falseCount.incrementAndGet();
	}

	private void stayFalse(Vertex vertex) {
		vertex.setNewPreference(false);
		falseCount.incrementAndGet();
	}

	private void stayTrue(Vertex vertex) {
		vertex.setNewPreference(true);
		trueCount.incrementAndGet();
	}
	
	private void resetCounters() {
		
		changesCount.set(0);
		trueCount.set(0);
		falseCount.set(0);
	}
	
	/**
	 * And helpers for round two algorithm.
	 */
	private void setPreferencesFromMapRound2() {
		
		for(int i = 0; i < resultMap.length; i++)
			initialList.get(i).setPreference(resultMap[i]);
	}
	
	private void convertNextVertex() {
		
		int firstFalse = -1;
		firstFalse = findFalseInStubbornGroup(firstFalse);
		
		resultMap[firstFalse] = true;
		preferencesMap[firstFalse] = true;
		totalTrues++;
	}

	private int findFalseInStubbornGroup(int firstFalse) {
		
		List<Vertex> candidates;
		candidates = stubbornGroup;
		
		while(firstFalse<0) {

			firstFalse = findFalseInList(candidates);
		
			if(firstFalse<0) {
				candidates = goDeeper(candidates);
			}
		}
		return firstFalse;
	}
	
	private int findFalseInList(List<Vertex> candidates) {
		
		Collections.sort(candidates, (a, b) -> b.getNeighbors().size() - a.getNeighbors().size());
		
		for(Vertex vertex : candidates) {
			if(!resultMap[vertex.getPrefsListIndex()])
				return vertex.getPrefsListIndex();
		}
		return -1;
	}
	
	private List<Vertex> goDeeper(List<Vertex> candidates) {
		
		List<Vertex> newCandidates = new LinkedList<>();
		
		for(Vertex vertex : candidates) {
			newCandidates.addAll(vertex.getNeighbors());
		}
		return newCandidates;
	}
	
	protected void interpretRoundResultRound2(int threshold) {
		
		if(falseCount.get() <= threshold) {
			done = true;
		}
	}
	
	/**
	 * Decides if we should add or remove some trues before next round of computation
	 */
	protected void interpretRoundResult(int threshold) {
		
		if(falseCount.get()<=threshold) {
			
			saveBestResult();
			
			forceChangeToFalse();
			setPreferencesFromMap();
		}
		else {
			forceChangeToTrue();
			setPreferencesFromMap();
		}
	}

	private void saveBestResult() {
		resultTrues = totalTrues;
		
		for(int i=0; i<initialList.size(); i++) {
			resultMap[i] = preferencesMap[i];
		}
	}

	private int takeResult() {
		
		int result;
		if(alreadyComputing.get())
			result = totalTrues;
		else
			result = -1;
		return result;
	}
	
	/**
	 * Some utility methods for engine controlling.
	 */
	public boolean isAlreadyComputing() {
		
		return alreadyComputing.get();
	}
	
	public void blockEngine() {
		
		alreadyComputing.set(true);
	}
	
	private void releaseEngine() {
		
		alreadyComputing.set(false);
	}
	
	public void cancelComputation() {
		
		releaseEngine();
	}
}
