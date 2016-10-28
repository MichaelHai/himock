package cn.michaelwang.himock;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cn.michaelwang.himock.annotations.Mock;

public class HiMockAnnotationTest extends HiMockBaseTest {
	@Mock
	private MockedInterface dummy;

	@Test
	public void testMockAnnotationCanCreateMockObject() {
		assertNotNull(dummy);
	}
}
