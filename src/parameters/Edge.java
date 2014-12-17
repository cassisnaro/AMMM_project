package parameters;

import java.util.*;


public class Edge {
	private String source;
	private String destination;
	private List<Transfer> transfers;
	
	public Edge(String s, String d) {
		this.source = s;
		this.destination = d;
		this.transfers = new ArrayList<Transfer>();
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
			freeSlices.removeAll(t.getOccupiedSlices(freeSlices));
		}
		return freeSlices;
	}

    public Collection<Integer> getTransmissionLimits() {
        Collection<Integer> transmissionLimits = new LinkedHashSet<>();

        for (Transfer t : transfers) {
            transmissionLimits.add(t.getFirstSlice());
            transmissionLimits.add(t.getLastSlice());
        }
        return transmissionLimits;
    }



    public Set<Integer> workOnSegment(Integer minSlice, Integer maxSlice, int extraSlicesNeeded){
        Set<Integer> impossibleSlicesForEdge=new HashSet<>();
        ReschedulePair reschedulePair = null;
        for (Transfer t : transfers){
            if(minSlice > t.getFirstSlice() && t.getLastSlice() > maxSlice){
                impossibleSlicesForEdge.addAll(t.getCurrentSlices());
            }else if (minSlice < t.getFirstSlice() && t.getLastSlice() < maxSlice) {
                impossibleSlicesForEdge.addAll(t.getCurrentSlices());
            }else {
                Collection<Integer> impossibleSlicesForTransfer=t.maximize_free_room(minSlice, maxSlice, extraSlicesNeeded);
                impossibleSlicesForEdge.addAll(impossibleSlicesForTransfer);
            }

        }
        return impossibleSlicesForEdge;
    }

    public Set<Integer> workOnTransferIntersection(Integer pos, int extraSlicesNeeded){
        Set<Integer> impossibleSlicesForEdge=new HashSet<>();
        for (Transfer t : transfers){
            impossibleSlicesForEdge.addAll(t.maximize_free_room(pos, extraSlicesNeeded));
        }
        return impossibleSlicesForEdge;
    }

   /* public ReschedulePair workOnSegment(Integer minSlice, Integer maxSlice, int extraSlicesNeeded){
        Set<Integer> impossibleSlicesForEdge=new HashSet<>();
        ReschedulePair reschedulePair = null;
        for (Transfer t : transfers){
            if(minSlice > t.getFirstSlice() && t.getLastSlice() > maxSlice){
                reschedulePair= t.freeSlicesWithinTransfer(minSlice, maxSlice);
            }else if (minSlice < t.getFirstSlice() && t.getLastSlice() < maxSlice) {
                Set<Integer> auxImpossibles=new HashSet<>();
                for(int i=minSlice; i<=maxSlice; i++){
                    auxImpossibles.add(new Integer(i));
                }
                impossibleSlicesForEdge.addAll(auxImpossibles);
            }else {
                Collection<Integer> impossibleSlicesForTransfer=t.maximize_free_room(minSlice, maxSlice, extraSlicesNeeded);
                impossibleSlicesForEdge.addAll(impossibleSlicesForTransfer);
            }

        }
        if (reschedulePair==null){
            return new ReschedulePair(impossibleSlicesForEdge,null);
        }else{
            reschedulePair.combineWithImpossibles(impossibleSlicesForEdge);
            return reschedulePair;
        }
    }*/


    public Collection<Integer> increaseRoom(Integer minSlice, Integer maxSlice, int extraSlicesNeeded){
        Set<Integer> impossibleSlicesForEdge=new HashSet<>();
        for (Transfer t : transfers){
            Collection<Integer> impossibleSlicesForTransfer=t.maximize_free_room(minSlice, maxSlice, extraSlicesNeeded);
            impossibleSlicesForEdge.addAll(impossibleSlicesForTransfer);
        }
        return impossibleSlicesForEdge;
    }

    public ReschedulePair makeRoom(Integer minSlice, Integer maxSlice){
        Set<Integer> impossibleSlicesForEdge=new HashSet<>();
        ReschedulePair slotsAfterReschedule=null;
        for (Transfer t : transfers){
            if(t.usesSlice(minSlice) && t.usesSlice(maxSlice)){
                slotsAfterReschedule=t.freeSlicesWithinTransfer(minSlice,maxSlice);
            }else {
                impossibleSlicesForEdge.addAll(t.getCurrentSlices());
            }
        }
        if(slotsAfterReschedule!=null){
            Set<Integer> impossibleSlicesForEdge2= new HashSet<>(impossibleSlicesForEdge);
            impossibleSlicesForEdge.addAll(slotsAfterReschedule.getImpossibleSlices1());
            impossibleSlicesForEdge2.addAll(slotsAfterReschedule.getImpossibleSlices2());
            return new ReschedulePair(impossibleSlicesForEdge, impossibleSlicesForEdge2);
        } else {
            return new ReschedulePair(impossibleSlicesForEdge, impossibleSlicesForEdge);
        }
    }
	
	public boolean isEqual(Edge edge) {
		boolean equal = false;
		
		if ((source.equals(edge.getSource()) && destination.equals(edge.getDestination())) || (source.equals(edge.getDestination()) && destination.equals(edge.getSource()))) {
			equal = true;
		}
		
		return equal;
	}
	
	public void addTransfer(Transfer t) {
		this.transfers.add(t);
	}
}
