package cn.michaelwang.himock;

import cn.michaelwang.himock.report.ExpectedInvocationNotSatisfiedException;
import cn.michaelwang.himock.report.UnexpectedInvocationCalledException;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class HiMock {
    private static final int NORMAL = 0;
    private static final int EXPECT = 1;

    private List<String> expectedInvocations = new ArrayList<>();
    private List<String> actuallyInvocation = new ArrayList<>();
    private int state = NORMAL;

    public <T> T mock(Class<T> mockedInterface) {
        if (!mockedInterface.isInterface()) {
                throw new MockNoninterfaceException(mockedInterface);
        }

        @SuppressWarnings("unchecked")
        T mock1 = (T) Proxy.newProxyInstance(mockedInterface.getClassLoader(),
                new Class<?>[]{mockedInterface},
                (proxy, method, args) -> {
                    switch (state) {
                        case NORMAL:
                            actuallyInvocation.add(getInvocationName(method));
                            break;
                        case EXPECT:
                            expectedInvocations.add(getInvocationName(method));
                            state = NORMAL;
                            break;
                    }
                    return null;
                });


        return mock1;
    }

    public String getInvocationName(Method method) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "()";
    }

    public void verify() {
        actuallyInvocation.forEach((invocation) -> {
            if (expectedInvocations.contains(invocation)) {
                expectedInvocations.remove(invocation);
            } else {
                throw new UnexpectedInvocationCalledException(actuallyInvocation);
            }
        });
        if (!expectedInvocations.isEmpty()) {
            throw new ExpectedInvocationNotSatisfiedException(expectedInvocations);
        }
    }

    public <T> T expect(T mock) {
        state = EXPECT;

        return mock;
    }
}
