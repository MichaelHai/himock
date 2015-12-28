package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.Invocation;

public class NoReturnTypeException extends RuntimeException {
    private final Invocation invocation;

    public NoReturnTypeException(Invocation invocation) {
        this.invocation = invocation;
    }

    public Invocation getInvocation() {
        return invocation;
    }
}
