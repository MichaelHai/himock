package cn.michaelwang.himock;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HiMockTimerTest extends HiMockBaseTest {
    @Test
    public void testTimerInExpect() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(1).times(3);
        });

        assertEquals(1, dummy.returnInt());
        assertEquals(1, dummy.returnInt());
        assertEquals(1, dummy.returnInt());

        mock.verify();
    }

    @Test
    public void testSetTimerOutExpectShouldFail() {
        reportTest(() -> {
                    mock.times(3);
                }, "Mock Process Error:\n" +
                        "\ttimer cannot be set outside expectation:\n" +
                        "\t-> at cn.michaelwang.himock.HiMockTimerTest.lambda$testSetTimerOutExpectShouldFail$?(HiMockTimerTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockTimerTest.testSetTimerOutExpectShouldFail(HiMockTimerTest.java:?)\n"

        );
    }
}
