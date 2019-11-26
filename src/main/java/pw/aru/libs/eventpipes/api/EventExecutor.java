package pw.aru.libs.eventpipes.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface EventExecutor {
    EventExecutor ASYNC = CompletableFuture::runAsync;
    EventExecutor ON_THREAD = runnable -> {
        CompletableFuture<?> completable = new CompletableFuture<>();
        try {
            runnable.run();
            completable.complete(null);
        } catch (Exception e) {
            completable.completeExceptionally(e);
        }
        return completable;
    };

    static EventExecutor of(ExecutorService service) {
        return runnable -> CompletableFuture.runAsync(runnable, service);
    }

    static EventExecutor upgrade(Consumer<Runnable> runnableConsumer) {
        return runnable -> {
            CompletableFuture<?> completable = new CompletableFuture<>();
            runnableConsumer.accept(() -> {
                try {
                    runnable.run();
                    completable.complete(null);
                } catch (Exception e) {
                    completable.completeExceptionally(e);
                }
            });
            return completable;
        };
    }

    static EventExecutor upgradeKeyed(BiConsumer<Object, Runnable> keyedRunnableConsumer) {
        return new EventExecutor() {
            @Override
            public CompletableFuture<?> execute(Runnable runnable) {
                return executeKeyed(runnable, runnable);
            }

            @Override
            public CompletableFuture<?> executeKeyed(Object key, Runnable runnable) {
                CompletableFuture<?> completable = new CompletableFuture<>();
                keyedRunnableConsumer.accept(key, () -> {
                    try {
                        runnable.run();
                        completable.complete(null);
                    } catch (Exception e) {
                        completable.completeExceptionally(e);
                    }
                });
                return completable;
            }
        };
    }

    CompletableFuture<?> execute(Runnable runnable);

    default CompletableFuture<?> executeKeyed(Object key, Runnable runnable) {
        return execute(runnable);
    }
}
