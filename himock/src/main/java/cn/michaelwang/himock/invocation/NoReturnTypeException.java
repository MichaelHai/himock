package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class NoReturnTypeException extends MockProcessErrorException {
    private Invocation invocation;

    public NoReturnTypeException(Invocation invocation) {
        this.invocation = invocation;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("invocation expected has no return value:");

        reportBuilder.buildNextLevel(() -> {
            reportBuilder.appendLine(invocation.getMethodName());
            reportBuilder.appendStackTrace(getStackTrace());
        });
    }
}
