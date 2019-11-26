package pw.aru.libs.eventpipes.api.typed;

import pw.aru.libs.eventpipes.api.EventPipe;
import pw.aru.libs.eventpipes.api.EventPublisher;
import pw.aru.libs.eventpipes.api.EventSubscriber;

import java.io.Closeable;

public interface TypedEventPipe extends TypedEventSubscriber, TypedEventPublisher, Closeable {
    TypedEventSubscriber subscriber();

    TypedEventPublisher publisher();

    <T> EventPipe<T> pipe(Class<T> type);

    <T> EventSubscriber<T> subscriber(Class<T> type);

    <T> EventPublisher<T> publisher(Class<T> type);

    @Override
    void close();
}
