package cn.michaelwang.himock;

import java.util.List;

public class UnexpectedInvocationException extends MockExpectationFailedException{
    private List<String> functionName;

    public UnexpectedInvocationException(List<String> functionName) {
        this.functionName = functionName;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage() +
                "\tunexpected invocation detected: ";
        for (String name : functionName) {
            message += "\n\t\t" + name;
        }

        return message;
    }
}
