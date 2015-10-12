package cn.michaelwang.himock;

public class MockNoninterfaceException extends RuntimeException {

    private Class<?> mockedClass;

    public MockNoninterfaceException(Class<?> mockedClass) {
        this.mockedClass = mockedClass;
    }

    public Class<?> getMockedClass() {
        return mockedClass;
    }

    @Override
    public String getMessage() {
        return mockedClass.getName() + " is not an interface and can(should) not be mocked.";
    }
}
