package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.report.ReportBuilder;

public class NoReturnTypeReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = -6880109008418479992L;

    private final Invocation invocation;

    public NoReturnTypeReporter(Invocation invocation) {
        this.invocation = invocation;
    }

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("invocation expected has no return value:");

        reportBuilder.buildNextLevel((levelBuilder) -> {
            levelBuilder.appendLine(invocation.getMethodName());
            levelBuilder.appendStackTrace(invocation.getInvocationStackTrace());
            levelBuilder.appendLine("return value being set:");
            levelBuilder.appendStackTrace(getStackTrace());
        });
    }
}
