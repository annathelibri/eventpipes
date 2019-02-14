package pw.aru.lib.eventpipes.internal;

import pw.aru.lib.eventpipes.api.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static pw.aru.lib.eventpipes.internal.Wrapper.wrapPublisher;
import static pw.aru.lib.eventpipes.internal.Wrapper.wrapSubscriber;

public class DefaultEventPipe<T> implements EventPipe<T> {
    private final EventExecutor executor;
    private final Set<EventConsumer<T>> consumers;

    public DefaultEventPipe(EventExecutor executor) {
        this.executor = executor;
        this.consumers = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Override
    public CompletableFuture<Void> publish(T event) {
        return CompletableFuture.allOf(
            consumers.stream()
                .map(consumer -> executor.execute(new EventRunnable(event, consumer)))
                .toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public EventSubscription<T> subscribe(EventConsumer<T> consumer) {
        return new Subscription(consumer);
    }

    private void unsubscribe(EventConsumer<T> consumer) {
        consumers.remove(consumer);
        if (consumers.isEmpty()) {
            onEmpty();
        }
    }

    protected void onEmpty() {
        //noop
    }

    @Override
    public EventSubscriber<T> subscriber() {
        return wrapSubscriber(this);
    }

    @Override
    public EventPublisher<T> publisher() {
        return wrapPublisher(this);
    }

    class Subscription implements EventSubscription<T>, EventConsumer<T> {
        private final EventConsumer<T> consumer;

        Subscription(EventConsumer<T> consumer) {
            this.consumer = Objects.requireNonNull(consumer);
            consumers.add(this);
        }

        @Override
        public void onEvent(T event) {
            consumer.onEvent(event);
        }

        @Override
        public void close() {
            unsubscribe(this);
        }

        @Override
        public EventPipe<T> pipe() {
            return DefaultEventPipe.this;
        }
    }

    class EventRunnable implements Runnable {
        private final T event;
        private final EventConsumer<T> consumer;

        EventRunnable(T event, EventConsumer<T> consumer) {
            this.event = event;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            consumer.onEvent(event);
        }
    }

}
