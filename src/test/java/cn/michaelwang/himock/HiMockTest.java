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

    @Test(expected = CannotMockClassException.class)
    public void testClassCannotBeMocked() {
        class DummyClass{}

        HiMock mock = new HiMock();
        mock.mock(DummyClass.class);
    }
}
