package cn.michaelwang.himock.process;

import java.util.List;

import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.utils.Utils;

public class Matchers {
    private List<Matcher<?>> matchers;

    public Matchers(List<Matcher<?>> matchers) {
        this.matchers = matchers;
    }

    public List<Matcher<?>> getMatchers() {
        return matchers;
    }

    @SuppressWarnings("unchecked")
    protected boolean match(Object[] toMatchArgs) {
        for (int i = 0; i < matchers.size(); i++) {
            Matcher<Object> thisArg = (Matcher<Object>) matchers.get(i);
            Object toCompareArg = toMatchArgs[i];
            
            if (!thisArg.isMatch(toCompareArg)) {
            	return false;
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
