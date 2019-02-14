package pw.aru.lib.eventpipes.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public interface EventExecutor {
    EventExecutor ASYNC = CompletableFuture::runAsync;
    EventExecutor ON_THREAD = runnable -> {
        runnable.run();
        return CompletableFuture.completedFuture(null);
    };

    static EventExecutor of(ExecutorService service) {
        return runnable -> CompletableFuture.runAsync(runnable, service);
    }

    CompletableFuture<?> execute(Runnable runnable);
}
