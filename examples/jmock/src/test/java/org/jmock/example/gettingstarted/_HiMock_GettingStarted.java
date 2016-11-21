package org.jmock.example.gettingstarted;

import cn.michaelwang.himock.HiMockRunner;
import cn.michaelwang.himock.annotations.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

import static cn.michaelwang.himock.HiMock.times;
import static cn.michaelwang.himock.HiMock.verify;

@RunWith(HiMockRunner.class)
public class _HiMock_GettingStarted {
    @Mock
    Subscriber subscriber;

    @Test
    public void oneSubscriberReceivesAMessage() {
        // set up

        Publisher publisher = new Publisher();
        publisher.add(subscriber);

        final String message = "message";

        // execute
        publisher.publish(message);

        // verify
        verify(() -> {
            subscriber.receive(message);
            times(1);
        });
    }
}
