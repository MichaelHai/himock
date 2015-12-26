package cn.michaelwang.himock;

import cn.michaelwang.himock.verify.VerificationFailedReporter;
import org.junit.Test;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockArgumentsTest extends HiMockBaseTest {
    @Test
    public void testMockWithOneIntArgumentsAndCalledShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.withOneIntArgument(1);
        });

        dummy.withOneIntArgument(1);

        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testMockWithOneIntArgumentsCalledWithAnotherShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);
        mock.expect(() -> {
            dummy.withOneIntArgument(1);
        });

        dummy.withOneIntArgument(2);

        mock.verify();
    }

    @Test
    public void testMockWithMultipleIntArgumentsAndCalledShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);
        mock.expect(() -> {
            dummy.withMultipleIntArguments(1, 2);
        });

        dummy.withMultipleIntArguments(1, 2);

        mock.verify();
    }

    @Test
    public void testMockWithObjectArgumentsAndCalledShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.withObjectArguments("o1", "o2");
        });

        dummy.withObjectArguments("o1", "o2");

        mock.verify();
    }

    @Test
    public void testMockWithObjectArgumentsAndCalledWithNotTheSameArgumentsShouldFail() {
        reportTest(() -> {
                    MockedInterface dummy = mock.mock(MockedInterface.class);

                    mock.expect(() -> {
                        dummy.withObjectArguments("o1", "o2");
                    });

                    dummy.withObjectArguments("o1", "o3");

                    mock.verify();
                }, "Verification failed:\n" +
                        "\tinvocation with unexpected arguments:\n" +
                        "\t\tmethod called:\tcn.michaelwang.himock.MockedInterface.withObjectArguments\n" +
                        "\t\targuments expected:\to1\to2\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockArgumentsTest.lambda$null$?(HiMockArgumentsTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockArgumentsTest.lambda$testMockWithObjectArgumentsAndCalledWithNotTheSameArgumentsShouldFail$?(HiMockArgumentsTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockArgumentsTest.testMockWithObjectArgumentsAndCalledWithNotTheSameArgumentsShouldFail(HiMockArgumentsTest.java:?)\n" +
                        "\t\targuments actually:\to1\to3\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockArgumentsTest.lambda$testMockWithObjectArgumentsAndCalledWithNotTheSameArgumentsShouldFail$?(HiMockArgumentsTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockArgumentsTest.testMockWithObjectArgumentsAndCalledWithNotTheSameArgumentsShouldFail(HiMockArgumentsTest.java:?)\n"
        );
    }
}
