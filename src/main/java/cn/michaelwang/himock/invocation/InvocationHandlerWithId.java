package cn.michaelwang.himock.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvocationHandlerWithId implements InvocationHandler {
    private InvocationListener invocationListener;
    private int id;

    public InvocationHandlerWithId(int id, InvocationListener invocationListener) {
        this.id = id;
        this.invocationListener = invocationListener;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new Invocation(id, getInvocationName(method), method.getReturnType(), args);
        return invocationListener.methodCalled(invocation);
    }

    private String getInvocationName(Method method) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "()";
    }
}