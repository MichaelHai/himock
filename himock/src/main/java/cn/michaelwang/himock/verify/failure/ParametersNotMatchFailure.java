package cn.michaelwang.himock.verify.failure;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.utils.Utils;
import cn.michaelwang.himock.verify.Verification;
import cn.michaelwang.himock.verify.VerificationFailure;

public class ParametersNotMatchFailure implements VerificationFailure {
    private Invocation actuallyInvocation;
    private Invocation expectedInvocation;

    public ParametersNotMatchFailure(Invocation actuallyInvocation, Invocation expectedInvocation) {
        this.actuallyInvocation = actuallyInvocation;
        this.expectedInvocation = expectedInvocation;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("invocation with unexpected parameters:");

        reportBuilder.buildNextLevel((levelBuilder) -> {
            levelBuilder.appendLine("method called:\t", Utils.removeParenthesesInFunctionName(actuallyInvocation.getMethodName()));
            levelBuilder.appendParameters("parameters expected:", expectedInvocation.getParameters());
            levelBuilder.appendStackTrace(expectedInvocation.getInvocationStackTrace());
            levelBuilder.appendParameters("parameters actually:", actuallyInvocation.getParameters());
            levelBuilder.appendStackTrace(actuallyInvocation.getInvocationStackTrace());
        });
    }
}
