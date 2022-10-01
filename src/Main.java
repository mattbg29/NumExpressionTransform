/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  To run the program.  In Main, I get user input via the UserInput class,
 * then generate output via the IO class.
 * See README for details about the overall program.
 **************************************************************************** */

import java.io.*;

public class Main {
    public static void main(String[] args) {
        // Creates a UserInput object for gathering program input from the user
        UserInput userInput = new UserInput();
        userInput.getUserInput();
        String inputFile = userInput.getInputFile();
        String outputFile = userInput.getOutputFile();
        String[] finalTransformSteps = userInput.getTransformSteps();

        // Check if the input file exists.  If it does, it creates an IO object which
        // runs the test and generates the output.
        // If the file does not exist, it prints an error message to the console and output file
        File checkFile = new File(inputFile);
        if (checkFile.exists()) {
            try {
                BufferedReader tests = new BufferedReader(new FileReader(inputFile));
                PrintWriter writer = new PrintWriter(outputFile);
                IO runTests = new IO(tests, writer, finalTransformSteps);
                runTests.runTests();
                tests.close();
                writer.close();

                // Since we've already checked if the input file exists this IOException
                // should only get called if there is a problem with the output file,
                // or with reading a line from the input file, both of which get
                // handled via this exception.  I include printStackTrace for debugging,
                // though my understanding is this is not ideal, but I am unsure what
                // is a better error handle here.
            } catch (IOException e) {
                System.out.println("Error reading input file or with the output file name");
                e.printStackTrace();
            }
        } else {
            try {
                System.out.println("Error: input file not found");
                PrintWriter writer = new PrintWriter(outputFile);
                writer.println("Input file not found");
                writer.close();

                // Handles the exception in the event that reading both the input
                // and the output files throws errors
            } catch (IOException e) {
                System.out.println("Error with both the input and output file names");
                e.printStackTrace();
            }
        }
    }
}
