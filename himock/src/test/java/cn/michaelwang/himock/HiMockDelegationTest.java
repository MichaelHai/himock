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
}
