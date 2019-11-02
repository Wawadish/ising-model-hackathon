import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Bridge {

    //Data Fields
    private int rows;
    private int cols;
    private double gamma;
    private String initialInput;
    Process process;
    private Runtime runtime;

    InputStream pythonInputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;

    //Variables
    private final String FILENAME = "../backend/ising_simulation.py";

    /**
     *
     * @param rows Numbers of Rows of Grind
     * @param cols Number of columns of Grid
     * @param initialInput The String of 1's and 0's representing the grid
     * @param gamma Value depending on Temperature and K (i.e T/K)
     */
    public Bridge(int rows, int cols, double gamma, String initialInput) {
        this.rows = rows;
        this.cols = cols;
        this.initialInput = initialInput;
        this.gamma = gamma;
        runtime = Runtime.getRuntime();
    }

    //Run Initial Python command
    public void runPythonCommand() throws IOException {
        String command = ("python " + FILENAME + " " + rows + " " + cols + " " + gamma + " " +initialInput);
        process = runtime.exec(command);
    }


}