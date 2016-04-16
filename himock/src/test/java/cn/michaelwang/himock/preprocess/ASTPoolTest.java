package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.MockedInterface;
import com.strobel.decompiler.languages.java.ast.CompilationUnit;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class ASTPoolTest {
    @Test
    public void testTheSameClassWillGenerateTheSameAST() {
        CompilationUnit ast1 = ASTPool.getInstance().getAST(MockedInterface.class);
        CompilationUnit ast2 = ASTPool.getInstance().getAST(MockedInterface.class);

        assertTrue(ast1 == ast2);
    }

    /*
     * This test is platform dependent. However, 10ms is enough in most circumstances if the ast is cached.
     */
    @Test
    public void testGetTheSameASTNotTheFirstTimeShouldLessThen10ms() {
        ASTPool.getInstance().getAST(MockedInterface.class);

        long start = System.currentTimeMillis();
        ASTPool.getInstance().getAST(MockedInterface.class);
        long end = System.currentTimeMillis();
        long cost = end - start;
        assertTrue(cost < 10);
    }
}
