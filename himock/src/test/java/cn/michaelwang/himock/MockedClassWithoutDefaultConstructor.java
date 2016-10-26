package cn.michaelwang.himock;

public class MockedClassWithoutDefaultConstructor {
	private String o;

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
