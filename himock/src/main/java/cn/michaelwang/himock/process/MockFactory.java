package cn.michaelwang.himock.process;

public interface MockFactory {
	<T> T createMock(Class<T> mockedInterface, InvocationListener invocationListener);

	<T> T createMock(Class<T> mockedType, InvocationListener invocationListener, Object[] constructorParameters);
}
