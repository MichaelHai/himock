package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class ReturnTypeIsNotSuitableException extends MockProcessErrorException {
    private InvocationRecord invocationRecord;
    private Class<?> setAgain;

    public ReturnTypeIsNotSuitableException(InvocationRecord invocationRecord, Class<?> setAgain) {
        this.invocationRecord = invocationRecord;
        this.setAgain = setAgain;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value type is not match:");

        reportBuilder.buildNextLevel(() -> {
            reportBuilder.appendLine("method setting return value: ", invocationRecord.getMethodName());
            reportBuilder.appendLine("return type expected:\t", invocationRecord.getReturnType());
            reportBuilder.appendStackTrace(invocationRecord.getInvocationStackTrace());
            reportBuilder.appendLine("return type being set:\t", setAgain);
            reportBuilder.appendStackTrace(getStackTrace());
        });
    }
}
