package cn.michaelwang.himock.mockup;

import cn.michaelwang.himock.Invocation;

public class ReturnTypeIsNotSuitableException extends RuntimeException {
	private static final long serialVersionUID = 7369768452877029652L;
	
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
