package cn.michaelwang.himock;

import cn.michaelwang.himock.report.MockProcessErrorReporter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HiMockReturnValueTest extends HiMockTest {
    @Test
    public void testExpectReturnIntType() {
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

    @Test(expected = MockProcessErrorReporter.class)
    public void testNoReturnShouldThrowException() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.doNothing();
            mock.willReturn(1);
        });

        dummy.doNothing();
    }

    @Test
    public void testExpectReturnObjectType() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        String expectedReturn = "to return";
        mock.expect(() -> {
            dummy.returnObject();
            mock.willReturn(expectedReturn);
        });

        Object returnValue = dummy.returnObject();

        assertEquals(expectedReturn, returnValue);
    }

    @Test
    public void testNoExpectationShouldReturnDefaultValue() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            dummy.returnBoolean();
            dummy.returnObject();
        });

        int intReturnValue = dummy.returnInt();
        boolean booleanReturnValue = dummy.returnBoolean();
        Object objectReturnValue = dummy.returnObject();

        assertEquals(0, intReturnValue);
        assertEquals(false, booleanReturnValue);
        assertEquals(null, objectReturnValue);
    }

    @Test(expected = MockProcessErrorReporter.class)
    public void testCannotSetNotSuitableTypeValue() {
        MockedInterface dummy = mock.mock(MockedInterface.class);

        mock.expect(() -> {
            dummy.returnInt();
            mock.willReturn(true);
        });

        dummy.returnInt();

        mock.verify();
    }

    @Test(expected = MockProcessErrorReporter.class)
    public void testCannotSetReturnValueOutsideExpect() {
        mock.willReturn(1);
    }
}
