package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.IMatcherIndex;

public class Preprocessor {
	private Class<?> testSuit;
	private IMatcherIndex matcherIndex;
	
	public Preprocessor(Class<?> testSuit) {
		this.testSuit = testSuit;
	}

	public void doPreprocess() throws IllegalArgumentException, IllegalAccessException {
		this.matcherIndex = MatcherIndexRepository.getInstance().getMatcherIndex(testSuit);
	}
	
	public IMatcherIndex getMatcherIndex() {
		return this.matcherIndex;
	}
}
