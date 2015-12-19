package cn.michaelwang.himock.invocation;

public class ReturnTypeIsNotSuitableException extends Exception {
    private final Invocation invocation;
    private final Class<?> toSetType;

    public ReturnTypeIsNotSuitableException(Invocation invocation, Class<?> toSetType) {
        this.invocation = invocation;
        this.toSetType = toSetType;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public Class<?> getToSetType() {
        return toSetType;
    }
}
