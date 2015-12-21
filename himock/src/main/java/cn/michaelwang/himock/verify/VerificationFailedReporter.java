package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.report.HiMockReporter;
import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.verify.failure.OrderFailure;

import java.util.ArrayList;
import java.util.List;

public class VerificationFailedReporter extends HiMockReporter {
    private List<VerificationFailure> failures;

    public VerificationFailedReporter(VerificationFailure failure) {
        failures = new ArrayList<>();
        failures.add(failure);
    }

    public VerificationFailedReporter(List<VerificationFailure> failures) {
        this.failures = failures;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("Verification failed:");

        failures.forEach(ex -> reportBuilder.buildNextLevel(ex::buildReport));
    }
}
