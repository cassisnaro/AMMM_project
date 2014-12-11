package com.upc.ammm.dctransfers.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
	private Map<String, LinkedHashSet<NodePair>> map = new HashMap<String, LinkedHashSet<NodePair>>();
    private Map<Link, Integer> linksIdentifier = new HashMap<>();
    private int identifierCount = 0;
    private ArrayList<Path> paths;

    public void addEdge(String node1, String node2, int node2Cost) {
        LinkedHashSet<NodePair> adjacent = map.get(node1);
        
        if (adjacent == null) {
            adjacent = new LinkedHashSet<NodePair>();
            map.put(node1, adjacent);
        }
        
        adjacent.add(new NodePair(node2, node2Cost));

    }

    public void addTwoWayVertex(String node1, int node1Cost, String node2, int node2Cost) {
        addEdge(node1, node2, node2Cost);
        addEdge(node2, node1, node1Cost);

        linksIdentifier.put(new Link(node1,node2), new Integer(identifierCount));
        identifierCount++;
    }

    public boolean isConnected(String node1, NodePair node2) {
    	Set<NodePair> adjacent = map.get(node1);
    	
        if (adjacent == null) {
            return false;
        }
        
        return adjacent.contains(node2);
    }

    public LinkedList<NodePair> adjacentNodes(String last) {
        LinkedHashSet<NodePair> adjacent = map.get(last);
        
        if (adjacent == null) {
            return new LinkedList<NodePair>();
        }
        
        return new LinkedList<NodePair>(adjacent);
    }

	public Map<Link, Integer> getLinksIdentifier() {
		return linksIdentifier;
	}
	
	public Set<String> getNodes() {
		return map.keySet();
	}
	
	public ArrayList<Path> getPaths() {
		return paths;
	}
}
