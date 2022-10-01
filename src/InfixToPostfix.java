/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  To convert an infix expression into postfix or prefix
 **************************************************************************** */

public class InfixToPostfix extends ExpressionTransform {
    private static final int[] PRECEDENCE = {1, 1, 2, 2, 3};
    private static final String VALID_INPUT = OPERANDS + OPERATORS + "(" + ")";
    private final StringBuilder output = new StringBuilder();
    private boolean prefix = false;

    // This method is overloaded; if no second parameter, it converts infix to postfix.
    // If the second parameter is the boolean true, it converts infix to prefix.
    // This was originally written for infix to postfix only, and the infix to prefix
    // version was added layer, hence the inconsistency between how the class handles
    // the two versions, and the inadequate name of the class itself.
    InfixToPostfix(String input) {
        this.input = input;
        expression = input.split("");
    }

    // If conversion is declared prefix, reverse the input and invert parentheses
    InfixToPostfix(String input, boolean prefix) {
        this.input = input;
        String[] inputSplit = input.split("");
        if (!prefix) {
            expression = input.split("");
        } else {
            this.prefix = true;
            expression = new String[inputSplit.length];
            int j = 0;
            for (int i = inputSplit.length - 1; i >= 0; i--) {
                if (inputSplit[i].equals("(")) {
                    expression[j] = ")";
                } else if (inputSplit[i].equals(")")) {
                    expression[j] = "(";
                } else {
                    expression[j] = inputSplit[i];
                }
                j++;
            }
        }
    }

    // Run the transform process by iterating through each element in the expression,
    // which has been reversed if this is an infix to prefix transformation
    void transform() {
        if (expression.length < 3) {
            errorHandle("Minimum");
        } else if (isOperator(expression[0]) | isOperator(expression[expression.length - 1])) {
            errorHandle("Syntax");
        } else {
            for (int i = 0; i < expression.length; i++) {
                String curElement = expression[i];

                // If an invalid character is found, output an error and
                // end the transform process
                if (isInvalid(curElement)) {
                    errorHandle("Invalid", curElement);
                    i = expression.length;
                    continue;
                }

                // If an invalid syntax is found, output an error and
                // end the transform process
                if (syntaxError(i)) {
                    errorHandle("Syntax");
                    i = expression.length;
                    continue;
                }

                // If the element is (, push it to the stack.
                // If the element is an operand, add append this to the output expression
                if (curElement.equals("(")) {
                    stack.push(curElement);
                } else if (isOperand(curElement)) {
                    output.append(curElement);

                    // If the current element is ), pop and append to the output expression
                    // until either the stack is empty or the top of the stack is (.
                    // Since && short circuits, peek() will not be called if the stack is empty.
                } else if (curElement.equals(")")) {
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        output.append(stack.pop());
                    }

                    // If the current element is ) and we popped the whole stack and
                    // never found a (, there is an error. This outputs an error and ends
                    // the transform process.
                    if (stack.isEmpty()) {
                        errorHandle("Delimiter error A");
                        i = expression.length;
                        continue;
                    }
                    stack.pop();

                    // Adds the current element to the stack if the stack is empty, the top element on the
                    // stack is (, or the current element has lower precedence than the top element.
                } else if (stack.isEmpty() || stack.peek().equals("(") || !lowerPrecedence(curElement)) {
                    stack.push(curElement);
                } else {

                    // Pops the stack and adds the popped element to the output expression if:
                    // the stack is not empty,
                    // the top of the stack is not (,
                    // and the current element has lower precedence than the top of the stack.
                    // Once any of these is no longer true, push the current element to the stack.
                    while (!stack.isEmpty() && !stack.peek().equals("(") && lowerPrecedence(curElement)) {
                        output.append(stack.pop());
                    }
                    stack.push(curElement);
                }
            }
            // Once we've gone through each element in the infix expression, there may
            // be more operations on the stack, which are now popped and added to the expression,
            // but there should be no (, and the presence of one indicates an error,
            // in which case I output an error and end the transform process.
            while (!stack.isEmpty()) {
                if (stack.peek().equals("(")) {
                    errorHandle("Delimiter error B");
                    break;
                }
                output.append(stack.pop());
            }
        }
    }

    // Checks the whether the current element has a lower precedence than the element
    // on the top of the stack, as defined as follows: + - is lower than * / is lower than ^
    // When working with a postfix expression, I return true if the precedence of the top
    // of the stack is greater than or equal to the precedence of the current element.
    // When working with a prefix expression, i return true only if the precedence of the top
    // is greater than the precedence of the current element
    private boolean lowerPrecedence(String curr) {
        String top = stack.peek();
        if (!prefix) {
            return PRECEDENCE[OPERATORS.indexOf(top)] >= PRECEDENCE[OPERATORS.indexOf(curr)];
        } else {
            return PRECEDENCE[OPERATORS.indexOf(top)] > PRECEDENCE[OPERATORS.indexOf(curr)];
        }
    }

    // Checks if the element is valid
    private boolean isInvalid(String element) {
        return !VALID_INPUT.contains(element);
    }

    // Checks for syntax errors
    private boolean syntaxError(int i) {
        if (i == 0) {
            return false;
        }

        // No operand can be next to another operand
        if (isOperand(expression[i]) && isOperand(expression[i - 1])) {
            return true;
        }

        // No operator can be next to an operator
        if (isOperator(expression[i]) && isOperator(expression[i - 1])) {
            return true;
        }

        // No operand can come after a )
        if (isOperand(expression[i]) && expression[i - 1].equals(")")) {
            return true;
        }

        // No operand can come before a (
        if (isOperand(expression[i - 1]) && expression[i].equals("(")) {
            return true;
        }

        // No operator can come after a (
        if (isOperator(expression[i]) && expression[i - 1].equals("(")) {
            return true;
        }

        // No operator can come before a )
        if (isOperator(expression[i - 1]) && expression[i].equals(")")) {
            return true;
        }

        // ( cannot be immediately followed by )
        if (expression[i - 1].equals("(") && expression[i].equals(")")) {
            return true;
        }

        // ) cannot be immediately followed by (
        if (expression[i - 1].equals(")") && expression[i].equals("(")) {
            return true;
        }

        return false;
    }

    // Returns the output, overriding the getOutput method in ExpressionTransform
    @Override
    String getOutput() {
        if (!prefix || foundError) {
            return output.toString();
        } else {
            StringBuilder reverse = new StringBuilder();
            for (int i = output.length() - 1; i >= 0; i--) {
                reverse.append(output.charAt(i));
            }
            return reverse.toString();
        }
    }

    // Empties the stack in the event that an error is found
    private void emptyStack() {
        while (!stack.isEmpty()) {
            stack.pop();
        }
    }

    // errorHandle is overloaded to account for the fact that I need two parameters only
    // when the error is that there is an Invalid element, in which case I output the
    // erroneous element.
    private void errorHandle(String errorMessage) {
        errorHandle(errorMessage, "N/A");
    }

    // Empties the stack and prints an error to the console and to the output file
    // when an Invalid element, incorrect syntax, or delimiter error is found.
    private void errorHandle(String errorMessage, String element) {
        foundError = true;
        switch (errorMessage) {
            case "Invalid":
                System.out.println("Found an invalid element in the following infix expression: " + input + ". See output for details.");
                output.append(" Invalid character: ").append(element);
                emptyStack();
                break;
            case "Syntax":
                System.out.println("Found a syntax error in the following infix expression: " + input + ". See output for details.");
                output.append(" Syntax error in entry");
                emptyStack();
                break;
            case "Delimiter error A":
                System.out.println("Found a delimiter error in the following infix expression: " + input + ". See output for details.");
                output.append(" Error: ) without (");
                break;
            case "Delimiter error B":
                System.out.println("Found a delimiter error in the following infix expression: " + input + ". See output for details.");
                output.append(" Error: ( without )");
                break;
            case "Minimum":
                System.out.println("The following postfix expression has less than 3 elements: " + input + ". See output for details.");
                output.append("ERROR: An expression must have at least 3 elements.");
                break;
            default:
                throw new IllegalStateException("Unexpected error case");
        }
    }
}
