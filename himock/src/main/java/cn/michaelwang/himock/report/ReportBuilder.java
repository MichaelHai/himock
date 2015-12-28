package cn.michaelwang.himock.report;

import cn.michaelwang.himock.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {
    private final String LEVEL_INDICATOR = "\t";
    private StringBuilder sb = new StringBuilder();

    private int level = 0;

    public void buildNextLevel(Level level) {
        levelStart();
        level.buildLevel(this);
        levelEnd();
    }

    public <T> void appendLine(T... lines) {
        lineStart();
        for (T line : lines) {
            append(line);
        }
        lineEnd();
    }

    public void appendInvocationDetail(String methodName, Object[] args, StackTraceElement[] stackTraceElements) {
        appendInvocationMessage(methodName, args);
        appendStackTrace(stackTraceElements);
    }

    public void appendStackTrace(StackTraceElement[] stackTrace) {
        StackTraceElement[] filteredTraces = simplifyTheStackTraces(stackTrace);
        appendLine("-> at ", filteredTraces[0]);
        for (int i = 1; i < filteredTraces.length; i++) {
            appendLine("   at ", filteredTraces[i]);
        }
    }

    public void appendParameters(String description, Object[] parameters) {
        lineStart();

        append(description);
        for (Object parameter : parameters) {
            append("\t");
            append(parameter);
        }

        lineEnd();
    }

    public void appendInvocationMessage(String methodName, Object[] args) {
        lineStart();

        sb.append(Utils.removeParenthesesInFunctionName(methodName));
        sb.append("(");
        for (Object parameter : args) {
            sb.append(parameter);
            sb.append(", ");
        }
        if (sb.lastIndexOf(",") != -1) {
            sb.delete(sb.lastIndexOf(","), sb.length());
        }
        sb.append(")");

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

    private StackTraceElement[] simplifyTheStackTraces(StackTraceElement[] traces) {
        List<StackTraceElement> usefulTraces = new ArrayList<>();

        for (StackTraceElement trace : traces) {
            if (trace.getClassName().equals("sun.reflect.NativeMethodAccessorImpl")) {
                break;
            }

            // the second and third condition is intend for the test of the class
            if (!(trace.getClassName().startsWith("cn.michaelwang.himock") || trace.getFileName() == null)
                    || (trace.getFileName() != null && trace.getFileName().contains("Test"))) {
                usefulTraces.add(trace);
            }
        }

        StackTraceElement[] newTraces = new StackTraceElement[usefulTraces.size()];
        usefulTraces.toArray(newTraces);
        return newTraces;
    }

    @FunctionalInterface
    public interface Level {
        void buildLevel(ReportBuilder reportBuilder);
    }
}
