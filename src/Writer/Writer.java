package Writer;

import parameters.Transfer;
import parameters.TransferCollections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by daniel on 5/12/14.
 */
public class Writer {
    public static void main(String[] args){
        TransferCollections transferCollections = new TransferCollections(4);
        transferCollections.add_transfer(new Transfer(0,1,4,3));

        File fileParent= new File(System.getProperty("user.dir"));
        try {
            transferCollections.formatData(new FileOutputStream(new File(fileParent,"output.txt").getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
