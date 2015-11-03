package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.Utils;
import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.report.VerificationFailedException;

public class ParametersNotMatchException extends VerificationFailedException {
    private InvocationRecord actuallyInvocation;
    private InvocationRecord expectedInvocation;

    public ParametersNotMatchException(InvocationRecord actuallyInvocation, InvocationRecord expectedInvocation) {
        this.actuallyInvocation = actuallyInvocation;
        this.expectedInvocation = expectedInvocation;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("invocation with unexpected parameters:");

        reportBuilder.buildNextLevel(() -> {
            reportBuilder.appendLine("method called:\t", Utils.removeParenthesesInFunctionName(actuallyInvocation.getMethodName()));
            reportBuilder.appendParameters("parameters expected:", expectedInvocation.getParameters());
            reportBuilder.appendStackTrace(expectedInvocation.getInvocationStackTrace());
            reportBuilder.appendParameters("parameters actually:", actuallyInvocation.getParameters());
            reportBuilder.appendStackTrace(actuallyInvocation.getInvocationStackTrace());
        });
    }
}
