package pw.aru.libs.eventpipes.api;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface EventSubscriber<T> {
    EventSubscription<T> subscribe(EventConsumer<T> consumer);

    CompletableFuture<T> first(Predicate<T> predicate);
}
