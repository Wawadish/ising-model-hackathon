package frontend;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ColorPane extends Pane {

    public String STYLE_ON = "-fx-background-color: white";
    public String STYLE_OFF = "-fx-background-color: black";

    private boolean state;

    public ColorPane(double width, double height, String styleOn, String styleOff) {
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        this.STYLE_ON = styleOn;
        this.STYLE_OFF = styleOff;

        this.state = false;
        this.setStyle(STYLE_OFF);
    }

    public void swapState() {
        this.setStyle(state ? STYLE_OFF : STYLE_ON);
        this.state = !this.state;
    }

    public boolean getState() {
        return state;
    }

    public void setStyleOn(String styleOn) {
        this.STYLE_ON = styleOn;
        if (state) {
            this.setStyle(styleOn);
        }
    }

    public void setStyleOff(String styleOff) {
        this.STYLE_OFF = styleOff;
        if (!state) {
            this.setStyle(styleOff);
        }
    }



}
