package cn.michaelwang.himock.process;

import cn.michaelwang.himock.mockup.InvocationListener;

public interface MockFactory {
    <T> T createMock(Class<T> mockedInterface, InvocationListener invocationListener);
}
