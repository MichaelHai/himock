package cn.michaelwang.himock;

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
}
