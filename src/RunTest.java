/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  To run a specific expression transformation as requested by
 * the IO class.
 **************************************************************************** */

public class RunTest {
    private String input, output;
    private final String inputType, outputType;
    private String[][] machineInput, machineOutput;
    private long totalTime;
    private boolean hasError;
    private int stackOperations;

    // The overloaded constructor allows for two different input types:
    // String[][] for machine instructions, and String for all others.
    RunTest(String input, String inputType, String outputType) {
        this.input = input;
        this.inputType = inputType;
        this.outputType = outputType;
    }

    RunTest(String[][] input, String inputType, String outputType) {
        this.machineInput = input;
        this.inputType = inputType;
        this.outputType = outputType;
    }

    // Run the requested expression transformation.  If a case outside of the allowable 5 is
    // attempted, throws an error, though this should not be possible given restrictions in
    // UserInput and IO.
    // If the transform is complete without errors, this also retrieves information on how
    // long the transformation took and how many stack operations took place.
    void run() {
        ExpressionTransform trial;
        long startTime = System.nanoTime();
        switch (inputType + "-" + outputType) {
            case "Postfix-Machine":
                trial = new PostfixMachineInstructions(input);
                break;
            case "Machine-Infix":
                trial = new MachineToInfix(machineInput);
                break;
            case "Infix-Postfix":
                trial = new InfixToPostfix(input);
                break;
            case "Infix-Prefix":
                trial = new InfixToPostfix(input, true);
                break;
            case "Prefix-Infix":
                trial = new PrefixToInfix(input);
                break;
            default:
                throw new IllegalStateException("Unexpected transform case: " + inputType + "-" + outputType);
        }
        trial.transform();
        long endTime = System.nanoTime();
        totalTime = endTime - startTime;
        hasError = trial.hasError();
        stackOperations = trial.getStackOperations();

        if (!outputType.equals("Machine")) {
            output = trial.getOutput();
        } else {
            machineOutput = trial.getMachineOutput();
        }
    }

    String getOutput() {
        return output;
    }

    String[][] getMachineOutput() {
        return machineOutput;
    }

    long getTotalTime() {
        return totalTime;
    }

    boolean isError() {
        return hasError;
    }

    int getStackOperations() {
        return stackOperations;
    }
}
