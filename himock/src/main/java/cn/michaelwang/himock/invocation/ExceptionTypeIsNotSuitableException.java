package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

import java.util.List;

public class ExceptionTypeIsNotSuitableException extends MockProcessErrorException {
    private Invocation invocationRecord;
    private Class<?> toSet;

    public ExceptionTypeIsNotSuitableException(Invocation invocation, Throwable toSet) {
        this.invocationRecord = invocation;
        this.toSet = toSet.getClass();
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("exception type is not match:");

        reportBuilder.buildNextLevel(() -> {
            reportBuilder.appendLine("method setting exception: ", invocationRecord.getMethodName());
            reportBuilder.appendLine("exception type expected:");
            reportBuilder.buildNextLevel(() -> {
                List<Class<Throwable>> exceptions = invocationRecord.getExceptionTypes();
                exceptions.stream().map(exception -> exception.getCanonicalName()).forEach(reportBuilder::appendLine);
            });
            reportBuilder.appendStackTrace(invocationRecord.getInvocationStackTrace());
            reportBuilder.appendLine("exception type being set:");
            reportBuilder.buildNextLevel(() -> reportBuilder.appendLine(toSet.getCanonicalName()));
            reportBuilder.appendStackTrace(getStackTrace());
        });
    }
}
