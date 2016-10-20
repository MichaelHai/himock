package cn.michaelwang.himock;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class HiMockRunner extends BlockJUnit4ClassRunner {
    public HiMockRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public Description getDescription() {
        return super.getDescription();
    }

    @Override
    public void run(RunNotifier runNotifier) {
        runNotifier.addListener(new RunListener() {
            @Override
            public void testStarted(Description description) throws Exception {
                HiMock.setup(description.getTestClass());
                super.testStarted(description);
            }
        });
        super.run(runNotifier);
    }
}
