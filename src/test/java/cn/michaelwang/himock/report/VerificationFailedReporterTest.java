package cn.michaelwang.himock.report;

import cn.michaelwang.himock.HiMock;
import cn.michaelwang.himock.MockedInterface;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VerificationFailedReporterTest {
    private HiMock mock;

    @Before
    public void init() {
        mock = new HiMock();
    }

    @Test
    public void testNotCalledExpectationShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expectStart();
        dummy.doNothing();
        mock.expectEnd();

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertEquals(ex.getMessage(),
                    "Verification failed: \n" +
                            "\texpected invocation not happened: \n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()");
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testNotCalledExpectationShouldProvideErrorInformation", ex.getStackTrace()[0].getMethodName());
        }
    }

    @Test
    public void testUnExpectedInvocationShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertEquals(ex.getMessage(),
                    "Verification failed: \n" +
                            "\tunexpected invocation happened: \n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()");
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testUnExpectedInvocationShouldProvideErrorInformation", ex.getStackTrace()[0].getMethodName());
        }
    }
}
