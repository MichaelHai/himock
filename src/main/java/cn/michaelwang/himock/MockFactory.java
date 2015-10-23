package cn.michaelwang.himock;

import cn.michaelwang.himock.recorder.InvocationRecorder;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MockFactory {
    private static MockFactory instance;

    private MockFactory() {
    }

    public static MockFactory getInstance() {
        if (instance == null) {
            instance = new MockFactory();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T createMock(Class<T> mockedInterface, InvocationRecorder invocationRecorder) {
        return (T) Proxy.newProxyInstance(
                mockedInterface.getClassLoader(),
                new Class<?>[]{mockedInterface},
                (proxy, method, args) -> invocationRecorder.methodCalled(getInvocationName(method), method.getReturnType(), method.getParameterTypes(), args));
    }

    private String getInvocationName(Method method) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "()";
    }
}