package cn.michaelwang.himock.process;

public interface TimerChecker {
    boolean check();

    void hit();

    void addTimer(Timer timer);

    int getHitTimes();

    String getExpect();
}
