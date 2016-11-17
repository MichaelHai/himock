package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.process.Timer;

public class MoreThanTimer implements Timer {
    private final int minTimes;
    private int hit;

    public MoreThanTimer(int minTimes) {
        this.minTimes = minTimes;
    }

    @Override
    public boolean hit() {
        hit++;
        return hit > minTimes;
    }

    @Override
    public Timer copy() {
        return new MoreThanTimer(minTimes);
    }

    @Override
    public String getTimes() {
        return "more than " + minTimes + " time" + (minTimes > 1 ? "s" : "");
    }
}
