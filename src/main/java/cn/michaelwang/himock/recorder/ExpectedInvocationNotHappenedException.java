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
        String message = "\texpected invocation not happened:";
        for (InvocationRecord invocation : functionName) {
            message += "\n\t\t" + invocation.getInvocationMessage();
            message += "\n\t\t-> ";
            StackTraceElement[] traces = invocation.getStackTraces();
            StackTraceElement[] filteredTraces = this.simplifyTheStackTraces(traces);
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement trace: filteredTraces) {
                sb.append("\t\t   at ");
                sb.append(trace.toString());
                sb.append("\n");
            }
            sb.delete(0, 5);
            sb.delete(sb.length()-1, sb.length());
            message += sb.toString();
        }

        return message;
    }
}
