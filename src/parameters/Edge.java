package parameters;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class Edge {
	private String source;
	private String destination;
	private List<Transfer> transfers;
	
	public Edge(String s, String d) {
		this.source = s;
		this.destination = d;
	}

	public static boolean containsPair(List<Edge> edges, Edge e) {
		boolean hasEdge = false;

		for (Edge pair : edges) {
			if ((pair.getSource().equals(e.getSource()) && pair.getDestination().equals(e.getDestination())) ||
				 pair.getSource().equals(e.getDestination()) && pair.getDestination().equals(e.getSource())) {
				hasEdge = true;

				break;
			}
		}

		return hasEdge;
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

	public List<Transfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<Transfer> transmissions) {
		this.transfers = transmissions;
	}

	public Collection<Integer> getFreeSlices(Collection<Integer> allSlices) {
		Collection<Integer> freeSlices = new HashSet<Integer>(allSlices);

		for (Transfer t : transfers) {
			freeSlices.removeAll(t.getFreeSlices(freeSlices));
		}

		return freeSlices;
	}
	
	public boolean isEqual(Edge edge) {
		boolean equal = false;
		
		if ((source.equals(edge.getSource()) && destination.equals(edge.getDestination())) || (source.equals(edge.getDestination()) && destination.equals(edge.getSource()))) {
			equal = true;
		}
		
		return equal;
	}
}
