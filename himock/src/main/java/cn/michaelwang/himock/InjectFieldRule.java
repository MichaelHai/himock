package cn.michaelwang.himock;

import org.junit.runner.Description;

public class InjectFieldRule extends HiMockTestRule {

	private Object testSuits;

	public InjectFieldRule(Object testSuits) {
		this.testSuits = testSuits;
	}

	@Override
	protected void executeRule(Description description) throws Throwable {
		HiMock.injectFields(testSuits);
	}

}
