package org.jmock.example.announcer;

import cn.michaelwang.himock.HiMockRunner;
import cn.michaelwang.himock.annotations.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.EventListener;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.fail;

@SuppressWarnings("CodeBlock2Expr")
@RunWith(HiMockRunner.class)
public class _HiMock_AnnouncerTests {
    public static class CheckedException extends Exception {
    }

    public interface Listener extends EventListener {
        void eventA();

        void eventB();

        void eventWithArguments(int a, int b);

        void badEvent() throws CheckedException;
    }

    Announcer<Listener> announcer = Announcer.to(Listener.class);

    @Mock
    Listener listener1;
    @Mock
    Listener listener2;

    @Before
    public void setUp() {
        announcer.addListener(listener1);
        announcer.addListener(listener2);
    }

    @Test
    public void testAnnouncesToRegisteredListenersInOrderOfAddition() {
        announcer.announce().eventA();
        announcer.announce().eventB();

        verifyInOrder(() -> {
            listener1.eventA();
            listener2.eventA();
            listener1.eventB();
            listener2.eventB();
        });
    }

    @Test
    public void testPassesEventArgumentsToListeners() {
        announcer.announce().eventWithArguments(1, 2);
        announcer.announce().eventWithArguments(3, 4);

        verify(() -> {
            listener1.eventWithArguments(1, 2);
            listener2.eventWithArguments(1, 2);
            listener1.eventWithArguments(3, 4);
            listener2.eventWithArguments(3, 4);
        });
    }

    @Test
    public void testCanRemoveListeners() {
        announcer.removeListener(listener1);

        announcer.announce().eventA();

        verify(() -> {
            listener2.eventA();
        });
    }

    @Test
    public void testDoesNotAllowListenersToThrowCheckedExceptions() throws Exception {
        expect(() -> {
            listener1.badEvent();
            willThrow(new CheckedException());
        });

        try {
            announcer.announce().badEvent();
            fail("should have thrown UnsupportedOperationException");
        }catch (UnsupportedOperationException ignored) {}
    }
}
