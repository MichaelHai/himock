package cn.michaelwang.himock.report;

import java.util.List;

public class VerificationFailedReporter extends RuntimeException {
    private List<VerificationFailedException> exceptions;

    public VerificationFailedReporter(List<VerificationFailedException> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public String getMessage() {
        String message = "Verification failed: ";
        for (VerificationFailedException ex: exceptions) {
            message += "\n";
            message += ex.getMessage();
        }

        return message;
    }
}
