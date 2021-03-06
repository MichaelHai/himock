package cn.michaelwang.himock;

import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockExceptionTest extends HiMockBaseTest {
    @Test(expected = UserException.class)
    public void testExpectThrowCheckedUserException() throws UserException {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.throwException();
            willThrow(new UserException());
        });

        dummy.throwException();
    }

    @Test
    public void testExpectThrowCheckedUserExceptionWithTimer() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.throwException();
            willThrow(new UserException());
            times(2);
        });

        int times = 0;
        try {
            dummy.throwException();
        } catch (UserException ex) {
            times++;
        }

        try {
            dummy.throwException();
        } catch (UserException ex) {
            times++;
        }

        assertEquals(2, times);
    }

    @Test
    public void testExpectThrowAndReturnCanBeMixed() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.throwException();
            willThrow(new UserException());
            willReturn(2);
            times(2);
            willThrow(new UserException());
        });

        int times = 0;
        try {
            dummy.throwException();
        } catch (UserException ex) {
            times++;
        }

        try {
            assertEquals(2, dummy.throwException());
            assertEquals(2, dummy.throwException());
        } catch (UserException ex) {
            fail("No more exception should be thrown");
        }
        try {
            dummy.throwException();
        } catch (UserException ex) {
            times++;
        }
        assertEquals(2, times);
    }

    @Test
    public void testExpectThrowOutsideExpectation() {
        reportTest(() -> {
            willThrow(new Exception());
        }, "Mock Process Error:\n" +
                "\texception thrown cannot be set outside expectation:\n" +
                "\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$testExpectThrowOutsideExpectation$?(HiMockExceptionTest.java:?)\n" +
                "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                "\t   at cn.michaelwang.himock.HiMockExceptionTest.testExpectThrowOutsideExpectation(HiMockExceptionTest.java:?)\n");
    }

    @Test
    public void testExpectUnsuitableExceptionType() {
        reportTest(() -> {
                    MockedInterface dummy = mock(MockedInterface.class);
                    expect(() -> {
                        dummy.throwException();
                        willThrow(new Exception());
                    });
                }, "Mock Process Error:\n" +
                        "\texception type is not match:\n" +
                        "\t\tmethod setting exception: cn.michaelwang.himock.MockedInterface.throwException()\n" +
                        "\t\texception type expected:\n" +
                        "\t\t\tcn.michaelwang.himock.UserException\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testExpectUnsuitableExceptionType$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.testExpectUnsuitableExceptionType(HiMockExceptionTest.java:?)\n" +
                        "\t\texception type being set:\n" +
                        "\t\t\tjava.lang.Exception\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testExpectUnsuitableExceptionType$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.testExpectUnsuitableExceptionType(HiMockExceptionTest.java:?)\n"
        );
    }

    @Test
    public void testSetThrownWhenNoCallExpected() {
        reportTest(() -> {
                    expect(() -> {
                        willThrow(new Exception());
                    });
                }, "Mock Process Error:\n" +
                        "\texception cannot be set before invocation expectation:\n" +
                        "\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testSetThrownWhenNoCallExpected$?(HiMockExceptionTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockExceptionTest.testSetThrownWhenNoCallExpected(HiMockExceptionTest.java:?)\n"
        );
    }

    @Test(expected = UserException.class)
    public void testCallMoreThenExpectedWithException() throws UserException {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.throwException();
            willThrow(new UserException());
        });

        try {
            dummy.throwException();
        } catch (UserException ex) {
        }

        dummy.throwException();
    }

    @Test
    public void testThrowCheckedExceptionForNotDeclaredMethod() {
        MockedInterface dummy = mock(MockedInterface.class);

        reportTest(() -> {
                    expect(() -> {
                        dummy.returnInt();
                        willThrow(new UserException());
                    });
                }, "Mock Process Error:\n" +
                        "\texception type is not match:\n" +
                        "\t\tmethod setting exception: cn.michaelwang.himock.MockedInterface.returnInt()\n" +
                        "\t\texception type expected:\n" +
                        "\t\t\t(none)\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testThrowCheckedExceptionForNotDeclaredMethod$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.testThrowCheckedExceptionForNotDeclaredMethod(HiMockExceptionTest.java:?)\n" +
                        "\t\texception type being set:\n" +
                        "\t\t\tcn.michaelwang.himock.UserException\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testThrowCheckedExceptionForNotDeclaredMethod$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.testThrowCheckedExceptionForNotDeclaredMethod(HiMockExceptionTest.java:?)\n"
        );
    }

    @Test(expected = UserUncheckedException.class)
    public void testThrowUncheckedExceptionForMethod() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
            willThrow(new UserUncheckedException());
        });

        dummy.returnInt();
    }

    @Test(expected = UserUncheckedException.class)
    public void testThrowUncheckedExceptionForMethodDeclaredException() throws UserException {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.throwException();
            willThrow(new UserUncheckedException());
        });

        dummy.throwException();
    }

    @Test(expected = UserUncheckedException.class)
    public void testUseWillReturnSetNotSuitableUncheckedException() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
            willReturn(new UserUncheckedException());
        });

        dummy.returnInt();
    }

    @Test(expected = UserException.class)
    public void testUseWillReturnSetCheckedExceptionNotMatchReturnTypeButExceptionType() throws UserException {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.throwException();
            willReturn(new UserException());
        });

        dummy.throwException();
    }

    @Test
    public void testUseWillReturnSetCheckedExceptionNotMatchEitherReturnTypeOrExceptionType() throws UserException {
        reportTest(() -> {
                    MockedInterface dummy = mock(MockedInterface.class);
                    expect(() -> {
                        dummy.throwException();
                        willReturn(new Exception());
                    });
                }, "Mock Process Error:\n" +
                        "\texception type is not match:\n" +
                        "\t\tmethod setting exception: cn.michaelwang.himock.MockedInterface.throwException()\n" +
                        "\t\texception type expected:\n" +
                        "\t\t\tcn.michaelwang.himock.UserException\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testUseWillReturnSetCheckedExceptionNotMatchEitherReturnTypeOrExceptionType$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.testUseWillReturnSetCheckedExceptionNotMatchEitherReturnTypeOrExceptionType(HiMockExceptionTest.java:?)\n" +
                        "\t\texception type being set:\n" +
                        "\t\t\tjava.lang.Exception\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testUseWillReturnSetCheckedExceptionNotMatchEitherReturnTypeOrExceptionType$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.testUseWillReturnSetCheckedExceptionNotMatchEitherReturnTypeOrExceptionType(HiMockExceptionTest.java:?)\n"
        );
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void testUseWillReturnSetUncheckedExceptionOfTheReturnTypeShouldReturn() {
        MockedInterface dummy = mock(MockedInterface.class);

        UserUncheckedException exception = new UserUncheckedException();
        expect(() -> {
            dummy.returnUserUncheckedException();
            willReturn(exception);
        });

        assertEquals(exception, dummy.returnUserUncheckedException());

        verify();
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test(expected = AnotherUserUncheckedException.class)
    public void testUseWillReturnSetUncheckedExceptionNotTheSameWithTheReturnTypeShouldThrow() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnUserUncheckedException();
            willReturn(new AnotherUserUncheckedException());
        });

        dummy.returnUserUncheckedException();
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void testUseWillReturnSetCheckedExceptionOfTheReturnTypeShouldReturn() {
        MockedInterface dummy = mock(MockedInterface.class);

        UserException exception = new UserException();
        expect(() -> {
            dummy.returnUserException();
            willReturn(exception);
        });

        assertEquals(exception, dummy.returnUserException());

        verify();
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void testUseWillReturnSetCheckedExceptionNotTheSameWithTheReturnTypeAndNotDeclaredShouldFail() {
        MockedInterface dummy = mock(MockedInterface.class);

        reportTest(() -> {
                    expect(() -> {
                        dummy.returnUserException();
                        willReturn(new Exception());
                    });
                }, "Mock Process Error:\n" +
                        "\texception type is not match:\n" +
                        "\t\tmethod setting exception: cn.michaelwang.himock.MockedInterface.returnUserException()\n" +
                        "\t\texception type expected:\n" +
                        "\t\t\t(none)\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testUseWillReturnSetCheckedExceptionNotTheSameWithTheReturnTypeAndNotDeclaredShouldFail$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.testUseWillReturnSetCheckedExceptionNotTheSameWithTheReturnTypeAndNotDeclaredShouldFail(HiMockExceptionTest.java:?)\n" +
                        "\t\texception type being set:\n" +
                        "\t\t\tjava.lang.Exception\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$null$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.lambda$testUseWillReturnSetCheckedExceptionNotTheSameWithTheReturnTypeAndNotDeclaredShouldFail$?(HiMockExceptionTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockExceptionTest.testUseWillReturnSetCheckedExceptionNotTheSameWithTheReturnTypeAndNotDeclaredShouldFail(HiMockExceptionTest.java:?)\n"
        );
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test(expected = Exception.class)
    public void testUseWillReturnSetCheckedExceptionNotTheSameWithTheReturnTypeButDeclaredShouldThrow() throws Exception {
        MockedInterface dummy = mock(MockedInterface.class);

        Exception exception = new Exception();
        expect(() -> {
            dummy.returnUserExceptionAndThrow();
            willReturn(exception);
        });

        dummy.returnUserExceptionAndThrow();
    }
}
