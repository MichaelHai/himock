package org.easymock.samples;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.michaelwang.himock.HiMockRunner;

import java.util.List;

@RunWith(HiMockRunner.class)
public class _HiMock_DocumentManagerTest {
	private DocumentManager classUnderTest;
	private Collaborator mock;

	@Before
	public void setup() {
		mock = mock(Collaborator.class);
		classUnderTest = new DocumentManager();
		classUnderTest.addListener(mock);
	}

	@Test
	public void removeNonExistingDocument() {
		assertTrue(classUnderTest.removeDocument("Does not exist"));
	}

	@Test
	public void addDocument() {
		//noinspection CodeBlock2Expr
		expect(() -> {
			mock.documentAdded("New Document");
		});

		classUnderTest.addDocument("New Document", new byte[0]);

		verify();
	}

	@Test
	public void addAndChangeDocument() {
		expect(() -> {
			mock.documentAdded("Document");

			mock.documentChanged("Document");
			times(3);
		});

		classUnderTest.addDocument("Document", new byte[0]);
		classUnderTest.addDocument("Document", new byte[0]);
		classUnderTest.addDocument("Document", new byte[0]);
		classUnderTest.addDocument("Document", new byte[0]);

		verify();
	}

	@Test
	public void voteForRemoval() {
		expect(() -> {
			mock.documentAdded("Document");

			mock.voteForRemoval("Document");
			willReturn(42);

			mock.documentRemoved("Document");
		});

		classUnderTest.addDocument("Document", new byte[0]);
		assertTrue(classUnderTest.removeDocument("Document"));

		verify();
	}

	@Test
	public void voteAgainstRemoval() {
		expect(() -> {
			mock.documentAdded("Document");

			mock.voteForRemoval("Document");
			willReturn(-42);
		});

		classUnderTest.addDocument("Document", new byte[0]);
		assertFalse(classUnderTest.removeDocument("Document"));

		verify();
	}
	
	@Test
	public void answerVsDelegate() {
		List l = mock(List.class);

		expect(() -> {
			l.remove(10);
			willAnswer(params -> params[0].toString());
		});

		assertEquals("10", l.remove(10));

		verify();
	}
}
