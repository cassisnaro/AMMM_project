package com.upc.ammm.dctransfers.models;

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
}
