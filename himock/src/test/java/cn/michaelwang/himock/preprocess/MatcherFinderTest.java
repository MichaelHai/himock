package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.IMatcherIndex;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class MatcherFinderTest {
	@Test
	public void matchesCanBeFound() {
		IMatcherIndex matcherIndex = Mockito.mock(IMatcherIndex.class);

		MatcherFinder finder = new MatcherFinder(AClass.class, matcherIndex);
		finder.find();

		Mockito.verify(matcherIndex).markMatcher(13, "a0");
		Mockito.verify(matcherIndex).markMatcher(20, "anonymous0");
		Mockito.verify(matcherIndex).markMatcher(26, "anonymous1");
		Mockito.verify(matcherIndex).markMatcher(37, "anonymous2");
		Mockito.verify(matcherIndex).markMatcher(56, "anonymous3");
		Mockito.verify(matcherIndex).useMatcher(Matchers.eq(26), Matchers.eq("aFunctionWithArgs"),
				Matchers.eq(new String[] { "a0", "anonymous1" }));
		Mockito.verify(matcherIndex).useMatcher(Matchers.eq(36), Matchers.eq("aFunctionWithArgs"),
				Matchers.eq(new String[] { "a0", "anonymous2" }));
		Mockito.verify(matcherIndex).useMatcher(Matchers.eq(45), Matchers.eq("aFunctionWithArgs"),
				Matchers.eq(new String[] { "a0", "a0" }));
		Mockito.verify(matcherIndex).useMatcher(Matchers.eq(56), Matchers.eq("withOneIntArgument"),
				Matchers.eq(new String[] { "anonymous3" }));
	}
}