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
		return create(builder, invocationListener);
	}

	@Override
	public <T> T createMock(Class<T> mockedType, InvocationListener invocationListener,
			Object[] constructorParameters) {
		MockBuilder<T> builder = new ClassMockWithConstructorBuilder<>(mockedType, constructorParameters);

		return create(builder, invocationListener);
	}

	private <T> T create(MockBuilder<T> builder, InvocationListener invocationListener) {
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