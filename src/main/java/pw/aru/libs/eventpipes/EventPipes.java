package pw.aru.libs.eventpipes;

import pw.aru.libs.eventpipes.api.EventExecutor;
import pw.aru.libs.eventpipes.api.EventPipe;
import pw.aru.libs.eventpipes.api.keyed.KeyedEventPipe;
import pw.aru.libs.eventpipes.api.typed.TypedEventPipe;
import pw.aru.libs.eventpipes.internal.DefaultEventPipe;
import pw.aru.libs.eventpipes.internal.DefaultKeyedEventPipe;
import pw.aru.libs.eventpipes.internal.DefaultTypedEventPipe;

import java.util.concurrent.ExecutorService;

public class EventPipes {
    public static <T> EventPipe<T> newPipe() {
        return new DefaultEventPipe<>(EventExecutor.ON_THREAD);
    }

    public static <T> EventPipe<T> newAsyncPipe() {
        return new DefaultEventPipe<>(EventExecutor.ASYNC);
    }

    public static <T> EventPipe<T> newAsyncPipe(ExecutorService executor) {
        return new DefaultEventPipe<>(EventExecutor.of(executor));
    }

    public static <T> EventPipe<T> newAsyncPipe(EventExecutor executor) {
        return new DefaultEventPipe<>(executor);
    }

    public static <K, V> KeyedEventPipe<K, V> newKeyedPipe() {
        return new DefaultKeyedEventPipe<>(EventExecutor.ON_THREAD);
    }

    public static <K, V> KeyedEventPipe<K, V> newAsyncKeyedPipe() {
        return new DefaultKeyedEventPipe<>(EventExecutor.ASYNC);
    }

    public static <K, V> KeyedEventPipe<K, V> newAsyncKeyedPipe(ExecutorService executor) {
        return new DefaultKeyedEventPipe<>(EventExecutor.of(executor));
    }

    public static <K, V> KeyedEventPipe<K, V> newAsyncKeyedPipe(EventExecutor executor) {
        return new DefaultKeyedEventPipe<>(executor);
    }

    public static TypedEventPipe newTypedPipe() {
        return new DefaultTypedEventPipe(EventExecutor.ON_THREAD);
    }

    public static TypedEventPipe newAsyncTypedPipe() {
        return new DefaultTypedEventPipe(EventExecutor.ASYNC);
    }

    public static TypedEventPipe newAsyncTypedPipe(ExecutorService executor) {
        return new DefaultTypedEventPipe(EventExecutor.of(executor));
    }

    public static TypedEventPipe newAsyncTypedPipe(EventExecutor executor) {
        return new DefaultTypedEventPipe(executor);
    }
}
