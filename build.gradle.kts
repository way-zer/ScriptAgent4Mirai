plugins {
    kotlin("jvm") version "1.3.70"
    id("me.qoomon.git-versioning") version "2.1.1"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "cf.wayzer"
version = "v1.x.x" //采用3位版本号v1.2.3 1为大版本 2为插件版本 3为脚本版本
val libraryVersion = "1.1.7"
val miraiVersion = "1.0-RC2-1"
val miraiConsoleVersion = "0.5.1"

gitVersioning.apply(closureOf<me.qoomon.gradle.gitversioning.GitVersioningPluginConfig> {
    tag(closureOf<me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.VersionDescription> {
        pattern = "v(?<tagVersion>[0-9].*)"
        versionFormat = "\${tagVersion}"
    })
    commit(closureOf<me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.CommitVersionDescription> {
        versionFormat = "\${version}-\${commit.short}\${dirty}"
    })
})

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven(url = "https://www.jitpack.io")
    maven("https://dl.bintray.com/way-zer/maven")
}
sourceSets {
    main {
        java.srcDir("src")
    }
    create("plugin") {
        this.compileClasspath += main.get().compileClasspath
        this.runtimeClasspath += main.get().runtimeClasspath
        java.srcDir("plugin/src")
        resources.srcDir("plugin/res")
    }
}
dependencies {
    api("cf.wayzer:ScriptAgent:$libraryVersion")
    implementation(kotlin("script-runtime"))
    implementation(kotlin("stdlib-jdk8"))

    //coreLibrary
    api("cf.wayzer:PlaceHoldLib:2.1.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    implementation("io.github.config4k:config4k:0.4.1")
    //mirai
    implementation("net.mamoe:mirai-core:$miraiVersion")
    implementation("net.mamoe:mirai-console:$miraiConsoleVersion")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<ProcessResources> {
        inputs.property("version", rootProject.version)
        filter(
                filterType = org.apache.tools.ant.filters.ReplaceTokens::class,
                properties = mapOf("tokens" to mapOf("version" to rootProject.version))
        )
    }
    create<Zip>("scriptsZip") {
        group = "plugin"
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("scripts")
    }
    create<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("buildPlugin") {
        dependsOn("scriptsZip")
        group = "plugin"
        from(sourceSets.getByName("plugin").output)
        archiveClassifier.set("")
        configurations = listOf(project.configurations.getByName("compileClasspath"))
        dependencies {
            include(dependency("cf.wayzer:ScriptAgent:$libraryVersion"))
            include(dependency("cf.wayzer:LibraryManager"))
        }
    }
}