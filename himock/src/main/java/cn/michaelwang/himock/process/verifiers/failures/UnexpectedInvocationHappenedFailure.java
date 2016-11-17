package cn.michaelwang.himock.process.verifiers.failures;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.verifiers.VerificationFailure;
import cn.michaelwang.himock.report.ReportBuilder;

public class UnexpectedInvocationHappenedFailure implements VerificationFailure {
    private final Invocation invocation;

    public UnexpectedInvocationHappenedFailure(Invocation invocation) {
        this.invocation = invocation;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("unexpected invocation happened:");

        reportBuilder.buildNextLevel(
                levelBuilder -> levelBuilder.appendInvocationDetail(
                        invocation.getMethodName(),
                        invocation.getArguments(),
                        invocation.getInvocationStackTrace())
        );
    }
}
