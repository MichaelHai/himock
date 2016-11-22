package cn.michaelwang.himock.process.mockup;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.michaelwang.himock.process.exceptions.NoExpectedInvocationException;
import cn.michaelwang.himock.utils.Utils;

public class InterfaceMockBuilder<T> extends BaseInvocationBuilder<T> {
	protected InterfaceMockBuilder(Class<T> mockedType, Class<?> testSuit) {
		super(mockedType, testSuit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createMock(int id) {
		return ((T) Proxy.newProxyInstance(
				mockedType.getClassLoader(),
				new Class<?>[] { mockedType },
				(proxy, method, args) -> {
                    int lineNumber = Utils.getLineNumberInTestSuit(testSuit);
                    InvocationImpl invocation = InvocationFactory.getInstance().create(id, method, args, lineNumber);
                    try {
                        return invocationListener.methodCalled(invocation);
                    } catch (NoExpectedInvocationException ex) {
                        return Utils.nullValue(invocation.getReturnType());
                    }
                }));
	}

}
