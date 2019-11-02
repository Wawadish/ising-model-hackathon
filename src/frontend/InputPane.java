package frontend;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Material;

import javax.xml.soap.Text;

public class InputPane extends VBox {

    private double x;
    private double y;
    private Material material;

    public InputPane(double width, double height){
        this.setPrefSize(width, height);

        //Materials
        ComboBox<Materials> cbxMaterial = new ComboBox<>();
        cbxMaterial.setItems(FXCollections.observableArrayList(Materials.values()));

        //X and Y input
        TextField xField = new TextField();
        TextField yField = new TextField();
        xField.setOnAction(e->{
            x = Double.valueOf(xField.getText());
            y = Double.valueOf(yField.getText());
        });
        yField.setOnAction(xField.getOnAction());
        Label xyLabel = new Label("Please input a rectangle width x height");
        HBox xyBox = new HBox();
        xyBox.getChildren().addAll(xyLabel, xField, yField);

        //Add to pane
        this.getChildren().addAll(cbxMaterial, xyBox);
    }
}
