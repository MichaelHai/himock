package cn.michaelwang.himock.process.mockup;

import cn.michaelwang.himock.process.InvocationListener;

public abstract class BaseInvocationBuilder<T> implements MockBuilder<T> {
	protected final Class<?> testSuit;
	protected final Class<T> mockedType;
	protected InvocationListener invocationListener;

	protected BaseInvocationBuilder(Class<T> mockedType, Class<?> testSuit) {
		this.mockedType = mockedType;
		this.testSuit = testSuit;
	}

	@Override
	public void setInvocationListener(InvocationListener listener) {
		this.invocationListener = listener;
	}
}
