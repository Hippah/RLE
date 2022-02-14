plugins {
    java
    id("maven-publish")
}

group = "rip.hippo"
version = "1.1.1"


repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Hippo:LWJEB:5.3.0")
    implementation("redis.clients:jedis:3.2.0")
    implementation("com.google.code.gson:gson:2.8.6")
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}