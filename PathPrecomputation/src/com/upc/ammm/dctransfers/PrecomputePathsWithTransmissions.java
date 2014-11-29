package com.upc.ammm.dctransfers;

import java.util.ArrayList;
import java.util.LinkedList;

import com.upc.ammm.dctransfers.models.Graph;
import com.upc.ammm.dctransfers.models.NodePair;
import com.upc.ammm.dctransfers.models.Path;

public class PrecomputePathsWithTransmissions {
	
	private Graph graph;	
	private String source;
	private String destination;
	private ArrayList<Path> paths;
	
	public PrecomputePathsWithTransmissions(Graph g) {
		this.graph = g;		
	}
	
	public void precumputePathsFromTransmission(String s, String d) {
		this.source = s;
		this.destination = d;
		
		LinkedList<NodePair> visited = new LinkedList<NodePair>();
        visited.add(new NodePair(source, 0));
        this.paths = bfs(visited, new ArrayList<Path>());
	}
	
	private ArrayList<Path> bfs(LinkedList<NodePair> visited, ArrayList<Path> paths) {
		LinkedList<NodePair> nodes = graph.adjacentNodes(visited.getLast().getName());
		
        // examine adjacent nodes
        for (NodePair node : nodes) {
            if (containsNode(visited, node)) {
                continue;
            }
            
            if (node.getName().equals(destination)) {
                visited.add(node);
                //printPath(visited);
                paths.add(new Path(new LinkedList<NodePair>(visited), computePathCost(visited)));
                visited.removeLast();
                break;
            }
        }
        
        // in breadth-first, recursion needs to come after visiting adjacent nodes
        for (NodePair node : nodes) {
            if (containsNode(visited, node) || node.getName().equals(destination)) {
                continue;
            }
            
            visited.addLast(node);
            bfs(visited, paths);
            visited.removeLast();
        }
        
        return paths;
    }
	
	public ArrayList<Path> getPaths() {
		return paths;
	}
	
	public int computePathCost(LinkedList<NodePair> path) {
		int cost = 0;
		
		for (NodePair node : path) {
			cost += node.getCost();
		}
		
		return cost;
	}
	
	public boolean containsNode(LinkedList<NodePair> visited, NodePair node) {
		boolean contained = false;
		
		for (NodePair n : visited) {
			if (n.getName().equals(node.getName())) {
				contained = true;
				
				break;
			}
		}
		
		return contained;
	}
}
