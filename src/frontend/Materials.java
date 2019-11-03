package frontend;

import javafx.scene.paint.Material;

public enum Materials {
    NOT_FERRO("Low J", 100),
    MEDIUM_FERRO("Medium J", 450),
    VERY_FERRO("High J", 600);

    private final String disp;
    private final double interactionStrength;

    private Materials(String disp, double j) {
        this.disp = disp;
        this.interactionStrength = j;
    }

    public double getInteractionStrength() {
        return this.interactionStrength;
    }

    @Override
    public String toString() {
        return disp + " (" + interactionStrength + ")";
    }
}
