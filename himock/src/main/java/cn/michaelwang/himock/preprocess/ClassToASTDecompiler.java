package cn.michaelwang.himock.preprocess;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.strobel.assembler.InputTypeLoader;
import com.strobel.assembler.metadata.DeobfuscationUtilities;
import com.strobel.assembler.metadata.ITypeLoader;
import com.strobel.assembler.metadata.MetadataSystem;
import com.strobel.assembler.metadata.MethodDefinition;
import com.strobel.assembler.metadata.TypeDefinition;
import com.strobel.assembler.metadata.TypeReference;
import com.strobel.decompiler.DecompilationOptions;
import com.strobel.decompiler.DecompilerContext;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.languages.java.ast.AstBuilder;
import com.strobel.decompiler.languages.java.ast.CompilationUnit;
import com.strobel.decompiler.languages.java.ast.ConstructorDeclaration;
import com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor;
import com.strobel.decompiler.languages.java.ast.Keys;
import com.strobel.decompiler.languages.java.ast.LambdaExpression;
import com.strobel.decompiler.languages.java.ast.MethodDeclaration;

public class ClassToASTDecompiler {

	private final Class<?> clazz;

	public ClassToASTDecompiler(Class<?> clazz) {
		this.clazz = clazz;
	}

	public CompilationUnit decompile() {
		String path = "/" + clazz.getName().replaceAll("\\.", "/") + ".class";
		URL resource = clazz.getResource(path);

		String filePath = resource.getFile();
		if (filePath.startsWith("/")) {
			// user type
			filePath = filePath.substring(0);
		} else {
			// not find, type in lib
			filePath = clazz.getCanonicalName();
		}

		ITypeLoader typeLoader = new InputTypeLoader();
		MetadataSystem metadataSystem = new MetadataSystem(typeLoader);
		TypeReference type = metadataSystem.lookupType(filePath);
		TypeDefinition resolvedType = type.resolve();

//		List<MethodDefinition> methods = resolvedType.getDeclaredMethods();

		DeobfuscationUtilities.processType(resolvedType);
		DecompilationOptions options = new DecompilationOptions();
		DecompilerSettings settings = DecompilerSettings.javaDefaults();
		settings.setShowDebugLineNumbers(true);
		options.setSettings(settings);
		options.setFullDecompilation(true);

//		CompilationUnit ast = new JavaLanguage().decompileTypeToAst(resolvedType, options);
		CompilationUnit ast = buildAst(resolvedType, options).getCompilationUnit();

//		InjectLambdaMethodDefinitionVisitor visitor = new InjectLambdaMethodDefinitionVisitor(methods);
//		ast.acceptVisitor(visitor, null);

		return ast;
	}


    private AstBuilder buildAst(final TypeDefinition type, final DecompilationOptions options) {
        final AstBuilder builder = createAstBuilder(options, type, false);
        builder.addType(type);
        return builder;
    }

	private AstBuilder createAstBuilder(
        final DecompilationOptions options,
        final TypeDefinition currentType,
        final boolean isSingleMember) {

        final DecompilerSettings settings = options.getSettings();
        final DecompilerContext context = new DecompilerContext();

        context.setCurrentType(currentType);
        context.setSettings(settings);

        AstBuilder builder =  new AstBuilder(context);
        
//        CompilationUnit ast = builder.getCompilationUnit();
//        new RewriteLocalClassesTransform(context).run(ast);
//        new IntroduceOuterClassReferencesTransform(context).run(ast);
//        new RewriteInnerClassConstructorCalls(context).run(ast);
//        new DeclareLocalClassesTransform(context).run(ast);
//		new RemoveHiddenMembersTransform(context).run(ast);
        
        return builder;
    }

	class InjectLambdaMethodDefinitionVisitor extends DepthFirstAstVisitor<Object, Object> {

		private List<MethodDefinition> methods;

		public InjectLambdaMethodDefinitionVisitor(List<MethodDefinition> methods) {
			this.methods = new ArrayList<>();
			for (MethodDefinition method : methods) {
				this.methods.add(method);
			}
		}

		private String currentMethod;

		@Override
		public Object visitConstructorDeclaration(ConstructorDeclaration node, Object data) {
			currentMethod = "new";
			return super.visitConstructorDeclaration(node, data);
		}

		@Override
		public Object visitMethodDeclaration(MethodDeclaration node, Object data) {
			currentMethod = node.getName();
			return super.visitMethodDeclaration(node, data);
		}

		@Override
		public Object visitLambdaExpression(LambdaExpression node, Object data) {
			String name = "lambda$" + currentMethod + "$";
			MethodDefinition method = findMethod(name);
			
			node.putUserData(Keys.METHOD_DEFINITION, method);
			methods.remove(method);

			return super.visitLambdaExpression(node, data);
		}

		private MethodDefinition findMethod(String name) {
			// The definition in the class file is reversed from the source
			// file.
			for (int i = methods.size() - 1; i >= 0; i--) {
				MethodDefinition method = methods.get(i);
				String methodName = method.getName();
				if (methodName.startsWith(name)) {
					return method;
				}
			}

			return null;
		}
	}
}
