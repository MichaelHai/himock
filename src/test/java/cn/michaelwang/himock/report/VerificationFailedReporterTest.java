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

        mock.expect(dummy).doNothing();

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertEquals(ex.getMessage(),
                    "Verification failed: \n" +
                            "\texpected invocation not satisfied: \n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()");
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
                            "\tunexpected invocation called: \n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()");
        }
    }

}
