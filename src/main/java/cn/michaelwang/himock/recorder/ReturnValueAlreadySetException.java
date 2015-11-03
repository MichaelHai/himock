package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.Utils;
import cn.michaelwang.himock.report.MockProcessErrorException;

public class ReturnValueAlreadySetException extends MockProcessErrorException {
    private InvocationRecord invocationRecord;
    private Object setAgain;

    public ReturnValueAlreadySetException(InvocationRecord invocationRecord, Object setAgain) {
        this.invocationRecord = invocationRecord;
        this.setAgain = setAgain;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("\treturn value has been set:\n");
        sb.append("\t\tmethod setting return value: ");
        sb.append(invocationRecord.getMethodName());
        sb.append("\n");

        sb.append("\t\treturn value already set:\t");
        sb.append(invocationRecord.getReturnValue());
        sb.append("\n");
        sb.append(invocationRecord.getSetReturnStackTrace());
        sb.append("\n");

        sb.append("\t\treturn value set again:\t");
        sb.append(setAgain);
        sb.append("\n");
        sb.append(Utils.buildStackTraceInformation(getStackTrace(), "\t\t"));

        return sb.toString();
    }
}
