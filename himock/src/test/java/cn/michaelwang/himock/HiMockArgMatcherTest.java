package cn.michaelwang.himock;

import org.junit.Test;

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
}
