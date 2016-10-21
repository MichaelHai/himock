package org.easymock.documentation;

import cn.michaelwang.himock.HiMockRunner;
import org.easymock.samples.Collaborator;
import org.junit.Test;
import org.junit.runner.RunWith;

import static cn.michaelwang.himock.HiMock.*;
import static junit.framework.TestCase.assertEquals;

@RunWith(HiMockRunner.class)
public class DocumentationExamplesHiMockTest {
    @Test
    public void changingBehaviorForTheSameMethodCall() {
        Collaborator collaborator = mock(Collaborator.class);
        String title = "Document";

        expect(() -> {
            collaborator.voteForRemoval(title);
            willReturn(42);
            times(3);
            willThrow(new RuntimeException());
            willReturn(-42);
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

        verify();
    }

    @Test
    public void checkingMethodCallOrderBetweenMocks() {
        MyInterface mock1 = mock(MyInterface.class);
        MyInterface mock2 = mock(MyInterface.class);

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

        verifyInOrder(() -> {
            mock1.a();
            mock2.a();
        });
        verifyInOrder(() -> {
            mock2.b();
            mock1.b();
        });
    }
}
