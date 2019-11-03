package frontend;

import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public class BridgeAI {

    //Data Fields
    private static final int NUM_ROWS = 100;
    private static final int NUM_COLS = 100;
    private boolean[][] input;

    private Process process;
    private Runnable runningThread;

    private Scanner scanner;
    private Consumer<Integer> callback;

    //Variables
    private final String FILENAME = "src/backend/predict_phase.py";
    private final File TEMP_FILE = new File("src/backend/temp_ai");

    /**
     * @param initialInput The String of 1's and 0's representing the grid
     * @param callback yeet
     */
    public BridgeAI(boolean[][] initialInput, Consumer<Integer> callback) {
        input = new boolean[NUM_ROWS][NUM_COLS];
        this.callback = callback;
		for (int i = 0; i < NUM_ROWS; ++i) {
			for (int j = 0; j < NUM_COLS; ++j) {
			    input[i][j] = initialInput[i][j];
			}
		}
    }

    public void encodeInitialState(boolean[][] newInput) throws IOException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(new File(TEMP_FILE.getPath())));
            for (boolean[] row : newInput) {
                for (boolean state : row) {
                    writer.print(state ? '1' : '0');
                }
                writer.print('\n');
            }

        } catch (IOException ex) {
            System.out.println(ex.getClass().getName()+": "+ex.getMessage());
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    //Run Initial Python command
    private void runPythonCommand() throws IOException {
        String command = String.format("python %s", FILENAME);
        System.out.println(command);
        process = Runtime.getRuntime().exec(command);
    }

    //Read the stream of python output
    private void readPythonOutput() {
        scanner = new Scanner(new InputStreamReader(process.getInputStream()));

        try {
            while (true) {
                String str = scanner.nextLine();
                if (!str.startsWith("Prediction ")) {
                    continue;
                }

                int output = Integer.parseInt(str.substring(11, 12));
                System.out.println("Prediction "+output);
                Platform.runLater(() -> callback.accept(output));
                break;
            }
        } catch (NoSuchElementException ex) { }
        finally {
            process.destroy();
        }

    }

    public void startProcess() {
        runningThread = () -> {
            try {
                encodeInitialState(input);
                runPythonCommand();
                readPythonOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(runningThread).start();
    }

    public void stop() {
        if (process != null) {
            process.destroy();
        }
    }
}