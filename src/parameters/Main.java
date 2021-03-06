package parameters;

import Reader.ReadWithScanner;
import dctransfers.PrecomputePathsWithTransmissions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    private static List<Transfer> T;
    private static int slicesNeeded;
    private static PrecomputePathsWithTransmissions getP;
    private static ArrayList<Path> P;
    private static int numSlices;
    private static Collection<Integer> allSlices = new ArrayList<Integer>();
    private static Set<Edge> E;
    private static Graph G;
    private static Edge e;
    private static Transfer t;
    private static RequestedTransfer reqT;
    private static int nrR = 0;
    private static TransferCollections transferCollections;
    private static Path reroutingPath=null;
    private static Collection<Integer> availableSlicesAfterReschedule=null;


    public static void main(String[] args) throws IOException, URISyntaxException {

        long startTime = System.currentTimeMillis();
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
        transferCollections = parser.getTransferCollections();
        T = transferCollections.getTransfers();
        G = parser.getGraph();
        getP = new PrecomputePathsWithTransmissions(G);
        getP.precumputePathsFromTransmission(source, destination);
        P = getP.getPaths();

        System.out.println("Elapsed at the beginning: "+(System.currentTimeMillis()-startTime));


        long startTimeMain = System.currentTimeMillis();
        for (Path p : P) {
            E = G.getEdges();
            Collection<Integer> S = new LinkedHashSet<>(allSlices);
            for (Edge e : E) {
                if (p.hasEdge(e)) {
                    S = e.getFreeSlices(S);
                    System.out.println(e.getSource() + " " + e.getDestination());
                }
            }
            int maxContinous = maxContinuity(S);
            if (maxContinous>=slicesNeeded){
                System.out.println("can be route over this path without rescheduling");
                reroutingPath=p;
                break;
            }
        }
        long endTrivial = System.currentTimeMillis();
        System.out.println("Elapsed time for trivial: "+(endTrivial-startTimeMain));
        if(reroutingPath==null){
            System.out.println("trivial case failed");
            for (Path p : P) {
                E = G.getEdges();
                Collection<Integer>S = new LinkedHashSet<>(allSlices);
                for (Edge e : E) {
                    if (p.hasEdge(e)) {
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
                            boolean reschedulePossible= halfHardReschedulePossible(p, firstSliceCurrentFreeSpace, previousSlice, slicesNeeded);
                            if (reschedulePossible){
                                System.out.println("one path found");
                                break;
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
                    boolean reschedulePossible = halfHardReschedulePossible(p, firstSliceCurrentFreeSpace, previousSlice, slicesNeeded);
                    if (reschedulePossible) {
                        System.out.println("one path found, half hard");
                        reroutingPath = p;
                        System.out.println("It needs: "+Transfer.getTotalReschedules()+" reschedules");
                        break;
                    }else{
                        Transfer.resetReschedules();
                    }
                }
            }
        }
        long endHalfHard=System.currentTimeMillis();
        System.out.println("time elapsed for Half Hard:"+(endHalfHard-endTrivial));
        if(reroutingPath==null){
            System.out.println("Half hard failed");
            for (Path p : P) {
                boolean reschedulePossible =hardReschedulePossible(p, slicesNeeded);
                if (reschedulePossible) {
                    System.out.println("one path found, half hard");
                    reroutingPath = p;
                    System.out.println("It needs: "+Transfer.getTotalReschedules()+" reschedules");
                    break;
                }else{
                    Transfer.resetReschedules();
                }
            }
        }
        long endHard=System.currentTimeMillis();
        System.out.println("time elapsed for Hard:"+(endHard-endHalfHard));
        if (Transfer.getTotalReschedules()>0){
            localSearch(slicesNeeded);
        }
        long endLocalSearch=System.currentTimeMillis();
        System.out.println("time elapsed for localSearch: "+(endLocalSearch-endHard));
        System.out.println("total time: "+(endHard-startTime));
        System.out.println("Final reschedules needed: "+Transfer.getTotalReschedules());
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