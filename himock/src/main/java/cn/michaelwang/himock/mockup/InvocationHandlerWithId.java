package cn.michaelwang.himock.mockup;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import cn.michaelwang.himock.utils.Utils;

public class InvocationHandlerWithId implements InvocationHandler {
	private InvocationListener invocationListener;
	private int id;

	public InvocationHandlerWithId(int id, InvocationListener invocationListener) {
		this.id = id;
		this.invocationListener = invocationListener;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		InvocationImpl invocation = InvocationFactory.getInstance().create(id, method, args);
		try {
			return invocationListener.methodCalled(invocation);
		} catch (NoExpectedInvocationException ex) {
			return Utils.nullValue(invocation.getReturnType());
		}
	}
}