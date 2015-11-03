package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class ReturnValueAlreadySetException extends MockProcessErrorException {
    private InvocationRecord invocationRecord;
    private Object setAgain;

    public ReturnValueAlreadySetException(InvocationRecord invocationRecord, Object setAgain) {
        this.invocationRecord = invocationRecord;
        this.setAgain = setAgain;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value has been set:");

        reportBuilder.buildNextLevel(() -> {
            reportBuilder.appendLine("method setting return value: ", invocationRecord.getMethodName());
            reportBuilder.appendLine("return value already set:\t", invocationRecord.getReturnValue());
            reportBuilder.appendStackTrace(invocationRecord.getSetReturnStackTrace());
            reportBuilder.appendLine("return value set again:\t", setAgain);
            reportBuilder.appendStackTrace(getStackTrace());
        });
    }
}
