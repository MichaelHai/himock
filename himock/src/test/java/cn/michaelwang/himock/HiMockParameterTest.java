package cn.michaelwang.himock;

import cn.michaelwang.himock.report.VerificationFailedReporter;
import org.junit.Test;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockParameterTest extends HiMockTest {
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

    @Test(expected = VerificationFailedReporter.class)
    public void testMockWithObjectParametersAndCalledWithNotTheSameParametersShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.withObjectParameters("o1", "o2");
        });

        dummy.withObjectParameters("o1", "o3");

        mock.verify();
    }
}
