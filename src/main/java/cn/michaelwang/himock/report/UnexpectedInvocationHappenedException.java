package cn.michaelwang.himock.report;

import cn.michaelwang.himock.recorder.InvocationRecord;

import java.util.List;

public class UnexpectedInvocationHappenedException extends VerificationFailedException {
    private List<InvocationRecord> actuallyInvocation;

    public UnexpectedInvocationHappenedException(List<InvocationRecord> actuallyInvocation) {
        this.actuallyInvocation = actuallyInvocation;
    }

    @Override
    public String getMessage() {
        String message = "\tunexpected invocation happened: ";
        for (InvocationRecord invocationRecord : actuallyInvocation) {
            message += "\n\t\t" + invocationRecord.getInvocation();
        }

        return message;
    }
}
