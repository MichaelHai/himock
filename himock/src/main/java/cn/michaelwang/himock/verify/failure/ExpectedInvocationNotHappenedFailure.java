package cn.michaelwang.himock.verify.failure;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.verify.VerificationFailure;

import java.util.List;

public class ExpectedInvocationNotHappenedFailure implements VerificationFailure {
    private final List<Invocation> missedInvocations;

    public ExpectedInvocationNotHappenedFailure(List<Invocation> missedInvocations) {
        this.missedInvocations = missedInvocations;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("expected invocation not happened:");

        reportBuilder.buildNextLevel(
                (levelBuilder) -> missedInvocations.forEach(
                        invocation -> levelBuilder.appendInvocationDetail(
                                invocation.getMethodName(),
                                invocation.getArguments(),
                                invocation.getInvocationStackTrace()))
        );
    }
}
