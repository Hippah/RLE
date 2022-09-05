plugins {
    java
    id("maven-publish")
}

group = "rip.hippo"
version = "1.2.1"


repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("rip.hippo:LWJEB:5.3.0")
    implementation("redis.clients:jedis:4.2.3")
    implementation("com.google.code.gson:gson:2.9.0")
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