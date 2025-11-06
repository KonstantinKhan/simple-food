plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

group = "com.khan366kos"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
}

val openApiOutputDir = layout.buildDirectory.dir("generated/openapi")

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("${rootDir}/specs/spec-simple-food-api.yaml")
    outputDir.set(openApiOutputDir.get().asFile.absolutePath)
    packageName.set("com.khan366kos.transport")
    apiPackage.set("com.khan366kos.transport.api")
    modelPackage.set("com.khan366kos.transport.model")

    // Генерируем только модели
    globalProperties.set(
        mapOf(
            "models" to "",
            "modelDocs" to "false",
            "apis" to "false",
            "apiDocs" to "false",
            "supportingFiles" to "false"
        )
    )

    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "serializationLibrary" to "jackson",
            "enumPropertyNaming" to "original"
        )
    )
}

tasks.named("openApiGenerate").configure {
    outputs.upToDateWhen { false }
    doFirst {
        delete(openApiOutputDir.get().asFile)
    }
}

sourceSets {
    main {
        java.srcDir(openApiOutputDir.map { it.dir("src/main/kotlin") })
        resources.srcDir(openApiOutputDir.map { it.dir("src/main/resources") })
    }
}

tasks.named("compileKotlin").configure {
    dependsOn(tasks.named("openApiGenerate"))
}

tasks.named("processResources").configure {
    dependsOn(tasks.named("openApiGenerate"))
}


