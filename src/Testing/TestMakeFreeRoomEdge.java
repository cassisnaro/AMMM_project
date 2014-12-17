package Testing;

import parameters.Edge;
import parameters.Transfer;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by daniel on 17/12/14.
 */
public class TestMakeFreeRoomEdge {
    public static void main(String... aArgs) {
        Edge testEdge = new Edge("s", "d");
        Transfer transmission = new Transfer(1, 2, 10, 20);
        transmission.setCurrentSlices(0, 20);
        Set<Integer> allSlices=new LinkedHashSet<>();
        for (int j = 0; j < 20; j++) {
            allSlices.add(j);
        }
        testEdge.addTransfer(transmission);


        testEdge.makeRoom(10, 15);
        System.out.println("Test one: ");
        System.out.println("Option one: ");
        transmission.print_tmpslices();
        System.out.println("Option two: ");
        transmission.print_tmpslices2();


    }
}