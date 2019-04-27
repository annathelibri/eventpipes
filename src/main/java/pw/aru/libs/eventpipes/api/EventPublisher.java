package pw.aru.libs.eventpipes.api;

import java.util.concurrent.CompletableFuture;

public interface EventPublisher<T> {
    CompletableFuture<Void> publish(T event);
}
