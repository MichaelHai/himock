package cn.michaelwang.himock;

public interface Matcher<T> {
    boolean isMatch(T actual);
}