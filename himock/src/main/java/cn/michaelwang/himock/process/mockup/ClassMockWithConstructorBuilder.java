package cn.michaelwang.himock.process.mockup;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.sf.cglib.proxy.Enhancer;

public class ClassMockWithConstructorBuilder<T> extends BaseInvocationBuilder<T> {
	private Object[] constructorParameters;

	protected ClassMockWithConstructorBuilder(Class<T> mockedType, Object[] constructorParameters) {
		super(mockedType);
		this.constructorParameters = constructorParameters;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createMock(int id) {
		Enhancer e = new Enhancer();
		e.setSuperclass(mockedType);
		e.setCallback(new ClassMockInterceptor(id, invocationListener));
		Class<?>[] types = new Class<?>[constructorParameters.length];
		Arrays.stream(constructorParameters)
				.map(Object::getClass)
				.collect(Collectors.toList())
				.toArray(types);
		return (T) e.create(types, constructorParameters);
	}

}
