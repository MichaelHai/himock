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
    public void testCannotMockClassExceptionShouldContainsMockedClassInformationAndGenerateProperMessage() {
        HiMock mock = new HiMock();
        try {
            mock.mock(String.class);
        } catch (MockProcessErrorReporter ex) {
            assertEquals("Mock Process Error:\n" +
                    "\tonly interface can(should) be mocked:\n" +
                    "\t\tclass being mocked: java.lang.String",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testCannotMockClassExceptionShouldContainsMockedClassInformationAndGenerateProperMessage", ex.getStackTrace()[0].getMethodName());
        }
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
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testNotCalledExpectationShouldProvideErrorInformation(VerificationFailedReporterTest.java:39)",
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
                            "\t\tcn.michaelwang.himock.MockedInterface.withObjectParameters(o1, o2)\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testNotCalledExpectationShouldProvideErrorInformationWithArgs$0(VerificationFailedReporterTest.java:60)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testNotCalledExpectationShouldProvideErrorInformationWithArgs(VerificationFailedReporterTest.java:59)",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testNotCalledExpectationShouldProvideErrorInformationWithArgs", ex.getStackTrace()[0].getMethodName());
        }

    }

    @Test
    public void testUnexpectedInvocationShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertEquals("Verification failed:\n" +
                            "\tunexpected invocation happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedInvocationShouldProvideErrorInformation(VerificationFailedReporterTest.java:82)",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testUnexpectedInvocationShouldProvideErrorInformation", ex.getStackTrace()[0].getMethodName());
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
                            "\t\tcn.michaelwang.himock.MockedInterface.withObjectParameters(o1, o2)\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedInvocationShouldProvideErrorInformationWithArgs(VerificationFailedReporterTest.java:101)",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testUnexpectedInvocationShouldProvideErrorInformationWithArgs", ex.getStackTrace()[0].getMethodName());
        }

    }

    @Test
    public void testUnexpectedParameterShouldProvideErrorInformation() {
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
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testUnexpectedParameterShouldProvideErrorInformation$1(VerificationFailedReporterTest.java:122)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedParameterShouldProvideErrorInformation(VerificationFailedReporterTest.java:121)\n" +
                            "\t\tparameters actually:\to1\to3\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedParameterShouldProvideErrorInformation(VerificationFailedReporterTest.java:125)",
                    ex.getMessage());
            assertEquals(1, ex.getStackTrace().length);
            assertEquals("testUnexpectedParameterShouldProvideErrorInformation", ex.getStackTrace()[0].getMethodName());
        }
    }
}
