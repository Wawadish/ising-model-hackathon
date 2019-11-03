package frontend;

import com.sun.deploy.xml.XMLable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Material;

import javax.xml.soap.Text;
import java.text.DecimalFormat;

public class InputPane extends VBox {

    public InputPane(double width, double height){
        this.setPrefSize(width, height);

        //Temperature
        Slider tempSlider = new Slider();
        tempSlider.setValue(0);
        tempSlider.setMax(1000);
        Label sliderLabel = new Label("000.00 + Kelvins");
        sliderLabel.setContentDisplay(ContentDisplay.RIGHT);
        //Materials
        ComboBox<Materials> cbxMaterial = new ComboBox<>();
        cbxMaterial.setItems(FXCollections.observableArrayList(Materials.values()));
        cbxMaterial.getSelectionModel().select(Materials.EINSTEINIUM);

        //X and Y input
        TextField xField = new TextField();
        TextField yField = new TextField();
        xField.setText("50");
        yField.setText("50");

        //Actions and Listeners
        xField.setOnAction(e->{
            if (!(xField.getText().equals("") || yField.getText().equals(""))) {
                Main.updateDisplay(new DisplayParticlePane(new Parameters(Integer.valueOf(xField.getText()),
                        Integer.valueOf(yField.getText()),
                        tempSlider.getValue(),
                        cbxMaterial.getSelectionModel().getSelectedItem())));
            }
        });
        yField.setOnAction(xField.getOnAction());
        cbxMaterial.setOnAction(xField.getOnAction());
        tempSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                sliderLabel.setText(String.format("%.2f + Kelvins", new_val));
            }
        });

        //Grid
        Label xyLabel = new Label("Please input a rectangle width x height");
        HBox xyBox = new HBox();
        xyBox.getChildren().addAll(xyLabel, xField, yField);

        //Temperature
        HBox tempBox = new HBox();
        tempBox.getChildren().addAll(sliderLabel, tempSlider);
        tempBox.setSpacing(this.getPrefWidth() / 4);



        //Add to pane
        this.getChildren().addAll(tempBox,cbxMaterial, xyBox);
    }
}
