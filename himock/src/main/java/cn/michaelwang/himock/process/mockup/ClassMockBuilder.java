package cn.michaelwang.himock.process.mockup;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.InvocationListener;
import cn.michaelwang.himock.process.exceptions.NoExpectedInvocationException;
import net.sf.cglib.proxy.Enhancer;

public class ClassMockBuilder<T> extends BaseInvocationBuilder<T> {
	public ClassMockBuilder(Class<T> mockedType) {
		super(mockedType);
		this.invocationListener = new NoExpectationListener();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createMock(int id) {
		return (T) Enhancer.create(mockedType, new ClassMockInterceptor(id, invocationListener));
	}

	private class NoExpectationListener implements InvocationListener {
		@Override
		public Object methodCalled(Invocation invocation) throws NoExpectedInvocationException, Throwable {
			throw new NoExpectedInvocationException();
		}
	}
}
