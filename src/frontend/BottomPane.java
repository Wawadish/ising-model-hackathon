package frontend;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class BottomPane extends Pane {

    private Label timer;
    private boolean stopped;
    private Button pause;
    private Button reset;

    public BottomPane(double width, double height) {
        this.setPrefSize(width, height);
        this.stopped = true;
        pause = new Button("START");

        reset = new Button("RESET");
        reset.setLayoutX(pause.getLayoutX() + width/10);

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

        reset.setOnAction((event) -> {
            Main.displayPane.stopAnimation();
            pause.setText("START");
            Main.updateDisplay(new DisplayParticlePane(Main.displayPane.getParams()));
        });



        this.getChildren().addAll(pause, reset);

    }






}
