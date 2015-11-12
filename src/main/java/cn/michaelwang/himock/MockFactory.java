package cn.michaelwang.himock;

import cn.michaelwang.himock.recorder.InvocationRecorder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MockFactory {
    private static MockFactory instance;
    private int id = 0;

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
        InvocationHandler handler = new InvocationHandlerWithId(id, invocationRecorder);
        id++;

        return (T) Proxy.newProxyInstance(
                mockedInterface.getClassLoader(),
                new Class<?>[]{mockedInterface},
                handler);
    }

    private class InvocationHandlerWithId implements InvocationHandler {
        private InvocationRecorder invocationRecorder;
        private int id;

        InvocationHandlerWithId(int id, InvocationRecorder invocationRecorder) {
            this.id = id;
            this.invocationRecorder = invocationRecorder;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return invocationRecorder.methodCalled(id, getInvocationName(method), method.getReturnType(), method.getParameterTypes(), args);
        }
    }

    private String getInvocationName(Method method) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "()";
    }
}