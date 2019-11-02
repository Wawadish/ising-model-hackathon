package frontend;

import javafx.scene.paint.Material;

public enum Materials {
    URANIUM(1.0),
    TITANIUM(2.0),
    MANGANESE(3.0),
    EINSTEINIUM(4.0);

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
