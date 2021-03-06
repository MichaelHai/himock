package cn.michaelwang.himock;

import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("CodeBlock2Expr")
public class HiMockReturnValueTest extends HiMockBaseTest {
    @Test
    public void testExpectReturnIntType() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
            willReturn(1);
        });

        int returnValue = dummy.returnInt();

        assertEquals(1, returnValue);
    }

    @Test
    public void testExpectReturnBooleanType() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnBoolean();
            willReturn(true);
        });

        boolean returnValue = dummy.returnBoolean();

        assertEquals(true, returnValue);
    }

    @Test
    public void testNoReturnShouldThrowException() {
        reportTest(() -> {
                    MockedInterface dummy = mock(MockedInterface.class);

                    expect(() -> {
                        dummy.doNothing();
                        willReturn(1);
                    });
                }, "Mock Process Error:\n" +
                        "\tinvocation expected has no return value:\n" +
                        "\t\tcn.michaelwang.himock.MockedInterface.doNothing()\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockReturnValueTest.lambda$null$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.lambda$testNoReturnShouldThrowException$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.testNoReturnShouldThrowException(HiMockReturnValueTest.java:?)\n" +
                        "\t\treturn value being set:\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockReturnValueTest.lambda$null$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.lambda$testNoReturnShouldThrowException$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.testNoReturnShouldThrowException(HiMockReturnValueTest.java:?)\n"
        );
    }

    @Test
    public void testExpectReturnObjectType() {
        MockedInterface dummy = mock(MockedInterface.class);

        String expectedReturn = "to return";
        expect(() -> {
            dummy.returnObject();
            willReturn(expectedReturn);
        });

        Object returnValue = dummy.returnObject();

        assertEquals(expectedReturn, returnValue);
    }

    @Test
    public void testNoExpectationShouldReturnDefaultValue() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
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

    @Test
    public void testCannotSetNotSuitableTypeValue() {
        reportTest(() -> {
                    MockedInterface dummy = mock(MockedInterface.class);

                    expect(() -> {
                        dummy.returnInt();
                        willReturn(true);
                    });

                    dummy.returnInt();

                    verify();
                }, "Mock Process Error:\n" +
                        "\treturn value type is not match:\n" +
                        "\t\tmethod setting return value: cn.michaelwang.himock.MockedInterface.returnInt()\n" +
                        "\t\treturn type expected:\tint\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockReturnValueTest.lambda$null$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.lambda$testCannotSetNotSuitableTypeValue$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.testCannotSetNotSuitableTypeValue(HiMockReturnValueTest.java:?)\n" +
                        "\t\treturn type being set:\tboolean\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockReturnValueTest.lambda$null$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.lambda$testCannotSetNotSuitableTypeValue$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.testCannotSetNotSuitableTypeValue(HiMockReturnValueTest.java:?)\n"
        );
    }

    @Test
    public void testCannotSetNotSuitableObjectTypeValue() {
        reportTest(() -> {
                    MockedInterface dummy = mock(MockedInterface.class);

                    expect(() -> {
                        dummy.returnInt();
                        willReturn(new Object());
                    });

                    dummy.returnInt();

                    verify();
                }, "Mock Process Error:\n" +
                        "\treturn value type is not match:\n" +
                        "\t\tmethod setting return value: cn.michaelwang.himock.MockedInterface.returnInt()\n" +
                        "\t\treturn type expected:\tint\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockReturnValueTest.lambda$null$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.lambda$testCannotSetNotSuitableObjectTypeValue$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.testCannotSetNotSuitableObjectTypeValue(HiMockReturnValueTest.java:?)\n" +
                        "\t\treturn type being set:\tjava.lang.Object\n" +
                        "\t\t-> at cn.michaelwang.himock.HiMockReturnValueTest.lambda$null$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.lambda$testCannotSetNotSuitableObjectTypeValue$?(HiMockReturnValueTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t\t   at cn.michaelwang.himock.HiMockReturnValueTest.testCannotSetNotSuitableObjectTypeValue(HiMockReturnValueTest.java:?)\n"
        );
    }

    @Test
    public void testCannotSetReturnValueOutsideExpect() {
        reportTest(() -> {
                    willReturn(1);
                }, "Mock Process Error:\n" +
                        "\treturn value cannot be set outside expectation:\n" +
                        "\t-> at cn.michaelwang.himock.HiMockReturnValueTest.lambda$testCannotSetReturnValueOutsideExpect$?(HiMockReturnValueTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockReturnValueTest.testCannotSetReturnValueOutsideExpect(HiMockReturnValueTest.java:?)\n"
        );
    }

    @Test
    public void testSetReturnTwiceShouldReturnCorrectlyInOrder() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
            willReturn(10);
            willReturn(0);
        });

        assertEquals(10, dummy.returnInt());
        assertEquals(0, dummy.returnInt());
    }

    @Test
    public void testSetReturnWhenNoCallExpected() {
        reportTest(() -> {
                    expect(() -> {
                        willReturn(1);
                    });
                }, "Mock Process Error:\n" +
                        "\treturn value cannot be set before invocation expectation:\n" +
                        "\t-> at cn.michaelwang.himock.HiMockReturnValueTest.lambda$null$?(HiMockReturnValueTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockReturnValueTest.lambda$testSetReturnWhenNoCallExpected$?(HiMockReturnValueTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
                        "\t   at cn.michaelwang.himock.HiMockReturnValueTest.testSetReturnWhenNoCallExpected(HiMockReturnValueTest.java:?)\n"
        );
    }

    @Test
    public void testCallMoreThenExpected() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnInt();
            willReturn(10);
        });

        assertEquals(10, dummy.returnInt());
        assertEquals(10, dummy.returnInt());
    }

    @Test
    public void testSetSubtypeOfTheReturn() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.returnObject();
            willReturn("hello");
        });

        assertEquals("hello", dummy.returnObject());

        verify();
    }
}
