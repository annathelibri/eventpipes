package pw.aru.libs.eventpipes.api.typed;

import java.util.concurrent.CompletableFuture;

public interface TypedEventPublisher {
    CompletableFuture<Void> publish(Object event);
}
