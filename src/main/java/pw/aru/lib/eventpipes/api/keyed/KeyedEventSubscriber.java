package pw.aru.lib.eventpipes.api.keyed;

import pw.aru.lib.eventpipes.api.EventConsumer;
import pw.aru.lib.eventpipes.api.EventSubscription;

public interface KeyedEventSubscriber<K, V> {
    EventSubscription<V> subscribe(K key, EventConsumer<V> consumer);
}
