package sample;

import Viewer.RectanglePairViewer;
import Viewer.TestAreasViewer;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import parameters.Rectangle;
import parameters.RectanglePair;
import sun.security.tools.keytool.Main;


public class WebViewSample extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("Web View");
        scene = new Scene(new Browser(),750,500, Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public Browser() {
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        //String url = Main.class.getResource("/sample/index.html").toExternalForm();
        webEngine.loadContent(genHtml2());
        //add the web view to the scene
        getChildren().add(browser);

    }
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private String genHtml2() {

        StringBuilder tHtml = new StringBuilder();

        tHtml.append("<DOCTYPE html>" + "\n");
        tHtml.append("<html lang='en-US'>" + "\n");
        tHtml.append("<head>" + "\n");
        tHtml.append("<meta charset=utf-8>" + "\n");
        tHtml.append("<title>Hello Java-Buddy!</title>" + "\n");
        tHtml.append("</head>" + "\n");
        tHtml.append("<body>" + "\n");
        tHtml.append(TestAreasViewer.genHTML());
        tHtml.append("</body>" + "\n");
        tHtml.append("</html>" + "\n");

        return tHtml.toString();
    }


        @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}
