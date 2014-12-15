package parameters;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by daniel on 8/12/14.
 */
public class RequestedTransfer {
    int node_origin;
    int node_destination;
    int time_completion;
    int data_amount;
    Set<Rectangle> A;
    public RequestedTransfer(int node_origin, int node_destination, int time_completion, int data_amount) {
        this.node_origin = node_origin;
        this.node_destination = node_destination;
        this.time_completion = time_completion;
        this.data_amount = data_amount;
    }

    public int getNode_origin() {
        return node_origin;
    }
    
    public int getNode_destination() {
    	return node_destination;
    }
    
    public int getTime_completion() {
        return time_completion;
    }
    
    public int getData_amount() {
    	return data_amount;
    }

    public Set<Rectangle> getA() {
        return A;
    }


    public void computeRectanglesSet (int maxFreqSlices) {
        A = getRectangles(0,time_completion,maxFreqSlices);
    }

    HashSet<Rectangle> getRectangles(int tStart, int tEnd, int maxFreqSlices){
        HashSet<Rectangle>resultList =new HashSet<Rectangle>(maxFreqSlices*time_completion);
        for (int time=1; time<=time_completion; time++){
            for(int lowSlice=0;lowSlice<maxFreqSlices;lowSlice++){
                for(int maxSlice=lowSlice; maxSlice<maxFreqSlices; maxSlice++){
                    Rectangle rectangle=new Rectangle(0,time,lowSlice,maxSlice);
                    if (rectangle.getArea()>data_amount) {
                        resultList.add(rectangle);
                    }
                }
            }
        }
        return resultList;
    }
}
