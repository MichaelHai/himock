package cn.michaelwang.himock.process;

import java.util.List;

import cn.michaelwang.himock.Invocation;

public class InvocationWithMatchers implements Invocation {

	private Invocation invocation;
	private Matchers matchers;

	public InvocationWithMatchers(Invocation original, Matchers matchers) {
		this.invocation = original;
		this.matchers = matchers;
	}

	@Override
	public int getObjectId() {
		return invocation.getObjectId();
	}

	@Override
	public String getMethodName() {
		return invocation.getMethodName();
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return invocation.getParameterTypes();
	}

	@Override
	public Object[] getArguments() {
		return matchers.getMatchers().toArray();
	}

	@Override
	public Class<?> getReturnType() {
		return invocation.getReturnType();
	}

	@Override
	public List<Class<Throwable>> getExceptionTypes() {
		return invocation.getExceptionTypes();
	}

	@Override
	public StackTraceElement[] getInvocationStackTrace() {
		return invocation.getInvocationStackTrace();
	}

	@Override
	public int getLineNumber() {
		return invocation.getLineNumber();
	}

	@Override
	public boolean sameMethod(Invocation invocation) {
		return invocation.sameMethod(invocation);
	}
}
