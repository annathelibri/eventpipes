package pw.aru.libs.eventpipes.api.keyed;

import pw.aru.libs.eventpipes.api.EventConsumer;
import pw.aru.libs.eventpipes.api.EventSubscription;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface KeyedEventSubscriber<K, V> {
    EventSubscription<V> subscribe(K key, EventConsumer<V> consumer);

    CompletableFuture<V> first(K key, Predicate<V> predicate);

    default CompletableFuture<V> first(K key) {
        return first(key, obj -> true);
    }
}
