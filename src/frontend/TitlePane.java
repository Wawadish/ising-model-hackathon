package frontend;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import javax.swing.*;

public class TitlePane extends Pane {

    private Label title;
    public double width;
    public double height;

    public TitlePane(double width, double height) {
        this.width = width;
        this.height = height;

        this.setPrefSize(width, height);


        this.title = new Label("Ising Model Simulation");
        this.title.setFont(new Font(this.height));
        this.title.setAlignment(Pos.TOP_LEFT);

        this.getChildren().addAll(this.title);

    }
}
