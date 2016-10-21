package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.Invocation;

import java.util.Arrays;
import java.util.List;

public class InvocationImpl implements Invocation {
    private int objectId;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
    private Class<?> returnType;
    private List<Class<Throwable>> exceptionTypes;

    private StackTraceElement[] stackTraceElements;
	private int lineNumber;

    public InvocationImpl(int objectId, String methodName, Class<?>[] parameterTypes, Object[] args, Class<?> returnType, List<Class<Throwable>> exceptionTypes) {
        this.objectId = objectId;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
        this.args = args == null ? new Object[0] : args;
        this.returnType = returnType;
        this.exceptionTypes = exceptionTypes;
        this.stackTraceElements = new Exception().getStackTrace();
        this.lineNumber = stackTraceElements[3].getLineNumber();
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
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return args;
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
    public StackTraceElement[] getInvocationStackTrace() {
        return stackTraceElements;
    }

	@Override
	public int getLineNumber() {
		return lineNumber;
	}

    @Override
    public boolean sameMethod(Invocation invocation) {
        return invocation.getObjectId() == this.getObjectId()
                && invocation.getMethodName().equals(this.getMethodName())
                && Arrays.equals(invocation.getParameterTypes(), this.getParameterTypes());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvocationImpl) {
            InvocationImpl toCompare = (InvocationImpl) obj;
            return this.sameMethod(toCompare)
                    && Arrays.equals(args, toCompare.getArguments());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode() + objectId + methodName.hashCode();
        for (Object arg : args) {
            if (arg != null) {
                hashCode += arg.hashCode();
            }
        }

        return hashCode;
    }
}
