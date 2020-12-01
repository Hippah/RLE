# RLE
Redis LWJEB Extension, this allows you to easily use LWJEB with your redis based applications, integrating with redis pubsub via Jedis.

Add RLE to your project:

```
repositories {
  ...
  maven { url 'https://jitpack.io' }
}
```

```
dependencies {
    implementation 'com.github.Hippo:RLE:1.0.0'
    implementation 'redis.clients:jedis:3.2.0'
    implementation 'com.google.code.gson:gson:2.8.6'

    //These are required for LWJEB
    implementation 'com.github.Hippo:LWJEB:5.1.2'
    implementation 'org.ow2.asm:asm:7.1'
    implementation 'org.ow2.asm:asm-tree:7.1'
}
```

