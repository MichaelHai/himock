package cn.michaelwang.himock.annotations;

import java.lang.reflect.Field;

import cn.michaelwang.himock.HiMock;

public class DefaultAnnotationHandler implements AnnotationHandler {

	@Override
	public void process(Object testSuit) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = testSuit.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Mock.class)) {
				Class<?> fieldType = (Class<?>) field.getGenericType();
				boolean accessable = field.isAccessible();
				field.setAccessible(true);
				Object mockedObject = HiMock.mock(fieldType);
				field.set(testSuit, mockedObject);
				field.setAccessible(accessable);
			}
		}
	}

}
