package parameters;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 18/11/14.
 */
public class TransferCollections {
    List<Transfer> transfers;
    RequestedTransfer requestedTransfer;
    int maxFreq;
    int maxTime;
    int maxA0;
    int maxA1;
    public TransferCollections(int maxFreq){
        transfers=new LinkedList<Transfer>();
        this.maxFreq=maxFreq;
        maxTime=-1;
        maxA0=-1;
        maxA1=-1;
    }

    public void setRequestedTransfer(RequestedTransfer requestedTransfer) {
        this.requestedTransfer = requestedTransfer;
        requestedTransfer.computeRectanglesSet(maxFreq);
    }

    public void add_transfer(Transfer transfer){
        transfer.computeRectanglesSets(maxFreq);
        transfers.add(transfer);
        if (transfer.getTime_completion()>maxTime){
            maxTime=transfer.getTime_completion();
        }
        if (transfer.getA0().size()>maxA0){
            maxA0=transfer.getA0().size();
        }
        if (transfer.getA1().size()>maxA1){
            maxA1=transfer.getA1().size();
        }
    }
    public void formatData(FileOutputStream file){
        LinkedHashSet<Rectangle> A0s, A1s;
        A0s=new LinkedHashSet<Rectangle>();
        A1s=new LinkedHashSet<Rectangle>();

        PrintStream printStream = new PrintStream(file);

        printStream.print("nA="+requestedTransfer.getA().size()+";\n");
        printStream.print("nA0="+maxA0+";\n");
        printStream.print("nA1="+maxA1+";\n");
        printStream.print("nS="+maxFreq+";\n");
        printStream.print("nT="+maxTime+";\n");
        printStream.print("nR="+transfers.size()+";\n");

        printStream.print("mA=[");
        A0s.clear();
        A0s.addAll(requestedTransfer.getA());
        for(Rectangle rectangle:A0s){
            System.out.println("freq size: " + rectangle.getFrequencySlices().size() + " t0=" + rectangle.getT_start() + " t1=" + rectangle.getT_end());
            printStream.print("[");
            for(int freq=0; freq<maxFreq; freq++){
                if (freq!=0){
                    printStream.print(", ");
                }
                if (rectangle.frequencyBelongsToRectangle(freq)){
                    printStream.print("1");
                } else {
                    printStream.print("0");
                }
            }
            printStream.print("]\n");
        }
        printStream.print("];\n");


        printStream.print("mA0=[");
        for(Transfer transfer: transfers){
            A0s.clear();
            A0s.addAll(transfer.getA0());
            printStream.print("[");
            for(Rectangle rectangle:A0s){
                System.out.println("freq size: " + rectangle.getFrequencySlices().size() + " t0=" + rectangle.getT_start() + " t1=" + rectangle.getT_end());
                printStream.print("[");
                for(int freq=0; freq<maxFreq; freq++){
                    if (freq!=0){
                        printStream.print(", ");
                    }
                    if (rectangle.frequencyBelongsToRectangle(freq)){
                        printStream.print("1");
                    } else {
                        printStream.print("0");
                    }
                }
                printStream.print("]\n");
            }
            for(int extraRectangle=A0s.size();extraRectangle<maxA0;extraRectangle++){
                printStream.print("[");
                for(int freq=0; freq<maxFreq; freq++){
                    if (freq!=0){
                        printStream.print(", ");
                    }
                    printStream.print("0");
                }
                printStream.print("]\n");
            }
            printStream.print("]\n");
            System.out.println(" ------------- ");
        }
        printStream.print("];\n");

        printStream.print("mA1=[");
        for(Transfer transfer: transfers){
            A1s.clear();
            A1s.addAll(transfer.getA1());
            printStream.print("[");
            for(Rectangle rectangle:A1s){
                System.out.println("freq size: "+rectangle.getFrequencySlices().size()+" t0="+rectangle.getT_start()+" t1="+rectangle.getT_end());
                printStream.print("[");
                for(int freq=0; freq<maxFreq; freq++){
                    if (freq!=0){
                        printStream.print(", ");
                    }
                    if (rectangle.frequencyBelongsToRectangle(freq)){
                        printStream.print("1");
                    } else {
                        printStream.print("0");
                    }
                }
                printStream.print("]\n");
            }
            for(int extraRectangle=A1s.size();extraRectangle<maxA1;extraRectangle++){
                printStream.print("[");
                for(int freq=0; freq<maxFreq; freq++){
                    if (freq!=0){
                        printStream.print(", ");
                    }
                    printStream.print("0");
                }
                printStream.print("]\n");
            }
            System.out.println("---------------------------");
            printStream.print("]\n");
        }
        printStream.print("];\n");

        printStream.print("beta_ra0a1=[");
        for(Transfer transfer: transfers){
            A0s.clear();
            A0s.addAll(transfer.getA0());
            A1s.clear();
            A1s.addAll(transfer.getA1());
            printStream.print("[");
            for(Rectangle rectangleA0:A0s){
                printStream.print("[");
                boolean first=true;
                for(Rectangle rectangleA1:A1s){
                    if(first) {
                        first=false;
                    }else {
                        printStream.print(", ");
                    }

                    if (transfer.computeFeasibility(rectangleA0, rectangleA1)){
                        printStream.print(1);
                    } else {
                        printStream.print(0);
                    }
                }
                for(int extraRectangleA1=A1s.size(); extraRectangleA1<maxA1; extraRectangleA1++){
                    printStream.print(", 0");
                }
                printStream.print("]\n");
            }
            for(int extraRectangleA0=A0s.size(); extraRectangleA0<maxA0; extraRectangleA0++){
                boolean first=true;
                printStream.print("[");
                for(int extraRectangleA1=0; extraRectangleA1<maxA1; extraRectangleA1++){
                    if(first) {
                        first=false;
                    }else {
                        printStream.print(", ");
                    }
                    printStream.print("0");
                }
                printStream.print("]");
            }
            printStream.print("]\n");
        }
        printStream.print("];\n");

        printStream.print("gamma_at=[");
        A0s.clear();
        A0s.addAll(requestedTransfer.getA());

        for(Rectangle rectangle:A0s) {
            boolean first=true;
            printStream.print("[");
            for(int t=0; t<maxTime; t++){
                if(first) {
                    first=false;
                }else {
                    printStream.print(", ");
                }
                if (rectangle.getT_start()<=t && t<rectangle.getT_end()){
                    printStream.print("1");
                } else {
                    printStream.print("0");
                }
            }
            printStream.print("]");

        }
        printStream.print("];\n");

        printStream.print("gamma_a0t=[");
        for(Transfer transfer: transfers){
            A0s.clear();
            A0s.addAll(transfer.getA0());
            printStream.print("[");

            for(Rectangle rectangle:A0s) {
                boolean first=true;
                printStream.print("[");
                for(int t=0; t<maxTime; t++){
                    if(first) {
                        first=false;
                    }else {
                        printStream.print(", ");
                    }
                    if (rectangle.getT_start()<=t && t<rectangle.getT_end()){
                        printStream.print("1");
                    } else {
                        printStream.print("0");
                    }
                }
                printStream.print("]");
            }
            for(int extraRectangleA0=A0s.size(); extraRectangleA0<maxA0; extraRectangleA0++){
                boolean first=true;
                printStream.print("[");
                for(int t=0; t<maxTime; t++){
                    if(first) {
                        first=false;
                    }else {
                        printStream.print(", ");
                    }
                    printStream.print("0");

                }
                printStream.print("]");
            }
            printStream.print("]\n");
        }
        printStream.print("];\n");

        printStream.print("gamma_a1t=[");
        for(Transfer transfer: transfers){
            A1s.clear();
            A1s.addAll(transfer.getA1());
            printStream.print("[");

            for(Rectangle rectangle:A1s) {
                boolean first=true;
                printStream.print("[");
                for(int t=0; t<maxTime; t++){
                    if(first) {
                        first=false;
                    }else {
                        printStream.print(", ");
                    }
                    if (rectangle.getT_start()<=t && t<rectangle.getT_end()){
                        printStream.print("1");
                    } else {
                        printStream.print("0");
                    }
                }
                printStream.print("]");
            }
            for(int extraRectangleA1=A1s.size(); extraRectangleA1<maxA1; extraRectangleA1++){
                boolean first=true;
                printStream.print("[");
                for(int t=0; t<maxTime; t++){
                    if(first) {
                        first=false;
                    }else {
                        printStream.print(", ");
                    }
                    printStream.print("0");

                }
                printStream.print("]");
            }
            printStream.print("]\n");
        }
        printStream.print("];\n");

        printStream.print("omega_ar=[");
        for(Transfer transfer: transfers) {
            A0s.clear();
            A0s.addAll(transfer.getA0());
            printStream.print("[");
            boolean first = true;
            boolean found = false;
            for (Rectangle rectangle : A0s) {
                if (first) {
                    first = false;
                } else {
                    printStream.print(", ");
                }
                if (transfer.rectangleConformantAllocation(rectangle)) {
                    printStream.print("1");
                    found = true;
                } else {
                    printStream.print("0");
                }

            }
            for (int extraRectangleA0 = A0s.size(); extraRectangleA0 < maxA0; extraRectangleA0++) {
                printStream.print(", 0");
            }
            if (found){
                System.out.println("OK");
            }else{
                System.out.println("Problem");
            }
            printStream.print("]");
        }
        printStream.print("];");
        printStream.close();

    }
}
