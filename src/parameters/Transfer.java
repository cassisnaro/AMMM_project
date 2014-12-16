package parameters;

import java.util.*;

/**
 * Created by daniel on 15/11/14.
 */
public class Transfer {
    int transferIdentifier;
    int node_origin;
    int node_destination;
    int time_completion;
    int data_amount;
    Set<Integer> tmpSlices;
    Set<Rectangle> A0;
    Set<Rectangle> A1;
    Set<RectanglePair> feasiblePairs;

    Set<Integer> currentSlices;
    int currentMaxTime;

    public Transfer(int node_origin, int node_destination, int time_completion, int data_amount) {
        this.node_origin = node_origin;
        this.node_destination = node_destination;
        this.time_completion = time_completion;
        this.data_amount = data_amount;
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
    }

    public Collection<Integer> getFreeSlices(Collection<Integer> freeSlices){
        Collection<Integer> currentFreeSlices = new HashSet<>();
        for(Integer slice:freeSlices){
            if (currentSlices.contains(slice)){
                currentFreeSlices.add(slice);
            }
        }
        return currentFreeSlices;
    }

    public int maximize_free_room(int pos, boolean toTheRight){
        tmpSlices = new LinkedHashSet<>(currentSlices);
        int min_slices=(int) Math.ceil(data_amount/time_completion);
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

}
