package parameters;

import Reader.ReadWithScanner;
import dctransfers.PrecomputePathsWithTransmissions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import utils.PathComparator;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class GRASPMain {
    private static int slicesNeeded;
    private static PrecomputePathsWithTransmissions getP;
    private static ArrayList<Path> P;
    private static ArrayList<Path> visitedPathsInSection=null;
    private static int numSlices;
    private static Collection<Integer> allSlices = new ArrayList<Integer>();
    private static Set<Edge> E;
    private static Graph G;
    private static RequestedTransfer reqT;
    private static Path reroutingPath=null;
    private static Collection<Integer> availableSlicesAfterReschedule=null;
    public static int costFunctionValue = -1;
    public static int numIterations = 10;
    public static int min_reschedule;

    public static void main(String[] args) throws IOException, URISyntaxException {
    	long startTimeMain = System.currentTimeMillis();

        grasp();
    	
    	long endTimeMain=System.currentTimeMillis();
        System.out.println("-----------total time enlapsed: "+(endTimeMain-startTimeMain));
        System.out.println("-----------Final reschedules needed: "+min_reschedule);
    }
    
    public static void grasp() throws IOException, URISyntaxException {
    	ReadWithScanner parser = new ReadWithScanner();
        System.out.println("Parser started.");
        reqT = parser.getRequestedTransfer();
        String source = parser.getGraph().getNodeNameFromIdentifier(reqT.getNode_origin());
        String destination = parser.getGraph().getNodeNameFromIdentifier(reqT.getNode_destination());
        int data = reqT.getData_amount();
        int timeComp = reqT.getTime_completion();
        slicesNeeded = (int) Math.ceil((double) data / (double) timeComp);
        System.out.format("Requested Transfer: s: %s, d: %s, data: %d, time: %d, slicesNeeded: %d\n", source, destination, data, timeComp, slicesNeeded);
        numSlices = parser.getNumSlices();
        for (int j = 0; j < numSlices; j++) {
            allSlices.add(j);
        }
        G = parser.getGraph();
        getP = new PrecomputePathsWithTransmissions(G);
        getP.precumputePathsFromTransmission(source, destination);
        P = getP.getPaths();
        
        List<Path> copyOfP = new ArrayList<Path>(P);
        visitedPathsInSection = new ArrayList<>();
        List<Path> rclPaths = null;
		Path auxPath = null;
        min_reschedule = Integer.MAX_VALUE;


		long startTimeMain = System.currentTimeMillis();


        while (copyOfP.size() != 0 && reroutingPath==null) {
        	rclPaths = getRCLPaths(copyOfP);
			auxPath = getRandomPath(rclPaths);
			
			E = G.getEdges();
            Collection<Integer> S = new LinkedHashSet<>(allSlices);
            for (Edge e : E) {
                if (auxPath.hasEdge(e)) {
                    S = e.getFreeSlices(S);
                    System.out.println(e.getSource() + " " + e.getDestination());
                }
            }
            int maxContinous = maxContinuity(S);
            if (maxContinous>=slicesNeeded){
                System.out.println("can be route over this path without rescheduling");
                reroutingPath=auxPath;
                min_reschedule = 0;
                break;
            }
			
			copyOfP.remove(auxPath);
        }
        
        long endTrivial = System.currentTimeMillis();
        System.out.println("Elapsed time for trivial: "+(endTrivial-startTimeMain));
        
        if(reroutingPath==null){
            System.out.println("trivial case failed");
            
            copyOfP = new ArrayList<Path>(P);
            rclPaths = null;
            auxPath = null;
            costFunctionValue = -1;

            visitedPathsInSection.clear();
            rclPaths = getRCLPaths(copyOfP);
            while (rclPaths.size() != 0) {
                System.out.println("-----------Half hard");

    			auxPath = getRandomPath(rclPaths);
                visitedPathsInSection.add(auxPath);
                rclPaths.remove(auxPath);
    			
    			E = G.getEdges();
                Collection<Integer>S = new LinkedHashSet<>(allSlices);
                for (Edge e : E) {
                    if (auxPath.hasEdge(e)) {
                        S = e.getFreeSlices(S);
                        System.out.println(e.getSource() + " " + e.getDestination());
                    }
                }
                int firstSliceCurrentFreeSpace=-1;
                int previousSlice=-1;
                for(Integer slice:S){
                    if(firstSliceCurrentFreeSpace==-1){
                        firstSliceCurrentFreeSpace=slice;
                        previousSlice=slice;
                    }else{
                        if(previousSlice+1==slice){
                            previousSlice=slice;
                        }else{
                            boolean reschedulePossible= halfHardReschedulePossible(auxPath, firstSliceCurrentFreeSpace, previousSlice, slicesNeeded);
                            if (reschedulePossible){
                                System.out.println("one path found");
                                localSearch(slicesNeeded);
                                if (Transfer.getTotalReschedules()<min_reschedule) min_reschedule = Transfer.getTotalReschedules();
                                Transfer.resetReschedules();
                            }else{
                                Transfer.resetReschedules();
                            }
                            firstSliceCurrentFreeSpace=slice;
                            previousSlice=slice;
                        }
                    }
                }
                /*Test with the last free range of the list*/
                if(previousSlice!=-1) {
                    boolean reschedulePossible = halfHardReschedulePossible(auxPath, firstSliceCurrentFreeSpace, previousSlice, slicesNeeded);
                    if (reschedulePossible) {
                        System.out.println("one path found, half hard");
                        reroutingPath = auxPath;
                        localSearch(slicesNeeded);
                        if (Transfer.getTotalReschedules()<min_reschedule) min_reschedule = Transfer.getTotalReschedules();
                        Transfer.resetReschedules();
                    }else{
                        Transfer.resetReschedules();
                    }
                }
                
                copyOfP.remove(auxPath);
            }
        }
        
        long endHalfHard=System.currentTimeMillis();
        System.out.println("time elapsed for Half Hard:"+(endHalfHard-endTrivial));
        
        if(reroutingPath==null){;
            
            copyOfP = new ArrayList<Path>(P);
            rclPaths = null;
            auxPath = null;
            costFunctionValue = -1;

            visitedPathsInSection.clear();
            rclPaths = getRCLPaths(copyOfP);

            while (rclPaths.size() != 0) {
                System.out.println("----------- hard");
    			auxPath = getRandomPath(rclPaths);
                rclPaths.remove(auxPath);
    			
    			boolean reschedulePossible =hardReschedulePossible(auxPath, slicesNeeded);
                if (reschedulePossible) {
                    System.out.println("one path found, from hard to half hard");
                    reroutingPath = auxPath;
                    System.out.println("It needs: "+Transfer.getTotalReschedules()+" reschedules");

                    localSearch(slicesNeeded);
                    if (Transfer.getTotalReschedules()<min_reschedule) min_reschedule = Transfer.getTotalReschedules();
                    Transfer.resetReschedules();
                }else{
                    Transfer.resetReschedules();
                }
                copyOfP.remove(auxPath);
            }
        }


//        System.out.println("time elapsed for localSearch: "+(endLocalSearch-endHard));
//        System.out.println("total time: "+(endHard-startTimeMain));
        System.out.println("Final reschedules needed: "+min_reschedule);
    }
    
    static Function<Path, Boolean> numberOfHops = new Function<Path, Boolean>() {
	    @Override
	    public Boolean apply(Path p) {
	        return p.getPath().size() <= costFunctionValue;
	    }
	};
	
	public static List<Path> getRCLPaths(List<Path> paths) {
		double alpha = 0.3;
		
		List<Path> sortedPaths = new ArrayList<Path>(paths);
		sortedPaths.sort(new PathComparator());

        sortedPaths.removeAll(visitedPathsInSection);
		
//		System.out.println("################## SORTED PATHS");
//		for (Path p : sortedPaths) {
//			System.out.println(p.toString());
//		}
		
		int qmin = sortedPaths.get(0).getPath().size();
		int qmax = sortedPaths.get(sortedPaths.size() - 1).getPath().size();	
		
		costFunctionValue = (int) Math.ceil((double)qmin + alpha * ((double)qmax - (double)qmin));
		
		Predicate<Path> rclPathsPredicate = Predicates.compose(Predicates.equalTo(true), numberOfHops);
		
		Iterable<Path> itRCLPaths = Iterables.filter(sortedPaths, rclPathsPredicate);
		
		List<Path> rclPaths = Lists.newArrayList(itRCLPaths);
		
		return rclPaths;
	}
	
	public static Path getRandomPath(List<Path> paths) {
		Random random = new Random();
		int randomPos;
		
		if (paths.size() != 1) {
			randomPos = random.nextInt(paths.size() - 1);
		}
		else {
			randomPos = 0;
		}
		
		return paths.get(randomPos);
	}

	static private boolean halfHardReschedulePossible(Path path, int firstSlice, int lastSlice, int minBitRate){
        int extraSlicesNeeded=minBitRate-(lastSlice-firstSlice);
        E = G.getEdges();
        Set<Integer> impossibleSlicesForPath=new HashSet<>();
        for (Edge e : E) {
            if (path.hasEdge(e)) {
                System.out.println("Looking at edge: "+e.getSource()+"-"+e.getDestination());
                Collection<Integer> impossibleSlicesForEdge=e.increaseRoom(firstSlice, lastSlice, extraSlicesNeeded);
                System.out.print("impossible slices for path: ");
                impossibleSlicesForPath.addAll(impossibleSlicesForEdge);
                for(Integer slice:impossibleSlicesForPath){
                    System.out.print(slice+" ");
                }
                System.out.println();
            }
        }
        Collection<Integer> availableSlices=allSlices;
        availableSlices.removeAll(impossibleSlicesForPath);
        for(Integer slice:availableSlices){
            System.out.print(slice+" ");
        }
        System.out.println();
        int maxContinuity=maxContinuity(availableSlices);
        boolean found= (maxContinuity>=minBitRate);
        if (found){
            availableSlicesAfterReschedule=availableSlices;
        }
        return found;
    }

    static private void localSearch( int min_slices){
        int possibilities=availableSlicesAfterReschedule.size()-min_slices;
        ArrayList<Integer> arrayAvailableSlices=new ArrayList<>(availableSlicesAfterReschedule);
        int maxUndo=-1;
        for(int i=0; i<=possibilities;i++){
            boolean continous=true;
            for(int j=0; j<min_slices-1; j++){
                if (arrayAvailableSlices.get(i+j)+1!=arrayAvailableSlices.get(i+j+1)){
                    continous=false;
                    break;
                }
            }
            if (continous) {
                ArrayList<Integer> tryWith = new ArrayList<>(availableSlicesAfterReschedule);
                tryWith.removeAll(arrayAvailableSlices.subList(i, i + min_slices));
                int currentUndo = howManyUndoes(tryWith);
                if (currentUndo > maxUndo) maxUndo = currentUndo;
            }
        }
        Transfer.undoReschedules(maxUndo);

    }

    static private int howManyUndoes(Collection<Integer> availableSlices){
        HashSet<Transfer> analyzedTransfers= new HashSet<>();
        int count=0;
        for(Edge e:E){
            for(Transfer t:e.getTransfers()){
                if (!analyzedTransfers.contains(t)){
                    analyzedTransfers.add(t);
                    if (t.canUndoReschedule(availableSlices)) count++;
                }
            }
        }
        return count;
    }

    static private boolean hardReschedulePossible(Path path, int minBitRate){
        E = G.getEdges();
//        Set<Integer> impossibleSlicesForPath=new HashSet<>();
        List<Collection<Integer>> availableSlicesSegments= new LinkedList<>();
        List<Collection<Integer>> availableTransmissionLimits= new LinkedList<>();


        for (Edge e : E) {
            if (path.hasEdge(e)) {
                Collection<Integer> S = new LinkedHashSet<>(allSlices);
                S = e.getFreeSlices(S);
                availableSlicesSegments.add(S);
                availableTransmissionLimits.add(e.getTransmissionLimits());
            }
        }


        for(Collection<Integer> slicesSegments:availableSlicesSegments) {
            int firstSliceCurrentFreeSpace=-1;
            int previousSlice=-1;
            System.out.println("trying with "+slicesSegments.size()+" slices");
            for (Integer slice : slicesSegments) {
                if (firstSliceCurrentFreeSpace == -1) {
                    firstSliceCurrentFreeSpace = slice;
                    previousSlice = slice;
                } else {
                    if (previousSlice + 1 == slice) {
                        previousSlice = slice;
                    } else {
                        int extraSlicesNeeded=minBitRate-(previousSlice-firstSliceCurrentFreeSpace);
                        Collection<Integer> impossiblesSlices= workOnSegment(path, firstSliceCurrentFreeSpace, previousSlice, extraSlicesNeeded);
                        Collection<Integer> availableSlices=new LinkedHashSet<>(allSlices);
                        availableSlices.removeAll(impossiblesSlices);
                        System.out.println("Slices available:");
                        for(Integer sliceAvailable:availableSlices){
                            System.out.print(sliceAvailable+" ");
                        }
                        System.out.println();
                        int maxContinuity=maxContinuity(availableSlices);
                        if (maxContinuity>=minBitRate){
                            availableSlicesAfterReschedule=availableSlices;
                            return true;
                        }else{
                            Transfer.resetReschedules();
                        }
                        firstSliceCurrentFreeSpace = slice;
                        previousSlice = slice;
                    }
                }
            }
            if (previousSlice!=-1) {
                int extraSlicesNeeded = minBitRate - (previousSlice - firstSliceCurrentFreeSpace);
                Collection<Integer> impossiblesSlices = workOnSegment(path, firstSliceCurrentFreeSpace, previousSlice, extraSlicesNeeded);
                Collection<Integer> availableSlices = new LinkedHashSet<>(allSlices);
                availableSlices.removeAll(impossiblesSlices);
                System.out.println("Slices available:");
                for (Integer sliceAvailable : availableSlices) {
                    System.out.print(sliceAvailable + " ");
                }
                System.out.println();
                int maxContinuity = maxContinuity(availableSlices);
                if (maxContinuity >= minBitRate) {
                    System.out.println("Found");
                    availableSlicesAfterReschedule=availableSlices;
                    return true;
                }else{
                    Transfer.resetReschedules();
                }
            }
        }
        System.out.println("Failed with slices segments");

        System.out.println("Trying with intersections");
        for(Collection<Integer> transmissionsIntersections:availableTransmissionLimits) {
            for(Integer intersection:transmissionsIntersections){
                System.out.println("\tintersection:"+intersection.toString());
                for (Edge e : E) {
                    if (path.hasEdge(e)) {
                        Collection<Integer> availableSlices = new LinkedHashSet<>(allSlices);
                        availableSlices.removeAll(e.workOnTransferIntersection(intersection-1, slicesNeeded));
                        int maxContinuity = maxContinuity(availableSlices);
                        if (maxContinuity >= slicesNeeded) {
                            int firstSliceCurrentFreeSpace = -1;
                            int previousSlice = -1;
                            for (Integer slice : availableSlices) {
                                if (firstSliceCurrentFreeSpace == -1) {
                                    firstSliceCurrentFreeSpace = slice;
                                    previousSlice = slice;
                                } else {
                                    if (previousSlice + 1 == slice) {
                                        previousSlice = slice;
                                    } else {
                                        Set<Integer> impossibleSlices = workOnSegment(path, firstSliceCurrentFreeSpace, previousSlice, slicesNeeded - (previousSlice - firstSliceCurrentFreeSpace), e);
                                        Collection<Integer> availableSlices2 = new LinkedHashSet<>(availableSlices);
                                        availableSlices2.removeAll(impossibleSlices);
                                        maxContinuity = maxContinuity(availableSlices2);
                                        if (maxContinuity >= slicesNeeded) {
                                            System.out.println("one path found");
                                            availableSlicesAfterReschedule=availableSlices2;
                                            return true;
                                        }else{
                                            System.out.println("reset");
                                            Transfer.resetReschedules();
                                        }
                                        firstSliceCurrentFreeSpace = slice;
                                        previousSlice = slice;
                                    }
                                }
                            }
                        /*Test with the last free range of the list*/
                            if (previousSlice != -1) {
                                Set<Integer> impossibleSlices = workOnSegment(path, firstSliceCurrentFreeSpace, previousSlice, slicesNeeded - (previousSlice - firstSliceCurrentFreeSpace), e);
                                Collection<Integer> availableSlices2 = new LinkedHashSet<>(availableSlices);
                                availableSlices2.removeAll(impossibleSlices);
                                maxContinuity = maxContinuity(availableSlices2);
                                if (maxContinuity >= slicesNeeded) {
                                    System.out.println("one path found");
                                    availableSlicesAfterReschedule=availableSlices2;
                                    return true;
                                }else{
                                    System.out.println("reset");
                                    Transfer.resetReschedules();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Failed with intersections");
        return false;
    }

    static private Set<Integer> workOnSegment(Path path, int minSlice, int maxSlice, int extraSlicesNeeded){
        return workOnSegment( path,  minSlice,  maxSlice,  extraSlicesNeeded, null);
    }

    static private Set<Integer> workOnSegment(Path path, int minSlice, int maxSlice, int extraSlicesNeeded, Edge edgeToAvoid){
        Set<Integer> impossibleSlices=new LinkedHashSet<>();
        for (Edge e : E) {
            if (edgeToAvoid==null) {
                if (path.hasEdge(e)) {
                    Set<Integer> result = e.workOnSegment(minSlice, maxSlice, extraSlicesNeeded);
                    impossibleSlices.addAll(result);
                }
            } else {
                if(!e.isEqual(edgeToAvoid)){
                    if (path.hasEdge(e)) {
                        Set<Integer> result = e.workOnSegment(minSlice, maxSlice, extraSlicesNeeded);
                        impossibleSlices.addAll(result);
                    }
                }
            }
        }
        return impossibleSlices;
    }


    static private int maxContinuity(Collection<Integer> S) {
    /*Testing for continuous portions of slices*/
        int previousSlice = -2;
        int maxContinous = -2;
        int currentContinous = 1;
        for (Integer slice : S) {
            if (slice == previousSlice + 1) {
                    /*Continous case*/
                currentContinous += 1;
                if (currentContinous > maxContinous) maxContinous = currentContinous;
            } else {
                if (currentContinous > maxContinous) maxContinous = currentContinous;
                currentContinous = 1;
            }
            previousSlice = slice;
        }
        return maxContinous;
    }
}
