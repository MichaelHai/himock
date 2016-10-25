package cn.michaelwang.himock.process.mockup;

import cn.michaelwang.himock.process.InvocationListener;

public abstract class BaseInvocationBuilder<T> implements MockBuilder<T> {
	protected InvocationListener invocationListener;
	protected Class<T> mockedType;

	protected BaseInvocationBuilder(Class<T> mockedType) {
		this.mockedType = mockedType;
	}

	@Override
	public void setInvocationListener(InvocationListener listener) {
		this.invocationListener = listener;
	}
}
