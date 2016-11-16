package cn.michaelwang.himock.process.timer;

public class HitMoreThanExpectedTimesException extends RuntimeException {
    public HitMoreThanExpectedTimesException(String s) {
        super(s);
    }

    public HitMoreThanExpectedTimesException() {
        super("The timer(s) are hit more than expected times.");
    }
}
