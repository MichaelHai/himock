package cn.michaelwang.himock;

import org.junit.Test;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockArgMatcherTest extends HiMockBaseTest{
    @Test
    public void testIntArgumentCanBeMatchedInVerification() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withOneIntParameter(10);

        mock.verify(() -> {
            dummy.withOneIntParameter(mock.matchInt(arg -> arg == 10));
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

        dummy.withStringParameter("hello");

        mock.verify(() -> {
            dummy.withStringParameter(mock.match(arg -> arg.length() == 5));
        });
    }

    @Test
    public void testMixMatchWithObject() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectParameters("hello", "world");

        mock.verify(() -> {
            dummy.withObjectParameters(mock.match(arg -> arg.length() == 5), "world");
        });
    }

    @Test
    public void testMixMatchWithNullObjectAfterMatcher() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectParameters("hello", null);

        mock.verify(() -> {
            dummy.withObjectParameters(mock.match(arg -> arg.length() == 5), null);
        });
    }

    @Test
    public void testMixMatchWithNullObjectBeforeMatcher() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectParameters(null, "world");

        mock.verify(() -> {
            dummy.withObjectParameters( null, mock.match(arg -> arg.length() == 5));
        });
    }

    @Test
    public void testMatcherForTwoFunctions() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectParameters(null, "world");
        dummy.withOneIntParameter(10);

        mock.verify(() -> {
            dummy.withObjectParameters( null, mock.match(arg -> arg.length() == 5));
            dummy.withOneIntParameter(mock.matchInt(arg -> arg == 10));
        });
    }

    @Test
    public void testMatcherInTwoVerifyBlock() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectParameters(null, "world");
        dummy.withOneIntParameter(10);

        mock.verify(() -> {
            dummy.withObjectParameters( null, mock.match(arg -> arg.length() == 5));
        });
        mock.verify(() -> {
            dummy.withOneIntParameter(mock.matchInt(arg -> arg == 10));
        });
    }
}
