package cn.michaelwang.himock.process.mockup;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.InvocationListener;
import cn.michaelwang.himock.process.exceptions.NoExpectedInvocationException;
import cn.michaelwang.himock.utils.Utils;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ClassMockInterceptor implements MethodInterceptor {
	private int id;
	private InvocationListener invocationListener;

	public ClassMockInterceptor(int id, InvocationListener invocationListener) {
		this.id = id;
		this.invocationListener = invocationListener;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		try {
			Invocation invocation = InvocationFactory.getInstance().create(id, method, args);
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
