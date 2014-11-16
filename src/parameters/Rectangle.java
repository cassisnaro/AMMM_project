package parameters;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by daniel on 15/11/14.
 */
public class Rectangle {
    Set<Integer> frequencySlices;

    public Rectangle(int t_start, int t_end, int low_slice, int max_slice) {
        this.t_start = t_start;
        this.t_end = t_end;
        frequencySlices = new HashSet<Integer>(max_slice-low_slice);
        for (int slice=low_slice; slice<= max_slice; slice++){
            frequencySlices.add(slice);
        }
    }

    public Set<Integer> getFrequencySlices() {
        return frequencySlices;
    }

    public void setFrequencySlices(Set<Integer> frequencySlices) {
        this.frequencySlices = frequencySlices;
    }

    public int getT_start() {
        return t_start;
    }

    public void setT_start(int t_start) {
        this.t_start = t_start;
    }

    public int getT_end() {
        return t_end;
    }

    public void setT_end(int t_end) {
        this.t_end = t_end;
    }

    int t_start;
    int t_end;


    public int getArea(){
        return frequencySlices.size()*(t_end-t_start);
    }

    public int getMinFreq(){
        return Collections.min(frequencySlices);
    }
    public int getMaxFreq(){
        return Collections.max(frequencySlices);
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof Rectangle) {
            Rectangle obj=(Rectangle)arg0;
            if (obj.getArea()==0 && getArea()==0) return true;
            return (getT_start()==obj.getT_start() && getT_end()==obj.getT_end() && getMinFreq()==obj.getMinFreq() && getMaxFreq()==getMaxFreq());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {

        int hash=t_start;
        hash = hash<<6 ^ t_end;
        if (getArea()==0) {
            return hash << 12;
        } else {
            hash = hash << 6 ^ getMinFreq();
            hash = hash << 6 ^ getMaxFreq();
            return hash;
        }
    }

}
