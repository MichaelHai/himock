package cn.michaelwang.himock.preprocess;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import cn.michaelwang.himock.IMatcherIndex;

public class MatcherFinderTest {
	@Test
	public void matchesCanBeFound() {
		IMatcherIndex matcherIndex = Mockito.mock(IMatcherIndex.class);

		MatcherFinder finder = new MatcherFinder(AClass.class, matcherIndex);
		finder.find();

		Mockito.verify(matcherIndex).markMatcher(9, "a0");
		Mockito.verify(matcherIndex).markMatcher(12, "anonymous0");
		Mockito.verify(matcherIndex).markMatcher(22, "anonymous1");
		Mockito.verify(matcherIndex).markMatcher(33, "anonymous2");
		Mockito.verify(matcherIndex).useMatcher(Matchers.eq(22), Matchers.eq("aFunctionWithArgs"),
				Matchers.eq(new String[] { "a0", "anonymous1" }));
		Mockito.verify(matcherIndex).useMatcher(Matchers.eq(32), Matchers.eq("aFunctionWithArgs"),
				Matchers.eq(new String[] { "a0", "anonymous2" }));
		Mockito.verify(matcherIndex).useMatcher(Matchers.eq(41), Matchers.eq("aFunctionWithArgs"),
				Matchers.eq(new String[] { "a0", "a0" }));
	}
}