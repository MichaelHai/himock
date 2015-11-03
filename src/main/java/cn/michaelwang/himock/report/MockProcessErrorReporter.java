package cn.michaelwang.himock.report;

public class MockProcessErrorReporter extends HiMockReporter {
    private final MockProcessErrorException ex;

    public MockProcessErrorReporter(MockProcessErrorException ex) {
        this.ex = ex;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("Mock Process Error:");
        reportBuilder.buildNextLevel(() -> ex.buildReport(reportBuilder));
    }
}
