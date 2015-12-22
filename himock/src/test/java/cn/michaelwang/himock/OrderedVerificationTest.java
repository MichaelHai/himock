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
                "\t\tverified order:\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                "\t\tactual order:\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n");
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
                "\t\tverified order:\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnBoolean()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\tactual order:\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnInt()\n" +
                "\t\t\tcn.michaelwang.himock.MockedInterface.returnBoolean()\n");
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
}
