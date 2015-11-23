package cn.michaelwang.himock.report;

public abstract class HiMockReporter extends HiMockException {
    protected HiMockReporter() {
        setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String getMessage() {
        ReportBuilder reportBuilder = new ReportBuilder();

        buildReport(reportBuilder);

        return reportBuilder.getReport();
    }
}
