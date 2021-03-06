package cn.michaelwang.himock;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class HiMockTestRule implements TestRule {
    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                HiMock.setup(description.getTestClass());
                statement.evaluate();
            }
        };
    }
}
