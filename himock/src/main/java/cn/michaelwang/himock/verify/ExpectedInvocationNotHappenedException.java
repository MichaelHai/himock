package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.List;

public class ExpectedInvocationNotHappenedException extends VerificationFailedException {
    private final List<Invocation> missedInvocations;

    public ExpectedInvocationNotHappenedException(List<Invocation> missedInvocations) {
        this.missedInvocations = missedInvocations;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("expected invocation not happened:");

        reportBuilder.buildNextLevel(() -> missedInvocations.forEach(
                invocation -> reportBuilder.appendInvocationDetail(
                        invocation.getMethodName(),
                        invocation.getParameters(),
                        invocation.getInvocationStackTrace()))
        );
    }
}
