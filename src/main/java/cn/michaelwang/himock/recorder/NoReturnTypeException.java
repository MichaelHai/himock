package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;

public class NoReturnTypeException extends MockProcessErrorException {
    private final InvocationRecord invocation;

    public NoReturnTypeException(InvocationRecord invocation) {
        this.invocation = invocation;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tinvocation expected has no return value:\n");
        sb.append("\t\t");
        sb.append(invocation.getMethodName());
        sb.append("\n");

        sb.append(invocation.getSetReturnStackTrace());
        return sb.toString();
    }
}
