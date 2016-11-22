package cn.michaelwang.himock.process.mockup;

import cn.michaelwang.himock.process.InvocationListener;
import cn.michaelwang.himock.process.MockFactory;

public class MockFactoryImpl implements MockFactory {
	private final Class<?> testSuit;
	private int id = 0;

	public MockFactoryImpl(Class<?> testSuit) {
		this.testSuit = testSuit;
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
		MockBuilder<T> builder = new ClassMockWithConstructorBuilder<>(mockedType, constructorParameters, testSuit);

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
			return new InterfaceMockBuilder<>(mocked, testSuit);
		} else {
			return new ClassMockBuilder<>(mocked, testSuit);
		}
	}
}