package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;
import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("CodeBlock2Expr")
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

	@Test
	public void testConstructorWithParameterCanBeUsedToMockClass() {
		MockedClassWithoutDefaultConstructor dummy = mock(MockedClassWithoutDefaultConstructor.class,
				"Hello World");

		assertEquals(0, dummy.aFunction());
		assertEquals("Hello World", dummy.getParameter());
	}
}
