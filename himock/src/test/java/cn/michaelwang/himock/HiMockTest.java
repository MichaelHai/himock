package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;
import cn.michaelwang.himock.verify.VerificationFailedReporter;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings({"CodeBlock2Expr", "Convert2MethodRef"})
public class HiMockTest extends HiMockBaseTest {

    @Test
    public void testMockObjectCanBeCreated() {
        MockedInterface mockedObject = mock(MockedInterface.class);
        assertNotNull("mockedObject should not be null", mockedObject);
    }

    @Test
    public void testClassCannotBeMocked() {
        reportTest(() -> {
                    mock(String.class);
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
        verify();
    }

    @Test
    public void testVerifyCalledInvocationShouldPass() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.doNothing();
        });

        dummy.doNothing();

        verify();
    }

    @Test
    public void testNotCalledExpectationShouldFail() {
        reportTest(() -> {
                    MockedInterface dummy = mock(MockedInterface.class);

                    expect(() -> {
                        dummy.doNothing();
                    });

                    verify();
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
                    MockedInterface dummy = mock(MockedInterface.class);

                    expect(() -> {
                        dummy.withObjectArguments("o1", "o2");
                    });
                    verify();
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
        MockedInterface dummy = mock(MockedInterface.class);

        int result = dummy.returnInt();

        assertEquals(0, result);
        verify();
    }

    @Test
    public void testVerificationInvocationOnMockObject() {
        MockedInterface dummy = mock(MockedInterface.class);

        dummy.doNothing();

        verify(() -> {
            dummy.doNothing();
        });
    }

    @Test
    public void testExpectedAndVerifiedWithOneInvocationShouldPass() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
            willReturn(10);
        });

        assertEquals(10, dummy.returnInt());

        verify(() -> {
            dummy.returnInt();
        });
    }

    @Test(expected = HiMockReporter.class)
    public void testNotCalledButVerifiedShouldFail() {
        MockedInterface dummy = mock(MockedInterface.class);

        verify(() -> {
            dummy.returnInt();
        });
    }

    @Ignore
    @Test(expected = HiMockReporter.class)
    public void testCalledOnceButVerifiedTwiceShouldFail() {
        MockedInterface dummy = mock(MockedInterface.class);

        dummy.returnInt();

        verify(() -> {
            dummy.returnInt();
            dummy.returnInt();
        });
    }


    @Test
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndCalledShouldPass() {
        MockedInterface dummy1 = mock(MockedInterface.class);
        MockedInterface dummy2 = mock(MockedInterface.class);

        expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            willReturn(1);
        });

        dummy1.doNothing();
        int result = dummy2.returnInt();

        assertEquals(1, result);
        verify();
    }

    @Test
    public void testMockObjectOfTheSameInterfaceBothExpectedAndCalledShouldReturnCorrectly() {
        MockedInterface dummy1 = mock(MockedInterface.class);
        MockedInterface dummy2 = mock(MockedInterface.class);

        expect(() -> {
            dummy1.returnInt();
            willReturn(10);
            dummy2.returnInt();
            willReturn(1);
        });

        assertEquals(10, dummy1.returnInt());
        assertEquals(1, dummy2.returnInt());
        verify();
    }

    @Test
    public void testMockObjectOfTheSameInterfaceBothExpectedAndCalledInReverseOrderShouldReturnCorrectly() {
        MockedInterface dummy1 = mock(MockedInterface.class);
        MockedInterface dummy2 = mock(MockedInterface.class);

        expect(() -> {
            dummy1.returnInt();
            willReturn(10);
            dummy2.returnInt();
            willReturn(1);
        });

        assertEquals(1, dummy2.returnInt());
        assertEquals(10, dummy1.returnInt());
        verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndOneCalledShouldFail() {
        MockedInterface dummy1 = mock(MockedInterface.class);
        MockedInterface dummy2 = mock(MockedInterface.class);

        expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            willReturn(1);
        });

        dummy1.doNothing();

        verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndCalledFromWrongObjectShouldFail() {
        MockedInterface dummy1 = mock(MockedInterface.class);
        MockedInterface dummy2 = mock(MockedInterface.class);

        expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            willReturn(1);
        });

        dummy2.doNothing();
        dummy1.returnInt();

        verify();
    }

    @Test
    public void testTwoDifferentInterfaceCanBeMocked() {
        MockedInterface dummy = mock(MockedInterface.class);
        List<?> dummyList = mock(List.class);

        expect(() -> {
            dummy.doNothing();
            dummyList.size();
            willReturn(10);
        });

        dummy.doNothing();
        assertEquals(10, dummyList.size());

        verify();
    }

    @Test
    public void testMockTheSameMethodOnTheSameMockObjectShouldReturnCorrect() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
            willReturn(10);
            dummy.returnInt();
            willReturn(0);
        });

        assertEquals(10, dummy.returnInt());
        assertEquals(0, dummy.returnInt());
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testMockTheSameMethodOnTheSameMockObjectButCalledOnceShouldFail() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
            willReturn(10);
            willReturn(0);
        });

        assertEquals(10, dummy.returnInt());

        verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testOverloadFunctions() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
        });

        dummy.returnInt(1);

        verify();
    }
}