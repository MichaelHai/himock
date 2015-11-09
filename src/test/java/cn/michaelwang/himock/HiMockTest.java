package cn.michaelwang.himock;

import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedReporter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndCalledShouldPass() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            mock.willReturn(1);
        });

        dummy1.doNothing();
        int result = dummy2.returnInt();

        assertEquals(1, result);
        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndOneCalledShouldFail() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            mock.willReturn(1);
        });

        dummy1.doNothing();

        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndCalledFromWrongObjectShouldFail() {
        MockedInterface dummy1 = mock.mock(MockedInterface.class);
        MockedInterface dummy2 = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            mock.willReturn(1);
        });

        dummy2.doNothing();
        dummy1.returnInt();

        mock.verify();
    }

}