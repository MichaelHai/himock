package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectThrowOutsideExpectReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = 5860489949800238598L;

	@Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("exception thrown cannot be set outside expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
