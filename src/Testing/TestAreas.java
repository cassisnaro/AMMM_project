package Testing;

import parameters.RectanglePair;
import parameters.Transfer;

import java.util.Set;

/**
 * Created by daniel on 15/11/14.
 */
public class TestAreas {
    public static Set<RectanglePair> test(){
        Transfer  transfer= new Transfer(0,1,3,3);
        transfer.computeRectanglesSets(3);
        return transfer.getFeasiblePairs();
    }
}
