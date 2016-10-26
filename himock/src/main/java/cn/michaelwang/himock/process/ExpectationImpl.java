package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.process.exceptions.ExceptionTypeIsNotSuitableException;
import cn.michaelwang.himock.process.exceptions.NoReturnTypeException;
import cn.michaelwang.himock.process.exceptions.ReturnTypeIsNotSuitableException;
import cn.michaelwang.himock.utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ExpectationImpl implements Expectation {
	private Queue<Answer> returnValue = new LinkedList<>();
	private Answer lastAnswer;

	private Invocation invocation;
	private Matchers matchers;

	public ExpectationImpl(Invocation invocation, List<Matcher<?>> matchers) {
		this.invocation = invocation;
		this.matchers = new Matchers(matchers);
	}

	@Override
	public boolean match(Invocation invocation) {
		return this.invocation.sameMethod(invocation)
				&& matchers.match(invocation.getArguments());
	}

	@Override
	public void addReturnValue(Object toSet, Class<?> toSetType)
			throws NoReturnTypeException, ReturnTypeIsNotSuitableException {
		Class<?> returnType = invocation.getReturnType();
		if (isSuitableType(toSet.getClass(), returnType)) {
			lastAnswer = new ReturnAnswer(toSet);
			returnValue.offer(lastAnswer);
		} else if (returnType == Void.TYPE) {
			throw new NoReturnTypeException(invocation);
		} else {
			throw new ReturnTypeIsNotSuitableException(invocation, toSetType);
		}
	}

	@Override
	public void addException(Throwable toThrow) {
		List<Class<Throwable>> exceptionTypes = invocation.getExceptionTypes();
		if (toThrow instanceof RuntimeException
				|| exceptionTypes
						.stream()
						.anyMatch(exceptionType -> exceptionType.isAssignableFrom(toThrow.getClass()))) {
			lastAnswer = new ThrowAnswer(toThrow);
			returnValue.offer(lastAnswer);
		} else {
			throw new ExceptionTypeIsNotSuitableException(invocation, toThrow);
		}
	}

	@Override
	public void answerMore(int times) {
		for (int i = 0; i < times; i++) {
			returnValue.offer(lastAnswer);
		}
	}

	@Override
	public boolean hasMultipleAnswer() {
		return returnValue.size() > 1;
	}

	@Override
	public Object getReturnValue() throws Throwable {
		if (!returnValue.isEmpty()) {
			lastAnswer = returnValue.poll();
		}

		return lastAnswer == null ? nullValue() : lastAnswer.doAnswer();
	}

	@Override
	public Invocation getInvocation() {
		return invocation;
	}

	@Override
	public List<Matcher<?>> getMatchers() {
		return matchers.getMatchers();
	}

	@Override
	public boolean equals(Invocation invocation, List<Matcher<?>> matchers) {
		return this.invocation.sameMethod(invocation)
				&& this.matchers.getMatchers().equals(matchers);
	}

	protected Object nullValue() {
		Class<?> returnType = invocation.getReturnType();
		return Utils.nullValue(returnType);
	}

	private boolean isSuitableType(Class<?> thisType, Class<?> targetType) {
		if (targetType.isAssignableFrom(thisType)) {
			return true;
		} else if (targetType.isPrimitive()) {
			String thisTypeName = thisType.getName();
			String targetTypeName = targetType.getName();

			if (thisTypeName.startsWith("java.lang.")) {
				thisTypeName = thisTypeName.substring("java.lang.".length());
				if (thisTypeName.equalsIgnoreCase(targetTypeName)
						|| ("Integer".equals(thisTypeName) && "int".equals(targetTypeName))
						|| ("Character".equals(thisTypeName) && "char".equals(targetTypeName))) {
					return true;
				}
			}
		}

		return false;
	}

	private class ReturnAnswer implements Answer {
		private Object returnValue;

		ReturnAnswer(Object returnValue) {
			this.returnValue = returnValue;
		}

		@Override
		public Object doAnswer() {
			return returnValue;
		}
	}

	private class ThrowAnswer implements Answer {
		private Throwable toThrow;

		ThrowAnswer(Throwable toThrow) {
			this.toThrow = toThrow;
		}

		@Override
		public Object doAnswer() throws Throwable {
			throw toThrow;
		}
	}
}
