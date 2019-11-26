package pw.aru.libs.eventpipes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pw.aru.libs.eventpipes.api.EventPipe;

import java.util.concurrent.CompletableFuture;

public class EventPipeTest implements Objects {
    @Test
    public void publishOnce() {
        EventPipes.newPipe().publish(any);
    }

    @Test
    public void publishSubscribe() {
        final EventPipe<Any> pipe = EventPipes.newPipe();
        final Any[] result = new Any[1];
        pipe.subscribe(any -> result[0] = any);
        pipe.publish(any);
        Assertions.assertNotNull(result[0]);
    }

    @Test
    public void publishFirst() {
        final EventPipe<Any> pipe = EventPipes.newPipe();
        final CompletableFuture<Any> first = pipe.first();
        pipe.publish(any);
        Assertions.assertNotNull(first.getNow(null));
    }

    @Test
    public void massPublish() {
        final int count = 1000;

        final EventPipe<Any> pipe = EventPipes.newPipe();
        final int[] result = new int[1];
        pipe.subscribe(any -> ++result[0]);
        for (int i = 0; i < count; i++) {
            pipe.publish(any);
        }
        Assertions.assertEquals(count, result[0]);
    }
}
