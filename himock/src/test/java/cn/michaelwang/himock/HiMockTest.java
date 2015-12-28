package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;
import cn.michaelwang.himock.verify.VerificationFailedReporter;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings({"CodeBlock2Expr", "Convert2MethodRef"})
public class HiMockTest extends HiMockBaseTest {

    @Test
    public void testMockObjectCanBeCreated() {
        MockedInterface mockedObject = mock.mock(MockedInterface.class);
        assertNotNull("mockedObject should not be null", mockedObject);
    }

    @Test
    public void testClassCannotBeMocked() {
        reportTest(() -> {
                    mock.mock(String.class);
                }, "Mock Process Error:\n" +
                        "\tonly interface can(should) be mocked:\n" +
                        "\t\tclass being mocked: java.lang.String\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockTest.lambda$testClassCannotBeMocked$?(HiMockTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockTest.testClassCannotBeMocked(HiMockTest.java:?)\n"
        );
    }

    @Test
    public void testAlwaysSatisfiedExpectation() {
        mock.verify();
    }

    @Test
    public void testVerifyCalledInvocationShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.doNothing();
        });

        dummy.doNothing();

        mock.verify();
    }

    @Test
    public void testNotCalledExpectationShouldFail() {
        reportTest(() -> {
                    MockedInterface dummy = mock.mock(MockedInterface.class);

                    mock.expect(() -> {
                        dummy.doNothing();
                    });

                    mock.verify();
                }, "Verification failed:\n" +
                        "\texpected invocation not happened:\n" +
                        "\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockTest.lambda$null$?(HiMockTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockTest.lambda$testNotCalledExpectationShouldFail$?(HiMockTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockTest.testNotCalledExpectationShouldFail(HiMockTest.java:?)\n"
        );
    }

    @Test
    public void testNotCalledExpectationWithArgs() {
        reportTest(() -> {
                    MockedInterface dummy = mock.mock(MockedInterface.class);

                    mock.expect(() -> {
                        dummy.withObjectArguments("o1", "o2");
                    });
                    mock.verify();
                }, "Verification failed:\n" +
                        "\texpected invocation not happened:\n" +
                        "\t\tcn.michaelwang.himock.MockedInterface.withObjectArguments(o1, o2)\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockTest.lambda$null$?(HiMockTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockTest.lambda$testNotCalledExpectationWithArgs$?(HiMockTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockTest.testNotCalledExpectationWithArgs(HiMockTest.java:?)\n"
        );
    }

    @Test
    public void testUnexpectedInvocationShouldReturnDefaultValue() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        int result = dummy.returnInt();

        assertEquals(0, result);
        mock.verify();
    }

    @Test
    public void testVerificationInvocationOnMockObject() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();

        mock.verify(() -> {
            dummy.doNothing();
        });
    }

    @Test
    public void testExpectedAndVerifiedWithOneInvocationShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(10);
        });

        assertEquals(10, dummy.returnInt());

        mock.verify(() -> {
            dummy.returnInt();
        });
    }

    @Test(expected = HiMockReporter.class)
    public void testNotCalledButVerifiedShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.verify(() -> {
            dummy.returnInt();
        });
    }

    @Ignore
    @Test(expected = HiMockReporter.class)
    public void testCalledOnceButVerifiedTwiceShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.returnInt();

        mock.verify(() -> {
            dummy.returnInt();
            dummy.returnInt();
        });
    }


    @Test
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndCalledShouldPass() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            mock.willReturn(1);
        });

        dummy1.doNothing();
        int result = dummy2.returnInt();

        assertEquals(1, result);
        mock.verify();
    }

    @Test
    public void testMockObjectOfTheSameInterfaceBothExpectedAndCalledShouldReturnCorrectly() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy1.returnInt();
            mock.willReturn(10);
            dummy2.returnInt();
            mock.willReturn(1);
        });

        assertEquals(10, dummy1.returnInt());
        assertEquals(1, dummy2.returnInt());
        mock.verify();
    }

    @Test
    public void testMockObjectOfTheSameInterfaceBothExpectedAndCalledInReverseOrderShouldReturnCorrectly() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy1.returnInt();
            mock.willReturn(10);
            dummy2.returnInt();
            mock.willReturn(1);
        });

        assertEquals(1, dummy2.returnInt());
        assertEquals(10, dummy1.returnInt());
        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndOneCalledShouldFail() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            mock.willReturn(1);
        });

        dummy1.doNothing();

        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndCalledFromWrongObjectShouldFail() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            mock.willReturn(1);
        });

        dummy2.doNothing();
        dummy1.returnInt();

        mock.verify();
    }

    @Test
    public void testTwoDifferentInterfaceCanBeMocked() {
        MockedInterface dummy = mock.mock(MockedInterface.class);
        List<?> dummyList = mock.mock(List.class);

        mock.expect(() -> {
            dummy.doNothing();
            dummyList.size();
            mock.willReturn(10);
        });

        dummy.doNothing();
        assertEquals(10, dummyList.size());

        mock.verify();
    }

    @Test
    public void testMockTheSameMethodOnTheSameMockObjectShouldReturnCorrect() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(10);
            dummy.returnInt();
            mock.willReturn(0);
        });

        assertEquals(10, dummy.returnInt());
        assertEquals(0, dummy.returnInt());
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testMockTheSameMethodOnTheSameMockObjectButCalledOnceShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(10);
            mock.willReturn(0);
        });

        assertEquals(10, dummy.returnInt());

        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testOverloadFunctions() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
        });

        dummy.returnInt(1);

        mock.verify();
    }
}