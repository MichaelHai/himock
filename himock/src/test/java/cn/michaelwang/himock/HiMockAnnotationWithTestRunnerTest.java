package cn.michaelwang.himock;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import cn.michaelwang.himock.annotations.Mock;

/*
 * The tests in this suit should be run together for cross UTs tests.
 */
public class HiMockAnnotationWithTestRunnerTest extends HiMockBaseTest {
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
	
	@Test 
	public void testMockedObjectCanBeCreatedByParameter(@Mock MockedInterface dummyInParameter) {
		assertNotNull(dummyInParameter);
	}

	@Test(expected = TestSuitNotAnnotatedException.class)
	public void testNotAnnotatedTestParameter(MockedInterface dummy) {
		fail("the case should not even started with " + dummy);
	}
}
