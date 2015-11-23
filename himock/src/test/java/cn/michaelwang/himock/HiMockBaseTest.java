package cn.michaelwang.himock;

import org.junit.Before;

public abstract class HiMockBaseTest {
    protected HiMock mock;

    @Before
    public void setup() {
        mock = new HiMock();
    }
}
