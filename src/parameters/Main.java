package parameters;

import Reader.ReadWithScanner;
import dctransfers.PrecomputePathsWithTransmissions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Main {
	private static List<Transfer> T;
	private static Collection<Integer> S;
	private static int slicesNeeded;
	private static PrecomputePathsWithTransmissions getP;
	private static ArrayList<Path> P;
	private static Path p;
	private static int numSlices;
	private static Collection<Integer> allSlices = new ArrayList<Integer>();
	private static List<Edge> E;
	private static Graph G;
	private static Edge e;
	private static Transfer t;
	private static RequestedTransfer reqT;
	private static int nrR = 0;
	private static TransferCollections transferCollections;
	
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		ReadWithScanner parser = new ReadWithScanner();
		System.out.println("Parser started.");
		reqT = parser.getRequestedTransfer();
        String source = parser.getGraph().getNodeNameFromIdentifier(reqT.getNode_origin());
        String destination = parser.getGraph().getNodeNameFromIdentifier(reqT.getNode_destination());
        int data = reqT.getData_amount();
        int timeComp = reqT.getTime_completion();
        slicesNeeded = (int) Math.ceil((double)data/(double)timeComp);
        System.out.format("Requested Transfer: s: %s, d: %s, data: %d, time: %d, slicesNeeded: %d\n", source, destination, data, timeComp, slicesNeeded);
        numSlices = parser.getNumSlices();
        for (int j=0; j<=numSlices; j++){
        	allSlices.add(j);
        }
        transferCollections = parser.getTransferCollections();
        T = transferCollections.getTransfers();
        G = parser.getGraph();
        getP = new PrecomputePathsWithTransmissions(G);
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
						System.out.format("Transfer can be assigned with nr of reschedulings: %d\n", nrR);
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
										System.out.format("Transfer can be assigned with nr of reschedulings: %d\n", nrR);
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
