package frontend;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class BottomPane extends Pane {

    private Label timer;
    private boolean stopped;
    private Button pause;

    public BottomPane(double width, double height) {
        this.setPrefSize(width, height);
        this.stopped = true;
        pause = new Button("START");

        pause.setOnAction((event) -> {
            if(stopped) {
                pause.setText("STOP");
                Main.displayPane.startAnimation();
                stopped = !stopped;
            } else {
                pause.setText("START");
                Main.displayPane.stopAnimation();
                stopped = !stopped;
            }

        });

        this.getChildren().addAll(pause);

    }






}
