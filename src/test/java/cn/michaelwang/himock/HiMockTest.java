package cn.michaelwang.himock;

import cn.michaelwang.himock.report.VerificationFailedReporter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HiMockTest {
    private HiMock mock;

    @Before
    public void init() {
        mock = new HiMock();
    }

    @Test
    public void testMockObjectCanBeCreated() {
        MockedInterface mockedObject = mock.mock(MockedInterface.class);
        assertNotNull("mockedObject should not be null", mockedObject);
    }

    @Test(expected = MockNoninterfaceException.class)
    public void testClassCannotBeMocked() {
        class DummyClass{}
        mock.mock(DummyClass.class);
    }

    @Test
    public void testAlwaysSatisfiedExpectation() {
        mock.verify();
    }

    @Test
    public void testVerifyCalledInvocationShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expectStart();
        dummy.doNothing();
        mock.expectEnd();

        dummy.doNothing();

        mock.verify();
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testNotCalledExpectationShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expectStart();
        dummy.doNothing();
        mock.expectEnd();

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

        //noinspection CodeBlock2Expr
        mock.expect(() -> {
            dummy.doNothing();
        });

        dummy.doNothing();

        mock.verify();
    }

    @Test
    public void testExpectReturnPrimitiveType() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(1);
        });

        int returnValue = dummy.returnInt();

        assertEquals(1, returnValue);
    }

    @Test
    public void testExpectReturnBooleanType() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnBoolean();
            mock.willReturn(true);
        });

        boolean returnValue = dummy.returnBoolean();

        assertEquals(true, returnValue);
    }


}