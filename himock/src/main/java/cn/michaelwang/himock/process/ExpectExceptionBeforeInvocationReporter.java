package cn.michaelwang.himock.process;

import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectExceptionBeforeInvocationReporter extends MockProcessErrorReporter {

    @Override
    protected void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("exception cannot be set before invocation expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
