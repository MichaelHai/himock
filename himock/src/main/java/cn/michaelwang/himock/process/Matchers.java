package cn.michaelwang.himock.process;

import java.util.List;

import cn.michaelwang.himock.Matcher;

public class Matchers {
	private List<Matcher<?>> matchers;

	public Matchers(List<Matcher<?>> matchers) {
		this.matchers = matchers;
	}

	public List<Matcher<?>> getMatchers() {
		return matchers;
	}

	@SuppressWarnings("unchecked")
	protected boolean match(Object[] toMatchArgs) {
		for (int i = 0; i < matchers.size(); i++) {
			Matcher<Object> thisArg = (Matcher<Object>) matchers.get(i);
			Object toCompareArg = toMatchArgs[i];

			try {
				if (!thisArg.isMatch(toCompareArg)) {
					return false;
				}
			} catch (NullPointerException ex) {
				return false;
			}
		}

		return true;
	}
}
