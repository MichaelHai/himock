package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.IMatcherIndex;

import java.util.HashMap;
import java.util.Map;

public class MatcherIndexRepository {
	private static MatcherIndexRepository instance;

    private final Map<Class<?>, IMatcherIndex> matcherIndexes = new HashMap<>();

	private MatcherIndexRepository() {
	}

	public static MatcherIndexRepository getInstance() {
		if (instance == null) {
			instance = new MatcherIndexRepository();
		}

		return instance;
	}


	public IMatcherIndex getMatcherIndex(Class<?> testSuit) {
		if (!matcherIndexes.containsKey(testSuit)) {
			IMatcherIndex matcherIndex = new MatcherIndex();
			MatcherFinder finder = new MatcherFinder(testSuit, matcherIndex);
			finder.find();

			matcherIndexes.put(testSuit, matcherIndex);
		}
		
		return matcherIndexes.get(testSuit);
	}
}
