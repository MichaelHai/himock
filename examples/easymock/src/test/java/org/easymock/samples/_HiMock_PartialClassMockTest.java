package org.easymock.samples;

import static cn.michaelwang.himock.HiMock.expect;
import static cn.michaelwang.himock.HiMock.mock;
import static cn.michaelwang.himock.HiMock.verify;
import static cn.michaelwang.himock.HiMock.willReturn;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.michaelwang.himock.HiMockRunner;

@RunWith(HiMockRunner.class)
public class _HiMock_PartialClassMockTest {
	private Rect rect;

	@Before
	public void setUp() {
		rect = mock(Rect.class);
	}

	@Test
	public void testGetArea() {
		expect(() -> {
			rect.getX();
			willReturn(4);

			rect.getY();
			willReturn(5);
		});

		assertEquals(20, rect.getArea());

		verify();
	}
}
