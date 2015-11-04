package cn.michaelwang.himock;

import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedReporter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockTest {
    protected HiMock mock;

    @Before
    public void init() {
        mock = new HiMock();
    }

    @Test
    public void testMockObjectCanBeCreated() {
        MockedInterface mockedObject = mock.mock(MockedInterface.class);
        assertNotNull("mockedObject should not be null", mockedObject);
    }

    @Test(expected = MockProcessErrorReporter.class)
    public void testClassCannotBeMocked() {
        class DummyClass {
        }
        mock.mock(DummyClass.class);
    }

    @Test
    public void testAlwaysSatisfiedExpectation() {
        mock.verify();
    }

    @Test
    public void testVerifyCalledInvocationShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.doNothing();
        });

        dummy.doNothing();

        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testNotCalledExpectationShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.doNothing();
        });

        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testUnexpectedInvocationShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();

        mock.verify();
    }

    @Test
    public void testLambdaSyntax() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.doNothing();
        });

        dummy.doNothing();

        mock.verify();
    }
}