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

    @Test
    public void checkingMethodCallOrderBetweenMocks() {
        HiMock mock = new HiMock();
        MyInterface mock1 = mock.mock(MyInterface.class);
        MyInterface mock2 = mock.mock(MyInterface.class);

        // Ordered:
        mock1.a();
        mock2.a();

        // Unordered:
        mock2.c();
        mock1.c();
        mock2.c();

        // Ordered:
        mock2.b();
        mock1.b();

        mock.verifyInOrder(() -> {
            mock1.a();
            mock2.a();
        });
        mock.verifyInOrder(() -> {
            mock2.b();
            mock1.b();
        });
    }
}
