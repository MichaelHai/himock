package cn.michaelwang.himock.invocation;

public class NoReturnTypeException extends Exception {
    private final InvocationImpl invocation;

    public NoReturnTypeException(InvocationImpl invocation) {
        this.invocation = invocation;
    }

    public InvocationImpl getInvocation() {
        return invocation;
    }
}
