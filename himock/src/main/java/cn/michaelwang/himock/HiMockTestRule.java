package cn.michaelwang.himock;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

@SuppressWarnings("unused") // not tested
public class HiMockTestRule implements TestRule {
	
    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                HiMock.setup(description.getTestClass());
                executeRule(description);
                statement.evaluate();
            }
        };
    }
    
    protected void executeRule(Description description) throws Throwable {
    	// default do nothing
    }
}
