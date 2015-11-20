package cn.michaelwang.himock.process;

import cn.michaelwang.himock.invocation.InvocationListener;

public interface MockFactory {
    <T> T createMock(Class<T> mockedInterface, InvocationListener invocationListener);
}
