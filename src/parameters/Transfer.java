package parameters;

import java.util.*;

/**
 * Created by daniel on 15/11/14.
 */
public class Transfer {
    static int totalReschedules=0;
    int transferIdentifier;
    int node_origin;
    int node_destination;
    int time_completion;
    int data_amount;
    Set<Integer> tmpSlices;
    Set<Integer> tmpSlices2;
    Set<Rectangle> A0;
    Set<Rectangle> A1;
    Set<RectanglePair> feasiblePairs;
    Path path;
    Set<Integer> currentSlices;
    int currentMaxTime;
    public Transfer(int node_origin, int node_destination, int time_completion, int data_amount) {
        this.node_origin = node_origin;
        this.node_destination = node_destination;
        this.time_completion = time_completion;
        this.data_amount = data_amount;
    }

    public static int getTotalReschedules() {
        return totalReschedules;
    }

    public static void resetReschedules(){
        totalReschedules=0;
    }

    private static void addReschedule(){
        totalReschedules++;
    }

    public Set<Integer> getCurrentSlices() {
        return currentSlices;
    }

    public void setCurrentAllocation(int minCurrentSlices, int maxCurrentSlices, int currentMaxTime){
        setCurrentSlices(minCurrentSlices, maxCurrentSlices);
        this.currentMaxTime=currentMaxTime;
    }

    public boolean rectangleConformantAllocation(Rectangle rectangle){
        if(rectangle.getT_end()!=currentMaxTime) return false;
        boolean slicesEquals=true;
        if(rectangle.getFrequencySlices().size()==currentSlices.size()){
            for(Integer slice:currentSlices){
                if(!rectangle.getFrequencySlices().contains(slice)){
                    slicesEquals=false;
                    break;
                }
            }
        } else {
            slicesEquals=false;
        }
        return (slicesEquals);
    }

    public void validate_reschedule(){
        currentSlices=tmpSlices;
    }

    public void setCurrentSlices(int from, int to){
        if (currentSlices==null){
            currentSlices=new LinkedHashSet<>();
        }
        for(int i=from; i<=to; i++){
            currentSlices.add(new Integer(i));
        }
    }
    public void setTemporarySlices(int from, int to){
        if (tmpSlices==null){
            tmpSlices=new LinkedHashSet<>();
        } else {
            tmpSlices.clear();
        }
        for(int i=from; i<=to; i++){
            tmpSlices.add(new Integer(i));
        }
        addReschedule();
    }
    public void setTemporarySlices2(int from, int to){
        if (tmpSlices2==null){
            tmpSlices2=new LinkedHashSet<>();
        } else {
            tmpSlices2.clear();
        }
        for(int i=from; i<=to; i++){
            tmpSlices2.add(new Integer(i));
        }
    }

    public Collection<Integer> getOccupiedSlices(Collection<Integer> freeSlices){
        Collection<Integer> currentFreeSlices = new HashSet<>();
        for(Integer slice:freeSlices){
            if (currentSlices.contains(slice)){
                currentFreeSlices.add(slice);
            }
        }
        return currentFreeSlices;
    }

    public boolean usesSlice(int slice){
        return currentSlices.contains(new Integer(slice));
    }

    public Collection<Integer> maximize_free_room(int minPos, int maxPos, int extraSlicesNeeded){
        Set<Integer> impossibleSlices=new HashSet<>();
        int min_slices=(int) Math.ceil((double)data_amount/(double)time_completion);
        if (min_slices>=currentSlices.size()){
            tmpSlices=currentSlices;
            impossibleSlices.addAll(currentSlices);
            return impossibleSlices;
        }
        int spaceAbleToFree=currentSlices.size()-min_slices;
        int first_slice=(int)currentSlices.toArray()[0];
        int last_slice=(int)currentSlices.toArray()[currentSlices.size()-1];
        boolean toTheRight=true;
        if(last_slice<minPos) toTheRight=false;
        if(toTheRight){
            if(maxPos+extraSlicesNeeded>=first_slice) {
                setTemporarySlices(first_slice + spaceAbleToFree, last_slice);
                for (int slicei = first_slice + spaceAbleToFree; slicei <= last_slice; slicei++) {
                    impossibleSlices.add(new Integer(slicei));
                }
            }
        } else {
            if (minPos-extraSlicesNeeded<=last_slice) {
                setTemporarySlices(first_slice, first_slice + min_slices - 1);
                for (int slicei = first_slice; slicei <= first_slice + min_slices - 1; slicei++) {
                    impossibleSlices.add(new Integer(slicei));
                }
            }
        }
        return impossibleSlices;

    }

    public Collection<Integer> maximize_free_room(int pos, int extraSlicesNeeded){
        Set<Integer> impossibleSlices=new HashSet<>();
        int min_slices=(int) Math.ceil((double)data_amount/(double)time_completion);
        if (min_slices>=currentSlices.size()){
            tmpSlices=currentSlices;
            impossibleSlices.addAll(currentSlices);
            return impossibleSlices;
        }
        int spaceAbleToFree=currentSlices.size()-min_slices;
        int first_slice=(int)currentSlices.toArray()[0];
        int last_slice=(int)currentSlices.toArray()[currentSlices.size()-1];

        if(pos==first_slice-1){
            setTemporarySlices(first_slice + spaceAbleToFree, last_slice);
            for (int slicei = first_slice + spaceAbleToFree; slicei <= last_slice; slicei++) {
                impossibleSlices.add(new Integer(slicei));
            }
        } else if(pos==last_slice){
            setTemporarySlices(first_slice, first_slice + min_slices - 1);
            for (int slicei = first_slice; slicei <= first_slice + min_slices - 1; slicei++) {
                impossibleSlices.add(new Integer(slicei));
            }
        } else {
            impossibleSlices.addAll(currentSlices);
        }
        return impossibleSlices;

    }

    public ReschedulePair freeSlicesWithinTransfer(int minPos, int maxPos){
        int first_slice=(int)currentSlices.toArray()[0];
        int last_slice=(int)currentSlices.toArray()[currentSlices.size()-1];

        if(first_slice<=minPos && maxPos<=last_slice){
            int min_slices=(int) Math.ceil((double)data_amount/(double)time_completion);
            if (last_slice-first_slice>=min_slices){
                /*We can squeeze everything to the right of the requested slices*/
                setTemporarySlices(maxPos+1,last_slice);
            }else{
                /*Was not possible*/
                setTemporarySlices(first_slice,last_slice);
            }
            if (minPos-first_slice>=min_slices){
                /*We can squeeze everything to the left of the requested slices*/
                setTemporarySlices(first_slice,minPos-1);
            }else{
                /*Was not possible*/
                setTemporarySlices(first_slice,last_slice);
            }
        }else{
            setTemporarySlices(first_slice,last_slice);
            setTemporarySlices2(first_slice,last_slice);
        }
        return new ReschedulePair(tmpSlices,tmpSlices2);
    }

    public int maximize_free_room(int pos, boolean toTheRight){
        tmpSlices = new LinkedHashSet<>(currentSlices);
        int min_slices=(int) Math.ceil((double)data_amount/(double)time_completion);
        if (min_slices>=currentSlices.size()){
            return 0;
        }
        int first_slice=(int)currentSlices.toArray()[0];
        int last_slice=(int)currentSlices.toArray()[currentSlices.size()-1];
        if(toTheRight){
            if (pos>last_slice) return 0;
            int count=0;
            boolean first_loop=true;
            ;
            for(Integer slice:currentSlices){
                if(slice<pos) count++;
                else break;
            }
            if(count>=min_slices){
                setTemporarySlices(first_slice,pos-1);
                return currentSlices.size()-count;
            }
            else return 0;
        } else {
            if (pos<first_slice) return 0;
            int count=0;
            for(Integer slice:currentSlices){
                if(slice<=pos) count++;
                else break;
            }
            if(currentSlices.size()-count>=min_slices){
                setTemporarySlices(pos+1,last_slice);
                return count;
            }
            else return 0;
        }
    }

    public void print_tmpslices(){
        System.out.print("\t");
        for(Integer slice: tmpSlices){
            System.out.print(slice+" ");
        }
        System.out.println();
    }

    public void print_tmpslices2(){
        System.out.print("\t");
        for(Integer slice: tmpSlices2){
            System.out.print(slice+" ");
        }
        System.out.println();
    }

    public int getFirstSlice(){
        return (int)currentSlices.toArray()[0];
    }
    public int getLastSlice(){
        return (int)currentSlices.toArray()[currentSlices.size()-1];
    }

    public int getTime_completion() {
        return time_completion;
    }

    public Set<Rectangle> getA0() {
        return A0;
    }

    public Set<Rectangle> getA1() {
        return A1;
    }

    public Set<RectanglePair> getFeasiblePairs() {
        return feasiblePairs;
    }

    public void computeRectanglesSets (int maxFreqSlices) {
        A0 = getRectangles(0,time_completion,maxFreqSlices);
        A1 = new HashSet<Rectangle>(maxFreqSlices*time_completion);
        feasiblePairs = new HashSet<RectanglePair>();

        for(Rectangle a0: A0 ){
            A1.addAll(getRectangles(a0, maxFreqSlices));
        }
        for(Rectangle a0:A0){
            for(Rectangle a1:A1){
                if (computeFeasibility(a0, a1)){
                    feasiblePairs.add(new RectanglePair(a0, a1));
                }
            }
        }

    }

    HashSet<Rectangle> getRectangles(int tStart, int tEnd, int maxFreqSlices){
        HashSet<Rectangle>resultList =new HashSet<Rectangle>(maxFreqSlices*time_completion);
        for (int time=1; time<=time_completion; time++){
            for(int lowSlice=0;lowSlice<maxFreqSlices;lowSlice++){
                for(int maxSlice=lowSlice; maxSlice<maxFreqSlices; maxSlice++){
                    resultList.add(new Rectangle(0,time,lowSlice,maxSlice));
                }
            }
        }
        return resultList;
    }
    List<Rectangle> getRectangles(Rectangle a0, int maxFreqSlices){
        List<Rectangle>resultList =new ArrayList<Rectangle>(maxFreqSlices*time_completion);
        for (int time=a0.getT_end(); time<=time_completion; time++){
            for(int lowSlice=0;lowSlice<=a0.getMinFreq();lowSlice++){
                for(int maxSlice=a0.getMaxFreq(); maxSlice<maxFreqSlices; maxSlice++){
                    resultList.add(new Rectangle(a0.getT_end(), time, lowSlice, maxSlice));
                }
            }
        }
        return resultList;
    }
    public boolean computeFeasibility(Rectangle a0, Rectangle a1){
        if (a1.getArea()==0) {
            return ((a0.getT_end()==a1.getT_start()) && a0.getArea() >= data_amount);
        } else {
            return ((a0.getT_end() == a1.getT_start()) && (a1.getMinFreq() <= a0.getMinFreq()) && (a0.getMaxFreq() <= a1.getMaxFreq()) && (a0.getArea() + a1.getArea()) >= data_amount);
        }
    }

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

}
