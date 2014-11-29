package com.upc.ammm.dctransfers.models;

import java.util.LinkedList;

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
}
