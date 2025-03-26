plugins {
    id("java")
}

group = "iuh.edu.vn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("jakarta.json:jakarta.json-api:2.1.3")
    implementation ("jakarta.json.bind:jakarta.json.bind-api:3.0.1")
    implementation ("org.eclipse.parsson:parsson:1.1.7")
    implementation ("org.eclipse:yasson:3.0.4")
    compileOnly ("org.projectlombok:lombok:1.18.36")
    annotationProcessor ("org.projectlombok:lombok:1.18.36")
    implementation ("net.datafaker:datafaker:2.4.2")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}