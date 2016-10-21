package cn.michaelwang.himock;

@FunctionalInterface
public interface Matcher<T> {
    boolean isMatch(T actual);
}