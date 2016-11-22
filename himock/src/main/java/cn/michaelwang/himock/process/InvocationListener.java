package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.exceptions.NoExpectedInvocationException;

public interface InvocationListener {
	/**
	 * The method to actually handler the invocation.
	 * 
	 * @param invocation
	 *            The invocation to be handled.
	 * @return The invocation result.
	 * @throws NoExpectedInvocationException
	 *             If the method is called not in expectation or verification
	 *             and there are no expectations set,
	 *             NoExpectedInvocationException should be thrown
	 * @throws Throwable
	 *             The invocation can throw exceptions if set in the
	 */
    Object methodCalled(Invocation invocation) throws Throwable;
}
