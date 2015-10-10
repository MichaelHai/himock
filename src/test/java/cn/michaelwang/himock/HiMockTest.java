package cn.michaelwang.himock;

import org.junit.Test;

import static org.junit.Assert.*;

public class HiMockTest {

    @Test
    public void testMockObjectCanBeCreated() {
        HiMock mock = new HiMock();
        MockedInterface mockedObject = mock.mock(MockedInterface.class);
        assertNotNull("mockedObject should not be null", mockedObject);
    }
}
