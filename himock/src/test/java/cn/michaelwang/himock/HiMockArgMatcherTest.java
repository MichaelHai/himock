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

        dummy.withStringParamter("hello");

        mock.verify(() -> {
            dummy.withStringParamter(mock.match(arg -> arg.length() == 5));
        });
    }
}
