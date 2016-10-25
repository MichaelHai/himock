package cn.michaelwang.himock.process.mockup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.any;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.MockedClass;
import cn.michaelwang.himock.process.InvocationListener;
import cn.michaelwang.himock.process.exceptions.NoExpectedInvocationException;
import cn.michaelwang.himock.process.mockup.ClassMockBuilder;

public class MockBuilderTest {
	@Test
	public void testClassCanBeMocked() {
		ClassMockBuilder<MockedClass> builder = new ClassMockBuilder<>(MockedClass.class);
		MockedClass dummy = builder.createMock(0);

		assertNotNull(dummy);
	}

	@Test
	public void testMockedClassShouldResponseAsCreatedOneIfNoExpectIsSettled() {
		ClassMockBuilder<MockedClass> builder = new ClassMockBuilder<>(MockedClass.class);
		MockedClass dummy = builder.createMock(0);

		assertEquals(5, dummy.add(2, 3));
	}

	@Test
	public void testInvocationHandlerCanBeUsed() throws NoExpectedInvocationException, Throwable {
		ClassMockBuilder<MockedClass> builder = new ClassMockBuilder<>(MockedClass.class);

		InvocationListener listener = Mockito.mock(InvocationListener.class);
		builder.setInvocationListener(listener);

		MockedClass dummy = builder.createMock(0);

		dummy.add(2, 3);

		Mockito.verify(listener).methodCalled(argThat(new ArgumentMatcher<Invocation>() {
			@Override
			public boolean matches(Object item) {
				if (item instanceof Invocation) {
					Invocation invocation = (Invocation) item;

					return "cn.michaelwang.himock.MockedClass.add()".equals(invocation.getMethodName())
							&& Arrays.deepEquals(invocation.getArguments(), new Object[] { 2, 3 })
							&& invocation.getObjectId() == 0;
				}

				return false;
			}
		}));
	}

	@Test
	public void testListnerReturnedValueShouldBeReturned() throws NoExpectedInvocationException, Throwable {
		ClassMockBuilder<MockedClass> builder = new ClassMockBuilder<>(MockedClass.class);

		InvocationListener listener = Mockito.mock(InvocationListener.class);
		builder.setInvocationListener(listener);

		MockedClass dummy = builder.createMock(0);

		Mockito.when(listener.methodCalled(any())).thenReturn(6);

		assertEquals(6, dummy.add(2, 3));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListnerHasNoExpectationShouldInvokeTheOriginalMethod()
			throws NoExpectedInvocationException, Throwable {
		ClassMockBuilder<MockedClass> builder = new ClassMockBuilder<>(MockedClass.class);

		InvocationListener listener = Mockito.mock(InvocationListener.class);
		builder.setInvocationListener(listener);

		MockedClass dummy = builder.createMock(0);

		Mockito.when(listener.methodCalled(any())).thenThrow(NoExpectedInvocationException.class);

		assertEquals(5, dummy.add(2, 3));
	}
}
