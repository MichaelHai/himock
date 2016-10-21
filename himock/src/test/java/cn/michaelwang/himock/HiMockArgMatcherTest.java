package cn.michaelwang.himock;

import static cn.michaelwang.himock.HiMock.expect;
import static cn.michaelwang.himock.HiMock.match;
import static cn.michaelwang.himock.HiMock.matchBoolean;
import static cn.michaelwang.himock.HiMock.matchInt;
import static cn.michaelwang.himock.HiMock.mock;
import static cn.michaelwang.himock.HiMock.verify;
import static cn.michaelwang.himock.HiMock.verifyInOrder;
import static cn.michaelwang.himock.HiMock.willReturn;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.michaelwang.himock.report.HiMockReporter;

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
	public void testCallWithNullValueAndVerifiedWithMatcherThatNotMatchesNull() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withStringArgument(null);

		verify(() -> {
			dummy.withStringArgument(match(str -> str.length() == 0));
		});
	}

	@Test
	public void testMatcherDescription() {
		MockedInterface dummy = mock(MockedInterface.class);

		reportTest(() -> {
			verify(() -> {
				dummy.withOneIntArgument(matchInt(arg -> arg == 10));
			});
		}, "Verification failed:\n"
				+ "\texpected invocation not happened:\n"
				+ "\t\tcn.michaelwang.himock.MockedInterface.withOneIntArgument('matcher')\n"
				+ "\t\t-> at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$null$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$testMatcherDescription$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.testMatcherDescription(HiMockArgMatcherTest.java:?)\n");
	}

	@Test
	public void testmatcherDescriptionWhenParamMatchingInVerifyFailed() {
		MockedInterface dummy = mock(MockedInterface.class);

		reportTest(() -> {
			dummy.withObjectArguments("hello", "world");

			verify(() -> {
				dummy.withObjectArguments(match(arg -> arg.length() == 4), "world");
			});
		}, "Verification failed:\n"
				+ "\tinvocation with unexpected arguments:\n"
				+ "\t\tmethod called:\tcn.michaelwang.himock.MockedInterface.withObjectArguments\n"
				+ "\t\targuments expected:\t'matcher'\tworld\n"
				+ "\t\t-> at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$null$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$testmatcherDescriptionWhenParamMatchingInVerifyFailed$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.testmatcherDescriptionWhenParamMatchingInVerifyFailed(HiMockArgMatcherTest.java:?)\n"
				+ "\t\targuments actually:\thello\tworld\n"
				+ "\t\t-> at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$testmatcherDescriptionWhenParamMatchingInVerifyFailed$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.testmatcherDescriptionWhenParamMatchingInVerifyFailed(HiMockArgMatcherTest.java:?)\n");
	}

	@Test
	public void testmatcherDescriptionWhenParamMatchingInExpectFailed() {
		MockedInterface dummy = mock(MockedInterface.class);

		reportTest(() -> {
			expect(() -> {
				dummy.withObjectArguments(match(arg -> arg.length() == 4), "world");
			});
			
			dummy.withObjectArguments("hello", "world");
			
			verify();
		}, "Verification failed:\n"
				+ "\tinvocation with unexpected arguments:\n"
				+ "\t\tmethod called:\tcn.michaelwang.himock.MockedInterface.withObjectArguments\n"
				+ "\t\targuments expected:\t'matcher'\tworld\n"
				+ "\t\t-> at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$null$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$testmatcherDescriptionWhenParamMatchingInExpectFailed$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.testmatcherDescriptionWhenParamMatchingInExpectFailed(HiMockArgMatcherTest.java:?)\n"
				+ "\t\targuments actually:\thello\tworld\n"
				+ "\t\t-> at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$testmatcherDescriptionWhenParamMatchingInExpectFailed$?(HiMockArgMatcherTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n"
				+ "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.testmatcherDescriptionWhenParamMatchingInExpectFailed(HiMockArgMatcherTest.java:?)\n");
	}
}
