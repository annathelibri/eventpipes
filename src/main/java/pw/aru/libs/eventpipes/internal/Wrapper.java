package pw.aru.libs.eventpipes.internal;

import pw.aru.libs.eventpipes.api.EventConsumer;
import pw.aru.libs.eventpipes.api.EventPublisher;
import pw.aru.libs.eventpipes.api.EventSubscriber;
import pw.aru.libs.eventpipes.api.EventSubscription;
import pw.aru.libs.eventpipes.api.keyed.KeyedEventPublisher;
import pw.aru.libs.eventpipes.api.keyed.KeyedEventSubscriber;

import java.util.concurrent.CompletableFuture;

public class Wrapper {
    public static <T> EventSubscriber<T> wrapSubscriber(EventSubscriber<T> wrapped) {
        return new WrappedSubscriber<T>(wrapped);
    }

    public static <K, V> KeyedEventSubscriber<K, V> wrapSubscriber(KeyedEventSubscriber<K, V> wrapped) {
        return new WrappedKeyedSubscriber<K, V>(wrapped);
    }

    public static <T> EventPublisher<T> wrapPublisher(EventPublisher<T> wrapped) {
        return new WrappedPublisher<T>(wrapped);
    }

    public static <K, V> KeyedEventPublisher<K, V> wrapPublisher(KeyedEventPublisher<K, V> wrapped) {
        return new WrappedKeyedPublisher<K, V>(wrapped);
    }

    private static class WrappedSubscriber<T> implements EventSubscriber<T> {
        private final EventSubscriber<T> wrapped;

        WrappedSubscriber(EventSubscriber<T> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public EventSubscription<T> subscribe(EventConsumer<T> consumer) {
            return wrapped.subscribe(consumer);
        }
    }

    private static class WrappedPublisher<T> implements EventPublisher<T> {
        private final EventPublisher<T> wrapped;

        WrappedPublisher(EventPublisher<T> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public CompletableFuture<Void> publish(T event) {
            return wrapped.publish(event);
        }
    }

    private static class WrappedKeyedSubscriber<K, V> implements KeyedEventSubscriber<K, V> {
        private final KeyedEventSubscriber<K, V> wrapped;

        WrappedKeyedSubscriber(KeyedEventSubscriber<K, V> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public EventSubscription<V> subscribe(K key, EventConsumer<V> consumer) {
            return wrapped.subscribe(key, consumer);
        }
    }

    private static class WrappedKeyedPublisher<K, V> implements KeyedEventPublisher<K, V> {
        private final KeyedEventPublisher<K, V> wrapped;

        WrappedKeyedPublisher(KeyedEventPublisher<K, V> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public CompletableFuture<Void> publish(K key, V value) {
            return wrapped.publish(key, value);
        }
    }
}
