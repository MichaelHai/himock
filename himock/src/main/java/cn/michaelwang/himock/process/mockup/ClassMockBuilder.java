package cn.michaelwang.himock.process.mockup;

import java.lang.reflect.Method;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.InvocationListener;
import cn.michaelwang.himock.process.exceptions.NoExpectedInvocationException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ClassMockBuilder<T> extends BaseInvocationBuilder<T> {
	public ClassMockBuilder(Class<T> mockedType) {
		super(mockedType);
		this.invocationListener = new NoExpectationListener();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createMock(int id) {
		return (T) Enhancer.create(mockedType, new MethodInterceptor() {
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

	private class NoExpectationListener implements InvocationListener {
		@Override
		public Object methodCalled(Invocation invocation) throws NoExpectedInvocationException, Throwable {
			throw new NoExpectedInvocationException();
		}
	}
}
