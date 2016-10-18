package cn.michaelwang.himock.preprocess;

import org.junit.Test;

import static cn.michaelwang.himock.HiMock.*;

public class MatchFinderTest {
	@Test
	public void matchesCanBeFound() {
	}
}

class AClass {
	Object a = match(a -> a != null);
	
	public void aFunction() {
		matchInt(AClass::zero);
	}
	
	public static boolean zero(int a) {
		return a == 0;
	}
}
