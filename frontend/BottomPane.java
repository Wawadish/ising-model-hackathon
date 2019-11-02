package frontend;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class BottomPane extends Pane {

    private Label timer;
    private Button pause;

    public BottomPane(double width, double height, Bridge bridge) {
        this.setPrefSize(width, height);

        pause = new Button("PAUSE");

        pause.setOnAction((event) -> {
            // Button was clicked, do something...
            bridge.setPaused(true);
            pause.setText("PAUSED");
        });

        this.getChildren().addAll(pause);

    }






}
