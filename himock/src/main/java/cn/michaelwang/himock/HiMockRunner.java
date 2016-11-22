package cn.michaelwang.himock;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.List;

public class HiMockRunner extends BlockJUnit4ClassRunner {
	private Object testSuit;

	public HiMockRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
    
	@Override
	protected void validateTestMethods(List<Throwable> errors) {
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(Test.class);

        for (FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoid(false, errors);
        }
	}

	@Override
	protected Object createTest() throws Exception {
		this.testSuit = super.createTest();
		return this.testSuit;
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object test) {
		return new TestMethodWithParameterInvoker(method, test);
	}

	@Override
    public void run(RunNotifier runNotifier) {
        runNotifier.addListener(new RunListener() {
            @Override
            public void testStarted(Description description) throws Exception {
                HiMock.setup(testSuit.getClass());
                HiMock.injectFields(testSuit);
                super.testStarted(description);
            }
        });
        super.run(runNotifier);
    }
}
