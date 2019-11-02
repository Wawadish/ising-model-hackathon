package frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bridge {

    //Data Fields
    private final int rows;
    private final int cols;
    private final double temperature;
    private final double jConstant;
    private String initialInput;

    private Process process;
    private ConcurrentLinkedDeque<List<Position>> changingPositions;
    private Runnable runningThread;
    private volatile boolean paused = false;

    private Scanner scanner;

    //Variables
    private final String FILENAME = "src/backend/ising_simulation.py";

    /**
     * @param rows         Numbers of Rows of Grind
     * @param cols         Number of columns of Grid
     * @param initialInput The String of 1's and 0's representing the grid
     * @param temperature  The temperature value
     * @param jConstant    The interaction strength constant
     */
    public Bridge(int rows, int cols, double temperature, double jConstant, String initialInput) {
        this.rows = rows;
        this.cols = cols;
        this.initialInput = initialInput;
        this.temperature = temperature;
        this.jConstant = jConstant;
        this.changingPositions = new ConcurrentLinkedDeque<>();
    }

    //Run Initial Python command
    private void runPythonCommand() throws IOException {
        String command = String.format("python %s %d %d %f %f %s", FILENAME, rows, cols, temperature, jConstant, initialInput);
        process = Runtime.getRuntime().exec(command);
    }

    //Read the stream of python output
    private void readPythonOutput() {
        scanner = new Scanner(new BufferedReader(new InputStreamReader(process.getInputStream())));

        try {
            while (!paused) {
                List<Position> changedPositions = new ArrayList<>();
                while (true) {
                    int x = scanner.nextInt();
                    if (x == -1) {
                        break;
                    }
                    int y = scanner.nextInt();
                    changedPositions.add(new Position(x, y));
                }

                changingPositions.addLast(changedPositions);
            }

        } catch (NoSuchElementException ex) { }
        finally {
            process.destroy();
        }

    }

    public void startProcess() {
        runningThread = () -> {
            try {
                runPythonCommand();
                readPythonOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(runningThread).start();
    }

    public void pause() {
        this.paused = true;
    }

    public ConcurrentLinkedDeque<List<Position>> getChangingPositions() {
        return changingPositions;
    }
}