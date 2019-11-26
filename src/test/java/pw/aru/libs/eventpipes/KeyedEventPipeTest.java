package pw.aru.libs.eventpipes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pw.aru.libs.eventpipes.api.keyed.KeyedEventPipe;

import java.util.concurrent.CompletableFuture;

public class KeyedEventPipeTest implements Objects {
    @Test
    public void publishOnce() {
        EventPipes.newKeyedPipe().publish(any, any);
    }

    @Test
    public void publishSubscribe() {
        final KeyedEventPipe<Any, Any> pipe = EventPipes.newKeyedPipe();
        final Objects.Any[] result = new Objects.Any[1];
        pipe.subscribe(any, any -> result[0] = any);
        pipe.publish(any, any);
        Assertions.assertNotNull(result[0]);
    }

    @Test
    public void publishFirst() {
        final KeyedEventPipe<Any, Any> pipe = EventPipes.newKeyedPipe();
        final CompletableFuture<Any> first = pipe.first(any);
        pipe.publish(any, any);
        Assertions.assertNotNull(first.getNow(null));
    }

    @Test
    public void massPublish() {
        final int[] expected = new int[]{500, 400, 300, 200, 100};

        final KeyedEventPipe<Integer, Any> pipe = EventPipes.newKeyedPipe();
        final int[] result = new int[expected.length];
        for (int k = 0; k < expected.length; k++) {
            int finalK = k;
            pipe.subscribe(k, any -> ++result[finalK]);
            for (int i = 0; i < expected[k]; i++) {
                pipe.publish(k, any);
            }
        }

        Assertions.assertArrayEquals(expected, result);
    }
}
