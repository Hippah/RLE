package rip.hippo.lwjeb.extensions.rle;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import rip.hippo.lwjeb.bus.AbstractAsynchronousPubSubMessageBus;
import rip.hippo.lwjeb.configuration.config.impl.BusConfiguration;
import rip.hippo.lwjeb.configuration.config.impl.ExceptionHandlingConfiguration;
import rip.hippo.lwjeb.extensions.rle.message.MessageAdapter;
import rip.hippo.lwjeb.extensions.rle.message.impl.GsonMessageAdapter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Hippo
 * <p>
 * A <tt>Redis Pub Sub</tt> is an implementation around {@link JedisPubSub}.
 * It wraps around and hooks into {@link AbstractAsynchronousPubSubMessageBus} for event publication.
 */
public final class RedisPubSub extends JedisPubSub {

  private static final String DEFAULT_CHANNEL = "LWJEB";

  /**
   * The parent bus.
   * <p>
   * The <tt>result queue</tt> of this bus will be modified.
   * </p>
   */
  private final AbstractAsynchronousPubSubMessageBus<Serializable> parentBus;

  /**
   * The message adapter.
   */
  private final MessageAdapter messageAdapter;

  /**
   * List of subscriber threads
   */
  private final List<Thread> subscriberThreads;

  /**
   * Constructs a <tt>Redis Pub Sub</tt> with the desired <tt>parent bus</tt> and <tt>message adapter</tt>.
   *
   * @param parentBus The parent bus.
   * @param messageAdapter The message adapter.
   */
  public RedisPubSub(AbstractAsynchronousPubSubMessageBus<Serializable> parentBus, MessageAdapter messageAdapter) {
    this.parentBus = parentBus;
    this.messageAdapter = messageAdapter;
    this.subscriberThreads = new LinkedList<>();
  }

  public RedisPubSub(AbstractAsynchronousPubSubMessageBus<Serializable> parentBus) {
    this(parentBus, new GsonMessageAdapter());
  }

  /**
   * Whenever a message is received from the redis pubsub channel
   * that message will be deserialized into a <tt>topic</tt> and added into the
   * <tt>parent bus</tt>'s result queue.
   *
   * @param channel The channel.
   * @param message The message
   */
  @Override
  public void onMessage(String channel, String message) {
    try {
      Serializable topic = messageAdapter.toSerializable(message);
      parentBus.getPublisher().publish(topic, parentBus).dispatch();
    } catch (Exception e) {
      parentBus.getConfigurations().get(ExceptionHandlingConfiguration.class).getExceptionHandler().handleException(e);
    }
  }

  /**
   * Post a <tt>topic</tt> to the default redis channel.
   *
   * @param jedisPool The jedis pool.
   * @param topic     The topic.
   */
  public void post(JedisPool jedisPool, Serializable topic) {
    post(jedisPool, "LWJEB", topic);
  }

  /**
   * Post a <tt>topic</tt> to the <tt>channel</tt> redis channel.
   *
   * @param jedisPool The jedis pool.
   * @param channel   The channel.
   * @param topic     the topic.
   */
  public void post(JedisPool jedisPool, String channel, Serializable topic) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.publish(channel, messageAdapter.toMessage(topic));
    }
  }

  /**
   * Subscribes to the default redis channel.
   *
   * @param jedisPool The jedis pool.
   */
  public void subscribeRedisChannel(JedisPool jedisPool) {
    subscribeRedisChannel(jedisPool, DEFAULT_CHANNEL);
  }

  /**
   * Subscribes to the <tt>channels</tt> redis channels.
   *
   * @param jedisPool The jedis pool.
   * @param channels The channels.
   */
  public void subscribeRedisChannel(JedisPool jedisPool, String... channels) {
    Thread thread = new Thread(() -> {
      try (Jedis jedis = jedisPool.getResource()) {
        jedis.subscribe(this, channels);
      }
    }, parentBus.getConfigurations().get(BusConfiguration.class).getIdentifier()
        + " - Redis Channels " + Arrays.toString(channels));
    thread.setDaemon(true);
    thread.start();
    subscriberThreads.add(thread);
  }

  public void using(JedisPool jedisPool, BiConsumer<RedisPubSub, Jedis> consumer) {
    try (Jedis jedis = jedisPool.getResource()) {
      consumer.accept(this, jedis);
    }
  }

  /**
   * Shuts down the <tt>parent bus</tt>.
   */
  public void shutdown() {
    parentBus.shutdown();
    for (Thread thread : subscriberThreads) {
      thread.interrupt();
    }
  }

  public AbstractAsynchronousPubSubMessageBus<Serializable> getParentBus() {
    return parentBus;
  }
}
