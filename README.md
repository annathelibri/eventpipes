# EventPipes

Fully configured lightweight event publishing/subscribing library, made in pure Java.

The library is REALLY small and is made on pure Java 8.

Licensed under the [MIT License](https://github.com/arudiscord/eventpipes/blob/master/LICENSE).

### Installation

![Latest Version](https://api.bintray.com/packages/arudiscord/maven/eventpipes/images/download.svg)

Using in Gradle:

```gradle
repositories {
  jcenter()
}

dependencies {
  compile 'pw.aru.libs:eventpipes:LATEST' // replace LATEST with the version above
}
```

Using in Maven:

```xml
<repositories>
  <repository>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>pw.aru.libs</groupId>
    <artifactId>eventpipes</artifactId>
    <version>LATEST</version> <!-- replace LATEST with the version above -->
  </dependency>
</dependencies>
```

### Usage

The starting point of the library is the `EventPipes` class.

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

### Support

Support is given on [Aru's Discord Server](https://discord.gg/URPghxg)

[![Aru's Discord Server](https://discordapp.com/api/guilds/403934661627215882/embed.png?style=banner2)](https://discord.gg/URPghxg)
