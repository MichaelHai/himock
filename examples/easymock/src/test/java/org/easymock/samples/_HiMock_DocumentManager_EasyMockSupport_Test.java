package org.easymock.samples;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.michaelwang.himock.HiMockRunner;

@RunWith(HiMockRunner.class)
public class _HiMock_DocumentManager_EasyMockSupport_Test {
	private Collaborator firstCollaborator;
	private Collaborator secondCollaborator;
	private DocumentManager classUnderTest;

	@Before
	public void setup() {
		classUnderTest = new DocumentManager();
	}

	@After
	public void verifyExpectations() {
		verify();
	}

	@Test
	public void addDocument() {
		firstCollaborator = mock(Collaborator.class);
		secondCollaborator = mock(Collaborator.class);
		classUnderTest.addListener(firstCollaborator);
		classUnderTest.addListener(secondCollaborator);

		expect(() -> {
			firstCollaborator.documentAdded("New Document");
			secondCollaborator.documentAdded("New Document");
		});

		classUnderTest.addDocument("New Document", new byte[0]);
	}

	@Test
    public void voteForRemovals() {
        firstCollaborator = mock(Collaborator.class);
        secondCollaborator = mock(Collaborator.class);
        classUnderTest.addListener(firstCollaborator);
        classUnderTest.addListener(secondCollaborator);

        expect(() -> {
            firstCollaborator.documentAdded("Document 1");
            secondCollaborator.documentAdded("Document 1");
            
            firstCollaborator.voteForRemovals("Document 1");
            willReturn(20);
            
            secondCollaborator.voteForRemovals("Document 1");
            willReturn(-10);

            firstCollaborator.documentRemoved("Document 1");
            secondCollaborator.documentRemoved("Document 1");
        });


        classUnderTest.addDocument("Document 1", new byte[0]);
        
        assertTrue(classUnderTest.removeDocuments("Document 1"));
    }
}
