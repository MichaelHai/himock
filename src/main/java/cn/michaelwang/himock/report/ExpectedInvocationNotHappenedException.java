package cn.michaelwang.himock.report;

import java.util.List;

public class ExpectedInvocationNotHappenedException extends VerificationFailedException {
    private List<String> functionName;

    public ExpectedInvocationNotHappenedException(List<String> functionName) {
        this.functionName = functionName;
    }

    @Override
    public String getMessage() {
        String message = "\texpected invocation not happened: ";
        for (String name : functionName) {
            message += "\n\t\t" + name;
        }

        return message;
    }
}
