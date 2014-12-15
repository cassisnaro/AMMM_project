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
    	//Daniel's example
//    	TransferCollections transferCollections = new TransferCollections(5);
//    	Transfer newTransfer = new Transfer(0,6,4,6);
//    	newTransfer.setCurrentAllocation(0,2,2);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(1,2,3,3);
//    	newTransfer.setCurrentAllocation(3,4,2);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(1,2,3,3);
//    	newTransfer.setCurrentAllocation(3,4,2);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(3,6,2,3);
//    	newTransfer.setCurrentAllocation(3, 4, 2);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(3,6,2,2);
//    	newTransfer.setCurrentAllocation(2,3,1);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(1,6,5,3);
//    	newTransfer.setCurrentAllocation(0,1,2);
//    	transferCollections.add_transfer(newTransfer);
//    	transferCollections.setRequestedTransfer(new RequestedTransfer(0,3,2,3));

    	//Victoria's example
//    	TransferCollections transferCollections = new TransferCollections(6);
//    	Transfer newTransfer = new Transfer(3,4,2,4);
//    	newTransfer.setCurrentAllocation(0,2,3);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(0,4,4,6);
//    	newTransfer.setCurrentAllocation(1,4,4);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(3,5,3,3);
//    	newTransfer.setCurrentAllocation(1,3,3);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(1,5,2,3);
//    	newTransfer.setCurrentAllocation(2, 4, 3);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(1,4,2,2);
//    	newTransfer.setCurrentAllocation(0,3,2);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(2,4,5,4);
//    	newTransfer.setCurrentAllocation(1,3,4);
//    	transferCollections.add_transfer(newTransfer);
//    	newTransfer = new Transfer(0,5,3,2);
//    	newTransfer.setCurrentAllocation(2,4,4);
//    	transferCollections.add_transfer(newTransfer);
//    	transferCollections.setRequestedTransfer(new RequestedTransfer(0,5,3,4));
    	
        TransferCollections transferCollections = new TransferCollections(5);
        Transfer newTransfer = new Transfer(0,5,6,6);
        newTransfer.setCurrentAllocation(0,1,3);        
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(0,2,3,3);
        newTransfer.setCurrentAllocation(2,2,3);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(3,5,4,3);
        newTransfer.setCurrentAllocation(3,3,3);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(1,3,3,3);
        newTransfer.setCurrentAllocation(4,4,3);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(4,5,7,4);
        newTransfer.setCurrentAllocation(0,1,3);
        transferCollections.add_transfer(newTransfer);
        newTransfer = new Transfer(2,4,5,2);
        newTransfer.setCurrentAllocation(2,2,2);
        transferCollections.add_transfer(newTransfer);
        transferCollections.setRequestedTransfer(new RequestedTransfer(0,4,3,5));

        File fileParent= new File(System.getProperty("user.dir"));
        try {
            transferCollections.formatData(new FileOutputStream(new File(fileParent,"output_T_gaby_v1.txt").getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
