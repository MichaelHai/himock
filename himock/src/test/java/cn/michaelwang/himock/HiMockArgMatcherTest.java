package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockArgMatcherTest extends HiMockBaseTest {
    @Test
    public void testIntArgumentCanBeMatchedInVerification() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withOneIntArgument(10);

        mock.verify(() -> {
            dummy.withOneIntArgument(mock.matchInt(arg -> arg == 10));
        });
    }

    @Test
    public void testBooleanArgumentCanBeMatchedInVerification() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withBooleanParameter(true);

        mock.verify(() -> {
            //noinspection PointlessBooleanExpression
            dummy.withBooleanParameter(mock.matchBoolean(arg -> arg == true));
        });
    }

    @Test
    public void testObjectArgumentCanBeMatchedInVerification() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withStringArgument("hello");

        mock.verify(() -> {
            dummy.withStringArgument(mock.match(arg -> arg.length() == 5));
        });
    }

    @Test
    public void testMixMatchWithObject() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectArguments("hello", "world");

        mock.verify(() -> {
            dummy.withObjectArguments(mock.match(arg -> arg.length() == 5), "world");
        });
    }

    @Test
    public void testMixMatchWithNullObjectAfterMatcher() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectArguments("hello", null);

        mock.verify(() -> {
            dummy.withObjectArguments(mock.match(arg -> arg.length() == 5), null);
        });
    }

    @Test
    public void testMixMatchWithNullObjectBeforeMatcher() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectArguments(null, "world");

        mock.verify(() -> {
            dummy.withObjectArguments(null, mock.match(arg -> arg.length() == 5));
        });
    }

    @Test
    public void testMatcherForTwoFunctions() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectArguments(null, "world");
        dummy.withOneIntArgument(10);

        mock.verify(() -> {
            dummy.withObjectArguments(null, mock.match(arg -> arg.length() == 5));
            dummy.withOneIntArgument(mock.matchInt(arg -> arg == 10));
        });
    }

    @Test
    public void testMatcherInTwoVerifyBlock() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectArguments(null, "world");
        dummy.withOneIntArgument(10);

        mock.verify(() -> {
            dummy.withObjectArguments(null, mock.match(arg -> arg.length() == 5));
        });
        mock.verify(() -> {
            dummy.withOneIntArgument(mock.matchInt(arg -> arg == 10));
        });
    }

    @Test
    public void testMatcherWithOrderedVerification() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectArguments(null, "world");
        dummy.withOneIntArgument(10);

        mock.verifyInOrder(() -> {
            dummy.withObjectArguments(null, mock.match(arg -> arg.length() == 5));
            dummy.withOneIntArgument(mock.matchInt(arg -> arg == 10));
        });
    }

    @Test
    public void testMatcherInExpectation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.withStringArgument(mock.match(str -> str.length() == 5));
            mock.willReturn(10);
        });

        assertEquals(10, dummy.withStringArgument("hello"));
        assertEquals(10, dummy.withStringArgument("world"));
    }

    @Test
    @Ignore
    public void testCallWithNullValueAndVerifiedWithoutMatcher() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withBooleanParameter(false);

        mock.verify(() -> {
            dummy.withBooleanParameter(false);
        });
    }

    @Test(expected = HiMockReporter.class)
    @Ignore
    public void testCallWithNullValueAndVerifiedWithMatcherThatNotMatchesNull() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withStringArgument(null);

        mock.verify(() -> {
            dummy.withStringArgument(mock.match(str -> str.length() == 0));
        });
    }

    @Ignore
    @Test
    public void testMatcherDescription() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        reportTest(() -> {
            mock.verify(() -> {
                dummy.withOneIntArgument(mock.matchInt(arg -> arg == 10));
            });
        }, "Verification failed:\n" +
                "\texpected invocation not happened:\n" +
                "\t\tcn.michaelwang.himock.MockedInterface.withOneIntArgument('matcher')\n" +
                "\t\t-> at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$null$?(HiMockArgMatcherTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.lambda$testMatcherDescription$?(HiMockArgMatcherTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.HiMockArgMatcherTest.testMatcherDescription(HiMockArgMatcherTest.java:?)\n");
    }
}
