package Writer;

import parameters.RequestedTransfer;
import parameters.Transfer;
import parameters.TransferCollections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by daniel on 5/12/14.
 */
public class WriterILP {
    public static void main(String[] args){
        TransferCollections transferCollections = new TransferCollections(5);
        Transfer newTransfer = new Transfer(0,5,6,6);
        newTransfer.setCurrentAllocation(0,1,2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(0,2,3,3);
        newTransfer.setCurrentAllocation(2,2,2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(3,5,4,3);
        newTransfer.setCurrentAllocation(3,3,2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(1,3,2,3);
        newTransfer.setCurrentAllocation(4,4,2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(4,5,7,4);
        newTransfer.setCurrentAllocation(0,1,2);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(2,4,5,2);
        newTransfer.setCurrentAllocation(2,3,2);
        transferCollections.add_transfer(newTransfer);
        transferCollections.setRequestedTransfer(new RequestedTransfer(0,4,3,5));

        File fileParent= new File(System.getProperty("user.dir"));
        try {
            transferCollections.formatData(new FileOutputStream(new File(fileParent,"output.txt").getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
