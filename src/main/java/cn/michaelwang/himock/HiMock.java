package cn.michaelwang.himock;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HiMock {
    private final ExpectationVerifier expectationVerifier = new ExpectationVerifier();

    public <T> T mock(Class<T> mockedInterface) {
        if (!mockedInterface.isInterface()) {
            throw new MockNoninterfaceException(mockedInterface);
        }

        return createMock(mockedInterface);
    }

    public <T> T expect(T mock) {
        expectationVerifier.beginExpect();

        return mock;
    }

    public void verify() {
        expectationVerifier.verify();
    }

    @SuppressWarnings("unchecked")
    private <T> T createMock(Class<T> mockedInterface) {
        return (T) Proxy.newProxyInstance(
                mockedInterface.getClassLoader(),
                new Class<?>[]{mockedInterface},
                (proxy, method, args) -> {
                    expectationVerifier.methodCalled(getInvocationName(method));
                    return null;
                });
    }

    private String getInvocationName(Method method) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "()";
    }
}
