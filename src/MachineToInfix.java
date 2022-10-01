/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  To convert machine instructions (derived from a postfix
 * expression) into an infix expression.
 **************************************************************************** */

import java.util.Arrays;
import java.util.List;

public class MachineToInfix extends ExpressionTransform {
    private final String[][] instructions;
    private StringBuilder[] tempVars;
    private int tempCount = -1;
    private final String[] OPERATORS = {"+", "-", "*", "/", "^"};
    private final List<String> INSTRUCTIONS = Arrays.asList("AD", "SB", "ML", "DV", "EX", "LD", "ST");

    // Maps the machine instructions into this class and initializes a new
    // StringBuilder array tempVars, which will hold the sub-expressions of the infix
    // expression.  This array is initialized to a size  that matches the number of instructions,
    // which by definition it cannot exceed, since each sub-expression is some combination of instructions.
    MachineToInfix(String[][] instructions) {
        this.instructions = new String[instructions.length][instructions[0].length];
        for (int i = 0; i < instructions.length; i++) {
            for (int j = 0; j < instructions[i].length; j++) {
                this.instructions[i][j] = instructions[i][j];
                tempVars = new StringBuilder[instructions.length];
            }
        }
    }

    // Converts the machine instructions into an infix expression
    void transform() {
        for (int i = 0; i < instructions.length; i++) {
            String curInstruction = instructions[i][0];

            // Skips any blank line
            if (curInstruction == null) {
                continue;
            }

            // Outputs an error and ends the method if an invalid instruction is found
            if (!INSTRUCTIONS.contains(curInstruction)) {
                foundError = true;
                i = instructions.length;
                output = "Error: invalid instruction: " + curInstruction;
                continue;
            }

            // curVar pulls in the element of this instruction
            StringBuilder curVar = new StringBuilder(instructions[i][1]);

            // If the element refers to a TEMP variable, retrieve the expression that
            // this TEMP variable represents, which is stored in tempVars at the index
            // position of the TEMP reference number less 1
            if (curVar.toString().contains("TEMP")) {
                curVar = tempVars[Integer.parseInt(curVar.substring(4)) - 1];
            }

            // if the instruction is LD, store the variable in the next tempVars index location
            if (curInstruction.equals("LD")) {
                tempVars[++tempCount] = new StringBuilder(curVar);

                // If the instruction is ST, store the sub-expression in tempVars.  Wrap
                // the sub-expression in parentheses.
            } else if (curInstruction.equals("ST")) {
                tempVars[tempCount] = new StringBuilder("(").append(curVar).append(")");

                // If the instruction is not LD or ST, it is an operation.
                // Convert this instruction operation into its infix counterpart.
                // Then, take the element currently in tempVars and replace it with
                // itself concatenated with the operation and the element that the
                // instructions say it is to be applied on.
            } else {
                String instructionOperation = OPERATORS[INSTRUCTIONS.indexOf(curInstruction)];
                tempVars[tempCount] = tempVars[tempCount].append(instructionOperation).append(curVar);
            }
        }

        // If no error has been found, assign the last element added to tempVars to the output
        if (!foundError) {
            output = tempVars[tempCount].toString();
        }
    }
}
