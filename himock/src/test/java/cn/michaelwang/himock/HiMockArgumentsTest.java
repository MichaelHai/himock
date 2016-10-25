package cn.michaelwang.himock;

import org.junit.Test;

import cn.michaelwang.himock.process.verifiers.VerificationFailedReporter;

import static cn.michaelwang.himock.HiMock.*;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockArgumentsTest extends HiMockBaseTest {
    @Test
    public void testMockWithOneIntArgumentsAndCalledShouldPass() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.withOneIntArgument(1);
        });

        dummy.withOneIntArgument(1);

        verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testMockWithOneIntArgumentsCalledWithAnotherShouldFail() {
        MockedInterface dummy = mock(MockedInterface.class);
        expect(() -> {
            dummy.withOneIntArgument(1);
        });

        dummy.withOneIntArgument(2);

        verify();
    }

    @Test
    public void testMockWithMultipleIntArgumentsAndCalledShouldPass() {
        MockedInterface dummy = mock(MockedInterface.class);
        expect(() -> {
            dummy.withMultipleIntArguments(1, 2);
        });

        dummy.withMultipleIntArguments(1, 2);

        verify();
    }

    @Test
    public void testMockWithObjectArgumentsAndCalledShouldPass() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.withObjectArguments("o1", "o2");
        });

        dummy.withObjectArguments("o1", "o2");

        verify();
    }

    @Test
    public void testMockWithObjectArgumentsAndCalledWithNotTheSameArgumentsShouldFail() {
    	
        reportTest(() -> {
                    MockedInterface dummy = mock(MockedInterface.class);

                    expect(() -> {
                        dummy.withObjectArguments("o1", "o2");
                    });

                    dummy.withObjectArguments("o1", "o3");

                    verify();
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
