package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.Utils;
import cn.michaelwang.himock.report.MockProcessErrorException;

public class ExpectReturnOutsideExpectException extends MockProcessErrorException {
    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\treturn value cannot be set outside expectation:\n");
        sb.append(Utils.buildStackTraceInformation(getStackTrace(), "\t"));

        return sb.toString();
    }
}
