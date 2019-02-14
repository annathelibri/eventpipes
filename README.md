# EventPipes
Event Pipes for Java. Quite Reactive.

The library is REALLY small and is made on pure Java 8.

### Get it on JCenter:

![Latest Version](https://api.bintray.com/packages/adriantodt/maven/eventpipes/images/download.svg)

### Using It:

Your starting point is `pw.aru.lib.eventpipes.EventPipes`.

- Use `EventPipes.newPipe` (or `newAsyncPipe`) to create your first pipe.
- If you need stuff being handled by the key, use `EventPipes.newKeyedPipe` (or `newAsyncKeyedPipe`)
- `EventPipe` implements `EventPublisher` and `EventSubscriber`, and the actual `publish`/`subscribe` methods are on that class.

### Example Implementations:

```java
public interface MyEvents {
    EventPipe<ReadyEvent> READY = newAsyncPipe();
    EventPipe<Integer> EVERY_SECOND = newAsyncPipe();
    EventPipe<RedisChannelMessage> REDIS_MESSAGES = newAsyncPipe();
}

public class EventEmitter extends MyEvents {
    public static void emitEvents() throws Exception {
        READY.publish(new ReadyEvent());
        
        for(int i = 0; i < 100; i++) {
            EVERY_SECOND.publish(i);
            Thread.sleep(1000);
        }
        
        MyRedis.instance().subscribe("very-important-messages", REDIS_MESSAGES::publish);
    }
}

public class MyEventAPI {
    public final EventSubscriber<ReadyEvent> onReady;
    public final EventSubscriber<Integer> eachSecond;
    
    public MyEventAPI() {
        EventPipe<ReadyEvent> readyPipe = newAsyncPipe();
        EventPipe<Integer> secondPipe = newAsyncPipe();
        
        onReady = readyPipe.subscriber();
        eachSecond = secondPipe.subscriber();
        
        MyEventGateway gateway = new GatewayImpl(this, readyPipe.publisher(), eachSecond.publisher());
        gateway.connect("https://my-gateway.what-a-cool-discord-bot.com/v6/");
    }
}
```