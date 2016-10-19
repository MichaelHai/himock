package cn.michaelwang.himock.preprocess;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

import cn.michaelwang.himock.Matcher;

public class MatcherIndexTest {
	@Test
	public void testLinesNumbersCanGetMatchers() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		IMatcherIndex matchers = new MatcherIndex();
		matchers.addMatcherMark(5, "aMatcherName");
		matchers.addMatcherUsage(5, "invokedMethod", new String[] { "aMatcherName" });
		matchers.addMatcher(5, aMatcher);

		assertEquals(aMatcher, matchers.getMatcher(5, "invokedMethod", 0));
	}

	@Test
	public void testMatchersMarkedInDifferentLineWithInvocation() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		IMatcherIndex matchers = new MatcherIndex();
		matchers.addMatcherMark(3, "aMatcherName");
		matchers.addMatcherUsage(5, "invokedMethod", new String[] { "aMatcherName" });
		matchers.addMatcher(3, aMatcher);

		assertEquals(aMatcher, matchers.getMatcher(5, "invokedMethod", 0));
	}

	@Test
	public void testMultipleMatcherMarks() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);
		Matcher<?> anotherMatcher = Mockito.mock(Matcher.class);

		IMatcherIndex matchers = new MatcherIndex();
		matchers.addMatcherMark(3, "aMatcherName");
		matchers.addMatcherMark(4, "anotherMatcherName");
		matchers.addMatcherUsage(5, "invokedMethod", new String[] { "aMatcherName" });
		matchers.addMatcher(3, aMatcher);
		matchers.addMatcher(4, anotherMatcher);

		assertEquals(aMatcher, matchers.getMatcher(5, "invokedMethod", 0));
	}

	@Test
	public void testMixMatchersWithNonMatchersInInovcation() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		IMatcherIndex matchers = new MatcherIndex();
		matchers.addMatcherMark(3, "aMatcherName");
		matchers.addMatcherUsage(5, "invokedMethod", new String[] { null, "aMatcherName" });
		matchers.addMatcher(3, aMatcher);

		assertEquals(aMatcher, matchers.getMatcher(5, "invokedMethod", 1));
	}

	@Test(expected = RetrieveMatcherForNonMatcherArgException.class)
	public void testGetMatcherForNonMatcherArgsWillThrowException() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		IMatcherIndex matchers = new MatcherIndex();
		matchers.addMatcherMark(3, "aMatcherName");
		matchers.addMatcherUsage(5, "invokedMethod", new String[] { null, "aMatcherName" });
		matchers.addMatcher(3, aMatcher);

		matchers.getMatcher(5, "invokedMethod", 0);
	}
}
