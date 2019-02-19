package pw.aru.lib.eventpipes.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public interface EventExecutor {
    EventExecutor ASYNC = CompletableFuture::runAsync;
    EventExecutor ON_THREAD = runnable -> {
        runnable.run();
        return CompletableFuture.completedFuture(null);
    };

    static EventExecutor of(ExecutorService service) {
        return runnable -> CompletableFuture.runAsync(runnable, service);
    }

    static EventExecutor upgrade(Consumer<Runnable> runnableConsumer) {
        return runnable -> {
            CompletableFuture<?> completable = new CompletableFuture<>();
            runnableConsumer.accept(() -> {
                runnable.run();
                completable.complete(null);
            });
            return completable;
        };
    }

    CompletableFuture<?> execute(Runnable runnable);
}
