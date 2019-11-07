package frontend;

import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DisplayParticlePane extends StackPane {
    public static final double WIDTH = 0.7 * Main.WIDTH;
    public static final double HEIGHT = Main.HEIGHT - 100;

    public static boolean firstColor = true;

    private int numRows;
    private int numCols;

    private Bridge bridge;
    public boolean[][] grid;

    public boolean isRunning;
    private Timeline timeline;
    private Parameters params;

    private GraphicsContext gc;
    private double dragRadius = 50;

    public Color colorOn, colorOff;
    private double cellWidth, cellHeight;
    private int iterationCounter = 0;
    private int computeCounter = 0;
    private BridgeAI bridgeAi;
    private int hellCount = 1;

    public DisplayParticlePane(frontend.Parameters p) {
        this.setPrefSize(WIDTH, HEIGHT);

        if (Main.displayPane != null) {
            this.colorOn = Main.displayPane.colorOn;
            this.colorOff = Main.displayPane.colorOff;
        } else {
            this.colorOn = Color.WHITE;
            this.colorOff = Color.BLACK;
        }

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        this.getChildren().add(canvas);

        this.numRows = p.getRows();
        this.numCols = p.getColumns();
        this.params = p;

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

            int minGridX = (int) Math.max(0, Math.floor((x - dragRadius)/cellWidth));
            int minGridY = (int) Math.max(0, Math.floor((y - dragRadius)/cellHeight));
            int maxGridX = (int) Math.min(numRows-1, Math.floor((x + dragRadius)/cellWidth));
            int maxGridY = (int) Math.min(numCols-1, Math.floor((y + dragRadius)/cellHeight));

            for (int i = minGridX; i <= maxGridX; ++i) {
                for (int j = minGridY; j <= maxGridY; ++j) {
                    swapGrid(i, j);
                }
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

    private void swapGrid(int i, int j) {
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
            if (grid.length >= 100 && grid[0].length >= 100) {
                if (iterationCounter % 5_000 == 0) {
                    iterationCounter = 0;
                    if (bridgeAi != null) {
                        bridgeAi.stop();
                    }

                    bridgeAi = new BridgeAI(grid, this::updatePrediction);
                    bridgeAi.startProcess();
                }
            }

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

            if (computeCounter % 100 == 0) {
                computeShit();
            }



            iterationCounter++;
            computeCounter++;
        }));

        timeline.setDelay(Duration.seconds(1));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        isRunning = true;
    }

    private void computeShit() {
        double magSum = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (grid[i][j]) {
                    magSum++;
                } else {
                    magSum--;
                }
            }
        }

        double averageMag = magSum / (numRows * numCols);
        FeedbackPane.valueMagnetization.setText(String.format("%.2f", averageMag));

        double energy = 0;
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numRows; ++j) {
                boolean spin0 = grid[i][j];
                boolean spin1 = grid[(i + 1) % numRows][j];
                boolean spin2 = grid[i][(j + 1) % numCols];

                energy += bool2spin(spin0) * bool2spin(spin1);
                energy += bool2spin(spin0) * bool2spin(spin2);
            }
        }

        energy /= -1000;

        FeedbackPane.valueEnergy.setText(String.format("%.2f", energy));
    }

    private int bool2spin(boolean b) {
        return b ? 1 : -1;
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

    public void updatePrediction(int n) {
        if (n == 0) {
            FeedbackPane.valueAI.setText("PARA");
        } else {
            FeedbackPane.valueAI.setText("FERRO");
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
