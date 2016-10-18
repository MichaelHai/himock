package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.MockedInterface;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.languages.java.ast.CompilationUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClassToASTDecompilerTest {
	@Test
	public void testStringClassCanBeDecompileToAST() {
		ClassToASTDecompiler stringDecompiler = new ClassToASTDecompiler(String.class);
		CompilationUnit ast = stringDecompiler.decompile();

		assertEquals(1, ast.getTypes().size());
		assertEquals("String", ast.getTypes().firstOrNullObject().getName());
	}

	@Test
	public void testUserTypeCanBeDecompileToAST() {
		ClassToASTDecompiler stringDecompiler = new ClassToASTDecompiler(MockedInterface.class);
		CompilationUnit ast = stringDecompiler.decompile();

		assertEquals(1, ast.getTypes().size());
		assertEquals("MockedInterface", ast.getTypes().firstOrNullObject().getName());
	}

	@Test
	public void testLibClassCanBeDecompileToAST() {
		ClassToASTDecompiler stringDecompiler = new ClassToASTDecompiler(Decompiler.class);
		CompilationUnit ast = stringDecompiler.decompile();

		assertEquals(1, ast.getTypes().size());
		assertEquals("Decompiler", ast.getTypes().firstOrNullObject().getName());
	}
}
