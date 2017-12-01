package com.zagorskidev.graphnet.graph;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Stores data about single graph node.
 * For specific project purposes, contains informations about preferences and position in list.
 * 
 * @author Tomasz Zagórski
 *
 */
public class Vertex {

	private final int value;
	private HashSet<Vertex> neighbors;
	
	private boolean preference;
	private boolean newPreference;
	private int prefsListIndex;
	
	public int getPrefsListIndex() {
		return prefsListIndex;
	}

	public void setPrefsListIndex(int prefsListIndex) {
		this.prefsListIndex = prefsListIndex;
	}
	
	public Vertex(int value) {
		this.value = value;
		this.neighbors = new HashSet<>();
	}
	
	public boolean getPreference() {
		return preference;
	}

	public void setNewPreference(boolean newPreference) {
		this.newPreference = newPreference;
	}
	
	public void setPreference(boolean preference) {
		this.preference = preference;
	}
	
	public void switchPreference() {
		
		preference = newPreference;
	}

	public void addNeighbor(Vertex neighbor) {
		this.neighbors.add(neighbor);
	}
	
	public int getValue() {
		return this.value;
	}
	
	public HashSet<Vertex> getNeighbors(){
		return this.neighbors;
	}

	public HashSet<Integer> getNeighborsAsIntSet() {
		return neighbors
			.stream()
			.map(element -> element.getValue())
			.collect(Collectors.toCollection(HashSet::new));
	}

	@Override
	public String toString() {
		return value + " (" + preference + ")";
	}
	
	
}
