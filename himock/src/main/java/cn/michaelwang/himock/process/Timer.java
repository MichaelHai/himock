package cn.michaelwang.himock.process;

public interface Timer {
    boolean hit();

    /**
     * @return a Timer which is exactly the same with this timer but in the init status.
     */
    Timer copy();

    String getTimes();
}
