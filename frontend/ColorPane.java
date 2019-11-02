import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ColorPane extends Pane {

    private static final String STYLE_ON = "-fx-background-color: white";
    private static final String STYLE_OFF = "-fx-background-color: black";

    private boolean state;

    public ColorPane(double width, double height) {
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

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
}
