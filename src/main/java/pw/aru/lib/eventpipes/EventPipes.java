package pw.aru.lib.eventpipes;

import pw.aru.lib.eventpipes.api.EventExecutor;
import pw.aru.lib.eventpipes.api.EventPipe;
import pw.aru.lib.eventpipes.api.keyed.KeyedEventPipe;
import pw.aru.lib.eventpipes.internal.DefaultEventPipe;
import pw.aru.lib.eventpipes.internal.DefaultKeyedEventPipe;

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
}
