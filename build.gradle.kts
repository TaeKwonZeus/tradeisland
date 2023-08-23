plugins {
    java
}

group = "net.cubicworld"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
