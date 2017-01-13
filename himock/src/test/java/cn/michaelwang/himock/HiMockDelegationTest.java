package cn.michaelwang.himock;

import cn.michaelwang.himock.annotations.Mock;
import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;

public class HiMockDelegationTest extends HiMockBaseTest {

    @Test
    public void testAnswerWithoutParameter(@Mock MockedInterface dummy) {
        expect(() -> {
            dummy.returnInt();
            willAnswer(p -> 1);
        });

        assertEquals(1, dummy.returnInt());

        verify();
    }

    @Test
    public void testAnswerWithParameter(@Mock MockedInterface dummy) {
        expect(() -> {
            dummy.withStringArgument(match((str) -> true));
            willAnswer(params -> ((String) params[0]).length());
        });

        assertEquals(5, dummy.withStringArgument("hello"));

        verify();
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Test
    public void testAnswerOutsideExpectShouldFail() {
        reportTest(() -> {
                    willAnswer(answer -> answer);
                }
                , "Mock Process Error:\n" +
                        "\tanswers cannot be set outside expectation:\n" +
                        "\t-> at cn.michaelwang.himock.HiMockDelegationTest.lambda$testAnswerOutsideExpectShouldFail$?(HiMockDelegationTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockDelegationTest.testAnswerOutsideExpectShouldFail(HiMockDelegationTest.java:?)\n"
        );
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Test
    public void testAnswerInVerificationShouldFail() {
        reportTest(() -> {
                    verify(() -> {
                        willAnswer(answer -> answer);
                    });
                }
                , "Mock Process Error:\n" +
                        "\tanswers cannot be set outside expectation:\n" +
                        "\t-> at cn.michaelwang.himock.HiMockDelegationTest.lambda$null$?(HiMockDelegationTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockDelegationTest.lambda$testAnswerInVerificationShouldFail$?(HiMockDelegationTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockDelegationTest.testAnswerInVerificationShouldFail(HiMockDelegationTest.java:?)\n"
        );
    }
}
