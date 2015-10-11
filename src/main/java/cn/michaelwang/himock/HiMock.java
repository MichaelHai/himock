package cn.michaelwang.himock;

import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

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
        Objenesis objenesis = new ObjenesisStd();
        T mock = objenesis.newInstance(proxyClass(mockedInterface));

        @SuppressWarnings("unchecked")
        T mock1 = (T) Proxy.newProxyInstance(mockedInterface.getClassLoader(),
                new Class<?>[]{mockedInterface},
                (proxy, method, args) -> {
                    switch (state) {
                        case NORMAL:
                            actuallyInvocation.add(method.getName());
                            break;
                        case EXPECT:
                            expectedInvocations.add(method.getName());
                            state = NORMAL;
                            break;
                    }

                    return null;
                });


        return mock1;
    }

    private <T> Class<T> proxyClass(Class<T> mockedInterface) {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(new Class<?>[]{mockedInterface});
            enhancer.setCallbackTypes(new Class[]{net.sf.cglib.proxy.InvocationHandler.class});

            //noinspection unchecked
            return (Class<T>) enhancer.createClass();
        } catch (IllegalStateException ex) {
            if (ex.getMessage().contains("is not an interface")) {
                throw new CannotMockClassException(mockedInterface);
            }
        }

        return null;
    }

    public void verify() {
        actuallyInvocation.forEach((invocation) -> expectedInvocations.remove(invocation));
        if (!expectedInvocations.isEmpty()) {
            throw new MockExpectationFailedException();
        }
    }

    public <T> T expect(T mock) {
        state = EXPECT;

        return mock;
    }
}
