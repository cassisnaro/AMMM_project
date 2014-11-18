package parameters;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by daniel on 18/11/14.
 */
public class TransferCollections {
    List<Transfer> transfers;
    int maxFreq;

    public TransferCollections(int maxFreq){
        transfers=new LinkedList<Transfer>();
        this.maxFreq=maxFreq;
    }
    public void add_transfer(Transfer transfer){
        transfer.computeRectanglesSets(maxFreq);
        transfers.add(transfer);
    }
    public void formatData(FileOutputStream file){
        TreeSet<Rectangle> A0s, A1s;
        A0s=new TreeSet<Rectangle>();
        A1s=new TreeSet<Rectangle>();
        for(Transfer transfer: transfers){
            A0s.addAll(transfer.getA0());
            A1s.addAll(transfer.getA0());
        }
        PrintStream printStream = new PrintStream(file);
        printStream.print("[");
        for(int freq=0; freq<maxFreq; freq++){
            printStream.print("[");
            for(Rectangle rectangle:A0s){
                if (rectangle.frequencyBelongsToRectangle(freq)){
                    printStream.print("1, ");
                } else {
                    printStream.print("0, ");
                }
            }
            printStream.print("]\n");
        }
        printStream.print("]\n");
        printStream.print("[");
        for(int freq=0; freq<maxFreq; freq++){
            printStream.print("[");
            for(Rectangle rectangle:A1s){
                if (rectangle.frequencyBelongsToRectangle(freq)){
                    printStream.print("1, ");
                } else {
                    printStream.print("0, ");
                }
            }
            printStream.print("]\n");
        }
        printStream.print("]\n");

        boolean[][] compatibilities = new boolean[A0s.size()][A1s.size()];
        for(int i=0; i<A0s.size(); i++){
            for(int j=0; j<A1s.size(); j++){
                compatibilities[i][j]=false;
            }
        }
        for(Transfer transfer: transfers){
            Set<RectanglePair> rectanglePairs=transfer.getFeasiblePairs();
            for(RectanglePair rectanglePair:rectanglePairs){
                compatibilities[rectanglePair.getA0().getId()][rectanglePair.getA1().getId()]=true;
            }
        }
        printStream.print("[");
        for(int a0RectangleId=0; a0RectangleId<A0s.size(); a0RectangleId++){
            printStream.print("[");
            for(int a1RectangleId=0; a1RectangleId<A1s.size(); a1RectangleId++){
                if (compatibilities[a0RectangleId][a1RectangleId]){
                    printStream.print("1, ");
                } else {
                    printStream.print("0, ");
                }
            }
            printStream.print("]");
        }
        printStream.print("]");


        printStream.close();

    }
}
