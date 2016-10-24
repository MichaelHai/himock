package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.process.MockProcessErrorReporter;
import cn.michaelwang.himock.report.ReportBuilder;

public class TimerOutsideExpectReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = -9201122176981793613L;

	@Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("timer cannot be set outside expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
