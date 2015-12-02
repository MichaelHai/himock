package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.report.HiMockReporter;
import cn.michaelwang.himock.report.ReportBuilder;

import java.util.List;

public class VerificationFailedReporter extends HiMockReporter {
    private List<VerificationFailure> failures;

    public VerificationFailedReporter(List<VerificationFailure> failures) {
        this.failures = failures;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("Verification failed:");

        for (VerificationFailure ex : failures) {
            reportBuilder.buildNextLevel((levelBuilder) -> ex.buildReport(levelBuilder));
        }
    }
}
