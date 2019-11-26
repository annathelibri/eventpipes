package pw.aru.libs.eventpipes.api.typed;

import pw.aru.libs.eventpipes.api.EventConsumer;
import pw.aru.libs.eventpipes.api.EventSubscription;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface TypedEventSubscriber {
    <T> EventSubscription<T> subscribe(Class<T> type, EventConsumer<T> consumer);

    <T> CompletableFuture<T> first(Class<T> type, Predicate<T> predicate);

    default <T> CompletableFuture<T> first(Class<T> type) {
        return first(type, obj -> true);
    }
}
