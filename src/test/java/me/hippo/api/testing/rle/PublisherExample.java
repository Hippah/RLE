package me.hippo.api.testing.rle;

import me.hippo.api.lwjeb.annotation.Handler;
import me.hippo.api.lwjeb.bus.PubSub;
import me.hippo.api.lwjeb.extensions.rle.RedisPubSub;
import me.hippo.api.testing.rle.topic.ExampleTopic;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;


/**
 * @author Hippo
 * @version 1.0.0, 4/21/20
 * @since 1.0.0
 */
public enum PublisherExample {
    ;

    public static void main(String[] args) {
        try {
            JedisPool jedisPool = new JedisPool("localhost");

            RedisPubSub redisPubSub = new RedisPubSub(new PubSub<>());

            redisPubSub.post(jedisPool, new ExampleTopic(69));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
