package Viewer;

import Testing.TestAreas;
import parameters.RectanglePair;

import java.util.Set;

/**
 * Created by daniel on 16/11/14.
 */
public class TestAreasViewer {
    public static String genHTML() {
        Set<RectanglePair> resultTest=TestAreas.test();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RectanglePairViewer.initRectanglePairViewing());
        for(RectanglePair rectanglePair: resultTest){
            stringBuilder.append(RectanglePairViewer.genHTML(rectanglePair));
        }

        return stringBuilder.toString();
    }
}
