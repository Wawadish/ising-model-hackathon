import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Stack;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
            BorderPane root = new BorderPane();
            //Top
            StackPane p = new StackPane();
            p.setPrefHeight(100);
            root.setTop(p);

            //Bottom
            p = new StackPane();
            p.setPrefHeight(100);
            root.setBottom(p);

            //Right
            p = new StackPane();
            p.setPrefWidth(400);
            root.setRight(p);

            //Center
            root.setCenter(new StackPane());
            root.getCenter().setStyle("-fx-background-color: green");

            //Left
            root.setLeft(null);

            Scene scene = new Scene(root , 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}