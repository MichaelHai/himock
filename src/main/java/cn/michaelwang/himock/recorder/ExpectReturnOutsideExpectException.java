package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class ExpectReturnOutsideExpectException extends MockProcessErrorException {

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value cannot be set outside expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
