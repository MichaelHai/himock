package cn.michaelwang.himock.report;

public abstract class HiMockReporter extends RuntimeException implements Reportable {
    @Override
    public String getMessage() {
        ReportBuilder reportBuilder = new ReportBuilder();

        buildReport(reportBuilder);

        return reportBuilder.getReport();
    }
}
