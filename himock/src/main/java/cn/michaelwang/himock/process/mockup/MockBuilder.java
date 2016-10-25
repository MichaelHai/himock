package cn.michaelwang.himock.process.mockup;

import cn.michaelwang.himock.process.InvocationListener;

public interface MockBuilder<T> {
	void setInvocationListener(InvocationListener listener);

	T createMock(int id);
}
