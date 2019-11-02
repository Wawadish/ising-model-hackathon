import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class TitlePane extends Pane {

    private Label title;
    public double width;
    public double height;

    public TitlePane(double width, double height) {
        this.width = width;
        this.height = height;

        this.setPrefSize(width, height);


        this.title = new Label("Ising Model Simulation");
        this.title.setFont(new Font(20));


        double w = (this.getWidth()/2)-(title.getWidth()/2);
        double h = (this.getHeight()/2)-(title.getHeight()/2);

        this.title.setLayoutX(w);
        this.title.setLayoutY(h);

        this.getChildren().addAll(this.title);

    }
}
