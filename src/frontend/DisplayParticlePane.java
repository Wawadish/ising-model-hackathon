package frontend;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.List;

public class DisplayParticlePane extends GridPane {
    public static final double WIDTH = 0.7 * Main.WIDTH;
    public static final double HEIGHT = 0.7 * Main.HEIGHT;

    private int NUM_ROWS = 75;
    private int NUM_COLUMNS = 60;

    private final Bridge bridge;
    private final ColorPane[][] grid;

    public DisplayParticlePane() {
        this.setStyle("-fx-background-color: red");
        setPrefSize(this.WIDTH, this.HEIGHT);

        this.grid = new ColorPane[NUM_ROWS][NUM_COLUMNS];

        //Individual Particle
        boolean alternate = false;
        for (int i = 0; i < NUM_ROWS; i++) {
            alternate = !alternate;
            for (int j = 0; j < NUM_COLUMNS; j++) {
                ColorPane cp = new ColorPane(WIDTH / NUM_ROWS, HEIGHT / NUM_COLUMNS);
                if (alternate) {
                    cp.swapState();
                }
                alternate = !alternate;
                this.add(cp, i, j);
                grid[i][j] = cp;
            }
        }

        String strState = stateToString();
        this.bridge = new Bridge(NUM_ROWS, NUM_COLUMNS, 300, 300, strState);

        bridge.startProcess();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            List<Position> changes = bridge.getChangingPositions().removeFirst();
        }));
    }

    private String stateToString() {
        StringBuilder sb = new StringBuilder(NUM_ROWS * NUM_COLUMNS);
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                sb.append(grid[i][j].getState() ? '1' : '0');
            }
        }

        return sb.toString();
    }

}
