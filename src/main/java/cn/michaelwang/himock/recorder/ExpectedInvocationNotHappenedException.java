package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.List;

public class ExpectedInvocationNotHappenedException extends VerificationFailedException {
    private List<InvocationRecord> functionName;

    public ExpectedInvocationNotHappenedException(List<InvocationRecord> functionName) {
        this.functionName = functionName;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\texpected invocation not happened:");

        for (InvocationRecord invocation : functionName) {
            sb.append("\n");
            sb.append(invocation.getInvocationRecordDetail());
        }

        return sb.toString();
    }
}
