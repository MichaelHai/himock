package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.Invocation;

import java.util.List;

public class InvocationImpl implements Invocation {
    private int objectId;
    private String methodName;

    private List<Class<Throwable>> exceptionTypes;

    private Object[] args;

    private StackTraceElement[] stackTraceElements;
    protected Class<?> returnType;

    public InvocationImpl(int objectId, String methodName, Class<?> returnType, Object[] args, List<Class<Throwable>> exceptionTypes) {
        this.objectId = objectId;
        this.methodName = methodName;
        this.returnType = returnType;
        this.args = args == null ? new Object[0] : args;
        this.stackTraceElements = new Exception().getStackTrace();
        this.exceptionTypes = exceptionTypes;
    }

    @Override
    public int getObjectId() {
        return objectId;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Object[] getParameters() {
        return args;
    }

    @Override
    public StackTraceElement[] getInvocationStackTrace() {
        return stackTraceElements;
    }

    @Override
    public boolean sameMethod(Invocation invocation) {
        return invocation.getObjectId() == this.getObjectId()
                && invocation.getMethodName().equals(this.getMethodName());

    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public List<Class<Throwable>> getExceptionTypes() {
        return exceptionTypes;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode() + objectId + methodName.hashCode() + returnType.hashCode();
        for (Object arg : args) {
            if (arg != null) {
                hashCode += arg.hashCode();
            }
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvocationImpl) {
            InvocationImpl toCompare = (InvocationImpl) obj;
            return objectId == toCompare.objectId
                    && methodName.equals(toCompare.methodName)
                    && returnType.equals(toCompare.returnType);
        }

        return false;
    }
}
