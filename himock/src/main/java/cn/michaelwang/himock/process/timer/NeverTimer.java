package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.HitNeverTimerException;
import cn.michaelwang.himock.process.Timer;

public class NeverTimer implements Timer {

    @Override
    public boolean hit() {
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
}
