package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.NullObjectPlaceHolder;
import cn.michaelwang.himock.utils.Utils;

import java.util.List;

public class Matchers {
    private List<Matcher<?>> matchers;
    private Object[] args;

    public Matchers(List<Matcher<?>> matchers, Object[] args) {
        this.matchers = matchers;
        this.args = args;
    }

    public List<Matcher<?>> getMatchers() {
        return matchers;
    }

    @SuppressWarnings("unchecked")
    protected boolean match(Object[] toMatchArgs) {
        int matcherIndex = 0;
        for (int i = 0; i < args.length; i++) {
            Object thisArg = args[i];
            Object toCompareArg = toMatchArgs[i];

            if (toCompareArg == NullObjectPlaceHolder.getInstance()) {
                if (!isNullValue(thisArg)) {
                    return false;
                }
            } else {
                if (isNullValue(thisArg)) {
                    Matcher<Object> matcher = (Matcher<Object>) matchers.get(matcherIndex);
                    matcherIndex++;
                    if (!matcher.isMatch(toCompareArg)) {
                        return false;
                    }
                } else if (!thisArg.equals(toCompareArg)) {
                    return false;
                }
            }
        }

        return true;
    }

    protected boolean isNullValue(Object thisArg) {
        if (thisArg == null) {
            return true;
        }

        Class<?> type = thisArg.getClass();
        if (Utils.isPrimitiveOrBoxType(type)) {
            if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
                return thisArg.equals(false);
            }
            return thisArg.equals(0);
        }

        return false;
    }
}
