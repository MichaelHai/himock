package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;
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
        assertEquals(0, dummy.returnInt());

        mock.verify();
    }

    @Test(expected = HiMockReporter.class)
    public void testSetTimerOutExpectShouldFail() {
        mock.times(3);
    }
}
