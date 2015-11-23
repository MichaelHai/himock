package cn.michaelwang.himock.invocation;

public class NullInvocation extends Invocation {
    public NullInvocation(Class<?> returnType) {
        super(-1, null, returnType, null);
    }
}