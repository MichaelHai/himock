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

		Mockito.verify(matcherIndex).markMatcher(8, "this.a0");
		Mockito.verify(matcherIndex).markMatcher(11, "anonymous0");
		Mockito.verify(matcherIndex).markMatcher(21, "anonymous1");
		Mockito.verify(matcherIndex).useMatcher(Matchers.eq(21), Matchers.eq("mock.aFunctionWithArgs"),
				Matchers.eq(new String[] { "this.a0", "anonymous1" }));
	}
}