package cn.michaelwang.himock.invocation;

public class NullInvocation extends InvocationImpl {
    public NullInvocation(Class<?> returnType) {
        super(-1, null, returnType, null, null);
    }

    @Override
    public Object getReturnValue() {
        return nullValue();
    }
}