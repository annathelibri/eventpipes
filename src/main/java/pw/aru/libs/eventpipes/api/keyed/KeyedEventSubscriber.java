package pw.aru.libs.eventpipes.api.keyed;

import pw.aru.libs.eventpipes.api.EventConsumer;
import pw.aru.libs.eventpipes.api.EventSubscription;

public interface KeyedEventSubscriber<K, V> {
    EventSubscription<V> subscribe(K key, EventConsumer<V> consumer);
}
