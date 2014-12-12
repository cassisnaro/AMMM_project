package com.upc.ammm.dctransfers.models;

import java.util.List;

public class Pair {
	private String source;
	private String destination;
	
	public Pair(String s, String d) {
		this.source = s;
		this.destination = d;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public static boolean containsPair(List<Pair> pairs, Pair p) {
		boolean hasPair = false;
		
		for (Pair pair : pairs) {
			if ((pair.getSource().equals(p.getSource()) && pair.getDestination().equals(p.getDestination())) || 
				 pair.getSource().equals(p.getDestination()) && pair.getDestination().equals(p.getSource())) {
				hasPair = true;
				
				break;
			}
		}
		
		return hasPair;
	}
}
