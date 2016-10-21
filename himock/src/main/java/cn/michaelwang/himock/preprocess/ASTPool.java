package cn.michaelwang.himock.preprocess;

import com.strobel.decompiler.languages.java.ast.CompilationUnit;

import java.util.HashMap;
import java.util.Map;

public class ASTPool {
    private static ASTPool instance;
    Map<Class<?>, CompilationUnit> asts = new HashMap<>();

    private ASTPool() {
    }

    public static ASTPool getInstance() {
        if (instance == null) {
            instance = new ASTPool();
        }

        return instance;
    }

    public CompilationUnit getAST(Class<?> clazz) {
        if (asts.containsKey(clazz)) {
            return asts.get(clazz);
        } else {
            CompilationUnit newAst = new ClassToASTDecompiler(clazz).decompile();
            asts.put(clazz, newAst);
            return newAst;
        }
    }
}
