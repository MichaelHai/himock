package cn.michaelwang.himock.process.verifiers.failures;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.verifiers.VerificationFailure;
import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectedInvocationNotHappenedFailure implements VerificationFailure {
    private final Invocation missedInvocation;

    public ExpectedInvocationNotHappenedFailure(Invocation missedInvocations) {
        this.missedInvocation = missedInvocations;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("expected invocation not happened:");

        reportBuilder.buildNextLevel(
                (levelBuilder) -> levelBuilder.appendInvocationDetail(
                        missedInvocation.getMethodName(),
                        missedInvocation.getArguments(),
                        missedInvocation.getInvocationStackTrace())
        );
    }
}
