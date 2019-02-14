package pw.aru.lib.eventpipes.api;

import java.util.concurrent.CompletableFuture;

public interface EventPublisher<T> {
    CompletableFuture<Void> publish(T event);
}
