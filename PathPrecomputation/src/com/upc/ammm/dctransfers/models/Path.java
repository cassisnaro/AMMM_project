package com.upc.ammm.dctransfers.models;

import java.util.LinkedList;

import com.oracle.jrockit.jfr.ValueDefinition;

public class Path {
	private LinkedList<NodePair> path;
	private int cost;
	
	public Path(LinkedList<NodePair> path, int cost) {
		this.path = path;
		this.cost = cost;
	}

	public LinkedList<NodePair> getPath() {
		return path;
	}

	public void setPath(LinkedList<NodePair> path) {
		this.path = path;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public boolean hasLink(Link l) {
		boolean hasLink = false;
		int pathSize = path.size();
		String previousNode = path.get(0).getName(); 	
		
		for (int i = 1; i < pathSize; i++) {
			if (l.isEqual(new Link(previousNode, path.get(i).getName()))) {
				hasLink = true;
				break;
			}
			
			previousNode = path.get(i).getName();
		}
		
		return hasLink;
	}
	 
	@Override
	public String toString() {
		String pathString = "[";
		int pathSize = path.size();
		int last = pathSize - 1;
		
		for (int i = 0; i < pathSize; i++) {
			pathString += path.get(i).getName();
			
			if (i != last) {
				pathString += ", ";
			}
		}
		
		pathString += "]";
		
		return pathString;
	}
}
