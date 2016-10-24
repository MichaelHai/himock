package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.MockProcessErrorReporter;
import cn.michaelwang.himock.report.ReportBuilder;

import java.util.List;

public class ExceptionTypeIsNotSuitableReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = -6402248681173019841L;
	
	private Invocation invocation;
    private Class<?> toSet;

    public ExceptionTypeIsNotSuitableReporter(Invocation invocation, Throwable toSet) {
        this.invocation = invocation;
        this.toSet = toSet.getClass();
    }

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("exception type is not match:");

        reportBuilder.buildNextLevel((levelBuilder) -> {
            levelBuilder.appendLine("method setting exception: ", invocation.getMethodName());
            levelBuilder.appendLine("exception type expected:");
            levelBuilder.buildNextLevel((exceptionListBuilder) -> {
                List<Class<Throwable>> exceptions = invocation.getExceptionTypes();
                if (!exceptions.isEmpty()) {
                    exceptions.stream().map(Class::getCanonicalName).forEach(exceptionListBuilder::appendLine);
                } else {
                    exceptionListBuilder.appendLine("(none)");
                }
            });
            levelBuilder.appendStackTrace(invocation.getInvocationStackTrace());
            levelBuilder.appendLine("exception type being set:");
            levelBuilder.buildNextLevel((classNameBuilder) -> classNameBuilder.appendLine(toSet.getCanonicalName()));
            levelBuilder.appendStackTrace(getStackTrace());
        });
    }
}
