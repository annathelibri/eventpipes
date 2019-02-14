package pw.aru.lib.eventpipes.api;

public interface EventPipe<T> extends EventSubscriber<T>, EventPublisher<T> {
    EventSubscriber<T> subscriber();

    EventPublisher<T> publisher();
}
