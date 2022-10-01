/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  To convert a postfix expression into machine instructions.
 **************************************************************************** */

public class PostfixMachineInstructions extends ExpressionTransform {
    private final String[] INSTRUCTIONS = {"AD", "SB", "ML", "DV", "EX"};
    private int tempCount = 0;
    private int outputCount = 0;

    // I define the instructions array to be 3x the length of the postfix expression
    // because technically a single element of the postfix can take up to 3 lines
    // of machine instruction (an operation on two TEMP variables), and thus
    // the total length of the instruction should not exceed 3x the postfix expression.
    PostfixMachineInstructions(String input) {
        this.input = input;
        expression = input.split("");
        machineOutput = new String[expression.length * 3][2];
    }

    // Transforms the postfix to machine instructions by iterating through each element in the
    // postfix expression and taking the following steps:
    // If the element is an operand, push it to the stack.
    // If the element is an operator, call processOperator.  If this results in an error,
    // end the loop.
    // If neither of the above, it must be invalid.  Call errorHandle and end the loop.
    // Once finished with the loop, check for errors by calling processEndOfExpression
    void transform() {
        if (expression.length < 3) {
            errorHandle("Minimum");
        } else {
            for (int i = 0; i < expression.length; i++) {
                if (isOperand(expression[i])) {
                    stack.push(expression[i]);
                } else if (isOperator(expression[i])) {
                    processOperator(expression[i]);
                    if (foundError) {
                        i = expression.length;
                    }
                } else {
                    errorHandle("Invalid", expression[i]);
                    i = expression.length;
                }
            }
            processEndOfExpression();
        }
    }

    // When the element is an operator, pop the stack twice.  If either pop results in an
    // attempt to pop an empty stack, there is a syntax error; call errorHandle and return
    // from the method.
    // If no error is found, print an instruction to load the 'first' element (the second one popped),
    // retrieve the instruction form of the operator element, and print an instruction to run
    // this operation on the 'second' element (the first one popped).
    // Finally, create a TEMP variable with an incremented count, push this to the stack,
    // and print an instruction to store this temp variable.
    private void processOperator(String element) {
        String second = stack.pop();
        String first = stack.pop();

        if (second.equals(" EmptyStackException ") || first.equals(" EmptyStackException ")) {
            errorHandle("Syntax");
            return;
        }

        printInstruction("LD", first);

        String instructionSecond = INSTRUCTIONS[OPERATORS.indexOf(element)];
        printInstruction(instructionSecond, second);

        String tempVariable = "TEMP" + ++tempCount;
        stack.push(tempVariable);
        printInstruction("ST", tempVariable);
    }

    // Adds the machine instructions to a two-dimensional array
    private void printInstruction(String operation, String element) {
        machineOutput[outputCount][0] = operation;
        machineOutput[outputCount++][1] = element;
    }

    // Once the loop through the postfix expression is complete, the stack should have one
    // remaining element: the final stored temp variable.  This checks that the stack does
    // in fact have just one element, and runs errorHandle if that is not the case.
    private void processEndOfExpression() {
        if (!foundError) {
            if (stack.isEmpty()) {
                errorHandle("Syntax");
            } else {
                stack.pop();
                if (!stack.isEmpty()) {
                    errorHandle("Syntax");
                }
            }
        }
    }

    // errorHandle is overloaded to account for the fact that I need two parameters only
    // when the error is that there is an Invalid element, in which case I output the
    // erroneous element.
    private void errorHandle(String errorMessage) {
        errorHandle(errorMessage, "N/A");
    }

    // Prints an error to the console and to the output file when either an Invalid element or
    // incorrect syntax is found.
    private void errorHandle(String errorMessage, String element) {
        foundError = true;
        switch (errorMessage) {
            case "Invalid":
                System.out.println("Found an invalid element in the following postfix expression: " + input + ". See output for details.");
                if (element.equals(" ")) {
                    element = "(blank space)";
                }
                printInstruction("ERROR", "Invalid input: " + element);
                break;
            case "Syntax":
                System.out.println("Found a syntax error in the following postfix expression: " + input + ". See output for details.");
                printInstruction("ERROR", "Syntax error found, terminating the instructions here");
                break;
            case "Minimum":
                System.out.println("The following postfix expression has less than 3 elements: " + input + ". See output for details.");
                printInstruction("ERROR", "An expression must have at least 3 elements.");
                break;
            default:
                throw new IllegalStateException("Unexpected error case");
        }
    }
}
