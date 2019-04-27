package pw.aru.libs.eventpipes.api;

public interface EventSubscriber<T> {
    EventSubscription<T> subscribe(EventConsumer<T> consumer);
}
