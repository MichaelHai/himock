package cn.michaelwang.himock.report;

import cn.michaelwang.himock.Utils;
import cn.michaelwang.himock.recorder.InvocationRecord;

import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {
    private final String LEVEL_INDICATOR = "\t";
    private StringBuilder sb = new StringBuilder();
    private int level = 0;

    @FunctionalInterface
    public interface Level {
        void buildLevel();
    }

    public void buildNextLevel(Level level) {
        levelStart();
        level.buildLevel();
        levelEnd();
    }

    @SafeVarargs // safe
    public final <T> void appendLine(T... lines) {
        lineStart();
        for (T line : lines) {
            append(line);
        }
        lineEnd();
    }

    public void appendInvocationDetail(InvocationRecord invocationRecord) {
        appendInvocationMessage(invocationRecord);
        appendStackTrace(invocationRecord.getInvocationStackTrace());
    }

    public void appendStackTrace(StackTraceElement[] stackTrace) {
        StackTraceElement[] filteredTraces = simplifyTheStackTraces(stackTrace);
        appendLine("-> at ", filteredTraces[0]);
        for (int i = 1; i < filteredTraces.length; i++) {
            appendLine("   at ", filteredTraces[i]);
        }
    }

    public void appendParameters(String label, Object[] parameters) {
        lineStart();

        append(label);
        for (Object parameter : parameters) {
            append("\t");
            append(parameter);
        }

        lineEnd();
    }

    public String getReport() {
        return sb.toString();
    }

    private void levelStart() {
        level++;
    }

    private void levelEnd() {
        level--;
    }

    private void lineStart() {
        for (int i = 0; i < level; i++) {
            sb.append(LEVEL_INDICATOR);
        }
    }

    private <T> void append(T toAppend) {
        sb.append(toAppend);
    }

    private void lineEnd() {
        sb.append("\n");
    }

    private void appendInvocationMessage(InvocationRecord invocationRecord) {
        lineStart();
        sb.append(Utils.removeParenthesesInFunctionName(invocationRecord.getMethodName()));
        sb.append("(");
        Object[] args = invocationRecord.getParameters();
        if (args != null) {
            for (Object parameter : args) {
                sb.append(parameter);
                sb.append(", ");
            }
            sb.delete(sb.lastIndexOf(","), sb.length());
        }
        sb.append(")");
        lineEnd();
    }

    private StackTraceElement[] simplifyTheStackTraces(StackTraceElement[] traces) {
        List<StackTraceElement> usefulTraces = new ArrayList<>();

        for (StackTraceElement trace : traces) {
            if (trace.getClassName().equals("sun.reflect.NativeMethodAccessorImpl")) {
                break;
            }

            // the second and third condition is intend for the test of the class
            if (!(trace.getClassName().startsWith("cn.michaelwang.himock") || trace.getFileName() == null)
                    || trace.getMethodName().startsWith("test")
                    || trace.getMethodName().startsWith("lambda$test")) {
                usefulTraces.add(trace);
            }
        }

        StackTraceElement[] newTraces = new StackTraceElement[usefulTraces.size()];
        usefulTraces.toArray(newTraces);
        return newTraces;
    }
}
