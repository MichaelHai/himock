package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.Invocation;

public class NoReturnTypeException extends RuntimeException {
	private static final long serialVersionUID = 5863779300505156683L;
	
	private final Invocation invocation;

    public NoReturnTypeException(Invocation invocation) {
        this.invocation = invocation;
    }

    public Invocation getInvocation() {
        return invocation;
    }
}
