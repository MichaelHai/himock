package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class NoReturnTypeException extends MockProcessErrorException {
    private final InvocationRecord invocation;

    public NoReturnTypeException(InvocationRecord invocation) {
        this.invocation = invocation;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("invocation expected has no return value:");

        reportBuilder.buildNextLevel(() -> {
            reportBuilder.appendLine(invocation.getMethodName());
            reportBuilder.appendStackTrace(invocation.getSetReturnStackTrace());
        });
    }
}
