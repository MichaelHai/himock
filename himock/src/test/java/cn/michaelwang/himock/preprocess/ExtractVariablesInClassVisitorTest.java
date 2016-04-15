package cn.michaelwang.himock.preprocess;

import com.strobel.decompiler.ast.Variable;
import com.strobel.decompiler.languages.java.ast.CompilationUnit;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class ExtractVariablesInClassVisitorTest {
    @Test
    public void testExtractLocalVariablesInMethod() {
        @SuppressWarnings("unused")
        class LocalVariablesInMethod {
            public void aMethodWithLocalVariables() {
                String localVariable = "hello";
                String anotherLocalVariable = "world";
            }
        }

        ExtractVariablesInClassVisitor visitor = new ExtractVariablesInClassVisitor();
        CompilationUnit ast = new ClassToASTDecompiler(LocalVariablesInMethod.class).decompile();
        ast.acceptVisitor(visitor, null);

        List<VariableWithType> variables = visitor.getAllLocalVariables();
        assertEquals(2, variables.size());
        assertEquals(VariableType.OBJECT, variables.get(0).getType());
        assertEquals("localVariable", variables.get(0).getName());
        assertEquals(VariableType.OBJECT, variables.get(1).getType());
        assertEquals("anotherLocalVariable", variables.get(1).getName());
    }

    @Test
    public void testExtractLocalVariablesInMultipleMethods() {
        @SuppressWarnings("unused")
        class LocalVariablesInMultipleMethods {
            public void aMethodWithLocalVariables() {
                String localVariable = "hello";
                String anotherLocalVariable = "world";
            }

            public void anotherMethodWithLocalVariables() {
                String localVariableInSecondMethod = "hello";
                String anotherLocalVariableInSecondMethod = "world";
            }
        }

        ExtractVariablesInClassVisitor visitor = new ExtractVariablesInClassVisitor();
        CompilationUnit ast = new ClassToASTDecompiler(LocalVariablesInMultipleMethods.class).decompile();
        ast.acceptVisitor(visitor, null);

        List<VariableWithType> variables = visitor.getAllLocalVariables();
        assertEquals(4, variables.size());
        assertEquals(VariableType.OBJECT, variables.get(0).getType());
        assertEquals("localVariable", variables.get(0).getName());
        assertEquals(VariableType.OBJECT, variables.get(1).getType());
        assertEquals("anotherLocalVariable", variables.get(1).getName());
        assertEquals(VariableType.OBJECT, variables.get(2).getType());
        assertEquals("localVariableInSecondMethod", variables.get(2).getName());
        assertEquals(VariableType.OBJECT, variables.get(3).getType());
        assertEquals("anotherLocalVariableInSecondMethod", variables.get(3).getName());
    }

    @Test
    public void testExtractLocalVariablesWithTheSameNameInMultipleMethods() {
        @SuppressWarnings("unused")
        class LocalVariablesWithTheSameNameInMultipleMethods {
            public void aMethodWithLocalVariables() {
                String localVariable = "hello";
                String anotherLocalVariable = "world";
            }

            public void anotherMethodWithLocalVariables() {
                String localVariable = "hello";
                String anotherLocalVariable = "world";
            }
        }

        ExtractVariablesInClassVisitor visitor = new ExtractVariablesInClassVisitor();
        CompilationUnit ast = new ClassToASTDecompiler(LocalVariablesWithTheSameNameInMultipleMethods.class).decompile();
        ast.acceptVisitor(visitor, null);

        List<VariableWithType> variables = visitor.getAllLocalVariables();
        assertEquals(4, variables.size());
        assertEquals(VariableType.OBJECT, variables.get(0).getType());
        assertEquals("localVariable", variables.get(0).getName());
        assertEquals(VariableType.OBJECT, variables.get(1).getType());
        assertEquals("anotherLocalVariable", variables.get(1).getName());
        assertEquals(VariableType.OBJECT, variables.get(2).getType());
        assertEquals("localVariable", variables.get(2).getName());
        assertEquals(VariableType.OBJECT, variables.get(3).getType());
        assertEquals("anotherLocalVariable", variables.get(3).getName());
    }

    @Test
    public void testExtractLocalVariablesOfDifferentTypesInMethod() {
        @SuppressWarnings("unused")
        class LocalVariablesInMethod {
            public void aMethodWithLocalVariables() {
                String objectVariable = "hello";
                int intVariable = 0;
            }
        }

        ExtractVariablesInClassVisitor visitor = new ExtractVariablesInClassVisitor();
        CompilationUnit ast = new ClassToASTDecompiler(LocalVariablesInMethod.class).decompile();
        ast.acceptVisitor(visitor, null);

        List<VariableWithType> variables = visitor.getAllLocalVariables();
        assertEquals(2, variables.size());
        assertEquals(VariableType.OBJECT, variables.get(0).getType());
        assertEquals("objectVariable", variables.get(0).getName());
        assertEquals(VariableType.PRIMITIVE, variables.get(1).getType());
        assertEquals("intVariable", variables.get(1).getName());
    }

    @Test
    public void testExtractClassMembers() {
        @SuppressWarnings("unused")
        class ClassWithMember {
            private String objectMember;
            private int intMember = 1;
        }

        ExtractVariablesInClassVisitor visitor = new ExtractVariablesInClassVisitor();
        CompilationUnit ast = new ClassToASTDecompiler(ClassWithMember.class).decompile();
        ast.acceptVisitor(visitor, null);

        List<VariableWithType> members = visitor.getMembers();
        assertEquals(2, members.size());
        assertEquals(VariableType.OBJECT, members.get(0).getType());
        assertEquals("objectMember", members.get(0).getName());
        assertEquals(VariableType.PRIMITIVE, members.get(1).getType());
        assertEquals("intMember", members.get(1).getName());
    }

    @Test
    public void testExtractBothClassMembersAndLocalVariable() {
        @SuppressWarnings("unused")
        class ClassWithMemberAndLocalVariable {
            private String objectMember;
            private int intMember = 1;

            public void aMethodWithLocalVariables() {
                String objectVariable = "hello";
                int intVariable = 0;
            }
        }

        ExtractVariablesInClassVisitor visitor = new ExtractVariablesInClassVisitor();
        CompilationUnit ast = new ClassToASTDecompiler(ClassWithMemberAndLocalVariable.class).decompile();
        ast.acceptVisitor(visitor, null);

        List<VariableWithType> members = visitor.getMembers();
        assertEquals(2, members.size());
        assertEquals(VariableType.OBJECT, members.get(0).getType());
        assertEquals("objectMember", members.get(0).getName());
        assertEquals(VariableType.PRIMITIVE, members.get(1).getType());
        assertEquals("intMember", members.get(1).getName());

        List<VariableWithType> localVariables = visitor.getAllLocalVariables();
        assertEquals(2, localVariables.size());
        assertEquals(2, members.size());
        assertEquals(VariableType.OBJECT, members.get(0).getType());
        assertEquals("objectMember", members.get(0).getName());
        assertEquals(VariableType.PRIMITIVE, members.get(1).getType());
        assertEquals("intMember", members.get(1).getName());
    }
}
