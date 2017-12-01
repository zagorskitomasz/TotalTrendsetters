/**
 * 
 */
package com.zagorskidev.graphnet.graph;

import java.util.Collection;
import java.util.HashMap;

/**
 * Specific graph implementation for this application purposes.
 * No unused methods like egonet etc. from starter code.
 * Simple and clean.
 * 
 * @author Tomasz Zagórski
 *
 */
public class Graph {

	private HashMap<Integer, Vertex> vertices;
	
	public Graph() {
		vertices = new HashMap<>();
	}
	
	public void addVertex(int num) {
		if(!vertices.containsKey(num))
			vertices.put(num, new Vertex(num));
	}
	
	public Collection<Vertex> getVertices(){
		return vertices.values();
	}

	public void addEdge(int from, int to) {
		confirmVertices(from, to);
		vertices.get(from).addNeighbor(vertices.get(to));
	}
	
	private void confirmVertices(int from, int to) {
		if(!vertices.containsKey(from))
			addVertex(from);
		if(!vertices.containsKey(to))
			addVertex(to);
	}
	
	public int numVertices() {
		return vertices.size();
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("\nGraph\n");
		for(Vertex vertex : vertices.values()) {
			stringBuilder.append("Vertex "+vertex+" with neighbors: "+vertex.getNeighbors()+"\n");
		}
		return stringBuilder.toString();
	}
}
