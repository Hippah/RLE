package me.hippo.api.testing.rle;

import me.hippo.api.testing.rle.topic.ExampleTopic;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPool;
import rip.hippo.lwjeb.annotation.Handler;
import rip.hippo.lwjeb.bus.PubSub;
import rip.hippo.lwjeb.extensions.rle.RedisPubSub;

import java.io.Serializable;


/**
 * @author Hippo
 * @version 1.0.1, 4/21/20
 * @since 1.0.0
 */
public final class ReceiverTest {

  @Test
  public void testReceive() {
    try {
      JedisPool jedisPool = new JedisPool("localhost");

      PubSub<Serializable> pubSub = new PubSub<>();
      pubSub.setupDispatchers();
      pubSub.subscribe(this);

      RedisPubSub redisPubSub = new RedisPubSub(pubSub);
      redisPubSub.subscribeRedisChannel(jedisPool);

      redisPubSub.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Handler
  public void onMessage(ExampleTopic exampleTopic) {
    System.out.println(exampleTopic);
  }
}
