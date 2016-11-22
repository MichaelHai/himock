package cn.michaelwang.himock;

@SuppressWarnings("ALL")
public class MockedClassWithoutDefaultConstructor {
    private final String o;

	public MockedClassWithoutDefaultConstructor(String o) {
		this.o = o;
	}

	public int aFunction() {
		return 0;
	}

	public String getParameter() {
		return o;
	}
}
