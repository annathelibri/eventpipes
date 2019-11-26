package pw.aru.libs.eventpipes.internal;

import pw.aru.libs.eventpipes.api.*;
import pw.aru.libs.eventpipes.api.typed.TypedEventPipe;
import pw.aru.libs.eventpipes.api.typed.TypedEventPublisher;
import pw.aru.libs.eventpipes.api.typed.TypedEventSubscriber;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static pw.aru.libs.eventpipes.internal.Wrapper.wrapPublisher;
import static pw.aru.libs.eventpipes.internal.Wrapper.wrapSubscriber;

public class DefaultTypedEventPipe implements TypedEventPipe {
    private final EventExecutor executor;
    private final Map<String, EventPipe<Object>> pipes;

    public DefaultTypedEventPipe(EventExecutor executor) {
        this.executor = executor;
        this.pipes = new ConcurrentHashMap<>();
    }

    @Override
    public CompletableFuture<Void> publish(Object value) {
        return CompletableFuture.allOf(
            collectTypes(value.getClass(), new LinkedHashSet<>()).stream()
                .map(pipes::get)
                .filter(Objects::nonNull)
                .map(pipe -> pipe.publish(value))
                .toArray(CompletableFuture[]::new)
        );
    }

    private Set<String> collectTypes(Class<?> type, Set<String> target) {
        if (type == null) return target;
        target.add(type.getName());
        collectTypes(type.getSuperclass(), target);
        for (Class<?> t : type.getInterfaces()) {
            collectTypes(t, target);
        }
        return target;
    }

    @Override
    public <T> EventSubscription<T> subscribe(Class<T> type, EventConsumer<T> consumer) {
        return this.<T>pipeOf(type.getName()).subscribe(consumer);
    }

    @Override
    public <T> CompletableFuture<T> first(Class<T> type, Predicate<T> predicate) {
        return this.<T>pipeOf(type.getName()).first(predicate);
    }

    @SuppressWarnings("unchecked")
    private <T> EventPipe<T> pipeOf(String type) {
        return (EventPipe<T>) pipes.computeIfAbsent(type, ignored -> new DefaultEventPipe<Object>(executor) {
            @Override
            protected void onEmpty() {
                pipes.remove(type);
            }

            @Override
            protected CompletableFuture<?> onExecute(Object event, EventConsumer<Object> consumer) {
                return executor.executeKeyed(type, new EventRunnable(event, consumer));
            }
        });
    }

    @Override
    public TypedEventSubscriber subscriber() {
        return wrapSubscriber(this);
    }

    @Override
    public TypedEventPublisher publisher() {
        return wrapPublisher(this);
    }

    @Override
    public <T> EventPipe<T> pipe(Class<T> type) {
        return new TypedPipe<T>(type);
    }

    @Override
    public <T> EventSubscriber<T> subscriber(Class<T> type) {
        return Wrapper.wrapSubscriber(pipe(type));
    }

    @Override
    public <T> EventPublisher<T> publisher(Class<T> type) {
        return Wrapper.wrapPublisher(pipe(type));
    }

    @Override
    public void close() {
        for (EventPipe<?> pipe : pipes.values()) {
            pipe.close();
        }
    }

    class TypedPipe<T> implements EventPipe<T> {
        private final Class<T> type;

        TypedPipe(Class<T> type) {
            this.type = type;
        }

        @Override
        public CompletableFuture<Void> publish(Object event) {
            return DefaultTypedEventPipe.this.publish(event);
        }

        public EventSubscription<T> subscribe(EventConsumer<T> consumer) {
            return DefaultTypedEventPipe.this.subscribe(type, consumer);
        }

        public CompletableFuture<T> first(Predicate<T> predicate) {
            return DefaultTypedEventPipe.this.first(type, predicate);
        }

        @Override
        public EventSubscriber<T> subscriber() {
            return wrapSubscriber(this);
        }

        @Override
        public EventPublisher<T> publisher() {
            return wrapPublisher(this);
        }

        @Override
        public void close() {
            pipeOf(type.getName()).close();
        }
    }
}
