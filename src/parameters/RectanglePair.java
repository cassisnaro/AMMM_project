package parameters;

import org.w3c.dom.css.Rect;

/**
 * Created by daniel on 15/11/14.
 */
public class RectanglePair {
    private static int idCount=0;

    int id;
    Rectangle a0;
    Rectangle a1;

    public Rectangle getA0() {
        return a0;
    }

    public int getId() {
        return id;
    }

    public Rectangle getA1() {
        return a1;
    }



    public RectanglePair(Rectangle a0, Rectangle a1) {
        this.a0 = a0;
        this.a1 = a1;
        id = idCount;
        idCount++;
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof RectanglePair) {
            RectanglePair obj=(RectanglePair)arg0;
            return (a0 == obj.getA0() && a1 == obj.getA1());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return a0.hashCode() ^ a1.hashCode();
    }
}
