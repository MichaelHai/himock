package cn.michaelwang.himock.preprocess;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VariableTypeTest {

    @Test
    public void testConvertToType() throws Exception {
        assertEquals(VariableType.OBJECT, VariableType.convertToType("String"));
        assertEquals(VariableType.OBJECT, VariableType.convertToType("Object"));
        assertEquals(VariableType.PRIMITIVE, VariableType.convertToType("int"));
        assertEquals(VariableType.PRIMITIVE, VariableType.convertToType("boolean"));
    }
}