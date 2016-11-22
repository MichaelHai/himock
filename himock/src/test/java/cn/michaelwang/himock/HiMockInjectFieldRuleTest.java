package cn.michaelwang.himock;

import cn.michaelwang.himock.annotations.Mock;
import org.junit.Rule;
import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HiMockInjectFieldRuleTest {
	@Rule
	public InjectFieldRule injectFiledRule = new InjectFieldRule(this);

    @SuppressWarnings("CanBeFinal")
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
