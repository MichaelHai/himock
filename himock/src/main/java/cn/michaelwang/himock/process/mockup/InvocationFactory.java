package cn.michaelwang.himock.process.mockup;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationFactory {
	private static InvocationFactory instance;

	private InvocationFactory() {
	};

	public static InvocationFactory getInstance() {
		if (instance == null) {
			instance = new InvocationFactory();
		}

		return instance;
	}

	public InvocationImpl create(int id, Method method, Object[] args) {
		List<Class<Throwable>> typeChecked = getExceptions(method);
		InvocationImpl invocation = new InvocationImpl(id, getInvocationName(method), method.getParameterTypes(), args,
				method.getReturnType(), typeChecked);
		return invocation;
	}

	@SuppressWarnings("unchecked")
	private List<Class<Throwable>> getExceptions(Method method) {
		Class<?>[] exceptions = method.getExceptionTypes();
		List<Class<Throwable>> typeChecked = new ArrayList<>();
		for (Class<?> exception : exceptions) {
			if (Throwable.class.isAssignableFrom(exception)) {
				typeChecked.add((Class<Throwable>) exception);
			}
		}
		return typeChecked;
	}

	private String getInvocationName(Method method) {
		return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "()";
	}
}
