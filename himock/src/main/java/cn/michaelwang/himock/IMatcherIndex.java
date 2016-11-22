package cn.michaelwang.himock;

public interface IMatcherIndex {

	void markMatcher(int lineNumber, String mark);
	
	/**
     * @param lineNumber line number of the matcher
     * @param methodName name of the method who use the matcher
     *
	 * @param args
	 *            The name of the arguments in the invocation. Non-matcher
	 *            arguments are set to null.
	 */
	void useMatcher(int lineNumber, String methodName, String[] args);

	void addMatcher(int lineNumber, Matcher<?> aMatcher);

	/**
	 * Get the <i>argIndex</i>-th argument's matcher
	 *
     * @param lineNumber line number of the matcher
     * @param methodName name of the method that the matcher belongs to
     * @param argIndex the index of the matcher in the arguments
     * @return the matcher
     */
	Matcher<?> getMatcher(int lineNumber, String methodName, int argIndex);

}