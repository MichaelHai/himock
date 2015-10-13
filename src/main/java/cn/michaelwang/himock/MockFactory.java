package cn.michaelwang.himock;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MockFactory {
    private static MockFactory instance;

    public static MockFactory getInstance() {
        if (instance == null) {
            instance = new MockFactory();
        }

        return instance;
    }

    private MockFactory() {}

    @SuppressWarnings("unchecked")
    public <T> T createMock(Class<T> mockedInterface, InvocationRecorder invocationRecorder) {
        return (T) Proxy.newProxyInstance(
                mockedInterface.getClassLoader(),
                new Class<?>[]{mockedInterface},
                (proxy, method, args) -> {
                    Object returnValue = invocationRecorder.methodCalled(getInvocationName(method));
                    if (returnValue == null) {
                        return nullValue(method.getReturnType());
                    } else {
                        return returnValue;
                    }
                });
    }

    private Object nullValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType.getName().equals("boolean")) {
                return false;
            }
            return 0;
        }

        return null;
    }

    private String getInvocationName(Method method) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "()";
    }
}