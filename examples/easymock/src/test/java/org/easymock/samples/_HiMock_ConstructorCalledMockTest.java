package org.easymock.samples;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.michaelwang.himock.HiMockRunner;

@RunWith(HiMockRunner.class)
public class _HiMock_ConstructorCalledMockTest {
	TaxCalculator tc;

	@Before
	public void setUp() {
		BigDecimal[] taxValues = { new BigDecimal("5"), new BigDecimal("15") };
		tc = mock(TaxCalculator.class, (Object) taxValues);
	}

	@After
	public void tearDown() {
		verify();
	}

	@Test
	public void testTax() {
		expect(() -> {
			tc.rate();
			willReturn(new BigDecimal("0.20"));
		});

		BigDecimal tax = tc.tax();

		assertEquals(new BigDecimal("4.00"), tax);
	}
	
	@Test
	public void testTax_ZeroRate() {
		expect(() -> {
			tc.rate();
			willReturn(BigDecimal.ZERO);
		});

        BigDecimal tax = tc.tax();

        assertEquals(BigDecimal.ZERO, tax);
	}
}
