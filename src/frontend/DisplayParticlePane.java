package frontend;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class DisplayParticlePane extends GridPane {
    public static final double WIDTH = 0.7 * Main.WIDTH;
    public static final double HEIGHT = 0.7 * Main.HEIGHT;

    private int numRows;
    private int numCols;

    private final Bridge bridge;
    private final ColorPane[][] grid;

    private boolean isRunning;
    private Timeline timeline;
    private Parameters params;

    public DisplayParticlePane(frontend.Parameters p) {
        this.numRows = p.getRows();
        this.numCols = p.getColumns();
        this.params = p;
        setMinSize(this.WIDTH, this.HEIGHT);
        setMaxSize(this.WIDTH, this.HEIGHT);
        this.setStyle("-fx-background-color: red");
        setPrefSize(this.WIDTH, this.HEIGHT);

        this.grid = new ColorPane[numRows][numCols];

        Random rand = new Random();

        //Individual Particle
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                ColorPane cp = new ColorPane(WIDTH / numRows, HEIGHT / numCols);
                if (rand.nextDouble() >= 0.5) {
                    cp.swapState();
                }

                this.add(cp, i, j);
                grid[i][j] = cp;
            }
        }

        String strState = stateToString();
        this.bridge = new Bridge(
                numRows,
                numCols,
                p.getTemperature(),
                p.getMaterial().getInteractionStrength(),
                strState);
    }

    public void startAnimation() {
        if (isRunning) {
            return;
        }

        bridge.startProcess();
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            List<Position> changes = bridge.getChangingPositions().pollFirst();
            if (changes == null) {
                stopAnimation();
                return;
            }

            for (Position pos : changes) {
                ColorPane pane = grid[pos.getX()][pos.getY()];
                pane.swapState();
            }
        }));

        timeline.setDelay(Duration.seconds(1));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        isRunning = true;
    }

    public void stopAnimation() {
        bridge.pause();
        if (timeline != null)
            timeline.stop();
        isRunning = false;
    }

    private String stateToString() {
        StringBuilder sb = new StringBuilder(numRows * numCols);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                sb.append(grid[i][j].getState() ? '1' : '0');
            }
        }

        return sb.toString();
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Parameters getParams() {
        return params;
    }
}
