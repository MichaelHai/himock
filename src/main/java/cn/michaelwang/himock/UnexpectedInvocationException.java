package cn.michaelwang.himock;

import java.util.List;

public class UnexpectedInvocationException extends MockExpectationFailedException {
    private List<String> actuallyInvocation;

    public UnexpectedInvocationException(List<String> actuallyInvocation) {
        this.actuallyInvocation = actuallyInvocation;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage() +
                "\tunexpected invocation detected: ";
        for (String name : actuallyInvocation) {
            message += "\n\t\t" + name;
        }

        return message;
    }
}
