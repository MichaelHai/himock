package cn.michaelwang.himock;

import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;

public class HiMockTimerTest extends HiMockBaseTest {
	@Test
	public void testTimerInExpect() {
		MockedInterface dummy = mock(MockedInterface.class);

		expect(() -> {
			dummy.returnInt();
			willReturn(1);
			times(3);
		});

		assertEquals(1, dummy.returnInt());
		assertEquals(1, dummy.returnInt());
		assertEquals(1, dummy.returnInt());

		verify();
	}

	@SuppressWarnings("CodeBlock2Expr")
	@Test
	public void testSetTimerOutExpectShouldFail() {
		reportTest(() -> {
			times(3);
		}, "Mock Process Error:\n" +
				"\ttimer cannot be set outside expectation:\n" +
				"\t-> at cn.michaelwang.himock.HiMockTimerTest.lambda$testSetTimerOutExpectShouldFail$?(HiMockTimerTest.java:?)\n"
				+
				"\t   at cn.michaelwang.himock.HiMockBaseTest.reportTest(HiMockBaseTest.java:?)\n" +
				"\t   at cn.michaelwang.himock.HiMockTimerTest.testSetTimerOutExpectShouldFail(HiMockTimerTest.java:?)\n"

		);
	}

	@Test
	public void testTimerInVerification() {
		MockedInterface dummy = mock(MockedInterface.class);

		dummy.doNothing();
		dummy.doNothing();
		dummy.doNothing();

		verify(() -> {
			dummy.doNothing();
			times(3);
		});
	}

	@Test
	public void testTimesExpectedButNotEnoughShouldReportError() {
		MockedInterface dummy = mock(MockedInterface.class);

		reportTest(() -> {
			expect(() -> {
				dummy.returnInt();
				times(3);
			});
		}, "");

		dummy.returnInt();
		dummy.returnInt();

		verify();
	}
}
