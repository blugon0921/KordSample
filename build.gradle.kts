plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val kotlinVersion = kotlin.coreLibrariesVersion
val buildPath = File("C:/Users/blugo/Desktop")


repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("reflect"))
    implementation("dev.kord:kord-core:latest.release")
    implementation("org.slf4j:slf4j-simple:latest.release")
}



tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
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
        attributes["Main-Class"] = "${project.group}.${project.name.toLowerCase()}.${project.name}Kt" //Main File
    }
}