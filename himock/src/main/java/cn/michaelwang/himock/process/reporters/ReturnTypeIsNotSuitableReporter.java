package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.process.MockProcessErrorReporter;
import cn.michaelwang.himock.report.ReportBuilder;

public class ReturnTypeIsNotSuitableReporter extends MockProcessErrorReporter {
    private Invocation invocationRecord;
    private Class<?> setAgain;

    public ReturnTypeIsNotSuitableReporter(Invocation invocationRecord, Class<?> setAgain) {
        this.invocationRecord = invocationRecord;
        this.setAgain = setAgain;
    }

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value type is not match:");

        reportBuilder.buildNextLevel((levelBuilder) -> {
            levelBuilder.appendLine("method setting return value: ", invocationRecord.getMethodName());
            levelBuilder.appendLine("return type expected:\t", invocationRecord.getReturnType().getCanonicalName());
            levelBuilder.appendStackTrace(invocationRecord.getInvocationStackTrace());
            levelBuilder.appendLine("return type being set:\t", setAgain.getCanonicalName());
            levelBuilder.appendStackTrace(getStackTrace());
        });
    }
}
