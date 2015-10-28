package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.Utils;

import java.util.Arrays;

public class InvocationRecord {
    private String methodName;

    private Object returnValue;
    private Class<?> returnType;

    private Object[] args;

    private StackTraceElement[] stackTraceElements;

    InvocationRecord(String methodName, Class<?> returnType, Object[] args) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.args = args;
        this.stackTraceElements = new Exception().getStackTrace();
    }

    public String getMethodName() {
        return methodName;
    }

    public Object getReturnValue() {
        return returnValue == null ? nullValue() : returnValue;
    }

    public void setReturnValue(Object returnValue) {
        if (this.returnValue != null) {
            throw new ReturnValueAlreadySetException();
        }

        if (isSuitableType(returnValue.getClass(), returnType)) {
            this.returnValue = returnValue;
        } else if (returnType == Void.TYPE) {
            throw new NoReturnTypeException(methodName);
        } else {
            throw new ReturnTypeIsNotSuitableException();
        }
    }

    public Object[] getParameters() {
        return args;
    }

    public StackTraceElement[] getStackTraces() {
        return stackTraceElements;
    }

    public String getInvocationRecordDetail() {
        return "\t\t" + getInvocationMessage() + "\n"
                + getInvocationStackTrace();
    }

    public String getInvocationStackTrace() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t-> ");

        StackTraceElement[] traces = getStackTraces();
        StackTraceElement[] filteredTraces = Utils.simplifyTheStackTraces(traces);
        for (StackTraceElement trace : filteredTraces) {
            sb.append("\t\t   at ");
            sb.append(trace.toString());
            sb.append("\n");
        }
        sb.delete(5, 10);
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    private String getInvocationMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.removeParenthesesInFunctionName(methodName));
        sb.append("(");
        if (args != null) {
            for (Object parameter : args) {
                sb.append(parameter);
                sb.append(", ");
            }
            sb.delete(sb.lastIndexOf(","), sb.length());
        }
        sb.append(")");
        return sb.toString();
    }

    private Object nullValue() {
        if (returnType.isPrimitive()) {
            if (returnType.equals(Boolean.TYPE)) {
                return false;
            }
            return 0;
        }

        return null;
    }

    private boolean isSuitableType(Class<?> thisType, Class<?> targetType) {
        if (targetType.isAssignableFrom(thisType)) {
            return true;
        } else if (targetType.isPrimitive()) {
            String thisTypeName = thisType.getName();
            String targetTypeName = targetType.getName();

            if (thisTypeName.startsWith("java.lang.")) {
                thisTypeName = thisTypeName.substring("java.lang.".length());
                if (thisTypeName.toLowerCase().equals(targetTypeName)
                        || (thisTypeName.equals("Integer") && targetTypeName.equals("int"))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode() + methodName.hashCode() + returnType.hashCode();
        for (Object arg : args) {
            hashCode += arg.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvocationRecord) {
            InvocationRecord toCompare = (InvocationRecord) obj;
            return methodName.equals(toCompare.methodName)
                    && returnType.equals(toCompare.returnType)
                    && Arrays.equals(args, toCompare.args);
        }

        return false;
    }
}
