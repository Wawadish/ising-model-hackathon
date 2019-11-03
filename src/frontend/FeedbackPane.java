package frontend;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

public class FeedbackPane extends GridPane {

    public static Label valueAI = new Label("");

    private static final Font FONT = new Font(20);
    private static final Font FONT_BOLD = new Font("System Bold", 25);

    public static Label valueEnergy;
    public static Label valueMagnetization;

    public FeedbackPane() {
        Label labelEnergy = new Label("Energy: ");
        Label labelMagnetization = new Label("Magnetization: ");
        Label labelAI = new Label("State Predicted by AI: ");

        valueEnergy = new Label("123");
        valueMagnetization = new Label("234");
        valueAI = new Label("___");

        Label labelSubtitle =new Label("Statistics: ");

        labelEnergy.setFont(FONT);
        labelMagnetization.setFont(FONT);
        labelAI.setFont(FONT);
        valueEnergy.setFont(FONT);
        valueMagnetization.setFont(FONT);
        valueAI.setFont(FONT_BOLD);
        labelSubtitle.setFont(FONT_BOLD);

        this.add(labelSubtitle, 0, 0);
        this.add(labelEnergy, 0, 1);
        this.add(valueEnergy, 1, 1);
        this.add(labelMagnetization, 0, 2);
        this.add(valueMagnetization, 1, 2);
        this.add(labelAI, 0, 3);
        this.add(valueAI, 1, 3);

        this.setVgap(20);
        this.setHgap(10);
        this.setPadding(new Insets(0, 0, 20, 30));
    }
}
