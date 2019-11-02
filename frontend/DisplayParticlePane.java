import javafx.scene.layout.*;

public class DisplayParticlePane extends GridPane {
    public static final double WIDTH = 0.7 * Main.WIDTH;
    public static final double HEIGHT = 0.7 * Main.HEIGHT;

    private int NUM_ROWS = 75;
    private int NUM_COLUMNS = 60;

    public DisplayParticlePane() {
        this.setStyle("-fx-background-color: red");
        setPrefSize(this.WIDTH, this.HEIGHT);

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
            }
        }
    }

}
