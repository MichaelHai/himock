package org.easymock.documentation;

import cn.michaelwang.himock.HiMock;
import org.easymock.samples.Collaborator;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class DocumentationExamplesHiMockTest {
    @Test
    public void changingBehaviorForTheSameMethodCall() {
        HiMock mock = new HiMock();
        Collaborator collaborator = mock.mock(Collaborator.class);
        String title = "Document";

        mock.expect(() -> {
            collaborator.voteForRemoval(title);
            mock.willReturn(42).times(3).willThrow(new RuntimeException()).willReturn(-42);
        });

        assertEquals(42, collaborator.voteForRemoval(title));
        assertEquals(42, collaborator.voteForRemoval(title));
        assertEquals(42, collaborator.voteForRemoval(title));

        try {
            collaborator.voteForRemoval(title);
        } catch (RuntimeException e) {
            // OK
        }

        assertEquals(-42, collaborator.voteForRemoval(title));

        mock.verify();
    }
}
