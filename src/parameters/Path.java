package parameters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
	
	public List<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		Edge auxEdge;
		int pathSize = path.size();
		
		for (int i = 1; i <= pathSize; i++) {
			auxEdge = new Edge(path.get(i-1).getName(), path.get(i).getName());
			edges.add(auxEdge);
		}
		
		return edges;
	}
 	
	public boolean hasEdge(Edge e) {
		boolean hasLink = false;
		int pathSize = path.size();
		String previousNode = path.get(0).getName(); 	
		
		for (int i = 1; i < pathSize; i++) {
//			System.out.println("COMPARING: (" + previousNode + "," + path.get(i).getName() + ") WITH (" + e.getSource() + "," + e.getDestination() + ")");
			if (e.isEqual(new Edge(previousNode, path.get(i).getName()))) {
//				System.out.println("IS EQUAL!");
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
