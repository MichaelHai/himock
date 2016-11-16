package cn.michaelwang.himock.process.timer;

public class NewAnswerTimer implements Timer {
    private boolean hit = false;

    @Override
    public boolean hit() {
        if (hit) {
            throw new HitMoreThanExpectedTimesException("NewAnswerTimer should be hit only once.");
        }
        hit = true;
        return true;
    }
}
