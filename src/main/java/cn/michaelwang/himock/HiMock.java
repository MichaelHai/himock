package cn.michaelwang.himock;

import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HiMock {
    public <T> T mock(Class<T> mockedInterface) {
        Objenesis objenesis = new ObjenesisStd();
        T mock = objenesis.newInstance(proxyClass(mockedInterface));

        @SuppressWarnings("unchecked")
        T mock1 = (T) Proxy.newProxyInstance(mockedInterface.getClassLoader(),
                new Class<?>[]{mockedInterface},
                (proxy, method, args) -> {
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
    }

    public <T> T expect(T mock) {
        return mock;
    }
}
