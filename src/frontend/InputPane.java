package frontend;

import com.sun.deploy.xml.XMLable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;

import javax.xml.soap.Text;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class InputPane extends VBox {

    public static ColorPicker colorPicker1;
    public static CheckBox checkBox1;
    public static ColorPicker colorPicker2;
    public static CheckBox checkBox2;
    public static Slider tempSlider;
    public static ComboBox<Materials> cbxMaterial;
    public static TextField xField;
    public static TextField yField;
    public static Label aIprediction;


    public InputPane(double width, double height){
        this.setPrefSize(width, height);

        //Temperature
        tempSlider = new Slider();
        tempSlider.setValue(0);

        tempSlider.setMin(1);
        tempSlider.setValue(tempSlider.getMin());

        tempSlider.setMax(1000);
        Label sliderLabel = new Label("000.00 + Kelvins");
        sliderLabel.setContentDisplay(ContentDisplay.RIGHT);
        //Materials
        cbxMaterial = new ComboBox<>();
        cbxMaterial.setItems(FXCollections.observableArrayList(Materials.values()));
        cbxMaterial.getSelectionModel().select(Materials.EINSTEINIUM);

        //X and Y input
        xField = new TextField();
        yField = new TextField();
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
        cbxMaterial.setOnAction(e -> {
            Main.displayPane.updateThings(Main.displayPane.getParams().temperature,
                    cbxMaterial.getSelectionModel().getSelectedItem());
        });
        tempSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                sliderLabel.setText(String.format("%.2f + Kelvins", new_val));
                Main.displayPane.updateThings(new_val.doubleValue(), Main.displayPane.getParams().material);
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

        //Color Picker 1
        colorPicker1 = new ColorPicker();
        colorPicker1.setLayoutY(height/2);
        checkBox1 = new CheckBox();
        checkBox1.setSelected(true);
        checkBox1.setDisable(true);

        //Color Picker 2
        colorPicker2 = new ColorPicker(new Color(0, 0, 0, 1));
        colorPicker2.setLayoutY(height/2 + height/20);
        checkBox2 = new CheckBox();


        checkBox1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue)
                    return;

                DisplayParticlePane.firstColor = true;
                checkBox2.setDisable(false);
                checkBox2.setSelected(false);
                checkBox1.setDisable(true);
            }
        });

        checkBox2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue)
                    return;

                DisplayParticlePane.firstColor = false;
                checkBox1.setDisable(false);
                checkBox1.setSelected(false);
                checkBox2.setDisable(true);
            }
        });

        HBox hbox1 = new HBox();
        hbox1.setSpacing(this.getPrefWidth()/4);
        hbox1.getChildren().addAll(colorPicker1,checkBox1);

        HBox hbox2 = new HBox();
        hbox2.setSpacing(this.getPrefWidth()/4);
        hbox2.getChildren().addAll(colorPicker2,checkBox2);


        colorPicker1.setOnAction(event -> {
            Color c = colorPicker1.getValue();
            Main.displayPane.changeColorOn(c);
        });

        colorPicker2.setOnAction(event -> {
            Color c = colorPicker2.getValue();
            Main.displayPane.changeColorOff(c);
        });

        //Ai Prediction
        aIprediction = new Label("State Predicted by AI: ");
        sliderLabel.setLayoutY(height/2 + height/10);;

        //Add to pane
        this.getChildren().addAll(tempBox,cbxMaterial, xyBox, hbox1, hbox2, aIprediction);
    }
}
