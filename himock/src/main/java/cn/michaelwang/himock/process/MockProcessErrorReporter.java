package cn.michaelwang.himock.process;

import cn.michaelwang.himock.report.HiMockReporter;
import cn.michaelwang.himock.report.ReportBuilder;

public abstract class MockProcessErrorReporter extends HiMockReporter {
	private static final long serialVersionUID = 3113510354071408544L;

	@Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("Mock Process Error:");
        reportBuilder.buildNextLevel(this::buildProcessError);
    }

    protected abstract void buildProcessError(ReportBuilder reportBuilder);
}
