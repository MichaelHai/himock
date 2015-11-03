package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.List;

public class UnexpectedInvocationHappenedException extends VerificationFailedException {
    private List<InvocationRecord> actuallyInvocation;

    public UnexpectedInvocationHappenedException(List<InvocationRecord> actuallyInvocation) {
        this.actuallyInvocation = actuallyInvocation;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("unexpected invocation happened:");

        reportBuilder.buildNextLevel(() -> actuallyInvocation.forEach(reportBuilder::appendInvocationDetail));
    }
}
