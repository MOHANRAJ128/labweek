plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "com.codeinsight.plugin"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

intellij {
    version.set("2023.3")
    type.set("IC")
    plugins.set(listOf("java"))
}

tasks {
    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set(null as String?)
    }

    runIde {
        autoReloadPlugins.set(true)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
    }
}
