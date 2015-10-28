package cn.michaelwang.himock.report;

public class MockProcessErrorReporter extends HiMockReporter {
    private final Exception ex;

    public MockProcessErrorReporter(MockProcessErrorException ex) {
        this.ex = ex;
    }

    @Override
    public String getMessage() {
        return "Mock Process Error:\n"
                + ex.getMessage();
    }
}
