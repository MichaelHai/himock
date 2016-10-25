package cn.michaelwang.himock.process;

public interface MockFactory {
    <T> T createMock(Class<T> mockedInterface, InvocationListener invocationListener);
}
