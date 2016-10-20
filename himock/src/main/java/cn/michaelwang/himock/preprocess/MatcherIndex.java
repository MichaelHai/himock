package cn.michaelwang.himock.preprocess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import cn.michaelwang.himock.IMatcherIndex;
import cn.michaelwang.himock.Matcher;

public class MatcherIndex implements IMatcherIndex {
	private Map<Integer, Queue<String>> marks = new HashMap<>();
	private Map<String, Matcher<?>> matchers = new HashMap<>();

	private Map<Integer, Map<String, Queue<String[]>>> matcherUsages = new HashMap<>();

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
		String mark = matcherUsages.get(lineNumber).get(methodName).poll()[argIndex];
		if (mark == null) {
			throw new RetrieveMatcherForNonMatcherArgException();
		}
		return matchers.get(mark);
	}

}
