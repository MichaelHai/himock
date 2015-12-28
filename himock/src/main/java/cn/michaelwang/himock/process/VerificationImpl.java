package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.NullObjectPlaceHolder;
import cn.michaelwang.himock.utils.Utils;
import cn.michaelwang.himock.verify.Verification;

import java.util.List;

public class VerificationImpl implements Verification {
    private Invocation invocation;
    private List<Matcher<?>> matchers;

    public VerificationImpl(Invocation invocation, List<Matcher<?>> matchers) {
        this.invocation = invocation;
        this.matchers = matchers;
    }

    @Override
    public Invocation getVerifiedInvocation() {
        return invocation;
    }

    @Override
    public boolean satisfyWith(Invocation invocation) {
        return this.invocation.sameMethod(invocation)
                && checkArguments(invocation);
    }

    @SuppressWarnings("unchecked")
    private boolean checkArguments(Invocation toCompare) {
        Object[] args = invocation.getArguments();

        int matcherIndex = 0;
        for (int i = 0; i < args.length; i++) {
            Object thisArg = args[i];
            Object toCompareArg = toCompare.getArguments()[i];

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

    private boolean isNullValue(Object thisArg) {
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
