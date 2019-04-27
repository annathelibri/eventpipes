package pw.aru.libs.eventpipes.api;

@FunctionalInterface
public interface EventConsumer<T> {
    void onEvent(T event);
}
