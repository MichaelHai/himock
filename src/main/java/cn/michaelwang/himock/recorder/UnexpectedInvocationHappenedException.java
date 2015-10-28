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
        StringBuilder sb = new StringBuilder();
        sb.append("\tunexpected invocation happened:");

        for (InvocationRecord invocationRecord : actuallyInvocation) {
            sb.append("\n");
            sb.append(invocationRecord.getInvocationRecordDetail());
        }

        return sb.toString();
    }
}
