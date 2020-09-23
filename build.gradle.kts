plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "ru.emkn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.shadowJar {
    archiveBaseName.set("runnable")
    archiveClassifier.set("")
    mergeServiceFiles()

    manifest {
        attributes["Main-Class"] = "ru.emkn.kotlin.MainKt"
    }
}

val runJar by tasks.creating(Exec::class) {
    val count = File(".git").walk().count()
    val count1 =  File(".git").listFiles().count()
    dependsOn(tasks.shadowJar)
    val argvString = project.findProperty("argv") as String? ?: ""
    val jarFile = tasks.shadowJar.get().outputs.files.singleFile
    val evalArgs = listOf("java", "-jar", jarFile.absolutePath, count1.toString()) + argvString.split(" ")
    commandLine(*evalArgs.toTypedArray())
}
