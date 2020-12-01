package me.hippo.api.testing.rle;

import rip.hippo.lwjeb.annotation.Handler;
import rip.hippo.lwjeb.bus.PubSub;
import rip.hippo.lwjeb.extensions.rle.RedisPubSub;
import me.hippo.api.testing.rle.topic.ExampleTopic;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;


/**
 * @author Hippo
 * @version 1.0.1, 4/21/20
 * @since 1.0.0
 */
public enum ReceiverExample {
    INSTANCE;

    public static void main(String[] args) {
        try {
            JedisPool jedisPool = new JedisPool("localhost");

            PubSub<Serializable> pubSub = new PubSub<>();
            pubSub.setupDispatchers();
            pubSub.subscribe(INSTANCE);

            RedisPubSub redisPubSub = new RedisPubSub(pubSub);
            redisPubSub.subscribeRedisChannel(jedisPool);

            redisPubSub.shutdown();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Handler
    public void onMessage(ExampleTopic exampleTopic) {
        System.out.println(exampleTopic);
    }
}
