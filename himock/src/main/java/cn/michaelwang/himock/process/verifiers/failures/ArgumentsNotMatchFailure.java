package cn.michaelwang.himock.process.verifiers.failures;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.verifiers.VerificationFailure;
import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.utils.Utils;

public class ArgumentsNotMatchFailure implements VerificationFailure {
    private Invocation actuallyInvocation;
    private Invocation expectedInvocation;

    public ArgumentsNotMatchFailure(Invocation actuallyInvocation, Invocation expectedInvocation) {
        this.actuallyInvocation = actuallyInvocation;
        this.expectedInvocation = expectedInvocation;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("invocation with unexpected arguments:");

        reportBuilder.buildNextLevel((levelBuilder) -> {
            levelBuilder.appendLine("method called:\t", Utils.removeParenthesesInFunctionName(actuallyInvocation.getMethodName()));
            levelBuilder.appendParameters("arguments expected:", expectedInvocation.getArguments());
            levelBuilder.appendStackTrace(expectedInvocation.getInvocationStackTrace());
            levelBuilder.appendParameters("arguments actually:", actuallyInvocation.getArguments());
            levelBuilder.appendStackTrace(actuallyInvocation.getInvocationStackTrace());
        });
    }
}
