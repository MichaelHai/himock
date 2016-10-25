package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectExceptionBeforeInvocationReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = -5107497216429356912L;

	@Override
    protected void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("exception cannot be set before invocation expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
