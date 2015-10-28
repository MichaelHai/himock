package cn.michaelwang.himock.report;

import java.util.List;

public class VerificationFailedReporter extends HiMockReporter {
    List<VerificationFailedException> exceptions;

    public VerificationFailedReporter(List<VerificationFailedException> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Verification failed:");

        for (VerificationFailedException ex : exceptions) {
            sb.append("\n");
            sb.append(ex.getMessage());
        }

        return sb.toString();
    }
}
