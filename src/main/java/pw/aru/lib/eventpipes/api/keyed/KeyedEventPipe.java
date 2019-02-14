package pw.aru.lib.eventpipes.api.keyed;

import pw.aru.lib.eventpipes.api.EventPipe;
import pw.aru.lib.eventpipes.api.EventPublisher;
import pw.aru.lib.eventpipes.api.EventSubscriber;

public interface KeyedEventPipe<K, V> extends KeyedEventSubscriber<K, V>, KeyedEventPublisher<K, V> {
    KeyedEventSubscriber<K, V> subscriber();

    KeyedEventPublisher<K, V> publisher();

    EventPipe<V> pipe(K key);

    EventSubscriber<V> subscriber(K key);

    EventPublisher<V> publisher(K key);
}
