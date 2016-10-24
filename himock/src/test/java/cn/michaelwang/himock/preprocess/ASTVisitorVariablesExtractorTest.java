package cn.michaelwang.himock.preprocess;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class ASTVisitorVariablesExtractorTest {
	@Test
	public void testExtractLocalVariablesInMethod() {
		@SuppressWarnings("unused")
		class LocalVariablesInMethod {
			public void aMethodWithLocalVariables() {
				String localVariable = "hello";
				String anotherLocalVariable = "world";
			}
		}

		ASTVisitorVariablesExtractor visitor = new ASTVisitorVariablesExtractor(LocalVariablesInMethod.class);

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

		ASTVisitorVariablesExtractor visitor = new ASTVisitorVariablesExtractor(LocalVariablesInMultipleMethods.class);

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

		ASTVisitorVariablesExtractor visitor = new ASTVisitorVariablesExtractor(
				LocalVariablesWithTheSameNameInMultipleMethods.class);

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

		ASTVisitorVariablesExtractor visitor = new ASTVisitorVariablesExtractor(LocalVariablesInMethod.class);

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

		ASTVisitorVariablesExtractor visitor = new ASTVisitorVariablesExtractor(ClassWithMember.class);

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

		ASTVisitorVariablesExtractor visitor = new ASTVisitorVariablesExtractor(ClassWithMemberAndLocalVariable.class);

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

	@Test
	public void testGetVariablesViaMethodName() {
		@SuppressWarnings("unused")
		class LocalVariablesInMultipleMethods {
			public void aMethodWithLocalVariables() {
				String localVariable = "hello";
				String anotherLocalVariable = "world";
			}

			public void anotherMethodWithLocalVariables() {
				String localVariableInSecondMethod = "hello";
				String anotherLocalVariableInSecondMethod = "world";
				int theThirdLocalVariableInSecondMethod = 2;
			}
		}

		ASTVisitorVariablesExtractor visitor = new ASTVisitorVariablesExtractor(LocalVariablesInMultipleMethods.class);

		List<VariableWithType> variablesInTheFirstMethod = visitor.getLocalVariablesIn("aMethodWithLocalVariables");
		assertEquals(2, variablesInTheFirstMethod.size());
		assertEquals(VariableType.OBJECT, variablesInTheFirstMethod.get(0).getType());
		assertEquals("localVariable", variablesInTheFirstMethod.get(0).getName());
		assertEquals(VariableType.OBJECT, variablesInTheFirstMethod.get(1).getType());
		assertEquals("anotherLocalVariable", variablesInTheFirstMethod.get(1).getName());

		List<VariableWithType> variablesInTheSecondMethod = visitor
				.getLocalVariablesIn("anotherMethodWithLocalVariables");
		assertEquals(3, variablesInTheSecondMethod.size());
		assertEquals(VariableType.OBJECT, variablesInTheSecondMethod.get(0).getType());
		assertEquals("localVariableInSecondMethod", variablesInTheSecondMethod.get(0).getName());
		assertEquals(VariableType.OBJECT, variablesInTheSecondMethod.get(1).getType());
		assertEquals("anotherLocalVariableInSecondMethod", variablesInTheSecondMethod.get(1).getName());
		assertEquals(VariableType.PRIMITIVE, variablesInTheSecondMethod.get(2).getType());
		assertEquals("theThirdLocalVariableInSecondMethod", variablesInTheSecondMethod.get(2).getName());
	}
}
