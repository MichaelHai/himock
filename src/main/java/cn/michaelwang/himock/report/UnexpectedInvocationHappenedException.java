package cn.michaelwang.himock.report;

import java.util.List;

public class UnexpectedInvocationHappenedException extends VerificationFailedException {
    private List<String> actuallyInvocation;

    public UnexpectedInvocationHappenedException(List<String> actuallyInvocation) {
        this.actuallyInvocation = actuallyInvocation;
    }

    @Override
    public String getMessage() {
        String message = "\tunexpected invocation happened: ";
        for (String name : actuallyInvocation) {
            message += "\n\t\t" + name;
        }

        return message;
    }
}
