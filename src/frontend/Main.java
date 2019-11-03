package frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static double WIDTH = 1280;
    public static double HEIGHT = 720;
    public static void main(String[] args) {
        launch(args);
    }

    private static BorderPane root;
    public static DisplayParticlePane displayPane;

    @Override
    public void start(Stage primaryStage) {
        try{
            root = new BorderPane();
            root.setStyle("-fx-background-color: white");

            //Top
            StackPane p = new StackPane();
            p.setPrefHeight(100);
            root.setTop(new TitlePane(WIDTH, 100));

            //Center
            displayPane = new DisplayParticlePane(new frontend.Parameters(100, 100 , 300, Materials.MEDIUM_FERRO));
            root.setCenter(displayPane);

            // Right
            p = new StackPane();
            p.setPrefWidth(400);
            InputPane inputPane = new InputPane(WIDTH - DisplayParticlePane.WIDTH, DisplayParticlePane.HEIGHT);
            VBox box = new VBox(new FeedbackPane(), inputPane);
            box.setPadding(new Insets(0, 20, 0, 20));
            root.setRight(box);

            //Left
            root.setLeft(null);

            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();

            primaryStage.setOnCloseRequest(e -> {
                displayPane.stopAnimation();
            });

            scene.setOnKeyReleased(e -> {
                if (e.getCode() == KeyCode.C) {
                    DisplayParticlePane.firstColor = !DisplayParticlePane.firstColor;
                    e.consume();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void updateDisplay(DisplayParticlePane p){
        Main.displayPane = new DisplayParticlePane(p.getParams());
        root.setCenter(Main.displayPane);

    }
}
