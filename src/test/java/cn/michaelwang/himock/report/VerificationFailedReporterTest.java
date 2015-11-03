package cn.michaelwang.himock.report;

import cn.michaelwang.himock.HiMock;
import cn.michaelwang.himock.MockedInterface;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.failNotEquals;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("CodeBlock2Expr")
public class VerificationFailedReporterTest {
    private HiMock mock;

    @Before
    public void init() {
        mock = new HiMock();
    }

    @Test
    public void testCannotMockClassExceptionShouldProvideErrorInformation() {
        HiMock mock = new HiMock();
        try {
            mock.mock(String.class);
        } catch (MockProcessErrorReporter ex) {
            assertStringEqualWithWildcardCharacter("Mock Process Error:\n" +
                            "\tonly interface can(should) be mocked:\n" +
                            "\t\tclass being mocked: java.lang.String\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testCannotMockClassExceptionShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)",
                    ex.getMessage());
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
            assertStringEqualWithWildcardCharacter("Verification failed:\n" +
                            "\texpected invocation not happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testNotCalledExpectationShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)",
                    ex.getMessage());
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
            assertStringEqualWithWildcardCharacter("Verification failed:\n" +
                            "\texpected invocation not happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.withObjectParameters(o1, o2)\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testNotCalledExpectationShouldProvideErrorInformationWithArgs$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testNotCalledExpectationShouldProvideErrorInformationWithArgs(VerificationFailedReporterTest.java:?)",
                    ex.getMessage());
        }

    }

    @Test
    public void testUnexpectedInvocationShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertStringEqualWithWildcardCharacter("Verification failed:\n" +
                            "\tunexpected invocation happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedInvocationShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)",
                    ex.getMessage());
        }
    }

    @Test
    public void testUnexpectedInvocationShouldProvideErrorInformationWithArgs() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.withObjectParameters("o1", "o2");

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertStringEqualWithWildcardCharacter("Verification failed:\n" +
                            "\tunexpected invocation happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.withObjectParameters(o1, o2)\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedInvocationShouldProvideErrorInformationWithArgs(VerificationFailedReporterTest.java:?)",
                    ex.getMessage());
        }

    }

    @Test
    public void testNoReturnShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        try {
            mock.expect(() -> {
                dummy.doNothing();
                mock.willReturn(1);
            });
        } catch (MockProcessErrorReporter reporter) {
            assertStringEqualWithWildcardCharacter("Mock Process Error:\n" +
                            "\tinvocation expected has no return value:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testNoReturnShouldProvideErrorInformation$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testNoReturnShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)",
                    reporter.getMessage());
        }

        dummy.doNothing();
    }

    @Test
    public void testSetReturnTwiceShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        try {
            mock.expect(() -> {
                dummy.returnInt();
                mock.willReturn(1);
                mock.willReturn(2);
            });
        } catch (MockProcessErrorReporter reporter) {
            assertStringEqualWithWildcardCharacter("Mock Process Error:\n" +
                            "\treturn value has been set:\n" +
                            "\t\tmethod setting return value: cn.michaelwang.himock.MockedInterface.returnInt()\n" +
                            "\t\treturn value already set:\t1\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testSetReturnTwiceShouldProvideErrorInformation$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testSetReturnTwiceShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n" +
                            "\t\treturn value set again:\t2\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testSetReturnTwiceShouldProvideErrorInformation$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testSetReturnTwiceShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)",
                    reporter.getMessage());
        }
    }

    @Test
    public void testSetNotSuitableTypeValueShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        try {
            mock.expect(() -> {
                dummy.returnInt();
                mock.willReturn(true);
            });

            dummy.returnInt();

            mock.verify();
        } catch (MockProcessErrorReporter reporter) {
            assertStringEqualWithWildcardCharacter("Mock Process Error:\n" +
                            "\treturn value type is not match:\n" +
                            "\t\tmethod setting return value: cn.michaelwang.himock.MockedInterface.returnInt()\n" +
                            "\t\treturn type expected:\tint\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testSetNotSuitableTypeValueShouldProvideErrorInformation$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testSetNotSuitableTypeValueShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n" +
                            "\t\treturn type being set:\tboolean\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testSetNotSuitableTypeValueShouldProvideErrorInformation$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testSetNotSuitableTypeValueShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)",
                    reporter.getMessage());
        }
    }

    @Test
    public void testSetReturnValueOutsideExpectShouldProvideErrorInformation() {
        try {
            mock.willReturn(1);
        } catch (MockProcessErrorReporter reporter) {
            assertStringEqualWithWildcardCharacter("Mock Process Error:\n" +
                            "\treturn value cannot be set outside expectation:\n" +
                            "\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testSetReturnValueOutsideExpectShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)",
                    reporter.getMessage());
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
            assertStringEqualWithWildcardCharacter("Verification failed:\n" +
                            "\tinvocation with unexpected parameters:\n" +
                            "\t\tmethod called:\tcn.michaelwang.himock.MockedInterface.withObjectParameters\n" +
                            "\t\tparameters expected:\to1\to2\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testUnexpectedParameterShouldProvideErrorInformation$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedParameterShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n" +
                            "\t\tparameters actually:\to1\to3\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedParameterShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)",
                    ex.getMessage());
        }
    }

    private void assertStringEqualWithWildcardCharacter(String expected, String actually) {
        String[] splitted = expected.split("\\?");

        int start;
        int end = 0;
        for (String sub : splitted) {
            start = actually.indexOf(sub, end);
            end = start + sub.length();

            if (start == -1) {
                failNotEquals(null, expected, actually);
                return;
            }
        }

        for (int i = end; i < actually.length(); i++) {
            assertEquals('?', actually.charAt(i));
        }
    }
}
