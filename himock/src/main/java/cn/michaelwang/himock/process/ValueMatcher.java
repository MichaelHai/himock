package cn.michaelwang.himock.process;

import java.util.Arrays;

import cn.michaelwang.himock.Matcher;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

public class ValueMatcher<T> implements Matcher<T> {
    private T value;

    public ValueMatcher(T value) {
        this.value = value;
    }

    @Override
    public boolean isMatch(Object actual) {
        return value == null
                ? (actual == null)
                : (actual instanceof Object[] && value instanceof Object[]
                ? Arrays.deepEquals((Object[]) actual, (Object[]) value)
                : value.equals(actual));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ValueMatcher
                && (value == null
                ? ((ValueMatcher) obj).value == null
                : value.equals(((ValueMatcher) obj).value));
    }
}
