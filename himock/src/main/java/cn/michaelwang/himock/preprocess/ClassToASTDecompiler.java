package cn.michaelwang.himock.preprocess;

import java.net.URL;

import com.strobel.assembler.InputTypeLoader;
import com.strobel.assembler.metadata.DeobfuscationUtilities;
import com.strobel.assembler.metadata.ITypeLoader;
import com.strobel.assembler.metadata.MetadataSystem;
import com.strobel.assembler.metadata.TypeDefinition;
import com.strobel.assembler.metadata.TypeReference;
import com.strobel.decompiler.DecompilationOptions;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.languages.java.JavaLanguage;
import com.strobel.decompiler.languages.java.ast.CompilationUnit;

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
			filePath = filePath.substring(1);
		} else {
			// not find, type in lib
			filePath = clazz.getCanonicalName();
		}

		ITypeLoader typeLoader = new InputTypeLoader();
		MetadataSystem metadataSystem = new MetadataSystem(typeLoader);
		TypeReference type = metadataSystem.lookupType(filePath);
		TypeDefinition resolvedType = type.resolve();
		DeobfuscationUtilities.processType(resolvedType);
		DecompilationOptions options = new DecompilationOptions();
		DecompilerSettings settings = DecompilerSettings.javaDefaults();
		settings.setShowDebugLineNumbers(true);
		options.setSettings(settings);
		options.setFullDecompilation(true);

		return new JavaLanguage().decompileTypeToAst(resolvedType, options);
	}
}
