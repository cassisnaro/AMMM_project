package parameters;

import Reader.ReadWithScanner;
import dctransfers.PrecomputePathsWithTransmissions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class GRASPMain {
    private static int slicesNeeded;
    private static PrecomputePathsWithTransmissions getP;
    private static ArrayList<Path> P;
    private static int numSlices;
    private static Collection<Integer> allSlices = new ArrayList<Integer>();
    private static Set<Edge> E;
    private static Graph G;
    private static RequestedTransfer reqT;


    public static void main(String[] args) throws IOException, URISyntaxException {
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

        Path reroutingPath=null;
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
                        break;
                    }
                }
            }
        }
        if(reroutingPath==null){
            System.out.println("Half hard failed");
            for (Path p : P) {
                boolean reschedulePossible =hardReschedulePossible(p, slicesNeeded);
                if (reschedulePossible) {
                    System.out.println("one path found, half hard");
                    reroutingPath = p;
                    break;
                }
            }
        }
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
        return (maxContinuity>=minBitRate);
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
                        //TODO transform into something
                        if (maxContinuity>=minBitRate){
                            System.out.println("Found");
                            return true;
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
                //TODO transform into something
                if (maxContinuity >= minBitRate) {
                    System.out.println("Found");
                    return true;
                }
            }
        }
        System.out.println("Failed with slices segments");

        System.out.println("Trying with intersections");
        for(Collection<Integer> transmissionsIntersections:availableTransmissionLimits) {
            for(Integer intersection:transmissionsIntersections){
                System.out.println("\tintersection:"+intersection.toString());
                for (Edge e : E) {
                    Collection<Integer> availableSlices = new LinkedHashSet<>(allSlices);
                    if (path.hasEdge(e)) {
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
                                            return true;
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
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Failed with segments");
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
