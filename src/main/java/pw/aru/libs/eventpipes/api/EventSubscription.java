package pw.aru.libs.eventpipes.api;

import java.io.Closeable;

public interface EventSubscription<T> extends Closeable {
    EventPipe<T> pipe();

    void close();
}
