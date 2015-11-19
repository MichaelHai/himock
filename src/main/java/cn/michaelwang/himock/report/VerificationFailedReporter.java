package cn.michaelwang.himock.report;

import java.util.List;

public class VerificationFailedReporter extends HiMockReporter {
    private List<VerificationFailedException> exceptions;

    public VerificationFailedReporter(List<VerificationFailedException> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("Verification failed:");

        for (VerificationFailedException ex : exceptions) {
            reportBuilder.buildNextLevel(() -> ex.buildReport(reportBuilder));
        }
    }
}
