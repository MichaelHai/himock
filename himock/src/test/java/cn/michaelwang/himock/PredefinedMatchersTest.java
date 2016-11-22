package cn.michaelwang.himock;

import cn.michaelwang.himock.annotations.Mock;
import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("CodeBlock2Expr")
public class PredefinedMatchersTest extends HiMockBaseTest {
    @SuppressWarnings("CanBeFinal")
    @Mock
    private MockedInterface dummy;

    @Test
    public void testAny() {
        expect(() -> {
            dummy.withStringArgument(match(any()));
            willReturn(20);
        });

        assertEquals(20, dummy.withStringArgument("Hello"));
    }

    @Test
    public void testAnyDescription() {
        MockedInterface dummy = mock(MockedInterface.class);

        reportTest(() -> {
            verify(() -> {
                dummy.withOneIntArgument(matchInt(any()));
            });
        }, "Verification failed:\n"
                + "\texpected invocation not happened:\n"
                + "\t\tcn.michaelwang.himock.MockedInterface.withOneIntArgument('any')\n"
                + "\t\t-> at cn.michaelwang.himock.PredefinedMatchersTest.lambda$null$?(PredefinedMatchersTest.java:?)\n"
                + "\t\t   at cn.michaelwang.himock.PredefinedMatchersTest.lambda$testAnyDescription$?(PredefinedMatchersTest.java:?)\n"
                + "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n"
                + "\t\t   at cn.michaelwang.himock.PredefinedMatchersTest.testAnyDescription(PredefinedMatchersTest.java:?)\n");
    }
}
