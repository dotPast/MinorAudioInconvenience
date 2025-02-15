@file:Suppress("UnstableApiUsage")


plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

val loader = prop("loom.platform")!!
val minecraft: String = stonecutter.current.version
val common: Project = requireNotNull(stonecutter.node.sibling("")) {
    "No common project for $project"
}

version = "${mod.version}+$minecraft"
base {
    archivesName.set("${mod.id}-$loader")
}
architectury {
    platformSetupLoomIde()
    fabric()
}

val commonBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

configurations {
    compileClasspath.get().extendsFrom(commonBundle)
    runtimeClasspath.get().extendsFrom(commonBundle)
    get("developmentFabric").extendsFrom(commonBundle)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    mappings("net.fabricmc:yarn:$minecraft+build.${common.mod.dep("yarn_build")}:v2")

    val clothConfigVersion = when (stonecutter.active.version) {
        "1.19" -> "8.3.134"
        "1.19.3" -> "9.1.104"
        "1.19.4" -> "10.1.135"
        "1.20.1" -> "11.1.136"
        "1.20.2" -> "12.0.137"
        "1.20.3" -> "13.0.138"
        "1.20.6" -> "14.0.139"
        "1.21.1" -> "15.0.140"
        else -> "17.0.144" // 1.21.2+
    }

    val modMenuVersion = when (stonecutter.active.version) {
        "1.19" -> "4.0.4"
        "1.19.3" -> "5.0.0"
        "1.19.4" -> "6.3.1"
        "1.20.1" -> "7.2.2"
        "1.20.2" -> "8.0.1"
        "1.20.3" -> "9.0.0"
        "1.20.6" -> "9.2.0"
        "1.21.1" -> "11.0.3"
        "1.21.2" -> "12.0.0"
        else -> "13.0.1" // 1.21.4
    }

    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion") {
        exclude("net.fabricmc.fabric-api")
        exclude("net.fabricmc.fabric-loader")
    }

    modApi("com.terraformersmc:modmenu:$modMenuVersion")

    commonBundle(project(common.path, "namedElements")) { isTransitive = false }
    shadowBundle(project(common.path, "transformProductionFabric")) { isTransitive = false }
}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    runConfigs.all {
        isIdeConfigGenerated = true
        runDir = "../../../run"
        vmArgs("-Dmixin.debug.export=true")
    }
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraft, ">=1.20.5"))
        JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
}

tasks.remapJar {
    injectAccessWidener = true
    input = tasks.shadowJar.get().archiveFile
    archiveClassifier = null
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier = "dev"
}

tasks.processResources {
    properties(listOf("fabric.mod.json"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to common.mod.prop("mc_dep_fabric")
    )
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}

tasks.register<Copy>("buildAndCollect") {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}