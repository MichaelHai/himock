package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.report.ReportBuilder;

public class InvalidMatchPositionReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = -7350766919844252282L;

	@Override
	public void buildProcessError(ReportBuilder reportBuilder) {
		reportBuilder.appendLine("matchers cannot be used outside expectations' or verifications' invocation:");
		reportBuilder.appendStackTrace(getStackTrace());
	}
}
