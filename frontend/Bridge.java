import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bridge {

    //Data Fields
    private int rows;
    private int cols;
    private double gamma;
    private String initialInput;
    private Process process;
    private Runtime runtime;
    private Queue<ArrayList<Position>> changingPositions;
    private Runnable runningThread;
    private volatile boolean paused = false;


    private InputStream pythonInputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private Scanner scanner;

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
        this.changingPositions = new LinkedList<>();
        runtime = Runtime.getRuntime();
    }

    //Run Initial Python command
    public void runPythonCommand() throws IOException {
        String command = ("python " + FILENAME + " " + rows + " " + cols + " " + gamma + " " + initialInput);
        process = runtime.exec(command);
    }

    //Read the stream of python output
    public void readPythonOutput() {
        pythonInputStream = process.getInputStream();
        inputStreamReader = new InputStreamReader(pythonInputStream);
        bufferedReader = new BufferedReader(inputStreamReader);
        scanner = new Scanner(bufferedReader);


        while(!paused) {
            ArrayList<Position> changedPositions = new ArrayList<>();
            while (true){
                int x = scanner.nextInt();
                if(x == -1 ) {
                    break;
                }
                int y = scanner.nextInt();
                changedPositions.add(new Position(x, y));
            }
            changingPositions.add(changedPositions);
        }
        process.destroy();
    }

    public void BridgeProcess() {
        runningThread = new Runnable() {
            @Override
            public void run() {
                try {
                    runPythonCommand();
                    readPythonOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread dataThread = new Thread(runningThread);

        dataThread.start();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}