plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val kotlinVersion = kotlin.coreLibrariesVersion
val buildPath = File("C:/Users/blugo/Desktop")
val mainClass = "${project.group}.${project.name.lowercase()}.${project.name}Kt" //Main File

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("reflect"))
    implementation("dev.kord:kord-core:latest.release")
    implementation("org.slf4j:slf4j-simple:latest.release")
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:latest.release")
}



tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_21.toString()
    }

    jar { this.build() }
    shadowJar { this.build() }
}

fun Jar.build() {
    from(sourceSets["main"].output)
    archiveBaseName.set(project.name) //Project Name
    archiveFileName.set("${project.name}.jar") //Build File Name
    archiveVersion.set(project.version.toString()) //Version

    doLast {
        copy {
            from(archiveFile) //Copy from
            into(buildPath) //Copy to
        }
    }

    manifest {
        attributes["Main-Class"] = mainClass //Main File
    }
}