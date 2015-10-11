package cn.michaelwang.himock;

public class MockExpectationFailedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Verification failed: \n";
    }
}
