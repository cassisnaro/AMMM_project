package parameters;

import java.util.Collection;

/**
 * Created by daniel on 17/12/14.
 */
public class ReschedulePair {
    Collection<Integer> impossibleSlices1;
    Collection<Integer> impossibleSlices2;

    public ReschedulePair(Collection<Integer> impossibleSlices1, Collection<Integer> impossibleSlices2) {
        this.impossibleSlices1 = impossibleSlices1;
        this.impossibleSlices2 = impossibleSlices2;
    }

    public Collection<Integer> getImpossibleSlices1() {
        return impossibleSlices1;
    }

    public Collection<Integer> getImpossibleSlices2() {
        return impossibleSlices2;
    }

    public void combineWithImpossibles(Collection<Integer> impossibles){
        impossibleSlices1.addAll(impossibles);
        impossibleSlices2.addAll(impossibles);
    }


}
