package pw.aru.lib.eventpipes.api;

@FunctionalInterface
public interface EventConsumer<T> {
    void onEvent(T event);
}
