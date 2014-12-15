package parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

public class Main {
	//private String source;
	//private String destination;
	private List<Transfer> T;
	private Collection<Integer> S;
	private Integer slicesNeeded;
	private ArrayList<Path> P;
	private Path p;
	private Collection<Integer> allSlices; 
	private Graph G;
	private Set<Edge> E;
	private Edge e;
	private Transfer t;
	private int nrR = 0;
	
	
	public void main(){
		P = G.getPaths();
		Iterator<Path> p_it = P.iterator();
		E = G.getEdges();
		while(p_it.hasNext()){
			p = p_it.next();
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
