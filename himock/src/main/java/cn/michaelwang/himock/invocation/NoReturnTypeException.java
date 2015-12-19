package cn.michaelwang.himock.invocation;

public class NoReturnTypeException extends Exception {
    private final Invocation invocation;

    public NoReturnTypeException(Invocation invocation) {
        this.invocation = invocation;
    }

    public Invocation getInvocation() {
        return invocation;
    }
}
