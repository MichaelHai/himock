package cn.michaelwang.himock;

import static cn.michaelwang.himock.HiMock.expect;
import static cn.michaelwang.himock.HiMock.mock;
import static cn.michaelwang.himock.HiMock.verify;

import org.junit.Test;

import cn.michaelwang.himock.report.HiMockReporter;

public class MockClassTest extends HiMockBaseTest {
	@Test
	public void testMockedClassCanBeExpected() {
		MockedClass dummy = mock(MockedClass.class);

		expect(() -> {
			dummy.add(1, 2);
		});

		dummy.add(1, 2);

		verify();
	}

	@Test(expected = HiMockReporter.class)
	public void testNotCalledExpectationShouldFail() {
		MockedClass dummy = mock(MockedClass.class);

		expect(() -> {
			dummy.add(1, 2);
		});

		verify();
	}
	
	@Test
	public void testAbstractFunctionCanBeAutomaticallyMocked() {
		MockedAbstractClass dummy = mock(MockedAbstractClass.class);
		
		dummy.anAbstractFunction();
	}
}
