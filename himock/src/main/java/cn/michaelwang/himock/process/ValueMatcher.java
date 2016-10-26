package cn.michaelwang.himock.process;

import java.util.Arrays;

import cn.michaelwang.himock.Matcher;

public class ValueMatcher<T> implements Matcher<T> {
	private T value;

	public ValueMatcher(T value) {
		this.value = value;
	}

	@Override
	public boolean isMatch(Object actual) {
		return value == null
				? (actual == null)
				: (actual instanceof Object[] && value instanceof Object[]
						? Arrays.deepEquals((Object[]) actual, (Object[]) value)
						: value.equals(actual));
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
