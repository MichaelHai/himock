package cn.michaelwang.himock.preprocess;

import com.strobel.decompiler.languages.java.ast.CompilationUnit;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ExtractVariablesInClassVisitorTest {
    @Test
    public void testExtractorLocalVariablesInMethod() {
        @SuppressWarnings("unused")
        class LocalVariablesInMethod {
            public void aMethodWithLocalVariables() {
                String hello = "hello";
                String world = "world";
            }
        }

        ExtractVariablesInClassVisitor visitor = new ExtractVariablesInClassVisitor();
        CompilationUnit ast = new ClassToASTDecompiler(LocalVariablesInMethod.class).decompile();
        ast.acceptVisitor(visitor, null);

        List<VariableWithType> variables = visitor.getAllVariables();
        assertEquals(2, variables.size());
        assertEquals(VariableType.OBJECT, variables.get(0).getType());
        assertEquals("hello", variables.get(0).getName());
        assertEquals(VariableType.OBJECT, variables.get(1).getType());
        assertEquals("world", variables.get(1).getName());
    }
}
