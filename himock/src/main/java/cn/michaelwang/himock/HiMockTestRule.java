package cn.michaelwang.himock;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class HiMockTestRule implements TestRule {
	private Object testSuit;
	
	public HiMockTestRule(Object testSuit) {
		this.testSuit = testSuit;
	}
	
    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                HiMock.setup(testSuit);
                statement.evaluate();
            }
        };
    }
}
