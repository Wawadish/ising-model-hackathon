package frontend;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Stack;

public class Main extends Application {
    public static double WIDTH = 1280;
    public static double HEIGHT = 720;
    public static void main(String[] args) {
        launch(args);
    }
    private static BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        try{
            root = new BorderPane();
            //Top
            StackPane p = new StackPane();
            p.setPrefHeight(100);
            root.setTop(new TitlePane(WIDTH, (HEIGHT-DisplayParticlePane.HEIGHT)/2));

            //Bottom
            p = new StackPane();
            p.setPrefHeight(100);
            root.setBottom(p);

            //Right
            p = new StackPane();
            p.setPrefWidth(400);
            root.setRight(new InputPane(WIDTH - DisplayParticlePane.WIDTH, DisplayParticlePane.HEIGHT));

            //Center

            //Left
            root.setLeft(null);

            Scene scene = new Scene(root , 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void updateDisplay(frontend.Parameters p){
        root.setCenter(new DisplayParticlePane(p));
    }
}
