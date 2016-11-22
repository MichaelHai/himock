package cn.michaelwang.himock.annotations;

import cn.michaelwang.himock.HiMock;

import java.lang.reflect.Field;

public class DefaultAnnotationHandler implements AnnotationHandler {

	@Override
	public void process(Object testSuit) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = testSuit.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Mock.class)) {
				Class<?> fieldType = (Class<?>) field.getGenericType();
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
				Object mockedObject = HiMock.mock(fieldType);
				field.set(testSuit, mockedObject);
                field.setAccessible(accessible);
            }
		}
	}

}
