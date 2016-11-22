package cn.michaelwang.himock;

import cn.michaelwang.himock.utils.Utils;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    
    @Test
    public void testNullValue() {
    	assertEquals(0, Utils.nullValue(byte.class));
    	assertEquals(0, Utils.nullValue(char.class));
    	assertEquals(0, Utils.nullValue(short.class));
    	assertEquals(0, Utils.nullValue(int.class));
    	assertEquals(0, Utils.nullValue(long.class));
    	assertEquals(0, Utils.nullValue(float.class));
    	assertEquals(0, Utils.nullValue(double.class));
    	assertEquals(false, Utils.nullValue(boolean.class));
    	assertEquals(null, Utils.nullValue(Object.class));
    	assertEquals(null, Utils.nullValue(String.class));
    	assertEquals(0, Utils.nullValue(Byte.class));
    	assertEquals(0, Utils.nullValue(Character.class));
    	assertEquals(0, Utils.nullValue(Short.class));
    	assertEquals(0, Utils.nullValue(Integer.class));
    	assertEquals(0, Utils.nullValue(Long.class));
    	assertEquals(0, Utils.nullValue(Float.class));
    	assertEquals(0, Utils.nullValue(Double.class));
    	assertEquals(false, Utils.nullValue(Boolean.class));
    }

    @Test
    public void testGetLineNumber() {
        int lineNumber = Utils.getLineNumberInTestSuit(this.getClass());
        // This test will fail if the above line number is changed.
        assertEquals(65, lineNumber);
    }
}