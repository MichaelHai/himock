package cn.michaelwang.himock.process.verifiers.failures;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.verifiers.VerificationFailure;
import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectedTimesNotSatisfiedFailure implements VerificationFailure {
	private Invocation invocation;
	private String expect;
	private int actuallyCount;

	public ExpectedTimesNotSatisfiedFailure(Invocation invocation, String expect, int actuallyCount) {
		this.invocation = invocation;
		this.expect = expect;
		this.actuallyCount = actuallyCount;
	}

	@Override
	public void buildReport(ReportBuilder reportBuilder) {
		reportBuilder.appendLine("expected invocation times not satisfied:");

		reportBuilder.buildNextLevel(
				(levelBuilder) -> {
					levelBuilder.appendLine("expected: " + expect);
					levelBuilder.appendLine("actually: " + actuallyCount + " time" + (actuallyCount > 1 ? "s" : ""));
					levelBuilder.appendLine("expected invocation:");
					levelBuilder.appendInvocationDetail(invocation.getMethodName(), invocation.getArguments(),
							invocation.getInvocationStackTrace());
				});
	}

}
