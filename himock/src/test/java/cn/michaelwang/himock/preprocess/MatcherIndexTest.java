package cn.michaelwang.himock.preprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import cn.michaelwang.himock.Matcher;

public class MatcherIndexTest {
	@Test
	public void testLinesNumbersCanGetMatchers() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		MatcherIndex matchers = new MatcherIndex();
		matchers.markMatcher(5, "aMatcherName");
		matchers.useMatcher(5, "invokedMethod", new String[] { "aMatcherName" });
		matchers.addMatcher(5, aMatcher);

		assertEquals(aMatcher, matchers.getMatcher(5, "invokedMethod", 0));
	}

	@Test
	public void testMatchersMarkedInDifferentLineWithInvocation() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		MatcherIndex matchers = new MatcherIndex();
		matchers.markMatcher(3, "aMatcherName");
		matchers.useMatcher(5, "invokedMethod", new String[] { "aMatcherName" });
		matchers.addMatcher(3, aMatcher);

		assertEquals(aMatcher, matchers.getMatcher(5, "invokedMethod", 0));
	}

	@Test
	public void testMultipleMatcherMarks() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);
		Matcher<?> anotherMatcher = Mockito.mock(Matcher.class);

		MatcherIndex matchers = new MatcherIndex();
		matchers.markMatcher(3, "aMatcherName");
		matchers.markMatcher(4, "anotherMatcherName");
		matchers.useMatcher(5, "invokedMethod", new String[] { "aMatcherName" });
		matchers.addMatcher(3, aMatcher);
		matchers.addMatcher(4, anotherMatcher);

		assertEquals(aMatcher, matchers.getMatcher(5, "invokedMethod", 0));
	}

	@Test
	public void testMixMatchersWithNonMatchersInInovcation() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		MatcherIndex matchers = new MatcherIndex();
		matchers.markMatcher(3, "aMatcherName");
		matchers.useMatcher(5, "invokedMethod", new String[] { null, "aMatcherName" });
		matchers.addMatcher(3, aMatcher);

		assertEquals(aMatcher, matchers.getMatcher(5, "invokedMethod", 1));
	}

	@Test
	public void testGetMatcherForNonMatcherArgsWillReturnNull() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		MatcherIndex matchers = new MatcherIndex();
		matchers.markMatcher(3, "aMatcherName");
		matchers.useMatcher(5, "invokedMethod", new String[] { null, "aMatcherName" });
		matchers.addMatcher(3, aMatcher);

		assertNull(matchers.getMatcher(5, "invokedMethod", 0));
	}
	
	@Test
	public void testAMatcherCanOnlyBeRetrievedOnce() {
		Matcher<?> aMatcher = Mockito.mock(Matcher.class);

		MatcherIndex matchers = new MatcherIndex();
		matchers.markMatcher(3, "aMatcherName");
		matchers.useMatcher(5, "invokedMethod", new String[] { null, "aMatcherName" });
		matchers.addMatcher(3, aMatcher);

		assertNotNull(matchers.getMatcher(5, "invokedMethod", 1));
		assertNull(matchers.getMatcher(5, "invokedMethod", 1));
	}
}
