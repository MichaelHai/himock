package cn.michaelwang.himock;

import cn.michaelwang.himock.annotations.Mock;
import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;

public class PredefinedMatchersTest extends HiMockBaseTest {
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
}
