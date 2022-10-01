/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  An abstract superclass that provides the framework for all of
 * the classes that deal with transforming numerical expressions to/from
 * infix, postfix, prefix, and machine instructions.
 **************************************************************************** */

// These member variables are "protected", which means they can be accessed by subclasses
// and by other classes in the same package. Ideally, they would only be mutable by subclasses,
// so that other classes in the package could not inadvertently alter their state.
// Some googling suggests there used to be a private-protected modifier that did just this,
// but it was removed some time ago.
public abstract class ExpressionTransform {
    protected final Stack stack = new Stack();
    protected static final String OPERATORS = "+-*/^";
    protected static final String OPERANDS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected boolean foundError = false;
    protected String input, output;
    protected String[] expression;
    protected String[][] machineOutput;

    protected boolean isOperator(String element) {
        return OPERATORS.contains(element);
    }

    protected boolean isOperand(String element) {
        return OPERANDS.contains(element);
    }

    boolean hasError() {
        return foundError;
    }

    abstract void transform();

    int getStackOperations() {
        return stack.getStackOperations();
    }

    String getOutput() {
        return output;
    }

    String[][] getMachineOutput() {
        return machineOutput;
    }


}
