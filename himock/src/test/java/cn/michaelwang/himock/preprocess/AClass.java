package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.MockedInterface;

import static cn.michaelwang.himock.HiMock.*;

interface AInterface {
	void aFunctionWithArgs(Object arg1, Object arg2);
}

@SuppressWarnings("ALL")// Modify this class will cause the position in tests changes
public class AClass {
	Object a = match(a -> a != null);

	public static boolean zero(int a) {
		return a == 0;
	}

	public void aFunction() {
		matchInt(AClass::zero);
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
			System.out.println("world");
		});
	}

	public void testIntArgumentCanBeMatchedInVerification() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.withOneIntArgument(10);

		verify(() -> {
			dummy.withOneIntArgument(matchInt(arg -> arg == 10));
			System.out.println("hello");
		});
	}
}
