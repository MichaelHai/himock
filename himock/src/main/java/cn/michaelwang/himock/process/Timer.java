package cn.michaelwang.himock.process;

public interface Timer {
    void hit();

    /**
     * @return a Timer which is exactly the same with this timer but in the init status.
     */
    Timer copy();

    String getTimes();

    boolean pass();

    boolean hitMore();
}
