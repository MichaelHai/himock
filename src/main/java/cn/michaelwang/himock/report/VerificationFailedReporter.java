package cn.michaelwang.himock.report;

import java.util.List;

public class VerificationFailedReporter extends HiMockReporter {
    private String message;

    public VerificationFailedReporter(List<VerificationFailedException> exceptions) {
        constructMessage(exceptions);
    }

    @Override
    public String getMessage() {
        return message;
    }

    private String constructMessage(List<VerificationFailedException> exceptions) {
        message = "Verification failed:";
        for (VerificationFailedException ex : exceptions) {
            message += "\n";
            message += ex.getMessage();
        }

        return message;
    }
}
