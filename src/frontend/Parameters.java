package frontend;

public class Parameters {
    private int rows;
    private int columns;
    public double temperature;
    public Materials material;

    public Parameters(int rows, int columns, double temperature, Materials material){
        this.rows = rows;
        this.columns = columns;
        this.temperature = temperature;
        this.material = material;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public double getTemperature() {
        return temperature;
    }

    public Materials getMaterial() {
        return material;
    }
}
