package cn.michaelwang.himock.process;

import java.util.ArrayList;
import java.util.List;

import cn.michaelwang.himock.IMatcherIndex;
import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.MockProcessManager;
import cn.michaelwang.himock.mockup.ExceptionTypeIsNotSuitableException;
import cn.michaelwang.himock.mockup.InvocationListener;
import cn.michaelwang.himock.mockup.NoReturnTypeException;
import cn.michaelwang.himock.mockup.ReturnTypeIsNotSuitableException;
import cn.michaelwang.himock.process.reporters.ExceptionTypeIsNotSuitableReporter;
import cn.michaelwang.himock.process.reporters.ExpectReturnBeforeInvocationReporter;
import cn.michaelwang.himock.process.reporters.ExpectReturnOutsideExpectReporter;
import cn.michaelwang.himock.process.reporters.ExpectThrowOutsideExpectReporter;
import cn.michaelwang.himock.process.reporters.InvalidMatchPositionReporter;
import cn.michaelwang.himock.process.reporters.MockNoninterfaceReporter;
import cn.michaelwang.himock.process.reporters.NoReturnTypeReporter;
import cn.michaelwang.himock.process.reporters.ReturnTypeIsNotSuitableReporter;
import cn.michaelwang.himock.process.reporters.TimerOutsideExpectReporter;
import cn.michaelwang.himock.utils.Utils;
import cn.michaelwang.himock.verify.InOrderVerifier;
import cn.michaelwang.himock.verify.NormalVerifier;
import cn.michaelwang.himock.verify.Verification;
import cn.michaelwang.himock.verify.Verifier;

public class MockStateManager implements MockProcessManager, InvocationListener {
	private MockFactory mockFactory;
	private InvocationRecorder invocationRecorder;
	private IMatcherIndex matcherIndex;

	private MockState state = new NormalState();

	private List<Verifier> verifiers = new ArrayList<>();
	private Verifier verifier;

	public MockStateManager(MockFactory mockFactory, InvocationRecorder invocationRecorder,
			IMatcherIndex matcherIndex) {
		this.mockFactory = mockFactory;
		this.invocationRecorder = invocationRecorder;
		this.matcherIndex = matcherIndex;
		this.verifier = new NormalVerifier();
		verifiers.add(verifier);
	}

	@Override
	public void toNormalState() {
		this.state = new NormalState();
	}

	@Override
	public void toExpectState() {
		this.state = new ExpectState();
	}

	@Override
	public void toVerifyState() {
		NormalVerifier verifier = new NormalVerifier();
		createVerificationState(verifier);
	}

	@Override
	public void toOrderedVerifyState() {
		InOrderVerifier verifier = new InOrderVerifier();
		createVerificationState(verifier);
	}

	@Override
	public <T> T mock(Class<T> mockedInterface) {
		if (!mockedInterface.isInterface()) {
			throw new MockNoninterfaceReporter(mockedInterface);
		}

		return mockFactory.createMock(mockedInterface, this);
	}

	@Override
	public <T> void lastCallReturn(T returnValue, Class<?> type) {
		state.lastCallReturn(returnValue, type);
	}

	@Override
	public void lastCallThrow(Throwable e) {
		state.lastCallThrow(e);
	}

	@Override
	public void lastReturnTimer(int times) {
		state.lastReturnTimer(times);
	}

	@Override
	public void doVerify() {
		verifiers.forEach((verifier) -> verifier.verify(invocationRecorder.getActuallyInvocations()));
	}

	@Override
	public <T> void addMatcher(Matcher<T> matcher) {
		StackTraceElement[] traces = new Exception().getStackTrace();
		int lineNumber = traces[2].getLineNumber();
		matcherIndex.addMatcher(lineNumber, matcher);
	}

	@Override
	public Object methodCalled(Invocation invocation) throws Throwable {
		return state.methodCalled(invocation);
	}

	private void createVerificationState(Verifier verifier) {
		verifiers.add(verifier);
		this.state = new VerificationState(verifier);
	}

	private List<Matcher<?>> getMatchers(Invocation invocation) {
		int lineNumber = invocation.getLineNumber();
		String methodName = invocation.getMethodName();
		methodName = Utils.removeParenthesesInFunctionName(methodName);
		methodName = methodName.substring(methodName.lastIndexOf(".") + 1);
		Object[] args = invocation.getArguments();

		List<Matcher<?>> matchers = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			Matcher<?> matcher = matcherIndex.getMatcher(lineNumber, methodName, i);
			if (matcher != null) {
				matchers.add(matcher);
			} else {
				matchers.add(new ValueMatcher<>(args[i]));
			}
		}

		return matchers;
	}

	private interface MockState {
		Object methodCalled(Invocation invocation) throws Throwable;

		<T> void lastCallReturn(T returnValue, Class<?> type);

		void lastCallThrow(Throwable e);

		void lastReturnTimer(int times);
	}

	private class NormalState implements MockState {
		@Override
		public Object methodCalled(Invocation invocation) throws Throwable {
			List<Matcher<?>> matchers = getMatchers(invocation);
			if (!matchers.stream().allMatch((matcher) -> matcher instanceof ValueMatcher)) {
				throw new InvalidMatchPositionReporter();
			}

			return invocationRecorder.actuallyCall(invocation);
		}

		@Override
		public <T> void lastCallReturn(T returnValue, Class<?> type) {
			throw new ExpectReturnOutsideExpectReporter();
		}

		@Override
		public void lastCallThrow(Throwable e) {
			throw new ExpectThrowOutsideExpectReporter();
		}

		@Override
		public void lastReturnTimer(int times) {
			throw new TimerOutsideExpectReporter();
		}
	}

	private class ExpectState implements MockState {
		private Expectation lastCall;

		@Override
		public Object methodCalled(Invocation invocation) {
			List<Matcher<?>> matchers = getMatchers(invocation);
			lastCall = invocationRecorder.expect(invocation, matchers);
			verifier.addVerification(lastCall.generateVerification());
			return new NullExpectation(invocation).getReturnValue();
		}

		@Override
		public <T> void lastCallReturn(T returnValue, Class<?> type) {
			if (lastCall == null) {
				throw new ExpectReturnBeforeInvocationReporter();
			}

			try {
				lastCall.addReturnValue(returnValue, type);
			} catch (NoReturnTypeException e) {
				throw new NoReturnTypeReporter(e.getInvocation());
			} catch (ReturnTypeIsNotSuitableException e) {
				if (returnValue instanceof Throwable) {
					lastCallThrow((Throwable) returnValue);
				} else {
					throw new ReturnTypeIsNotSuitableReporter(e.getInvocation(), e.getToSetType());
				}
			}
		}

		@Override
		public void lastCallThrow(Throwable toThrow) {
			if (lastCall == null) {
				throw new ExpectExceptionBeforeInvocationReporter();
			}

			try {
				lastCall.addException(toThrow);
			} catch (ExceptionTypeIsNotSuitableException e) {
				throw new ExceptionTypeIsNotSuitableReporter(e.getInvocation(), e.getToThrow());
			}
		}

		@Override
		public void lastReturnTimer(int times) {
			lastCall.answerMore(times - 1);
		}
	}

	private class VerificationState implements MockState {
		protected Verifier verifier;
		private Verification lastVerification;

		public VerificationState(Verifier verifier) {
			this.verifier = verifier;
		}

		@Override
		public Object methodCalled(Invocation invocation) {
			lastVerification = newVerification(invocation);
			verifier.addVerification(lastVerification);
			return new NullExpectation(invocation).getReturnValue();
		}

		@Override
		public <T> void lastCallReturn(T returnValue, Class<?> type) {
			throw new ExpectReturnOutsideExpectReporter();
		}

		@Override
		public void lastCallThrow(Throwable e) {
			throw new ExpectThrowOutsideExpectReporter();
		}

		@Override
		public void lastReturnTimer(int times) {
			for (int i = 0; i < times - 1; i++) {
				verifier.addVerification(lastVerification);
			}
		}

		private Verification newVerification(Invocation invocation) {
			List<Matcher<?>> matchers = getMatchers(invocation);
			Verification verification = new VerificationImpl(invocation, matchers);
			return verification;
		}
	}
}
