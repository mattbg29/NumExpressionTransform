/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  To gather the following user input via the console: input file
 * with expressions, output file where the output will be written to, input file
 * format, and one or more output expression transformations.
 **************************************************************************** */

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserInput {
    private final String[] EXPRESSIONS = {"Infix", "Postfix", "Prefix", "Machine"};
    private final Scanner inputReader = new Scanner(System.in);
    private final List<String> transformOptionsInput = Arrays.asList("11", "22", "33", "44", "14", "21", "23", "32", "34", "42", "43");
    private final String[] transformOptionsOutput = {"1", "2", "3", "4", "124", "241", "2413", "312", "3124", "412", "413"};
    private String inputFile, outputFile, inputExpression, furtherOutput, finalOutput;
    private String[] finalTransformSteps;

    void getUserInput() {

        getFileNames();
        getInputExpression();
        getOutputExpression();

        finalOutput = inputExpression + furtherOutput;

        convertTransformSteps();

        // finalTransformSteps should only be < 2 if the user requests only transformations to the
        // same format as the input.
        if (finalTransformSteps.length < 2) {
            System.out.println("Your input matches your output. Let's start over");
            getUserInput();
        }
        inputReader.close();
    }

    // Retrieves the input and output file names
    private void getFileNames() {
        System.out.println("\nPlease enter the input file source with expressions only, " +
                "\nall of one format (infix, postfix, or prefix): ");
        inputFile = inputReader.nextLine();

        System.out.println("Please enter the output file source: ");
        outputFile = inputReader.nextLine();
    }

    // Retrieves the input file format from the user
    private void getInputExpression() {
        boolean validEntry = false;

        while (!validEntry) {
            System.out.println("What format is your input? " +
                    "\nEnter 1 for Infix, 2 for Postfix, 3 for Prefix: ");
            inputExpression = inputReader.nextLine();
            if (inputExpression.equals("1") || inputExpression.equals("2") || inputExpression.equals("3")) {
                validEntry = true;
            } else {
                System.out.println("Invalid entry.  Please try again.");
            }
        }
    }

    // Retrieves the user's requested transformation steps
    private void getOutputExpression() {
        boolean validEntry = false;
        while (!validEntry) {
            System.out.println("What would you like to transform the input expressions to?" +
                    "\nEnter 1 for Infix, 2 for Postfix, 3 for Prefix, 4 for Machine Instructions" +
                    "\nYou can request subsequent transformations, though some may require intermediate steps." +
                    "\nFor instance, to transform the expressions to Machine Instructions," +
                    "\nthen transform the Machine Instructions to Infix and finally transform Infix to Prefix, " +
                    "\ntype: 413 and hit enter:");

            furtherOutput = inputReader.nextLine();
            if (furtherOutput.length() == 0) {
                System.out.println("Your entry is empty.  Please try again");
            } else {
                for (int i = 0; i < furtherOutput.length(); i++) {
                    char currentEntry = furtherOutput.charAt(i);
                    if (currentEntry == '1' || currentEntry == '2' || currentEntry == '3' || currentEntry == '4') {
                        validEntry = true;
                    } else {
                        validEntry = false;
                        i = furtherOutput.length();
                        System.out.println("Invalid entry.  Please try again.");
                    }
                }
            }
        }
    }

    // First, convert the user's requested transform steps into steps the program can process;
    // in particular, remove any duplicate steps (the program cannot transform a format to itself)
    // and add any requisite intermediate steps (see README for details).
    // Second, convert this new string of steps, currently in numeral format, into
    // the name steps "Infix", "Postfix", "Prefix", or "Machine".
    private void convertTransformSteps() {
        StringBuilder addTransformSteps = new StringBuilder();
        addTransformSteps.append(inputExpression);

        for (int i = 1; i < finalOutput.length(); i++) {
            String stepsToCheck = finalOutput.substring(i - 1, i + 1);
            int checkTransformSteps = transformOptionsInput.indexOf(stepsToCheck);
            if (checkTransformSteps == -1) {
                addTransformSteps.append(stepsToCheck.substring(1));
            } else {
                addTransformSteps.append(transformOptionsOutput[transformOptionsInput.indexOf(stepsToCheck)].substring(1));
            }
        }

        finalTransformSteps = addTransformSteps.toString().split("");
        for (int i = 0; i < finalTransformSteps.length; i++) {
            finalTransformSteps[i] = EXPRESSIONS[Integer.parseInt(finalTransformSteps[i]) - 1];
        }
    }

    String getInputFile() {
        return inputFile;
    }

    String getOutputFile() {
        return outputFile;
    }

    String[] getTransformSteps() {
        return finalTransformSteps;
    }
}
