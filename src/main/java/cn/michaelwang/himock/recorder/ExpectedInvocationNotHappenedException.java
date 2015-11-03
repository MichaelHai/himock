package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.List;

public class ExpectedInvocationNotHappenedException extends VerificationFailedException {
    private List<InvocationRecord> missedInvocations;

    public ExpectedInvocationNotHappenedException(List<InvocationRecord> missedInvocations) {
        this.missedInvocations = missedInvocations;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("expected invocation not happened:");

        reportBuilder.buildNextLevel(() -> missedInvocations.forEach(reportBuilder::appendInvocationDetail));
    }
}
