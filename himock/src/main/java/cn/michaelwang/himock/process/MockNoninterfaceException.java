package cn.michaelwang.himock.process;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.ReportBuilder;

public class MockNoninterfaceException extends MockProcessErrorException {

    private Class<?> mockedClass;

    public MockNoninterfaceException(Class<?> mockedClass) {
        this.mockedClass = mockedClass;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("only interface can(should) be mocked:");

        reportBuilder.buildNextLevel(() -> {
            reportBuilder.appendLine("class being mocked: ", mockedClass.getCanonicalName());
            reportBuilder.appendStackTrace(getStackTrace());
        });
    }
}
