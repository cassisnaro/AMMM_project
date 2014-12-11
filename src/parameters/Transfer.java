package parameters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by daniel on 15/11/14.
 */
public class Transfer {
    int transferIdentifier;
    int node_origin;
    int node_destination;
    int time_completion;
    int data_amount;
    Set<Rectangle> A0;
    Set<Rectangle> A1;
    Set<RectanglePair> feasiblePairs;
    public Transfer(int node_origin, int node_destination, int time_completion, int data_amount) {
        this.node_origin = node_origin;
        this.node_destination = node_destination;
        this.time_completion = time_completion;
        this.data_amount = data_amount;
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
            if (time==0){
                System.out.println("ups");
            }
            for(int lowSlice=0;lowSlice<=a0.getMinFreq();lowSlice++){
                for(int maxSlice=a0.getMaxFreq(); maxSlice<maxFreqSlices; maxSlice++){
                    resultList.add(new Rectangle(a0.getT_end(), time, lowSlice, maxSlice));
                }
            }
        }
        return resultList;
    }
    public boolean computeFeasibility(Rectangle a0, Rectangle a1){
        System.out.println("a0: from t0="+a0.getT_start()+" to t1="+a0.getT_end()+" with "+a0.getArea()+" area");
        System.out.println("a1: from t1="+a1.getT_start()+" to tf="+a1.getT_end()+" with "+a1.getArea()+" area numSlices="+a1.getFrequencySlices().size());
        if (a0.getArea()==4){
            System.out.println("Here");
        }
        if (a1.getArea()==0) {
            return ((a0.getT_end()==a1.getT_start()) && a0.getArea() >= data_amount);
        } else {
            return ((a0.getT_end() == a1.getT_start()) && (a1.getMinFreq() <= a0.getMinFreq()) && (a0.getMaxFreq() <= a1.getMaxFreq()) && (a0.getArea() + a1.getArea()) >= data_amount);
        }
    }

}
