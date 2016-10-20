package cn.michaelwang.himock.preprocess;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.languages.java.ast.CompilationUnit;
import com.strobel.decompiler.languages.java.ast.TypeDeclaration;

import cn.michaelwang.himock.MockedInterface;

public class ClassToASTDecompilerTest {
	@Test
	public void testStringClassCanBeDecompileToAST() {
		ClassToASTDecompiler stringDecompiler = new ClassToASTDecompiler(String.class);
		CompilationUnit ast = stringDecompiler.decompile();

		// This is a bug in the lib, the hasNext always return null after next
		// being called. However, next must be called after a invocation of
		// hasNext or else there will be exception
		Iterator<TypeDeclaration> iterator = ast.getTypes().iterator();
		iterator.hasNext();
		assertEquals("String", iterator.next().getName());
	}

	@Test
	public void testUserTypeCanBeDecompileToAST() {
		ClassToASTDecompiler stringDecompiler = new ClassToASTDecompiler(MockedInterface.class);
		CompilationUnit ast = stringDecompiler.decompile();

		Iterator<TypeDeclaration> iterator = ast.getTypes().iterator();
		iterator.hasNext();
		assertEquals("MockedInterface", iterator.next().getName());
	}

	@Test
	public void testLibClassCanBeDecompileToAST() {
		ClassToASTDecompiler stringDecompiler = new ClassToASTDecompiler(Decompiler.class);
		CompilationUnit ast = stringDecompiler.decompile();

		Iterator<TypeDeclaration> iterator = ast.getTypes().iterator();
		iterator.hasNext();
		assertEquals("Decompiler", iterator.next().getName());
	}
}
