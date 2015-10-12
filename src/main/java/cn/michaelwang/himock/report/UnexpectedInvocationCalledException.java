package cn.michaelwang.himock.report;

import java.util.List;

public class UnexpectedInvocationCalledException extends VerificationFailedException {
    private List<String> actuallyInvocation;

    public UnexpectedInvocationCalledException(List<String> actuallyInvocation) {
        this.actuallyInvocation = actuallyInvocation;
    }

    @Override
    public String getMessage() {
        String message = "\tunexpected invocation called: ";
        for (String name : actuallyInvocation) {
            message += "\n\t\t" + name;
        }

        return message;
    }
}
