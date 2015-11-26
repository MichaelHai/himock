package cn.michaelwang.himock.report;

import cn.michaelwang.himock.HiMock;
import cn.michaelwang.himock.HiMockBaseTest;
import cn.michaelwang.himock.MockedInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SuppressWarnings("CodeBlock2Expr")
public class VerificationFailedReporterTest extends HiMockBaseTest {

    @Test
    public void testCannotMockClassExceptionShouldProvideErrorInformation() {
        HiMock mock = new HiMock();
        try {
            mock.mock(String.class);
        } catch (MockProcessErrorReporter ex) {
            assertStringEqualWithWildcardCharacter("Mock Process Error:\n" +
                            "\tonly interface can(should) be mocked:\n" +
                            "\t\tclass being mocked: java.lang.String\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testCannotMockClassExceptionShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n",
                    ex.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");
    }

    @Test
    public void testNotCalledExpectationShouldProvideErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.doNothing();
        });

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertStringEqualWithWildcardCharacter("Verification failed:\n" +
                            "\texpected invocation not happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testNotCalledExpectationShouldProvideErrorInformation$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testNotCalledExpectationShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n",
                    ex.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");
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
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testNotCalledExpectationShouldProvideErrorInformationWithArgs(VerificationFailedReporterTest.java:?)\n",
                    ex.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");

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
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testNoReturnShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n",
                    reporter.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");
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
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testSetNotSuitableTypeValueShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n",
                    reporter.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");
    }

    @Test
    public void testSetReturnValueOutsideExpectShouldProvideErrorInformation() {
        try {
            mock.willReturn(1);
        } catch (MockProcessErrorReporter reporter) {
            assertStringEqualWithWildcardCharacter("Mock Process Error:\n" +
                            "\treturn value cannot be set outside expectation:\n" +
                            "\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testSetReturnValueOutsideExpectShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n",
                    reporter.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");
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
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testUnexpectedParameterShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n",
                    ex.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");
    }

    @Test
    public void testMultipleVerificationFailShouldProvideAllErrorInformation() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
        });

        dummy.doNothing();

        try {
            mock.verify();
        } catch (VerificationFailedReporter ex) {
            assertStringEqualWithWildcardCharacter("Verification failed:\n" +
                            "\texpected invocation not happened:\n" +
                            "\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                            "\t\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.lambda$testMultipleVerificationFailShouldProvideAllErrorInformation$?(VerificationFailedReporterTest.java:?)\n" +
                            "\t\t   at cn.michaelwang.himock.report.VerificationFailedReporterTest.testMultipleVerificationFailShouldProvideAllErrorInformation(VerificationFailedReporterTest.java:?)\n",
                    ex.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");
    }

    @Test
    public void testSetTimerOutExpectShouldProvideErrorInformation() {
        try {
            mock.times(3);
        } catch (HiMockReporter reporter) {
            assertStringEqualWithWildcardCharacter("Mock Process Error:\n" +
                            "\ttimer cannot be set outside expectation:\n" +
                            "\t-> at cn.michaelwang.himock.report.VerificationFailedReporterTest.testSetTimerOutExpectShouldProvideErrorInformation(VerificationFailedReporterTest.java:?)\n",
                    reporter.getMessage());
            return;
        }

        fail("Exception is expected, the test should return in the catch block.");
    }

    private void assertStringEqualWithWildcardCharacter(String expected, String actually) {
        String[] splitted = expected.split("\\?");

        boolean first = true;
        int start;
        int end = 0;
        for (String sub : splitted) {
            start = actually.indexOf(sub, end);
            end = start + sub.length();

            if (first) {
                if (start != 0) {
                    fail("expected:\n" + expected + "\n" + "actually:\n" + actually);
                    return;
                }
                first = false;
            }

            if (start == -1) {
                fail("expected:\n" + expected + "\n" + "actually:\n" + actually);
                return;
            }
        }

        for (int i = end; i < actually.length(); i++) {
            assertEquals('?', actually.charAt(i));
        }
    }
}
