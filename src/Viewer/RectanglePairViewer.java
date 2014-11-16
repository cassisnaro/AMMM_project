package Viewer;

import parameters.RectanglePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by daniel on 15/11/14.
 */
public class RectanglePairViewer {
    public static String initRectanglePairViewing(){
        try {

            URI path=RectanglePairViewer.class.getResource("RectanglePairViewerJavascript").toURI();
            return readFile(path, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String genHTML(RectanglePair rectanglePair){

        // BuildMyString.com generated code. Please enjoy your StringBuilder responsibly.
        System.out.println("trying to plot: "+rectanglePair.getA1().getT_start()+","+rectanglePair.getA1().getMinFreq()+","+(rectanglePair.getA1().getT_end()-rectanglePair.getA1().getT_start())+","+rectanglePair.getA1().getFrequencySlices().size());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t\t<canvas id=\"c"+rectanglePair.getId()+"\" width=\"101px\" height=\"101px\" style=\"display:block;  margin-bottom: 50px;\"></canvas><script>plotAreas(\"c"+rectanglePair.getId()+"\","+rectanglePair.getA0().getT_start()+","+rectanglePair.getA0().getMinFreq()+","+(rectanglePair.getA0().getT_end()-rectanglePair.getA0().getT_start())+","+rectanglePair.getA0().getFrequencySlices().size()+","+rectanglePair.getA1().getT_start()+","+rectanglePair.getA1().getMinFreq()+","+(rectanglePair.getA1().getT_end()-rectanglePair.getA1().getT_start())+","+rectanglePair.getA1().getFrequencySlices().size()+");</script>\n");
        stringBuilder.append("<p>"+rectanglePair.getA0().getT_start()+","+rectanglePair.getA0().getMinFreq()+","+(rectanglePair.getA0().getT_end()-rectanglePair.getA0().getT_start())+","+rectanglePair.getA0().getFrequencySlices().size()+","+rectanglePair.getA1().getT_start()+","+rectanglePair.getA1().getMinFreq()+","+(rectanglePair.getA1().getT_end()-rectanglePair.getA1().getT_start())+","+rectanglePair.getA1().getFrequencySlices().size()+", area of A1="+rectanglePair.getA1().getArea()+"</p>");
        return stringBuilder.toString();
    }

    private static String readFile(URI path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }



}
