package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Matcher;

public class ValueMatcher<T> implements Matcher<T> {
	private T value;

	public ValueMatcher(T value) {
		this.value = value;
	}

	@Override
	public boolean isMatch(Object actual) {
		return value == null ? (actual == null) : value.equals(actual);
	}

}
