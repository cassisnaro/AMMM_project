package com.upc.ammm.dctransfers;

import java.util.ArrayList;
import java.util.LinkedList;

import com.upc.ammm.dctransfers.models.Graph;

public class PrecomputePathsWithTransmissions {
	
	private Graph graph;	
	private String source;
	private String destination;
	private ArrayList<LinkedList<String>> paths;
	
	public PrecomputePathsWithTransmissions(Graph g) {
		this.graph = g;		
	}
	
	public void precumputePathsFromTransmission(String s, String d) {
		this.source = s;
		this.destination = d;
		
		LinkedList<String> visited = new LinkedList<String>();
        visited.add(source);
        this.paths = bfs(visited, new ArrayList<LinkedList<String>>());
	}
	
	private ArrayList<LinkedList<String>> bfs(LinkedList<String> visited, ArrayList<LinkedList<String>> paths) {
		LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
		
        // examine adjacent nodes
        for (String node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            
            if (node.equals(destination)) {
                visited.add(node);
                //printPath(visited);
                paths.add(new LinkedList<String>(visited));
                visited.removeLast();
                break;
            }
        }
        
        // in breadth-first, recursion needs to come after visiting adjacent nodes
        for (String node : nodes) {
            if (visited.contains(node) || node.equals(destination)) {
                continue;
            }
            
            visited.addLast(node);
            bfs(visited, paths);
            visited.removeLast();
        }
        
        return paths;
    }
	
	public ArrayList<LinkedList<String>> getPaths() {
		return paths;
	}
}
