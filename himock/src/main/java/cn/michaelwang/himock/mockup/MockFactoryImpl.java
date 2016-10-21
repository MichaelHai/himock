package cn.michaelwang.himock.mockup;

import cn.michaelwang.himock.invocation.InvocationHandlerWithId;
import cn.michaelwang.himock.invocation.InvocationListener;
import cn.michaelwang.himock.process.MockFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

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
    public <T> T createMock(Class<T> mockedInterface, InvocationListener invocationListener) {
        InvocationHandler handler = new InvocationHandlerWithId(id, invocationListener);
        id++;

        return (T) Proxy.newProxyInstance(
                mockedInterface.getClassLoader(),
                new Class<?>[]{mockedInterface},
                handler);
    }
}