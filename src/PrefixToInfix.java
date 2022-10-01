/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  To convert a prefix expression into infix.
 **************************************************************************** */

public class PrefixToInfix extends ExpressionTransform {

    // takes the given prefix expression, splits it into an array, and
    // stores its values in reverse order in the instance variable 'expression'
    PrefixToInfix(String input) {
        this.input = input;
        String[] inputSplit = input.split("");
        expression = new String[inputSplit.length];
        int j = 0;
        for (int i = inputSplit.length - 1; i >= 0; i--) {
            expression[j] = inputSplit[i];
            j++;
        }
    }

    // transforms the reversed prefix expression by iterating through it and performing
    // various operations
    void transform() {
        if (expression.length < 3) {
            errorHandle("Minimum");
        } else {
            for (int i = 0; i < expression.length; i++) {

                // If the element is an operand, push it to the stack
                if (isOperand(expression[i])) {
                    stack.push(expression[i]);

                    // If the element is an operator, pop it twice.  If either pop operation results
                    // in popping an empty stack, call errorHandle and end the loop.
                } else if (isOperator(expression[i])) {
                    String first = stack.pop();
                    String second = stack.pop();

                    if (second.equals(" EmptyStackException ") || first.equals(" EmptyStackException ")) {
                        errorHandle("Syntax");
                        i = expression.length;
                        continue;
                    }

                    // concatenate the first element popped from the stack with the operator and
                    // the second element popped from the stack, wrap the combined expression in
                    // parentheses and push this to the stack
                    String result = "(" + first + expression[i] + second + ")";
                    stack.push(result);

                    // If the element is neither an operand nor an operation, it is invalid.
                    // Call errorHandle and end the loop.
                } else {
                    errorHandle("Invalid", expression[i]);
                    i = expression.length;
                }
            }

            // If I reach the end of the expression and the stack is either empty or has more
            // than one element, there has been a syntax error, and this calls errorHandle.
            if (!foundError) {
                if (stack.isEmpty()) {
                    errorHandle("Syntax");
                } else {
                    output = stack.pop();
                    if (!stack.isEmpty()) {
                        errorHandle("Syntax");
                    }
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
                System.out.println("Found an invalid element in the following prefix expression: " + input + ". See output for details.");
                output = "Invalid character: " + element;
                break;
            case "Syntax":
                System.out.println("Found a syntax error in the following prefix expression: " + input + ". See output for details.");
                output = "Syntax error found";
                break;
            case "Minimum":
                System.out.println("The following postfix expression has less than 3 elements: " + input + ". See output for details.");
                output = "ERROR: An expression must have at least 3 elements.";
                break;
            default:
                throw new IllegalStateException("Unexpected error case");
        }
    }
}

