package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.utils.Utils;

public class ParametersNotMatchException extends VerificationFailedException {
    private Invocation actuallyInvocation;
    private Invocation expectedInvocation;

    public ParametersNotMatchException(Invocation actuallyInvocation, Invocation expectedInvocation) {
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
