package cn.michaelwang.himock.process.verifiers;

import cn.michaelwang.himock.report.HiMockReporter;
import cn.michaelwang.himock.report.ReportBuilder;

import java.util.ArrayList;
import java.util.List;

public class VerificationFailedReporter extends HiMockReporter {
	private static final long serialVersionUID = -3120362566885275449L;

    private final List<VerificationFailure> failures;

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
