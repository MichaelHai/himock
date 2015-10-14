package cn.michaelwang.himock.report;

import cn.michaelwang.himock.recorder.InvocationRecord;

import java.util.List;

public class ExpectedInvocationNotHappenedException extends VerificationFailedException {
    private List<InvocationRecord> functionName;

    public ExpectedInvocationNotHappenedException(List<InvocationRecord> functionName) {
        this.functionName = functionName;
    }

    @Override
    public String getMessage() {
        String message = "\texpected invocation not happened: ";
        for (InvocationRecord invocation : functionName) {
            message += "\n\t\t" + invocation.getInvocation();
        }

        return message;
    }
}
