package cn.michaelwang.himock.invocation;

public class ReturnTypeIsNotSuitableException extends Exception {
    private final InvocationImpl invocation;
    private final Class<?> toSetType;

    public ReturnTypeIsNotSuitableException(InvocationImpl invocation, Class<?> toSetType) {
        this.invocation = invocation;
        this.toSetType = toSetType;
    }

    public InvocationImpl getInvocation() {
        return invocation;
    }

    public Class<?> getToSetType() {
        return toSetType;
    }
}
