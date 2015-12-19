package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.process.MockProcessErrorReporter;
import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectReturnBeforeInvocationReporter extends MockProcessErrorReporter {

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value cannot be set before invocation expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
