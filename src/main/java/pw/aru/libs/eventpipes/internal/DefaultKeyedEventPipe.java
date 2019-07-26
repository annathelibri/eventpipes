package pw.aru.libs.eventpipes.internal;

import pw.aru.libs.eventpipes.api.*;
import pw.aru.libs.eventpipes.api.keyed.KeyedEventPipe;
import pw.aru.libs.eventpipes.api.keyed.KeyedEventPublisher;
import pw.aru.libs.eventpipes.api.keyed.KeyedEventSubscriber;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static pw.aru.libs.eventpipes.internal.Wrapper.wrapPublisher;
import static pw.aru.libs.eventpipes.internal.Wrapper.wrapSubscriber;

public class DefaultKeyedEventPipe<K, V> implements KeyedEventPipe<K, V> {
    private final EventExecutor executor;
    private final Map<K, EventPipe<V>> pipes;

    public DefaultKeyedEventPipe(EventExecutor executor) {
        this.executor = executor;
        this.pipes = new ConcurrentHashMap<>();
    }

    @Override
    public CompletableFuture<Void> publish(K key, V value) {
        return pipeOf(key).publish(value);
    }

    @Override
    public EventSubscription<V> subscribe(K key, EventConsumer<V> consumer) {
        return pipeOf(key).subscribe(consumer);
    }

    @Override
    public CompletableFuture<V> first(K key, Predicate<V> predicate) {
        return pipeOf(key).first(predicate);
    }

    private EventPipe<V> pipeOf(K key) {
        return pipes.computeIfAbsent(key, ignored -> new DefaultEventPipe<V>(executor) {
            @Override
            protected void onEmpty() {
                pipes.remove(key);
            }

            @Override
            protected CompletableFuture<?> onExecute(V event, EventConsumer<V> consumer) {
                return executor.executeKeyed(key, new EventRunnable(event, consumer));
            }
        });
    }

    @Override
    public KeyedEventSubscriber<K, V> subscriber() {
        return wrapSubscriber(this);
    }

    @Override
    public KeyedEventPublisher<K, V> publisher() {
        return wrapPublisher(this);
    }

    @Override
    public EventPipe<V> pipe(K key) {
        return new KeyedPipe(key);
    }

    @Override
    public EventSubscriber<V> subscriber(K key) {
        return Wrapper.wrapSubscriber(pipe(key));
    }

    @Override
    public EventPublisher<V> publisher(K key) {
        return Wrapper.wrapPublisher(pipe(key));
    }

    @Override
    public void close() {
        for (EventPipe<V> pipe : pipes.values()) {
            pipe.close();
        }
    }

    class KeyedPipe implements EventPipe<V> {
        private final K key;

        KeyedPipe(K key) {
            this.key = key;
        }

        @Override
        public CompletableFuture<Void> publish(V event) {
            return DefaultKeyedEventPipe.this.publish(key, event);
        }

        @Override
        public EventSubscription<V> subscribe(EventConsumer<V> consumer) {
            return DefaultKeyedEventPipe.this.subscribe(key, consumer);
        }

        @Override
        public CompletableFuture<V> first(Predicate<V> predicate) {
            return DefaultKeyedEventPipe.this.first(key, predicate);
        }

        @Override
        public EventSubscriber<V> subscriber() {
            return wrapSubscriber(this);
        }

        @Override
        public EventPublisher<V> publisher() {
            return wrapPublisher(this);
        }

        @Override
        public void close() {
            pipeOf(key).close();
        }
    }
}
