package frontend;

import javafx.scene.paint.Material;

public enum Materials {
    URANIUM(300),
    TITANIUM(400),
    MANGANESE(500),
    EINSTEINIUM(600);

    private final double interactionStrength;

    private Materials(double j) {
        this.interactionStrength = j;
    }

    public double getInteractionStrength() {
        return this.interactionStrength;
    }

    @Override
    public String toString() {
        String s = super.toString().toLowerCase();
        s = s.substring(0,1).toUpperCase() + s.substring(1);
        return s;
    }
}
