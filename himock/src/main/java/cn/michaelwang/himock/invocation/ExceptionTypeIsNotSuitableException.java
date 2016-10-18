package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.Invocation;

public class ExceptionTypeIsNotSuitableException extends RuntimeException {
	private static final long serialVersionUID = 3688032659954119147L;
	
	private final Invocation invocation;
    private final Throwable toThrow;

    public ExceptionTypeIsNotSuitableException(Invocation invocation, Throwable toThrow) {
        this.invocation = invocation;
        this.toThrow = toThrow;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public Throwable getToThrow() {
        return toThrow;
    }
}
