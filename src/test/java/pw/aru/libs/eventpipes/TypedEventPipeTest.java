package pw.aru.libs.eventpipes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pw.aru.libs.eventpipes.api.typed.TypedEventPipe;

import java.util.concurrent.CompletableFuture;

public class TypedEventPipeTest implements Objects {

    @Test
    public void publishOnce() {
        EventPipes.newTypedPipe().publish(any);
    }

    @Test
    public void publishSubscribe() {
        final TypedEventPipe pipe = EventPipes.newTypedPipe();
        final Any[] result = new Any[1];
        pipe.subscribe(Any.class, any -> result[0] = any);
        pipe.publish(any);
        Assertions.assertNotNull(result[0]);
    }

    @Test
    public void publishFirst() {
        final TypedEventPipe pipe = EventPipes.newTypedPipe();
        final CompletableFuture<Any> first = pipe.first(Any.class);
        pipe.publish(any);
        Assertions.assertNotNull(first.getNow(null));
    }

    @Test
    public void publishMulti() {
        final TypedEventPipe pipe = EventPipes.newTypedPipe();
        final CompletableFuture<Object> firstObject = pipe.first(Object.class);
        final CompletableFuture<Any> firstAny = pipe.first(Any.class);
        final CompletableFuture<A> firstA = pipe.first(A.class);
        final CompletableFuture<A1> firstA1 = pipe.first(A1.class);
        final CompletableFuture<AA1> firstAA1 = pipe.first(AA1.class);

        pipe.publish(a1);
        Assertions.assertNotNull(firstObject.getNow(null));
        Assertions.assertNotNull(firstAny.getNow(null));
        Assertions.assertNotNull(firstA.getNow(null));
        Assertions.assertNotNull(firstA1.getNow(null));
        Assertions.assertNull(firstAA1.getNow(null));
    }

    @Test
    public void massPublish() {
        final int[] expected = {500, 400, 100, 200, 100};
        final Object[] input = {any, a, aa, a1, aa1};
        final Class<?>[] types = {Any.class, A.class, AA.class, A1.class, AA1.class};
        final int each = 100;

        final TypedEventPipe pipe = EventPipes.newTypedPipe();
        final int[] result = new int[expected.length];
        for (int i = 0; i < input.length; i++) {
            int finalI = i;
            pipe.subscribe(types[i], any -> ++result[finalI]);
        }

        for (int i = 0; i < each; i++) {
            for (Object obj : input) {
                pipe.publish(obj);
            }
        }
        Assertions.assertArrayEquals(expected, result);
    }
}
