package frontend;

import javafx.geometry.Insets;
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
        setMinHeight(height);
        setMaxHeight(height);
        setPrefHeight(height);


        this.title = new Label("Ising Model Simulation");
        this.title.setFont(new Font("System Bold", 44));
        this.title.setAlignment(Pos.TOP_LEFT);
        this.title.setPadding(new Insets(20, 0, 10, 20));

        this.getChildren().addAll(this.title);

        title.setFocusTraversable(true);
        title.requestFocus();
    }
}
