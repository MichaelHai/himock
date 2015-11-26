package cn.michaelwang.himock.process;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class TimerOutsideExpectException extends MockProcessErrorException {
    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("timer cannot be set outside expectation:");
        reportBuilder.appendStackTrace(getStackTrace());
    }
}
