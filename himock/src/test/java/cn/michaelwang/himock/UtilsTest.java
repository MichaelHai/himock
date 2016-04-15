package cn.michaelwang.himock;

import cn.michaelwang.himock.utils.Utils;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class UtilsTest {
    @Test
    public void testConvertFunctionNameWithParentheses() {
        String result = Utils.removeParenthesesInFunctionName("thisIsAFunctionName()");

        assertEquals("thisIsAFunctionName", result);
    }

    @Test
    public void testConvertFunctionNameWithoutParentheses() {
        String result = Utils.removeParenthesesInFunctionName("thisIsAFunctionName");

        assertEquals("thisIsAFunctionName", result);
    }

    @Test
    public void testIsNameOfPrimitiveType() {
        assertTrue(Utils.isNameOfPrimitiveType("byte"));
        assertTrue(Utils.isNameOfPrimitiveType("char"));
        assertTrue(Utils.isNameOfPrimitiveType("short"));
        assertTrue(Utils.isNameOfPrimitiveType("int"));
        assertTrue(Utils.isNameOfPrimitiveType("long"));
        assertTrue(Utils.isNameOfPrimitiveType("float"));
        assertTrue(Utils.isNameOfPrimitiveType("double"));
        assertTrue(Utils.isNameOfPrimitiveType("boolean"));
        assertFalse(Utils.isNameOfPrimitiveType("Byte"));
        assertFalse(Utils.isNameOfPrimitiveType("Object"));
        assertFalse(Utils.isNameOfPrimitiveType("Integer"));
    }
}