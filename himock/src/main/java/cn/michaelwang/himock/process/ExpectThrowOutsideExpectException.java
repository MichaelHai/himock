package cn.michaelwang.himock.process;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectThrowOutsideExpectException extends MockProcessErrorException {
    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("exception thrown cannot be set outside expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
