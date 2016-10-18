package cn.michaelwang.himock.report;

public abstract class HiMockReporter extends RuntimeException implements Reportable {
	private static final long serialVersionUID = 6658301925122665598L;

	@Override
    public String getMessage() {
        ReportBuilder reportBuilder = new ReportBuilder();

        buildReport(reportBuilder);

        return reportBuilder.getReport();
    }
}
