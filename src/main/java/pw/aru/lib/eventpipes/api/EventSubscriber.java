package pw.aru.lib.eventpipes.api;

public interface EventSubscriber<T> {
    EventSubscription<T> subscribe(EventConsumer<T> consumer);
}
