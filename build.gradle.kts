import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsRootPlugin

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

fun resolveNodeCommand(): String {
    System.getenv("NODE_BINARY")
        ?.takeIf { it.isNotBlank() }
        ?.let { return it }

    val nvmDir = System.getenv("NVM_DIR")
        ?.takeIf { it.isNotBlank() }
        ?.let(::file)
        ?: file("${System.getProperty("user.home")}/.nvm")
    val pinnedVersion = rootDir.resolve(".nvmrc")
        .takeIf { it.isFile }
        ?.readText()
        ?.trim()
        ?.takeIf { it.isNotBlank() }
    if (pinnedVersion != null) {
        val pinnedNode = nvmDir.resolve("versions/node/$pinnedVersion/bin/node")
        if (pinnedNode.canExecute()) {
            return pinnedNode.absolutePath
        }
    }

    System.getenv("NVM_BIN")
        ?.takeIf { it.isNotBlank() }
        ?.let { file("$it/node") }
        ?.takeIf { it.canExecute() }
        ?.let { return it.absolutePath }

    System.getenv("NODE_HOME")
        ?.takeIf { it.isNotBlank() }
        ?.let { file("$it/bin/node") }
        ?.takeIf { it.canExecute() }
        ?.let { return it.absolutePath }

    return "node"
}

val resolvedNodeCommand = resolveNodeCommand()

allprojects {
    plugins.withType<NodeJsPlugin> {
        the<NodeJsEnvSpec>().download.set(false)
        the<NodeJsEnvSpec>().command.set(resolvedNodeCommand)
    }

    plugins.withType<WasmNodeJsPlugin> {
        the<WasmNodeJsEnvSpec>().download.set(false)
        the<WasmNodeJsEnvSpec>().command.set(resolvedNodeCommand)
    }
}

rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.the<NodeJsEnvSpec>().download.set(false)
    rootProject.the<NodeJsEnvSpec>().command.set(resolvedNodeCommand)
}

rootProject.plugins.withType<WasmNodeJsRootPlugin> {
    rootProject.the<WasmNodeJsEnvSpec>().download.set(false)
    rootProject.the<WasmNodeJsEnvSpec>().command.set(resolvedNodeCommand)
}
