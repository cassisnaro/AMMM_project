package Testing;

import parameters.Transfer;

/**
 * Created by daniel on 13/12/14.
 */
public class TestMaximizeFreeRoom {
    public static void main(String... aArgs){
        int test_time_completion=10;
        int test_data=20;
        int test_min_slice=2;
        int test_max_slice=6;
        Transfer transfer=new Transfer(1,2,test_time_completion,test_data);
        transfer.setCurrentSlices(test_min_slice,test_max_slice);
        System.out.println("Test maximize free room in transmission:");
        System.out.println("Min slices: "+(int)Math.ceil(test_data/test_time_completion));
        System.out.println("Currently assigned: "+test_min_slice+"-"+test_max_slice);
        int pos=1;
        test_1(transfer,pos);
        pos=2;
        test_1(transfer,pos);
        pos=3;
        test_1(transfer,pos);
        pos=4;
        test_1(transfer,pos);
        pos=6;
        test_1(transfer,pos);
        pos=7;
        test_1(transfer,pos);
    }
    private static void test_1(Transfer transfer, int pos){
        System.out.println("Space we can make free at the left of position "+pos+": "+transfer.maximize_free_room(pos,false));
        transfer.print_tmpslices();
        System.out.println("Space we can make free at the right of position "+pos+": "+transfer.maximize_free_room(pos,true));
        transfer.print_tmpslices();
    }
}
