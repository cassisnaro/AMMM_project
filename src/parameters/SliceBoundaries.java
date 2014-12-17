package parameters;

/**
 * Created by daniel on 16/12/14.
 */
public class SliceBoundaries {
    int lowSlice;
    int highSlice;

    public SliceBoundaries(int lowSlice, int highSlice) {
        this.lowSlice = lowSlice;
        this.highSlice = highSlice;
    }

    public int getLowSlice() {
        return lowSlice;
    }

    public int getHighSlice() {
        return highSlice;
    }
}
