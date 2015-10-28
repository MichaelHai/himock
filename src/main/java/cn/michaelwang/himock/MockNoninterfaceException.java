package cn.michaelwang.himock;

public class MockNoninterfaceException extends RuntimeException {

    private Class<?> mockedClass;

    public MockNoninterfaceException(Class<?> mockedClass) {
        this.mockedClass = mockedClass;
    }

    @Override
    public String getMessage() {
        return "\tonly interface can(should) be mocked:\n" +
                "\t\tclass being mocked: " + mockedClass.getName();
    }
}
