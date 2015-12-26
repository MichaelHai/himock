package cn.michaelwang.himock.invocation;

public class ExceptionTypeIsNotSuitableException extends RuntimeException {
    private final InvocationImpl invocation;
    private final Throwable toThrow;

    public ExceptionTypeIsNotSuitableException(InvocationImpl invocation, Throwable toThrow) {
        this.invocation = invocation;
        this.toThrow = toThrow;
    }

    public InvocationImpl getInvocation() {
        return invocation;
    }

    public Throwable getToThrow() {
        return toThrow;
    }
}
