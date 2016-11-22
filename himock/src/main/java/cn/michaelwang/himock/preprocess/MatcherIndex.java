package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.IMatcherIndex;
import cn.michaelwang.himock.Matcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MatcherIndex implements IMatcherIndex {
    private final Map<Integer, Queue<String>> marks = new HashMap<>();
    private final Map<String, Matcher<?>> matchers = new HashMap<>();

    private final Map<Integer, Map<String, Queue<String[]>>> matcherUsages = new HashMap<>();

	@Override
	public void markMatcher(int lineNumber, String mark) {
		Queue<String> line = marks.getOrDefault(lineNumber, new LinkedList<>());
		marks.putIfAbsent(lineNumber, line);

		line.offer(mark);
	}

	@Override
	public void useMatcher(int lineNumber, String methodName, String[] args) {
		Map<String, Queue<String[]>> lineUsage = matcherUsages.getOrDefault(lineNumber, new HashMap<>());
		matcherUsages.putIfAbsent(lineNumber, lineUsage);

		Queue<String[]> argsQueue = lineUsage.getOrDefault(methodName, new LinkedList<>());
		lineUsage.putIfAbsent(methodName, argsQueue);

		argsQueue.offer(args);
	}

	@Override
	public void addMatcher(int lineNumber, Matcher<?> aMatcher) {
		String mark = marks.get(lineNumber).poll();
		matchers.put(mark, aMatcher);
	}

	@Override
	public Matcher<?> getMatcher(int lineNumber, String methodName, int argIndex) {
		String[] marks = matcherUsages.getOrDefault(lineNumber, new HashMap<>())
				.getOrDefault(methodName, new LinkedList<>()).peek();
		if (marks == null) {
			return null;
		}
		String mark = marks[argIndex];
		if (mark == null) {
			return null;
		}
		marks[argIndex] = null;

		if (allRetrieved(marks)) {
			matcherUsages.get(lineNumber).get(methodName).poll();
		}

		return matchers.get(mark);
	}

	private boolean allRetrieved(String[] marks) {
		for (String mark : marks) {
			if (mark != null) {
				return false;
			}
		}

		return true;
	}

}
