package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.process.MockProcessErrorReporter;
import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectReturnOutsideExpectReporter extends MockProcessErrorReporter {

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value cannot be set outside expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
