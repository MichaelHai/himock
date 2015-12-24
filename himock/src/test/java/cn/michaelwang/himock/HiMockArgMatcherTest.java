package cn.michaelwang.himock;

import org.junit.Test;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockArgMatcherTest extends HiMockBaseTest{
    @Test
    public void testArgumentCanBeMatched() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withOneIntParameter(10);

        mock.verify(() -> {
            dummy.withOneIntParameter(mock.matchInt(arg -> arg == 10));
        });
    }
}
