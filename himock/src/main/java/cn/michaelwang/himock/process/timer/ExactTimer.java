package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.process.Timer;

public class ExactTimer implements Timer {
    private final int times;
    private int hit;

    public ExactTimer(int times) {
        this.times = times;
    }

    @Override
    public void hit() {
        if (hit == times) {
            throw new HitMoreThanExpectedTimesException("Timer should be hit exactly " + times + " times");
        } else {
            hit++;
        }
    }

    @Override
    public Timer copy() {
        return new ExactTimer(times);
    }

    @Override
    public String getTimes() {
        return times + "";
    }

    @Override
    public boolean pass() {
        return hit == times;
    }

    @Override
    public boolean hitMore() {
        return hit < times;
    }
}
