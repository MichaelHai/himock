package cn.michaelwang.himock;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HiMockTest {

    private HiMock mock;

    @Before
    public void init() {
        mock = new HiMock();
    }
    @Test
    public void testMockObjectCanBeCreated() {
        MockedInterface mockedObject = mock.mock(MockedInterface.class);
        assertNotNull("mockedObject should not be null", mockedObject);
    }

    @Test(expected = CannotMockClassException.class)
    public void testClassCannotBeMocked() {
        class DummyClass{}
        mock.mock(DummyClass.class);
    }

    @Test
    public void testAlwaysSatisfiedExpectation() {
        mock.verify();
    }

    @Test
    public void testVerifyCalledInvocationShouldPass() {
        DummyInterface dummy = mock.mock(DummyInterface.class);

        mock.expect(dummy).doNothing();

        dummy.doNothing();

        mock.verify();
    }

    @Test
    @Ignore
    public void testNotCalledExpectationShouldFail() {
        DummyInterface dummy = mock.mock(DummyInterface.class);

        mock.expect(dummy).doNothing();

        mock.verify();
    }
}


interface DummyInterface {
    void doNothing();
}