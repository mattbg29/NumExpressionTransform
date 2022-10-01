/* *****************************************************************************
 * Name: Matthew Green
 * Date: 21Sep2022
 * Purpose:  Implementation of a stack via a basic array of Strings
 * with dynamic resizing. The method keeps track of the majority of stack
 * operations (in particular, it counts 1 for each method call and 1 for each
 * element in an array that needs to be copied to a new array during array resizing.
 * The total number of operations is reported back to the user via the RunTest class.
 **************************************************************************** */

public class Stack {
    private static final int STARTING_STACK = 10;
    private int maxStack = STARTING_STACK;
    private String[] stack = new String[maxStack];
    private int topOfStack = -1;
    private int stackOperations = 0;

    // The push method checks first if the stack is full and, if so, copies the values
    // to a new array twice the size and then adds the element to the incremented
    // top of the new stack
    void push(String element) {
        stackOperations++;
        this.isFull();
        stack[++topOfStack] = element;
    }

    // If the stack is empty, the pop method will return a string with an Error,
    // rather then throw an error that will stop the program.
    // If it is not empty, isMostlyEmpty() will check if the array needs to be
    // reduced in size, then returns the top of the stack and decrements it,
    // after which it sets the previous top of stack to null to save memory.
    String pop() {
        stackOperations++;
        if (isEmpty()) {
            return " EmptyStackException ";
        } else {
            this.isMostlyEmpty();
            String poppedItem = stack[topOfStack--];
            stack[topOfStack + 1] = null;
            return poppedItem;
        }
    }

    // Calls resizeArray to double the array size if it fills up
    private void isFull() {
        stackOperations++;
        if (topOfStack == (maxStack - 1)) {
            maxStack *= 2;
            resizeArray("double");
        }
    }

    // Calls resizeArray to halve the array size if it becomes less than 25% full
    private void isMostlyEmpty() {
        stackOperations++;
        if (topOfStack <= (maxStack / 4) && maxStack > STARTING_STACK) {
            maxStack /= 2;
            resizeArray("halve");
        }
    }

    // Resizes the array through by copying its values into a new array with the
    // requested new size.
    private void resizeArray(String input) {
        String[] stackNew = new String[maxStack];
        switch (input) {
            case "double":
                for (int i = 0; i < stack.length; i++) {
                    stackOperations++;
                    stackNew[i] = stack[i];
                }
                break;
            case "halve":
                for (int i = 0; i < maxStack; i++) {
                    stackOperations++;
                    stackNew[i] = stack[i];
                }
                break;
        }
        stack = stackNew;
    }

    // Checks if the stack is empty, as defined by topOfStack equaling -1
    boolean isEmpty() {
        stackOperations++;
        return topOfStack == -1;
    }

    // Returns a String error message if the stack is empty, else returns the element
    // at the top of the stack.
    String peek() {
        stackOperations++;
        if (isEmpty()) {
            return " EmptyStackException ";
        } else {
            return stack[topOfStack];
        }
    }

    int getStackOperations() {
        return stackOperations;
    }
}
