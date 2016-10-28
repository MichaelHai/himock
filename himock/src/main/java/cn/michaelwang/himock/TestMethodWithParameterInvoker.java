package cn.michaelwang.himock;

import java.lang.reflect.Parameter;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import cn.michaelwang.himock.annotations.Mock;

public class TestMethodWithParameterInvoker extends Statement {
	private FrameworkMethod method;
	private Object test;

	public TestMethodWithParameterInvoker(FrameworkMethod method, Object test) {
		this.method = method;
		this.test = test;
	}

	@Override
	public void evaluate() throws Throwable {
		Parameter[] parameters = method.getMethod().getParameters();

		Object[] params = new Object[parameters.length];
		for (int i = 0; i < params.length; i++) {
			Parameter param = parameters[i];
			if (!param.isAnnotationPresent(Mock.class)) {
				throw new TestSuitNotAnnotatedException();
			}

			params[i] = HiMock.mock(param.getType());
		}

		method.invokeExplosively(test, params);
	}
}