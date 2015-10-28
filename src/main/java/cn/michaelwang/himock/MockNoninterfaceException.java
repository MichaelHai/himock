package cn.michaelwang.himock;

import cn.michaelwang.himock.report.MockProcessErrorException;

public class MockNoninterfaceException extends MockProcessErrorException {

    private Class<?> mockedClass;

    public MockNoninterfaceException(Class<?> mockedClass) {
        this.mockedClass = mockedClass;
    }

    @Override
    public String getMessage() {
        return "\tonly interface can(should) be mocked:\n"
                + "\t\tclass being mocked: " + mockedClass.getName();
    }
}
