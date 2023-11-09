package edu.upvictoria.poo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    public static void main( String[] args )
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var vbox = new VBox();
        var scene = new Scene(vbox,800,600);
        stage.setScene(scene);
        stage.show();
    }
}
