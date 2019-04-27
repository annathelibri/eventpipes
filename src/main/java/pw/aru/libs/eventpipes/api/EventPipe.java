package pw.aru.libs.eventpipes.api;

import java.io.Closeable;

public interface EventPipe<T> extends EventSubscriber<T>, EventPublisher<T>, Closeable {
    EventSubscriber<T> subscriber();

    EventPublisher<T> publisher();

    @Override
    void close();
}
