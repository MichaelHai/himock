package cn.michaelwang.himock;

import cn.michaelwang.himock.report.VerificationFailedException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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

    @Test(expected = MockNoninterfaceException.class)
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
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(dummy).doNothing();

        dummy.doNothing();

        mock.verify();
    }

    @Test(expected = VerificationFailedException.class)
    public void testNotCalledExpectationShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(dummy).doNothing();

        mock.verify();
    }

    @Test(expected = VerificationFailedException.class)
    public void testUnexpectedInvocationShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();

        mock.verify();
    }
}