package frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
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
            //Top
            StackPane p = new StackPane();
            p.setPrefHeight(100);
            root.setTop(new TitlePane(WIDTH, 100));

            // Right
            p = new StackPane();
            p.setPrefWidth(400);
            InputPane inputPane = new InputPane(WIDTH - DisplayParticlePane.WIDTH, DisplayParticlePane.HEIGHT);
            root.setRight(new VBox(new FeedbackPane(), inputPane));

            //Center
            displayPane = new DisplayParticlePane(new frontend.Parameters(50, 50 , 300, Materials.MEDIUM_FERRO));
            root.setCenter(displayPane);
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
