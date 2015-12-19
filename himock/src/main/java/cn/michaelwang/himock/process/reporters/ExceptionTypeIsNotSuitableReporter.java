package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.process.MockProcessErrorReporter;
import cn.michaelwang.himock.report.ReportBuilder;

import java.util.List;

public class ExceptionTypeIsNotSuitableReporter extends MockProcessErrorReporter {
    private Invocation invocationRecord;
    private Class<?> toSet;

    public ExceptionTypeIsNotSuitableReporter(Invocation invocation, Throwable toSet) {
        this.invocationRecord = invocation;
        this.toSet = toSet.getClass();
    }

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("exception type is not match:");

        reportBuilder.buildNextLevel((levelBuilder) -> {
            levelBuilder.appendLine("method setting exception: ", invocationRecord.getMethodName());
            levelBuilder.appendLine("exception type expected:");
            levelBuilder.buildNextLevel((exceptionListBuilder) -> {
                List<Class<Throwable>> exceptions = invocationRecord.getExceptionTypes();
                if (!exceptions.isEmpty()) {
                    exceptions.stream().map(Class::getCanonicalName).forEach(exceptionListBuilder::appendLine);
                } else {
                    exceptionListBuilder.appendLine("(none)");
                }
            });
            levelBuilder.appendStackTrace(invocationRecord.getInvocationStackTrace());
            levelBuilder.appendLine("exception type being set:");
            levelBuilder.buildNextLevel((classNameBuilder) -> classNameBuilder.appendLine(toSet.getCanonicalName()));
            levelBuilder.appendStackTrace(getStackTrace());
        });
    }
}
