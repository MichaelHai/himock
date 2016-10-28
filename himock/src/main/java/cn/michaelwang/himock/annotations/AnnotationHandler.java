package cn.michaelwang.himock.annotations;

public interface AnnotationHandler {
	void process(Object testSuit) throws IllegalArgumentException, IllegalAccessException;
}
