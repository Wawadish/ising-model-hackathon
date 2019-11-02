package frontend;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class BottomPane extends Pane {

    private Label timer;
    private Button pause;

    public BottomPane(double width, double height, Pane pane) {
        this.setPrefSize(width, height);

        pause = new Button("START");

        pause.setOnAction((event) -> {
            // Button was clicked, do something...
            //pane.stopAnimation();
            pause.setText("STOP");
        });

        this.getChildren().addAll(pause);

    }






}
