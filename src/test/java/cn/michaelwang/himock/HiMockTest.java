package cn.michaelwang.himock;

import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedReporter;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void testUnexpectedInvocationShouldReturnDefaultValue() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        int result = dummy.returnInt();

        assertEquals(0, result);
        mock.verify();
    }

    @Test
    public void testVerificationInvocationOnMockObject() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        dummy.doNothing();

        mock.verify(() -> {
            dummy.doNothing();
        });
    }

    @Test(expected = MockProcessErrorReporter.class)
    public void testExpectReturnInVerificationShouldFail() {
        mock.verify(() -> {
            mock.willReturn(1);
        });
    }

    @Test
    public void testExpectedAndVerifiedWithOneInvocationShouldPass() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(10);
        });

        assertEquals(10, dummy.returnInt());

        mock.verify(() -> {
            dummy.returnInt();
        });
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

    @Test
    public void testTwoDifferentInterfaceCanBeMocked() {
        MockedInterface dummy = mock.mock(MockedInterface.class);
        List<?> dummyList = mock.mock(List.class);

        mock.expect(() -> {
            dummy.doNothing();
            dummyList.size();
            mock.willReturn(10);
        });

        dummy.doNothing();
        assertEquals(10, dummyList.size());

        mock.verify();
    }

    @Test
    public void testMockTheSameMethodOnTheSameMockObjectShouldReturnCorrect() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(10);
            dummy.returnInt();
            mock.willReturn(0);
        });

        assertEquals(10, dummy.returnInt());
        assertEquals(0, dummy.returnInt());
    }

    @Test(expected = VerificationFailedReporter.class)
    public void testMockTheSameMethodOnTheSameMockObjectButCalledOnceShouldFail() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(10);
            mock.willReturn(0);
        });

        assertEquals(10, dummy.returnInt());

        mock.verify();
    }
}