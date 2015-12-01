package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class ReturnTypeIsNotSuitableException extends MockProcessErrorException {
    private Invocation invocationRecord;
    private Class<?> setAgain;

    public ReturnTypeIsNotSuitableException(Invocation invocationRecord, Class<?> setAgain) {
        this.invocationRecord = invocationRecord;
        this.setAgain = setAgain;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value type is not match:");

        reportBuilder.buildNextLevel(() -> {
            reportBuilder.appendLine("method setting return value: ", invocationRecord.getMethodName());
            reportBuilder.appendLine("return type expected:\t", invocationRecord.getReturnType().getCanonicalName());
            reportBuilder.appendStackTrace(invocationRecord.getInvocationStackTrace());
            reportBuilder.appendLine("return type being set:\t", setAgain.getCanonicalName());
            reportBuilder.appendStackTrace(getStackTrace());
        });
    }
}
