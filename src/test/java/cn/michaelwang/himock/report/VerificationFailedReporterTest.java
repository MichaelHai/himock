package cn.michaelwang.himock.report;

import cn.michaelwang.himock.HiMock;
import cn.michaelwang.himock.MockedInterface;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("CodeBlock2Expr")
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
            assertEquals("Verification failed:\n" +
                            "\texpected invocation not happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testNotCalledExpectationShouldProvideErrorInformation", ex.getStackTrace()[0].getMethodName());
        }
    }

    @Test
    public void testNotCalledExpectationShouldProvideErrorInformationWithArgs() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.withObjectParameters("o1", "o2");
        });

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertEquals("Verification failed:\n" +
                            "\texpected invocation not happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.withObjectParameters(o1, o2)",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testNotCalledExpectationShouldProvideErrorInformationWithArgs", ex.getStackTrace()[0].getMethodName());
        }

    }

    @Test
    public void testUnExpectedInvocationShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertEquals("Verification failed:\n" +
                            "\tunexpected invocation happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testUnExpectedInvocationShouldProvideErrorInformation", ex.getStackTrace()[0].getMethodName());
        }
    }

    @Test
    public void testUnexpectedInvocationShouldProvideErrorInformationWithArgs() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectParameters("o1", "o2");

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertEquals("Verification failed:\n" +
                            "\tunexpected invocation happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.withObjectParameters(o1, o2)",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testUnexpectedInvocationShouldProvideErrorInformationWithArgs", ex.getStackTrace()[0].getMethodName());
        }

    }

    @Test
    public void testUnExpectedParameterShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.withObjectParameters("o1", "o2");
        });

        dummy.withObjectParameters("o1", "o3");

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertEquals("Verification failed:\n" +
                            "\tinvocation with unexpected parameters:\n" +
                            "\t\tmethod called:\tcn.michaelwang.himock.MockedInterface.withObjectParameters\n" +
                            "\t\tparameters expected:\to1\to2\n" +
                            "\t\tparameters actually:\to1\to3",
                    ex.getMessage());
        }
    }
}
