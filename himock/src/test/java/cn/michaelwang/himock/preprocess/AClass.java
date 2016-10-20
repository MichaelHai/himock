package cn.michaelwang.himock.preprocess;

import static cn.michaelwang.himock.HiMock.match;
import static cn.michaelwang.himock.HiMock.matchInt;
import static cn.michaelwang.himock.HiMock.expect;

// Modify this class will cause the position in tests changes
public class AClass {
	Object a = match(a -> a != null);

	public void aFunction() {
		matchInt(AClass::zero);
	}

	public static boolean zero(int a) {
		return a == 0;
	}

	public void aTestUseMatcher() {
		AInterface mock = getMock();

		mock.aFunctionWithArgs(a, match((arg) -> true));
	}

	private AInterface getMock() {
		return null;
	}

	public void matcherUsedInDifferentLinesInOneInvocation() {
		AInterface mock = getMock();

		mock.aFunctionWithArgs(a,
				match((arg) -> true));
	}

	public void matcherInSideExpect() {
		AInterface mock = getMock();
		
		expect(() -> {
			System.out.println("hello");
			mock.aFunctionWithArgs(a, a);
		});
	}
}

interface AInterface {
	void aFunctionWithArgs(Object arg1, Object arg2);
}
