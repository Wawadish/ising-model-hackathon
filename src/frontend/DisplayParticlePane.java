package frontend;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.*;
import javafx.util.Duration;
import jdk.nashorn.internal.ir.CatchNode;

import java.util.List;
import java.util.Random;

public class DisplayParticlePane extends GridPane {
    public static final double WIDTH = 0.7 * Main.WIDTH;
    public static final double HEIGHT = 0.7 * Main.HEIGHT;

    public static boolean firstColor = false;

    private int numRows;
    private int numCols;

    private final Bridge bridge;
    private ColorPane[][] grid;

    private boolean isRunning;
    private Timeline timeline;
    private Parameters params;

    private double dragRadius = this.WIDTH/20;

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
                        "-fx-background-color: rgb(" + (InputPane.colorPicker1.getValue().getRed()*255) + "," + (InputPane.colorPicker1.getValue().getGreen()*255) + ", " + (InputPane.colorPicker1.getValue().getBlue()*255) + ")",
                        "-fx-background-color: rgb(" + (InputPane.colorPicker2.getValue().getRed()*255) + "," + (InputPane.colorPicker2.getValue().getGreen()*255) + ", " + (InputPane.colorPicker2.getValue().getBlue()*255) + ")");
                if (rand.nextDouble() >= 0.5) {
                    cp.swapState();
                }

                this.add(cp, i, j);
                grid[i][j] = cp;
            }
        }

        this.setOnMouseDragged(e->{
            double x = e.getX();
            double y = e.getY();
            int gridX = (int)Math.floor(x/grid[0][0].getWidth());
            int gridY = (int)Math.floor(y/grid[0][0].getHeight());
            double gridRadius = dragRadius/grid[0][0].getWidth();
            grid[gridX][gridY].swapState();
            for (int i = 0; i < gridRadius; i++){
                swapGrid(gridX+i, gridY+i);
                swapGrid(gridX+i, gridY);
                swapGrid(gridX, gridY);
                swapGrid(gridX, gridY+i);

                swapGrid(gridX-i, gridY-i);
                swapGrid(gridX-i, gridY);
                swapGrid(gridX-i, gridY-i);
                swapGrid(gridX, gridY-i);
            }

        });
        this.bridge = new Bridge(
                numRows,
                numCols,
                p.getTemperature(),
                p.getMaterial().getInteractionStrength(),
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
