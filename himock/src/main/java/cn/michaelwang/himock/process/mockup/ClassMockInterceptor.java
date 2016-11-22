package cn.michaelwang.himock.process.mockup;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.InvocationListener;
import cn.michaelwang.himock.process.exceptions.NoExpectedInvocationException;
import cn.michaelwang.himock.utils.Utils;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassMockInterceptor implements MethodInterceptor {
	private final Class<?> testSuit;
    private final int id;
    private final InvocationListener invocationListener;

	public ClassMockInterceptor(int id, InvocationListener invocationListener, Class<?> testSuit) {
		this.id = id;
		this.invocationListener = invocationListener;
		this.testSuit = testSuit;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		try {
			int lineNumber = Utils.getLineNumberInTestSuit(testSuit);
			Invocation invocation = InvocationFactory.getInstance().create(id, method, args, lineNumber);
			return invocationListener.methodCalled(invocation);
		} catch (NoExpectedInvocationException ex) {
			int modifierMod = method.getModifiers();
			if (Modifier.isAbstract(modifierMod)) {
				return Utils.nullValue(method.getReturnType());
			}
			return proxy.invokeSuper(obj, args);
		}
	}
}
