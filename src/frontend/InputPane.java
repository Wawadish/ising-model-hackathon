package frontend;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InputPane extends GridPane {

    private static final Font FONT = new Font(20);
    private static final Font FONT_BOLD = new Font("System Bold", 20);

    public static CheckBox checkBox1;
    public static CheckBox checkBox2;

    public static ColorPicker colorPicker1;
    public static ColorPicker colorPicker2;

    public static Slider tempSlider;
    public static ComboBox<Materials> cbxMaterial;
    public static TextField xField;
    public static TextField yField;

    private Button startStopButton, resetButton;

    public InputPane(double width, double height) {
        this.setPrefSize(width, height);

        //Temperature
        Label labelTemperature = new Label("Temperature: ");
        Label valueTemperature = new Label("1.00 K");
        labelTemperature.setFont(FONT);
        valueTemperature.setFont(FONT);
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

        cbxMaterial.setButtonCell(new ListCell(){

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item==null){
                    setStyle("-fx-font-size:18");
                } else {
                    setStyle("-fx-font-size:18");
                    setText(item.toString());
                }
            }

        });

        //X and Y input
        xField = new TextField();
        yField = new TextField();
        xField.setText("50");
        yField.setText("50");
        xField.setPrefColumnCount(2);
        yField.setPrefColumnCount(2);
        xField.setAlignment(Pos.CENTER);
        yField.setAlignment(Pos.CENTER);
        xField.setFont(new Font(18));
        yField.setFont(new Font(18));


        //Actions and Listeners
        xField.setOnAction(e -> {
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
                valueTemperature.setText(String.format("%.2f K", new_val));
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
        colorPicker1.setLayoutY(height / 2);
        checkBox1 = new CheckBox();
        checkBox1.setSelected(true);
        checkBox1.setDisable(true);

        //Color Picker 2
        colorPicker2 = new ColorPicker(new Color(0, 0, 0, 1));
        colorPicker2.setLayoutY(height / 2 + height / 20);
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

        colorPicker1.setOnAction(event -> {
            Color c = colorPicker1.getValue();
            Main.displayPane.changeColorOn(c);
        });
        colorPicker2.setOnAction(event -> {
            Color c = colorPicker2.getValue();
            Main.displayPane.changeColorOff(c);
        });

        //Ai Prediction
        sliderLabel.setLayoutY(height / 2 + height / 10);


        Label labelSubtitle = new Label("User Inputs");
        Label labelMaterial = new Label("Material: ");
        Label labelGridSize = new Label("Grid Size: ");
        Label temp = new Label("  by  ");
        temp.setFont(FONT);
        labelMaterial.setFont(FONT);
        labelGridSize.setFont(FONT);
        labelSubtitle.setFont(new Font("System Bold", 25));

        Label labelColor1 = new Label("Color 1: ");
        Label labelColor2 = new Label("Color 2: ");
        labelColor1.setFont(FONT);
        labelColor2.setFont(FONT);

        setupButtons();

        // Add to pane
        this.add(labelSubtitle, 0, 0, 2, 1);

        this.add(labelGridSize, 0, 1);
        this.add(new HBox(xField, temp, yField), 1, 1);

        this.add(labelMaterial, 0, 2);
        this.add(cbxMaterial, 1, 2);

        this.add(labelTemperature, 0,3);
        this.add(valueTemperature, 1,3);

        this.add(tempSlider, 0, 4, 2, 1);

        this.add(labelColor1, 0, 5);
        this.add(colorPicker1, 1, 5);
        this.add(labelColor2, 0, 6);
        this.add(colorPicker2, 1, 6);

        this.add(new HBox(), 0, 7);

        this.add(startStopButton, 0, 8);
        this.add(resetButton, 1, 8);

        this.setVgap(15);
        this.setPadding(new Insets(10, 0, 0, 30));
    }

    private void setupButtons() {
        startStopButton = new Button("START");
        resetButton = new Button("RESET");

        startStopButton.setFont(FONT_BOLD);
        resetButton.setFont(FONT_BOLD);

        startStopButton.setOnAction((event) -> {
            if(!Main.displayPane.isRunning) {
                startStopButton.setText("STOP");
                cbxMaterial.setDisable(true);
                tempSlider.setDisable(true);
                xField.setDisable(true);
                yField.setDisable(true);
                Main.displayPane.startAnimation();
            } else {
                startStopButton.setText("START");
                cbxMaterial.setDisable(false);
                tempSlider.setDisable(false);
                xField.setDisable(false);
                yField.setDisable(false);
                Main.displayPane.stopAnimation();
            }
        });

        resetButton.setOnAction((event) -> {
            Main.displayPane.stopAnimation();
            startStopButton.setText("START");
            cbxMaterial.setDisable(false);
            tempSlider.setDisable(false);
            xField.setDisable(false);
            yField.setDisable(false);
            Main.updateDisplay(new DisplayParticlePane(Main.displayPane.getParams()));
        });
    }
}
