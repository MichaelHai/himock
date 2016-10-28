package cn.michaelwang.himock;

import static cn.michaelwang.himock.HiMock.expect;
import static cn.michaelwang.himock.HiMock.verify;
import static cn.michaelwang.himock.HiMock.willReturn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;

import cn.michaelwang.himock.annotations.Mock;

public class HiMockInjectFieldRuleTest {
	@Rule
	public InjectFieldRule injectFiledRule = new InjectFieldRule(this);
	
	@Mock
	private MockedInterface dummy;

	@Test
	public void testMockAnnotationCanCreateMockObject() {
		assertNotNull(dummy);
	}

	@Test
	public void testMockedFieldWillReinitForDifferentTests1() {
		expect(() -> {
			dummy.returnInt();
			willReturn(1);
		});

		assertEquals(1, dummy.returnInt());

		verify();
	}

	@Test
	public void testMockedFieldWillReinitForDifferentTests2() {
		// the default value is 0
		assertEquals(0, dummy.returnInt());

		verify();
	}
}
