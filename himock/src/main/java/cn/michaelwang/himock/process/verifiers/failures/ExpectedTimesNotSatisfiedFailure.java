package cn.michaelwang.himock.process.verifiers.failures;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.verifiers.VerificationFailure;
import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectedTimesNotSatisfiedFailure implements VerificationFailure {
	private Invocation invocation;
	private int expectedCount;
	private int actuallyCount;

	public ExpectedTimesNotSatisfiedFailure(Invocation invocation, int expectedCount, int actuallyCount) {
		this.invocation = invocation;
		this.expectedCount = expectedCount;
		this.actuallyCount = actuallyCount;

	}

	@Override
	public void buildReport(ReportBuilder reportBuilder) {
		reportBuilder.appendLine("expected invocation times not satisfied:");

		reportBuilder.buildNextLevel(
				(levelBuilder) -> {
					levelBuilder.appendLine("expected: " + expectedCount + " time" + (expectedCount > 1 ? "s" : ""));
					levelBuilder.appendLine("actually: " + actuallyCount + " time" + (actuallyCount > 1 ? "s" : ""));
					levelBuilder.appendLine("expected invocation:");
					levelBuilder.appendInvocationDetail(invocation.getMethodName(), invocation.getArguments(),
							invocation.getInvocationStackTrace());
				});
	}

}
