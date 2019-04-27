package pw.aru.libs.eventpipes.api.keyed;

import java.util.concurrent.CompletableFuture;

public interface KeyedEventPublisher<K, V> {
    CompletableFuture<Void> publish(K key, V value);
}
