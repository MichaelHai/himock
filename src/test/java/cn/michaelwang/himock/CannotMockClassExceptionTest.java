package cn.michaelwang.himock;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CannotMockClassExceptionTest {
    @Test
    public void testCannotMockClassExceptionShouldContainsMockedClassInformation() {
        HiMock mock = new HiMock();
        try {
            mock.mock(String.class);
        } catch (CannotMockClassException ex) {
            assertEquals("the exception should contains the failed mocked class information", String.class, ex.getMockedClass());
        }
    }

    @Test
    public void testCannotMockClassExceptionShouldGenerateProperMessage() {
        HiMock mock = new HiMock();
        try {
            mock.mock(String.class);
        } catch (CannotMockClassException ex) {
            assertEquals("java.lang.String is not an interface and can(should) not be mocked.", ex.getMessage());
        }
    }
}
