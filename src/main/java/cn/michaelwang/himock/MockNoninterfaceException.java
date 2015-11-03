package cn.michaelwang.himock;

import cn.michaelwang.himock.report.MockProcessErrorException;

public class MockNoninterfaceException extends MockProcessErrorException {

    private Class<?> mockedClass;

    public MockNoninterfaceException(Class<?> mockedClass) {
        this.mockedClass = mockedClass;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tonly interface can(should) be mocked:\n");
        sb.append("\t\tclass being mocked: ");
        sb.append(mockedClass.getName());
        sb.append("\n");

        sb.append(Utils.buildStackTraceInformation(getStackTrace(), "\t\t"));

        return sb.toString();
    }
}
