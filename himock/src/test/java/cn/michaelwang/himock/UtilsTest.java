package cn.michaelwang.himock;

import cn.michaelwang.himock.utils.Utils;
import org.junit.Test;

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
}