package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.Matcher;

public interface IMatcherIndex {

	void addMatcherMark(int lineNumber, String mark);
	
	/**
	 * @param lineNumber
	 * @param methodName
	 * 
	 * @param args
	 *            The name of the arguments in the invocation. Non-matcher
	 *            arguments are set to null.
	 */
	void addMatcherUsage(int lineNumber, String methodName, String[] args);

	void addMatcher(int lineNumber, Matcher<?> aMatcher);

	/**
	 * Get the <i>argIndex</i>-th argument's matcher
	 * 
	 * @param lineNumber
	 * @param methodName
	 * @param argIndex
	 * @return
	 */
	Matcher<?> getMatcher(int lineNumber, String methodName, int argIndex);

}