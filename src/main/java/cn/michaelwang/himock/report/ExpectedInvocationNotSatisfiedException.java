package cn.michaelwang.himock.report;

import java.util.List;

public class ExpectedInvocationNotSatisfiedException extends VerificationFailedException {
    private List<String> functionName;

    public ExpectedInvocationNotSatisfiedException(List<String> functionName) {
        this.functionName = functionName;
    }

    @Override
    public String getMessage() {
        String message = "\texpected invocation not satisfied: ";
        for (String name : functionName) {
            message += "\n\t\t" + name;
        }

        return message;
    }
}
