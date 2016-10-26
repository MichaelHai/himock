package cn.michaelwang.himock.process;

import java.util.ArrayList;
import java.util.List;

import cn.michaelwang.himock.IMatcherIndex;
import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.MockProcessManager;
import cn.michaelwang.himock.process.exceptions.ExceptionTypeIsNotSuitableException;
import cn.michaelwang.himock.process.exceptions.NoReturnTypeException;
import cn.michaelwang.himock.process.exceptions.ReturnTypeIsNotSuitableException;
import cn.michaelwang.himock.process.mockup.MockFactoryImpl;
import cn.michaelwang.himock.process.reporters.ExceptionTypeIsNotSuitableReporter;
import cn.michaelwang.himock.process.reporters.ExpectExceptionBeforeInvocationReporter;
import cn.michaelwang.himock.process.reporters.ExpectReturnBeforeInvocationReporter;
import cn.michaelwang.himock.process.reporters.ExpectReturnOutsideExpectReporter;
import cn.michaelwang.himock.process.reporters.ExpectThrowOutsideExpectReporter;
import cn.michaelwang.himock.process.reporters.InvalidMatchPositionReporter;
import cn.michaelwang.himock.process.reporters.NoReturnTypeReporter;
import cn.michaelwang.himock.process.reporters.ReturnTypeIsNotSuitableReporter;
import cn.michaelwang.himock.process.reporters.TimerOutsideExpectReporter;
import cn.michaelwang.himock.process.verifiers.InOrderVerifier;
import cn.michaelwang.himock.process.verifiers.NormalVerifier;
import cn.michaelwang.himock.utils.Utils;

public class MockStateManager implements MockProcessManager, InvocationListener {
	private MockFactory mockFactory;
	private InvocationRecorder invocationRecorder;
	private IMatcherIndex matcherIndex;

	private MockState state = new NormalState();

	private List<Verifier> verifiers = new ArrayList<>();
	private Verifier verifier;

	public MockStateManager(InvocationRecorder invocationRecorder,
			IMatcherIndex matcherIndex) {
		this.mockFactory = MockFactoryImpl.getInstance();
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
		this.state = new ExpectState(verifier);
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
	public <T> T mock(Class<T> mockedType) {
		return mockFactory.createMock(mockedType, this);
	}

	@Override
	public <T> T mock(Class<T> mockedType, Object[] constructorParameters) {
		return mockFactory.createMock(mockedType, this, constructorParameters);
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

	private class ExpectState extends VerificationState {
		private Expectation lastCall;

		public ExpectState(Verifier verifier) {
			super(verifier);
		}

		@Override
		public Object methodCalled(Invocation invocation) {
			List<Matcher<?>> matchers = getMatchers(invocation);
			lastCall = invocationRecorder.expect(invocation, matchers);

			createAndAddVerification(invocation, matchers);
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

			addVerificationForAnswer();
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

			addVerificationForAnswer();
		}

		@Override
		public void lastReturnTimer(int times) {
			lastCall.answerMore(times - 1);
			super.lastReturnTimer(times);
		}

		private void addVerificationForAnswer() {
			if (lastCall.hasMultipleAnswer()) {
				createAndAddVerification(lastCall.getInvocation(), lastCall.getMatchers());
			}
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
			List<Matcher<?>> matchers = getMatchers(invocation);
			createAndAddVerification(invocation, matchers);
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
			verifier.lastVerificationTimes(times);
		}

		protected void createAndAddVerification(Invocation invocation, List<Matcher<?>> matchers) {
			Verification verification = new VerificationImpl(invocation, matchers);
			verifier.addVerification(verification);
			lastVerification = verification;
		}
	}
}
