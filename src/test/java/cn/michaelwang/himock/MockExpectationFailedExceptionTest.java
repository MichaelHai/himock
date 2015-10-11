package cn.michaelwang.himock;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MockExpectationFailedExceptionTest {
    private HiMock mock;

    @Before
    public void init() {
        mock = new HiMock();
    }

    @Test
    public void testNotCalledExpectationShouldProvideErrorInformation() {
        DummyInterface dummy = mock.mock(DummyInterface.class);

        mock.expect(dummy).doNothing();

        try {
            mock.verify();
        } catch (MockExpectationFailedException ex) {
            assertEquals(ex.getMessage(),
                    "Verification failed: \n" +
                            "\texpectation not satisfied: \n" +
                            "\t\tcn.michaelwang.himock.DummyInterface.doNothing()");
        }
    }

    @Test
    public void testUnExpectedInvocationShouldProvideErrorInformation() {
        DummyInterface dummy = mock.mock(DummyInterface.class);

        dummy.doNothing();

        try {
            mock.verify();
        } catch (MockExpectationFailedException ex) {
            assertEquals(ex.getMessage(),
                    "Verification failed: \n" +
                            "\tunexpected invocation detected: \n" +
                            "\t\tcn.michaelwang.himock.DummyInterface.doNothing()");
        }
    }

}
