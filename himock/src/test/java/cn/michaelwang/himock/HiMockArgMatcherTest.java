package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockArgMatcherTest extends HiMockBaseTest {
	@Test
	public void testIntArgumentCanBeMatchedInVerification() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withOneIntArgument(10);

		verify(() -> {
			dummy.withOneIntArgument(matchInt(arg -> arg == 10));
		});
	}

	@Test
	public void testBooleanArgumentCanBeMatchedInVerification() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withBooleanParameter(true);

		verify(() -> {
			// noinspection PointlessBooleanExpression
			dummy.withBooleanParameter(matchBoolean(arg -> arg == true));
		});
	}

	@Test
	public void testObjectArgumentCanBeMatchedInVerification() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withStringArgument("hello");

		verify(() -> {
			dummy.withStringArgument(match(arg -> arg.length() == 5));
		});
	}

	@Test
	public void testMixMatchWithObject() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withObjectArguments("hello", "world");

		verify(() -> {
			dummy.withObjectArguments(match(arg -> arg.length() == 5), "world");
		});
	}

	@Test
	public void testMixMatchWithNullObjectAfterMatcher() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withObjectArguments("hello", null);

		verify(() -> {
			dummy.withObjectArguments(match(arg -> arg.length() == 5), null);
		});
	}

	@Test
	public void testMixMatchWithNullObjectBeforeMatcher() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withObjectArguments(null, "world");

		verify(() -> {
			dummy.withObjectArguments(null, match(arg -> arg.length() == 5));
		});
	}

	@Test
	public void testMatcherForTwoFunctions() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withObjectArguments(null, "world");
		dummy.withOneIntArgument(10);

		verify(() -> {
			dummy.withObjectArguments(null, match(arg -> arg.length() == 5));
			dummy.withOneIntArgument(matchInt(arg -> arg == 10));
		});
	}

	@Test
	public void testMatcherInTwoVerifyBlock() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withObjectArguments(null, "world");
		dummy.withOneIntArgument(10);

		verify(() -> {
			dummy.withObjectArguments(null, match(arg -> arg.length() == 5));
		});
		verify(() -> {
			dummy.withOneIntArgument(matchInt(arg -> arg == 10));
		});
	}

	@Test
	public void testMatcherWithOrderedVerification() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withObjectArguments(null, "world");
		dummy.withOneIntArgument(10);

		verifyInOrder(() -> {
			dummy.withObjectArguments(null, match(arg -> arg.length() == 5));
			dummy.withOneIntArgument(matchInt(arg -> arg == 10));
		});
	}

	@Test
	public void testMatcherInExpectation() {
		MockedInterface dummy = mock(MockedInterface.class);

		expect(() -> {
			dummy.withStringArgument(match(str -> str.length() == 5));
			willReturn(10);
		});

		assertEquals(10, dummy.withStringArgument("hello"));
		assertEquals(10, dummy.withStringArgument("world"));
	}

	@Test
	public void testMatcherCannotBeUsedOutsideExpectOrVerification() {
		reportTest(() -> {
			MockedInterface dummy = mock(MockedInterface.class);
			dummy.withStringArgument(match(str -> str.length() == 5));
		}, "Mock Process Error:\n"
				+ "\tmatchers cannot be used outside expectations' or verifications' invocation:\n"
				+ "\t-> at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$testMatcherCannotBeUsedOutsideExpectOrVerification$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n"
				+ "\t   at cn.michaelwang.himock.HiMockArgMatcherTest.testMatcherCannotBeUsedOutsideExpectOrVerification(HiMockArgMatcherTest.java:?)\n");
	}

	@Test
	public void testCreateMatcherOutsideTheInvocation() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withObjectArguments("hello", "world");

		verify(() -> {
			String strMatcher = match(arg -> arg.length() == 5);
			dummy.withObjectArguments(strMatcher, "world");
		});
	}

	@Test
	public void testCreateMatcherOutsideTheInvocationUsedWithAnotherMatcher() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withObjectArguments("hello", "world");

		verify(() -> {
			String strMatcher = match(arg -> arg.length() == 5);
			dummy.withObjectArguments(match(arg -> arg.equals("hello")), strMatcher);
		});
	}

	@Test
	public void testCallWithNullValueAndVerifiedWithoutMatcher() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withBooleanParameter(false);

		verify(() -> {
			dummy.withBooleanParameter(false);
		});
	}

	@Test(expected = HiMockReporter.class)
	@Ignore
	public void testCallWithNullValueAndVerifiedWithMatcherThatNotMatchesNull() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withStringArgument(null);

		verify(() -> {
			dummy.withStringArgument(match(str -> str.length() == 0));
		});
	}

	@Ignore
	@Test
	public void testMatcherDescription() {
		MockedInterface dummy = mock(MockedInterface.class);

		reportTest(() -> {
			verify(() -> {
				dummy.withOneIntArgument(matchInt(arg -> arg == 10));
			});
		}, "Verification failed:\n" + "\texpected invocation not happened:\n"
				+ "\t\tcn.michaelwang.himock.MockedInterface.withOneIntArgument('matcher')\n"
				+ "\t\t-> at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$null$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$testMatcherDescription$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.testMatcherDescription(HiMockArgMatcherTest.java:?)\n");
	}
}
