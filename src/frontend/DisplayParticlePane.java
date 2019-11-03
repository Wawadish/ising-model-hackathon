package frontend;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class DisplayParticlePane extends StackPane {
    public static final double WIDTH = 0.7 * Main.WIDTH;
    public static final double HEIGHT = 0.7 * Main.HEIGHT;

    public static boolean firstColor = true;

    private int numRows;
    private int numCols;

    private Bridge bridge;
    public boolean[][] grid;

    private boolean isRunning;
    private Timeline timeline;
    private Parameters params;

    private GraphicsContext gc;
    private double dragRadius = this.WIDTH / 20;

    private Color colorOn, colorOff;

    private double cellWidth, cellHeight;

    public DisplayParticlePane(frontend.Parameters p) {
        this.setPrefSize(WIDTH, HEIGHT);
        this.colorOn = Color.WHITE;
        this.colorOff = Color.BLACK;

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        this.getChildren().add(canvas);

        this.numRows = p.getRows();
        this.numCols = p.getColumns();
        this.params = p;
        this.setStyle("-fx-background-color: black");

        this.grid = new boolean[numRows][numCols];

        Random rand = new Random();

        //Individual Particle
        gc.setFill(Color.WHITE);
        cellWidth = WIDTH / numRows;
        cellHeight = HEIGHT / numCols;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (rand.nextDouble() >= 0.5) {
                    grid[i][j] = true;
                    fillCell(i, j);
                } else {
                    clearCell(i, j);
                }
            }
        }

        this.setOnMouseDragged(e -> {
            if (isRunning)
                return;

            double x = e.getX();
            double y = e.getY();
            int gridX = (int)Math.floor(x/cellWidth);
            int gridY = (int)Math.floor(y/cellHeight);
            double gridRadius = dragRadius/cellWidth;
            swapGrid(gridX, gridY);
            for (int i = 0; i < gridRadius; i++){
                swapGrid(gridX+i, gridY+i);
                swapGrid(gridX+i, gridY);
                swapGrid(gridX, gridY);
                swapGrid(gridX, gridY + i);

                swapGrid(gridX - i, gridY - i);
                swapGrid(gridX - i, gridY);
                swapGrid(gridX - i, gridY - i);
                swapGrid(gridX, gridY - i);
            }

        });
        this.bridge = new Bridge(
                numRows,
                numCols,
                p.getTemperature(),
                p.getMaterial().getInteractionStrength(),
                grid);
    }

    public void updateThings(double temp, Materials material) {
        if (isRunning) {
            throw new IllegalStateException();
        }

        params.material = material;
        params.temperature = temp;
        this.bridge = new Bridge(
                numRows,
                numCols,
                params.getTemperature(),
                params.getMaterial().getInteractionStrength(),
                grid);
    }

    private void swapGrid(int i, int j){
        if (i < 0 || j < 0 || i >= numRows || j >= numCols)
            return;

        if (!firstColor && this.grid[i][j]) {
            this.grid[i][j] = false;
            clearCell(i, j);
        }

        if (firstColor && !this.grid[i][j]) {
            this.grid[i][j] = true;
            fillCell(i, j);
        }
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
                int x = pos.getX();
                int y = pos.getY();
                if (grid[x][y]) {
                    clearCell(x, y);
                } else {
                    fillCell(x, y);
                }

                grid[x][y] = !grid[x][y];
            }
        }));

        timeline.setDelay(Duration.seconds(1));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        isRunning = true;
    }

    private void clearCell(int i, int j) {
        gc.setFill(colorOff);
        gc.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
    }

    private void fillCell(int i, int j) {
        gc.setFill(colorOn);
        gc.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
    }

    public void stopAnimation() {
        bridge.pause();
        if (timeline != null)
            timeline.stop();
        isRunning = false;
    }

    public void changeColorOn(Color colorOn) {
        this.colorOn = colorOn;
        redrawCanvas();
    }

    public void changeColorOff(Color colorOff) {
        this.colorOff = colorOff;
        redrawCanvas();
    }

    private void redrawCanvas() {
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                if (grid[i][j]) {
                    fillCell(i, j);
                } else {
                    clearCell(i, j);
                }
            }
        }
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
