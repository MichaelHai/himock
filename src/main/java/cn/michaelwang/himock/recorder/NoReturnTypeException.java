package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;

public class NoReturnTypeException extends MockProcessErrorException {
    private final String method;

    public NoReturnTypeException(String method) {
        this.method = method;
    }

    @Override
    public String getMessage() {
        return "\tinvocation expected has no return value: " + method;
    }
}
