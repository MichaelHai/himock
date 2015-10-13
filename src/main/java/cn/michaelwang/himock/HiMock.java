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

    public void expectStart() {
        expectationVerifier.expectStart();
    }

    public void expectEnd() {
        expectationVerifier.expectEnd();
    }

    @FunctionalInterface
    interface Expectation {
        void expect();
    }

    public void expect(Expectation expectation) {
        expectStart();
        expectation.expect();
        expectEnd();
    }

    public <T> void willReturn(T returnValue) {
        expectationVerifier.lastCallReturn(returnValue);
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
                    Object returnValue = expectationVerifier.methodCalled(getInvocationName(method));
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