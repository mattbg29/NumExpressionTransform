/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  To take the information retrieved from UserInput (input file
 * with expressions, output file, and transformation steps) and call RunTest on each
 * transform step, writing the results to the output.
 **************************************************************************** */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class IO {
    private int testCount = 0;
    private final BufferedReader input;
    private final PrintWriter writer;
    private final String[] transform;
    private final List<String> EXPRESSIONS = Arrays.asList("Infix", "Postfix", "Prefix", "Machine");
    private String[][] machineInstructions;
    private String expression;
    private boolean hasError;

    IO(BufferedReader input, PrintWriter output, String[] transform) {
        this.input = input;
        this.writer = output;
        this.transform = new String[transform.length];
        for (int i = 0; i < transform.length; i++) {
            this.transform[i] = transform[i];
        }
    }

    // Iterate through the expressions in the input file and, for each, call runTrial
    // to process the requested transformations.  End the transformation process early if
    // an error is found with one of the transformations.
    void runTests() throws IOException {
        writeIntro();
        expression = input.readLine();

        while (expression != null) {
            // Skip the line if there is nothing in it
            if (expression.length() != 0) {
                writer.println("\n------------------ Test " + ++testCount + " ------------------");
                if (transform[0].equals("Machine")) {
                    writer.println("Error: Cannot begin with machine instructions");
                } else {
                    for (int i = 1; i < transform.length; i++) {
                        if (!EXPRESSIONS.contains(transform[i - 1]) || !EXPRESSIONS.contains(transform[i])) {
                            writer.println("Error: Invalid expression type");
                            i = transform.length;
                            continue;
                        }
                        runTrial(transform[i - 1], transform[i]);
                        if (hasError) {
                            i = transform.length;
                            continue;
                        }
                        writer.println();
                    }
                }
            }
            expression = input.readLine();
        }
    }

    // Process the requested transformation.  Only 5 direct transformations exist in this
    // program, each of which is covered by a case below.  This will throw an error if any
    // other case is attempted, though this should not be possible given the restrictions in
    // UserInput.
    // Write to the output information on each trial: a description of the trial, whether the
    // trial succeeded, and if so, total time and stack operations for the trial.
    private void runTrial(String inputType, String outputType) {
        RunTest trial;
        writer.println("Running the following trial: " + inputType + " expression to " + outputType + " expression");
        if (!inputType.equals("Machine")) {
            writer.println(inputType + " expression input: " + expression);
        }
        switch (inputType + "-" + outputType) {
            case "Postfix-Machine":
                trial = new RunTest(expression, "Postfix", "Machine");
                trial.run();
                machineInstructions = trial.getMachineOutput();
                prefixToMachine(machineInstructions);
                break;
            case "Machine-Infix":
                trial = new RunTest(machineInstructions, "Machine", "Infix");
                trial.run();
                expression = trial.getOutput();
                break;
            case "Infix-Postfix":
                trial = new RunTest(expression, "Infix", "Postfix");
                trial.run();
                expression = trial.getOutput();
                break;
            case "Infix-Prefix":
                trial = new RunTest(expression, "Infix", "Prefix");
                trial.run();
                expression = trial.getOutput();
                break;
            case "Prefix-Infix":
                trial = new RunTest(expression, "Prefix", "Infix");
                trial.run();
                expression = trial.getOutput();
                break;
            default:
                throw new IllegalStateException("Unexpected transform case: " + inputType + "-" + outputType);
        }
        hasError = trial.isError();
        if (!outputType.equals("Machine")) {
            writer.println(outputType + " expression output: " + expression);
        }
        if (hasError) {
            writer.println(inputType + " to " + outputType + " trial failed.");
        } else {
            writer.println(inputType + " to " + outputType + " trial passed.");
            if (trial.getStackOperations() == 0) {
                writer.println("Nanoseconds to process this trial: " + trial.getTotalTime());
            } else {
                writer.println("Nanoseconds / stack operations to process this trial: " + trial.getTotalTime() + " / " + trial.getStackOperations());
            }
        }
    }

    // Print the machine instructions to the output file.
    private void prefixToMachine(String[][] output) {
        writer.println("Machine Instructions:");
        for (String[] strings : output) {
            if (strings[0] != null) {
                writer.println(strings[0] + " " + strings[1]);
            }
        }
    }

    private void writeIntro() {
        writer.println("These tests will perform the following transformations using " +
                "\nan input file with " + transform[0] + " expressions to start:");

        StringBuilder summary = new StringBuilder(transform[0]);
        for (int i = 1; i < transform.length; i++) {
            summary.append("-").append(transform[i]);
        }
        writer.println(summary);
    }
}
