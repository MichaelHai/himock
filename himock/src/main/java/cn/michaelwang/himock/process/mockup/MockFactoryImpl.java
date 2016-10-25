package cn.michaelwang.himock.process.mockup;

import cn.michaelwang.himock.process.InvocationListener;
import cn.michaelwang.himock.process.MockFactory;

public class MockFactoryImpl implements MockFactory {
	private static MockFactoryImpl instance;
	private int id = 0;

	private MockFactoryImpl() {
	}

	public static MockFactoryImpl getInstance() {
		if (instance == null) {
			instance = new MockFactoryImpl();
		}

		return instance;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T createMock(Class<T> mocked, InvocationListener invocationListener) {
		MockBuilder<T> builder = getBuilder(mocked);
		builder.setInvocationListener(invocationListener);
		T mock = builder.createMock(id);
		id++;
		
		return mock;
	}

	private <T> MockBuilder<T> getBuilder(Class<T> mocked) {
		if (mocked.isInterface()) {
			return new InterfaceMockBuilder<>(mocked);
		} else {
			return new ClassMockBuilder<>(mocked);
		}
	}
}