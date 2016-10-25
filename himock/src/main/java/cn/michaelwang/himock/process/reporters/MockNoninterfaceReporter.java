package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.report.ReportBuilder;

public class MockNoninterfaceReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = 1436742152819640924L;

	private Class<?> mockedClass;

    public MockNoninterfaceReporter(Class<?> mockedClass) {
        this.mockedClass = mockedClass;
    }

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("only interface can(should) be mocked:");

        reportBuilder.buildNextLevel((levelBuilder) -> {
            levelBuilder.appendLine("class being mocked: ", mockedClass.getCanonicalName());
            levelBuilder.appendStackTrace(getStackTrace());
        });
    }
}
