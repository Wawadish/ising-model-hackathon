package frontend;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Bridge {

    //Data Fields
    private final int rows;
    private final int cols;
    private final double temperature;
    private final double jConstant;
    private boolean[][] initialInput;

    private Process process;
    private ConcurrentLinkedDeque<List<Position>> changingPositions;
    private Runnable runningThread;
    private volatile boolean paused = false;

    private Scanner scanner;

    //Variables
    private final String FILENAME = "./src/backend/ising_simulation.py";
    private final File TEMP_FILE = new File("./src/backend/temp");

    /**
     * @param rows         Numbers of Rows of Grind
     * @param cols         Number of columns of Grid
     * @param initialInput The String of 1's and 0's representing the grid
     * @param temperature  The temperature value
     * @param jConstant    The interaction strength constant
     */
    public Bridge(int rows, int cols, double temperature, double jConstant, boolean[][] initialInput) {
        this.rows = rows;
        this.cols = cols;
        this.initialInput = initialInput;
        this.temperature = temperature;
        this.jConstant = jConstant;
        this.changingPositions = new ConcurrentLinkedDeque<>();
    }

    private void encodeInitialState() throws IOException {
		System.out.println(new File(".").getAbsolutePath());
        PrintWriter writer = new PrintWriter(new FileOutputStream(TEMP_FILE));
        for (boolean[] row : initialInput) {
            for (boolean state : row) {
                writer.print(state ? '1' : '0');
            }
            writer.print('\n');
        }
        writer.flush();
        writer.close();
    }

    //Run Initial Python command
    private void runPythonCommand() throws IOException {
        String command = String.format("python \"%s\" %d %d %f %f", FILENAME, rows, cols, temperature, jConstant);
        System.out.println(command);
        process = Runtime.getRuntime().exec(command);
    }

    //Read the stream of python output
    private void readPythonOutput() {
        scanner = new Scanner(new InputStreamReader(process.getInputStream()));

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
                encodeInitialState();
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