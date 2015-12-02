package cn.michaelwang.himock;

import cn.michaelwang.himock.verify.VerificationFailedReporter;
import org.junit.Test;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockParameterTest extends HiMockBaseTest {
    @Test
    public void testMockWithOneIntParameterAndCalledShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.withOneIntParameter(1);
        });

        dummy.withOneIntParameter(1);

        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testMockWithOneIntParameterCalledWithAnotherShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);
        mock.expect(() -> {
            dummy.withOneIntParameter(1);
        });

        dummy.withOneIntParameter(2);

        mock.verify();
    }

    @Test
    public void testMockWithMultipleIntParametersAndCalledShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);
        mock.expect(() -> {
            dummy.withMultipleIntParameters(1, 2);
        });

        dummy.withMultipleIntParameters(1, 2);

        mock.verify();
    }

    @Test
    public void testMockWithObjectParametersAndCalledShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.withObjectParameters("o1", "o2");
        });

        dummy.withObjectParameters("o1", "o2");

        mock.verify();
    }

    @Test
    public void testMockWithObjectParametersAndCalledWithNotTheSameParametersShouldFail() {
        reportTest(() -> {
                    MockedInterface dummy = mock.mock(MockedInterface.class);

                    mock.expect(() -> {
                        dummy.withObjectParameters("o1", "o2");
                    });

                    dummy.withObjectParameters("o1", "o3");

                    mock.verify();
                }, "Verification failed:\n" +
                        "\tinvocation with unexpected parameters:\n" +
                        "\t\tmethod called:\tcn.michaelwang.himock.MockedInterface.withObjectParameters\n" +
                        "\t\tparameters expected:\to1\to2\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockParameterTest.lambda$null$?(HiMockParameterTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockParameterTest.lambda$testMockWithObjectParametersAndCalledWithNotTheSameParametersShouldFail$?(HiMockParameterTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockParameterTest.testMockWithObjectParametersAndCalledWithNotTheSameParametersShouldFail(HiMockParameterTest.java:?)\n" +
                        "\t\tparameters actually:\to1\to3\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockParameterTest.lambda$testMockWithObjectParametersAndCalledWithNotTheSameParametersShouldFail$?(HiMockParameterTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockParameterTest.testMockWithObjectParametersAndCalledWithNotTheSameParametersShouldFail(HiMockParameterTest.java:?)\n"
        );
    }
}
