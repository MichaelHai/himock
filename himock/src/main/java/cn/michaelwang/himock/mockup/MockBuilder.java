package cn.michaelwang.himock.mockup;

import java.lang.reflect.Method;

import cn.michaelwang.himock.Invocation;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MockBuilder<T> {
	private Class<T> mockedClass;
	private InvocationListener invocationListener = new NoExpectationListener();

	private class NoExpectationListener implements InvocationListener {
		@Override
		public Object methodCalled(Invocation invocation) throws NoExpectedInvocationException, Throwable {
			throw new NoExpectedInvocationException();
		}

	}

	public MockBuilder(Class<T> clazz) {
		this.mockedClass = clazz;
	}

	@SuppressWarnings("unchecked")
	public T createMock(int id) {
		return (T) Enhancer.create(mockedClass, new MethodInterceptor() {
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				try {
					Invocation invocation = InvocationFactory.getInstance().create(id, method, args);
					return invocationListener.methodCalled(invocation);
				} catch (NoExpectedInvocationException ex) {
					return proxy.invokeSuper(obj, args);
				}
			}
		});
	}

	public void setInvocationListener(InvocationListener listener) {
		this.invocationListener = listener;
	}
}
