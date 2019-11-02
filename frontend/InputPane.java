import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.xml.soap.Text;

public class InputPane extends VBox {

    private double x;
    private double y;

    public InputPane(double width, double height){
        this.setPrefSize(width, height);
        TextField xField = new TextField();
        TextField yField = new TextField();
        Label xyLabel = new Label("Please input a rectangle width x height");
        HBox xyBox = new HBox();
        xyBox.getChildren().addAll(xyLabel, xField, yField);
        this.getChildren().add(xyBox);
    }
}
