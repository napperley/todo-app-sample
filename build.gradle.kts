import org.gradle.api.tasks.Copy
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

//import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "org.webscene"
version = "0.1-SNAPSHOT"

buildscript {
    var kotlinVer: String by extra

    kotlinVer = "1.2.0"

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin(module = "gradle-plugin", version = kotlinVer))
    }
}

apply {
    plugin("kotlin2js")
    plugin("kotlin-dce-js")
}

val kotlinVer: String by extra

repositories {
    mavenCentral()
    maven {
        url = uri("libs")
    }
}

dependencies {
    val websceneClientVer = "0.1-SNAPSHOT"

    "compile"(kotlin(module = "stdlib-js", version = kotlinVer))
    "compile"("org.webscene:webscene-client:$websceneClientVer")
}

val compileKotlin2Js by tasks.getting(Kotlin2JsCompile::class) {
    val fileName = "todo-app.js"

    kotlinOptions {
        outputFile = "${projectDir.absolutePath}/web/js/$fileName"
        sourceMap = true
        moduleKind = "umd"
    }
    doFirst { File("${projectDir.absolutePath}/web/js").deleteRecursively() }
}
val build by tasks
val assembleWeb by tasks.creating(Copy::class) {
    dependsOn("classes")
    configurations["compile"].forEach { file ->
        from(zipTree(file.absolutePath)) {
            includeEmptyDirs = false
            include { fileTreeElement ->
                val path = fileTreeElement.path

                path.endsWith(".js") && path.startsWith("META-INF/resources/") || !path.startsWith("META_INF/")
            }
        }
    }
    from(compileKotlin2Js.destinationDir)
    into("${projectDir.absolutePath}/web/js")
}

task<Copy>("deployClient") {
    dependsOn("runDceKotlinJs", assembleWeb)
    from("${projectDir.absolutePath}/src/main/resources")
    into("${projectDir.absolutePath}/web")
}
