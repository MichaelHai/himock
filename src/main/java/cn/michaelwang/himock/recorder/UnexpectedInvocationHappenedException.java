package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.List;

public class UnexpectedInvocationHappenedException extends VerificationFailedException {
    private List<InvocationRecord> actuallyInvocation;

    public UnexpectedInvocationHappenedException(List<InvocationRecord> actuallyInvocation) {
        this.actuallyInvocation = actuallyInvocation;
    }

    @Override
    public String getMessage() {
        String message = "\tunexpected invocation happened:";
        for (InvocationRecord invocationRecord : actuallyInvocation) {
            message += invocationRecord.getInvocationRecordDetail();
        }

        return message;
    }
}
