package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.Utils;
import cn.michaelwang.himock.report.MockProcessErrorException;

public class ReturnTypeIsNotSuitableException extends MockProcessErrorException {private InvocationRecord invocationRecord;
    private Class<?> setAgain;

    public ReturnTypeIsNotSuitableException(InvocationRecord invocationRecord, Class<?> setAgain) {
        this.invocationRecord = invocationRecord;
        this.setAgain = setAgain;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("\treturn value type is not match:\n");
        sb.append("\t\tmethod setting return value: ");
        sb.append(invocationRecord.getMethodName());
        sb.append("\n");

        sb.append("\t\treturn type expected:\t");
        sb.append(invocationRecord.getReturnType());
        sb.append("\n");
        sb.append(invocationRecord.getInvocationStackTrace());
        sb.append("\n");

        sb.append("\t\treturn type being set:\t");
        sb.append(setAgain);
        sb.append("\n");
        sb.append(Utils.buildStackTraceInformation(getStackTrace(), "\t\t"));

        return sb.toString();
    }
}
