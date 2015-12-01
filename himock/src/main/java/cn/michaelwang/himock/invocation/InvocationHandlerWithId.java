package cn.michaelwang.himock.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationHandlerWithId implements InvocationHandler {
    private InvocationListener invocationListener;
    private int id;

    public InvocationHandlerWithId(int id, InvocationListener invocationListener) {
        this.id = id;
        this.invocationListener = invocationListener;
    }


    @SuppressWarnings("unchecked")
    private List<Class<Throwable>> getExceptions(Method method) {
        Class<?>[] exceptions = method.getExceptionTypes();
        List<Class<Throwable>> typeChecked = new ArrayList<>();
        for (Class<?> exception : exceptions) {
            if (Throwable.class.isAssignableFrom(exception)) {
                typeChecked.add((Class<Throwable>) exception);
            }
        }
        return typeChecked;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Class<Throwable>> typeChecked = getExceptions(method);
        Invocation invocation = new Invocation(id, getInvocationName(method), method.getReturnType(), args, typeChecked);
        return invocationListener.methodCalled(invocation);
    }

    private String getInvocationName(Method method) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "()";
    }
}