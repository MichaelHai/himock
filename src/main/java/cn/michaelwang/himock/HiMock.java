package cn.michaelwang.himock;

import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class HiMock {
    public <T> T mock(Class<T> mockedInterface) {
        Objenesis objenesis = new ObjenesisStd();
        return objenesis.newInstance(proxyClass(mockedInterface));
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
}
