Name:  Matthew Green

IDE:  I am using IntelliJ IDEA Community Edition 2021.1.2 for this lab

Java version:  I am using Java Version 8 Update 281

Program overview:
I chose lab choice number 1, but added quite a few enhancements which I'll describe here.

When this program is run, it will ask the user for an input file, which should contain
one or more expressions all of the same format.  The allowable input expression formats
are: infix, postfix, or prefix.  The user will then be asked which expressions to
transform the input expressions to.  The user may pick one or more transformations,
which will take place sequentially.  For instance, if the user chooses an input file with
postfix expressions and asks they they be converted to machine instructions, infix expressions,
and prefix expressions, the program will convert first from postfix to machine, then from
machine to infix, and finally from infix to prefix.

Please note: in order to run the transformation required of choice 1 exclusively, use
postfix_input.txt as the input, declare that the input as postfix, and request that
the transformation be to machine instructions.

While this program runs every aforementioned transformation, it has the capability to do
only 5 direct transformations: infix to prefix, infix to postfix, postfix to machine instructions,
machine instructions to infix, and prefix to infix.  Thus, any other combination will require at
least one intermediate step, which will be included in the output.  For instance, if the user asks
to transform prefix to postfix, the program will convert prefix to infix, then convert infix
to postfix.  If at anytime one transformation fails, further transformations will be cancelled.

The output file will include the requested input and output expressions (input is omitted if
it is machine instructions, to save space), a message on whether the conversion succeeded, and, if
it did, information on how long the transformation process took and how many stack operations
were required.  Note that neither the length of time nor number of stack operations is precise,
but it is reasonably close and offers useful information for comparing different tests and
expressions.

The input file should contain expressions only, all of the same format.
Valid operations: + - * / ^
Valid operands: capital letters
Valid delimiters: ( )
