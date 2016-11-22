package cn.michaelwang.himock;

import org.junit.runner.Description;

@SuppressWarnings("unused") // not tested
public class InjectFieldRule extends HiMockTestRule {

    private final Object testSuits;

	public InjectFieldRule(Object testSuits) {
		this.testSuits = testSuits;
	}

	@Override
	protected void executeRule(Description description) throws Throwable {
		HiMock.injectFields(testSuits);
	}

}
