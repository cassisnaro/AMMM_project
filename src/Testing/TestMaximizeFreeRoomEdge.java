package Testing;

import parameters.Edge;
import parameters.Transfer;

/**
 * Created by daniel on 16/12/14.
 */
public class TestMaximizeFreeRoomEdge {
    public static void main(String... aArgs) {
        Edge testEdge = new Edge("s", "d");
        Transfer lowSliceTransmission = new Transfer(1, 2, 10, 20);
        lowSliceTransmission.setCurrentSlices(0, 10);
        testEdge.addTransfer(lowSliceTransmission);
        Transfer highSliceTransmission = new Transfer(1, 2, 11, 20);
        highSliceTransmission.setCurrentSlices(12, 22);
        testEdge.addTransfer(highSliceTransmission);

        testEdge.increaseRoom(11,12,1);
        System.out.println("lowSliceTransmission: ");
        lowSliceTransmission.print_tmpslices();
        System.out.println("highSliceTransmission: ");
        highSliceTransmission.print_tmpslices();


        testEdge = new Edge("s","d");
        lowSliceTransmission = new Transfer(1, 2, 10, 20);
        lowSliceTransmission.setCurrentSlices(0, 9);
        testEdge.addTransfer(lowSliceTransmission);
        highSliceTransmission = new Transfer(1, 2, 10, 20);
        highSliceTransmission.setCurrentSlices(10, 19);
        testEdge.addTransfer(highSliceTransmission);
        testEdge.workOnTransferIntersection(9, 2);
        System.out.println("lowSliceTransmission: ");
        lowSliceTransmission.print_tmpslices();
        System.out.println("highSliceTransmission: ");
        highSliceTransmission.print_tmpslices();
    }
}
