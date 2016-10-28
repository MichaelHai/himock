package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectAnswerOutsideExpectReporter extends MockProcessErrorReporter {

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("answers cannot be set outside expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
