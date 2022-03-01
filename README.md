# RLE
Redis LWJEB Extension, this allows you to easily use LWJEB with your Redis based applications, integrating with Redis pubsub via Jedis.

Add RLE to your project:

```kotlin
repositories {
  maven("https://jitpack.io")
}
```

```
dependencies {
    implementation("rip.hippo:RLE:1.1.1")
    implementation("redis.clients:jedis:3.2.0")
    implementation("com.google.code.gson:gson:2.8.6")

    //These are required for LWJEB
    implementation("rip.hipo:LWJEB:3.1.0")
}
```

