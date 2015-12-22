package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;
import org.junit.Test;

@SuppressWarnings("CodeBlock2Expr")
public class OrderedVerificationTest extends HiMockBaseTest {
    @Test
    public void testOrderedVerification() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.returnInt();
        dummy.doNothing();
        dummy.returnInt();

        mock.verifyInOrder(() -> {
            dummy.returnInt();
            dummy.doNothing();
            dummy.returnInt();
        });
    }

    @Test
    public void testNotTheSameOrderShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.returnInt();
        dummy.doNothing();
        dummy.returnInt();

        reportTest(() -> {
            mock.verifyInOrder(() -> {
                dummy.returnInt();
                dummy.returnInt();
                dummy.doNothing();
            });
        }, "Verification failed:\n" +
                "\tmethod called in different order:\n" +
                "\t\tactual order:\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\tverified order:\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                "\t\t-> at cn.michaelwang.himock.OrderedVerificationTest.lambda$null$?(OrderedVerificationTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.OrderedVerificationTest.lambda$testNotTheSameOrderShouldFail$?(OrderedVerificationTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.OrderedVerificationTest.testNotTheSameOrderShouldFail(OrderedVerificationTest.java:?)\n");
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void testOrderWithOtherCallsCanBeVerified() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.returnInt();
        dummy.returnObject();
        dummy.returnBoolean();
        dummy.returnUserException();

        mock.verifyInOrder(() -> {
            dummy.returnInt();
            dummy.returnBoolean();
        });
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void testWithOtherCallsNotOrderedShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.returnInt();
        dummy.returnObject();
        dummy.returnBoolean();
        dummy.returnUserException();

        reportTest(() -> {
            mock.verifyInOrder(() -> {
                dummy.returnBoolean();
                dummy.returnInt();
            });
        }, "Verification failed:\n" +
                "\tmethod called in different order:\n" +
                "\t\tactual order:\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnBoolean()\n" +
                "\t\tverified order:\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnBoolean()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t-> at cn.michaelwang.himock.OrderedVerificationTest.lambda$null$?(OrderedVerificationTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.OrderedVerificationTest.lambda$testWithOtherCallsNotOrderedShouldFail$?(OrderedVerificationTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                "\t\t   at cn.michaelwang.himock.OrderedVerificationTest.testWithOtherCallsNotOrderedShouldFail(OrderedVerificationTest.java:?)\n");
    }

    @Test
    public void testDifferentMockedObjectCanBeVerifiedWithOrder() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        dummy1.doNothing();
        dummy2.doNothing();

        mock.verifyInOrder(() -> {
            dummy1.doNothing();
            dummy2.doNothing();
        });
    }


    @Test(expected = HiMockReporter.class)
    public void testDifferentMockedObjectNotOrderedShouldFail() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        dummy1.doNothing();
        dummy2.doNothing();

        mock.verifyInOrder(() -> {
            dummy2.doNothing();
            dummy1.doNothing();
        });
    }

    @Test
    public void testTwoOrderSequence() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();
        dummy.returnInt();
        dummy.returnBoolean();
        dummy.returnObject();

        mock.verifyInOrder(() -> {
            dummy.doNothing();
            dummy.returnBoolean();
        });

        mock.verifyInOrder(() -> {
            dummy.returnInt();
            dummy.returnObject();
        });
    }

    @Test
    public void testTwoOrderSequenceWithOverlap() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();
        dummy.returnInt();
        dummy.returnBoolean();

        mock.verifyInOrder(() -> {
            dummy.doNothing();
            dummy.returnBoolean();
        });

        mock.verifyInOrder(() -> {
            dummy.doNothing();
            dummy.returnInt();
        });
    }

    @Test(expected = HiMockReporter.class)
    public void testTwoOrderSequenceWithOverlapFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();
        dummy.returnInt();
        dummy.returnBoolean();

        mock.verifyInOrder(() -> {
            dummy.returnBoolean();
            dummy.doNothing();
        });

        mock.verifyInOrder(() -> {
            dummy.doNothing();
            dummy.returnInt();
        });
    }

    @Test
    public void testTheSameCallBeforeAndAfterShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();
        dummy.returnInt();
        dummy.doNothing();

        mock.verifyInOrder(() -> {
            dummy.returnInt();
            dummy.doNothing();
        });
    }
}
