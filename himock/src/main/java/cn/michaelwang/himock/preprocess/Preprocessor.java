package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.IMatcherIndex;

public class Preprocessor {
    private final Class<?> testSuit;
    private IMatcherIndex matcherIndex;
	
	public Preprocessor(Class<?> testSuit) {
		this.testSuit = testSuit;
	}

    public void doPreprocess() throws IllegalArgumentException {
        this.matcherIndex = MatcherIndexRepository.getInstance().getMatcherIndex(testSuit);
	}
	
	public IMatcherIndex getMatcherIndex() {
		return this.matcherIndex;
	}
}
