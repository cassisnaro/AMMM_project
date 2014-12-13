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
        TransferCollections transferCollections = new TransferCollections(3);
        transferCollections.add_transfer(new Transfer(0,1,4,3));
        transferCollections.add_transfer(new Transfer(0,1,3,9));
        transferCollections.setRequestedTransfer(new RequestedTransfer(0,2,2,3));

        File fileParent= new File(System.getProperty("user.dir"));
        try {
            transferCollections.formatData(new FileOutputStream(new File(fileParent,"output.txt").getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
