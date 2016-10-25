package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectReturnOutsideExpectReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = 7995777191893256740L;

	@Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value cannot be set outside expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
