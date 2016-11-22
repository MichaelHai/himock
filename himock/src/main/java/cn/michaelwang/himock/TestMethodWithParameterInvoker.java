package cn.michaelwang.himock;

import cn.michaelwang.himock.annotations.Mock;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Parameter;

public class TestMethodWithParameterInvoker extends Statement {
    private final FrameworkMethod method;
    private final Object test;

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