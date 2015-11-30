package cn.michaelwang.himock;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockExceptionTest extends HiMockBaseTest {
    @Test(expected = UserException.class)
    public void testExpectThrowCheckedUserException() throws UserException {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.throwException();
            mock.willThrow(new UserException());
        });

        dummy.throwException();
    }

    @Test
    public void testExpectThrowCheckedUserExceptionWithTimer() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.throwException();
            mock.willThrow(new UserException()).times(2);
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
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.throwException();
            mock.willThrow(new UserException()).willReturn(2).times(2).willThrow(new UserException());
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
            mock.willThrow(new Exception());
        }, "Mock Process Error:\n" +
                "\texception thrown cannot be set outside expectation:\n" +
                "\t-> at cn.michaelwang.himock.HiMockExceptionTest.lambda$testExpectThrowOutsideExpectation$?(HiMockExceptionTest.java:?)\n" +
                "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                "\t   at cn.michaelwang.himock.HiMockExceptionTest.testExpectThrowOutsideExpectation(HiMockExceptionTest.java:?)\n");
    }
}
