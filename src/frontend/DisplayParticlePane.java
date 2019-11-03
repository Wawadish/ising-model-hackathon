package frontend;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class DisplayParticlePane extends GridPane {
    public static final double WIDTH = 0.7 * Main.WIDTH;
    public static final double HEIGHT = 0.7 * Main.HEIGHT;

    public static boolean firstColor = true;

    private int numRows;
    private int numCols;

    private Bridge bridge;
    public ColorPane[][] grid;

    private boolean isRunning;
    private Timeline timeline;
    private Parameters params;

    private double dragRadius = this.WIDTH / 20;

    private int itterationCounter = 0;
    private BridgeAI bridgeAi;
    private ColorPane[][] copy;

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
                ColorPane cp = new ColorPane(WIDTH / numRows, HEIGHT / numCols,
                        "-fx-background-color: rgb(" + (InputPane.colorPicker1.getValue().getRed() * 255) + "," + (InputPane.colorPicker1.getValue().getGreen() * 255) + ", " + (InputPane.colorPicker1.getValue().getBlue() * 255) + ")",
                        "-fx-background-color: rgb(" + (InputPane.colorPicker2.getValue().getRed() * 255) + "," + (InputPane.colorPicker2.getValue().getGreen() * 255) + ", " + (InputPane.colorPicker2.getValue().getBlue() * 255) + ")");
                if (rand.nextDouble() >= 0.5) {
                    cp.swapState();
                }

                this.add(cp, i, j);
                grid[i][j] = cp;
            }
        }

        this.setOnMouseDragged(e -> {
            double x = e.getX();
            double y = e.getY();
            int gridX = (int)Math.floor(x/grid[0][0].getWidth());
            int gridY = (int)Math.floor(y/grid[0][0].getHeight());
            double gridRadius = dragRadius/grid[0][0].getWidth();
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
        try{
            String initialStyle = this.grid[i][j].getStyle();
            this.grid[i][j].setColor(firstColor);
            String finalStyle = this.grid[i][j].getStyle();
            if(!initialStyle.equals(finalStyle)){
                this.grid[i][j].setState(!this.grid[i][j].getState());
            }

        }catch(Exception e){

        }
    }

    public void startAnimation() {

        if (isRunning) {
            return;
        }

        bridge.startProcess();
        this.timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {

            if(grid.length == 100 && grid[0].length == 100) {
                copy = new ColorPane[100][100];
                if (itterationCounter % 1000 == 0) {
                    itterationCounter = 0;
                    for(int i=0; i<grid.length; i++) {
                        for (int j = 0; j < grid[i].length; j++) {
                            copy[i][j] = grid[i][j];
                        }
                    }
                    if (bridgeAi == null) {
                        bridgeAi = new BridgeAI(copy, this::updatePrediction);
                    } else {
                        try {
                            bridgeAi.encodeInitialState(copy);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(bridgeAi == null) {
                        try {
                            bridgeAi.startProcess();
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            List<Position> changes = bridge.getChangingPositions().pollFirst();
            if (changes == null) {
                stopAnimation();
                return;
            }

            for (Position pos : changes) {
                ColorPane pane = grid[pos.getX()][pos.getY()];
                pane.swapState();
            }

            itterationCounter++;
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

    public void changeColorOn(String styleOn) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j].setStyleOn(styleOn);
            }
        }
    }

    public void changeColorOff(String styleOff) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j].setStyleOff(styleOff);
            }
        }
    }

    public void updatePrediction(int n) {
        if(n == 1) {
            InputPane.aIprediction.setText("State Predicted by AI: Ferromagnetic");
        }else {
            InputPane.aIprediction.setText("State Predicted by AI: Paramagnetic");
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
