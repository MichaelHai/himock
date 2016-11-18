package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.HitNeverTimerException;
import cn.michaelwang.himock.process.Timer;

public class NeverTimer implements Timer {
    private boolean hit = false;

    @Override
    public void hit() {
        hit = true;
        throw new HitNeverTimerException();
    }

    @Override
    public Timer copy() {
        return new NeverTimer();
    }

    @Override
    public String getTimes() {
        return "never";
    }

    @Override
    public boolean pass() {
        return !hit;
    }

    @Override
    public boolean hitMore() {
        // receive one hit to check
        return !hit;
    }
}
