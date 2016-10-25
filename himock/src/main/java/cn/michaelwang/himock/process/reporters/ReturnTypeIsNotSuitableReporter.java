package cn.michaelwang.himock.process.reporters;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.report.ReportBuilder;

public class ReturnTypeIsNotSuitableReporter extends MockProcessErrorReporter {
	private static final long serialVersionUID = 504650906917138729L;
	
	private Invocation invocation;
    private Class<?> setAgain;

    public ReturnTypeIsNotSuitableReporter(Invocation invocation, Class<?> setAgain) {
        this.invocation = invocation;
        this.setAgain = setAgain;
    }

    @Override
    public void buildProcessError(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("return value type is not match:");

        reportBuilder.buildNextLevel((levelBuilder) -> {
            levelBuilder.appendLine("method setting return value: ", invocation.getMethodName());
            levelBuilder.appendLine("return type expected:\t", invocation.getReturnType().getCanonicalName());
            levelBuilder.appendStackTrace(invocation.getInvocationStackTrace());
            levelBuilder.appendLine("return type being set:\t", setAgain.getCanonicalName());
            levelBuilder.appendStackTrace(getStackTrace());
        });
    }
}
