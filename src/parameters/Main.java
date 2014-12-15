package parameters;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
//import Reader.ReadWithScanner;
import dctransfers.PrecomputePathsWithTransmissions;

public class Main {
	//private String source;
	//private String destination;
	private static List<Transfer> T;
	private static Collection<Integer> S;
	private static int slicesNeeded;
	private static PrecomputePathsWithTransmissions getP;
	private static ArrayList<Path> P;
	private static Path p;
	private static Collection<Integer> allSlices;
	private static List<Edge> E;
	private static Edge e;
	private static Transfer t;
	private static RequestedTransfer reqT;
	private static int nrR = 0;
	
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		TransferCollections transferCollections = new TransferCollections(5);
        Transfer newTransfer = new Transfer(0,6,4,6);
        newTransfer.setCurrentAllocation(0,2,2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(1,2,3,3);
        newTransfer.setCurrentAllocation(3,4,2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(1,2,3,3);
        newTransfer.setCurrentAllocation(3,4,2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(3,6,2,3);
        newTransfer.setCurrentAllocation(3, 4, 2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(3,6,2,2);
        newTransfer.setCurrentAllocation(2,3,1);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(1,6,5,3);
        newTransfer.setCurrentAllocation(0,1,2);
        transferCollections.add_transfer(newTransfer);
        transferCollections.setRequestedTransfer(new RequestedTransfer(0,3,2,3));
        reqT = transferCollections.getRequestedTransfer();
        int tempSource = reqT.getNode_origin();
        int tempDestination = reqT.getNode_destination();
        String source = "" + tempSource;
        String destination = "" + tempDestination; 
        int data = reqT.getData_amount();
        int timeComp = reqT.getTime_completion();
        slicesNeeded = (int) Math.ceil(data/timeComp);
        T = transferCollections.getTransfers();
        getP.precumputePathsFromTransmission(source, destination);
		P = getP.getPaths();
		Iterator<Path> p_it = P.iterator();
		while(p_it.hasNext()){
			p = p_it.next();
			E = p.getEdges();
			Iterator<Edge> e_it = E.iterator();
			e = e_it.next();
			while (e_it.hasNext()) {
				if (p.hasEdge(e)){
					S = e.getFreeSlices(allSlices);
					Iterator<Integer> s_it = S.iterator();
					s_it.hasNext();
					Integer temp_s = s_it.next();
					Integer temp_slicesNeeded = slicesNeeded;
					while(temp_slicesNeeded>0){
						s_it.hasNext();
						Integer s1 = s_it.next();
						if(s1 == temp_s+1){
							temp_slicesNeeded = temp_slicesNeeded-1;
							temp_s = s1;
						} else {
							temp_slicesNeeded = slicesNeeded;
							temp_s = s1;
						}
					}
					if(temp_slicesNeeded == 0){
						System.out.format("Transfer can be assigned with nr of reschedulings: %d", nrR);
					} else {
						while(temp_slicesNeeded>0){
							s_it.hasNext();
							Integer s1 = s_it.next();
							if(s1 == temp_s+1){
								temp_slicesNeeded = temp_slicesNeeded-1;
								temp_s = s1;
							} else {
								int pos = temp_s.intValue();
								for (int i = 0; i < T.size(); i++) {
									t = T.get(i);
									int slicesFree = t.maximize_free_room(pos, true);
									if(slicesFree == temp_slicesNeeded){
										t.validate_reschedule();
										nrR = nrR + 1;
										System.out.format("Transfer can be assigned with nr of reschedulings: %d", nrR);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
