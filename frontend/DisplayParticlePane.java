import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;

import java.awt.*;

public class DisplayParticlePane extends Pane {
    public static final double WIDTH = Main.WIDTH/10 * 7;
    public static  final double HEIGHT = Main.HEIGHT/10 * 7;
    private int gridX = 75;
    private int gridY = 60;
    public DisplayParticlePane(){
        this.setStyle("-fx-background-color: red");
        this.setPrefSize(this.WIDTH, this.HEIGHT);
        //grid
        GridPane grid = new GridPane();
        grid.setPrefSize(this.WIDTH, this.HEIGHT);
        //Individual Particle
        boolean alternate = false;
        for (int i = 0; i < gridX; i++) {
            alternate = !alternate;
            for (int j = 0; j < gridY; j++) {
                ColorPane cp = new ColorPane(WIDTH / gridX, HEIGHT / gridY);
                if(alternate){
                    cp.color();
                }
                alternate = !alternate;
                grid.add(cp, i, j);
            }
        }

        this.getChildren().add(grid);

    }

}
