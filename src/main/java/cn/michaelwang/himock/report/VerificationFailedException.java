package cn.michaelwang.himock.report;

public class VerificationFailedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Verification failed: \n";
    }
}
