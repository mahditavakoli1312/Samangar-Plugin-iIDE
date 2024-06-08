package ir.mahditavakoli.samangar.utils

import com.github.weisj.jsvg.p
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.projectsDataDir
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.remoteDev.util.UrlParameterKeys.Companion.projectPath
import com.intellij.util.io.awaitExit
import com.intellij.util.io.readLineAsync
import ir.mahditavakoli.samangar.MyToolWindow
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.coroutines.suspendCoroutine

// way 1
fun getGitBranchName(): String {
    return try {
        val process = ProcessBuilder("git", "symbolic-ref", "--short", "HEAD").start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val errorReader = BufferedReader(InputStreamReader(process.errorStream))

        // Read the output from the process
        val branchName = reader.readLine()
        // Check for any errors
        val error = errorReader.readLine()
        if (error != null) {
            println("Error occurred: $error")
        }

        process.waitFor()
        branchName
    } catch (e: Exception) {
        e.printStackTrace()
        "Error retrieving branch name"
    }
}

// way 2
suspend fun getHeadBranchName(project: Project): ProcessResult {
    return executeCommand(project, listOf("git", "symbolic-ref", "--short", "HEAD"))
}

suspend fun executeCommand(project: Project, commandArgs: List<String>): ProcessResult = withContext(
    Dispatchers.IO
) {
    return@withContext coroutineScope {

        val path = ModuleRootManager.getInstance(ModuleManager.getInstance(project).modules[0])
            .sourceRoots[0].path

        val process = ProcessBuilder(commandArgs)
            .directory(File(path))
            .start()


        println("project path is : ${path}")

        val sb = StringBuilder()
        val job1 = launch {
            val reader = process.inputStream.bufferedReader()
            var line = reader.readLineAsync()
            while (line != null) {
                sb.append(line).append("\n")
                line = reader.readLineAsync()
            }
        }

        val sbError = StringBuilder()
        val jobError = launch {
            val reader = process.errorStream.bufferedReader()
            var line = reader.readLineAsync()
            while (line != null) {
                sb.append(line).append("\n")
                line = reader.readLineAsync()
            }
        }

        val exitCode = process.awaitExit()
        job1.join()
        jobError.join()
        println("------------------------------")
        println(exitCode)
        println(sb.toString())
        println("------------------------------")

        ProcessResult(
            exitCode = exitCode,
            message = sb.toString(),
            errorMessage = sbError.toString()
        )
    }
//    return@withContext runCatching {
//
//
//        val outputStream = async {
//            println("Context for output stream -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
//            readStream(process.inputStream)
//        }
//        val errorStream = async {
//            println("Context for error stream -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
//            readStream(process.errorStream)
//        }
//        println("Context for exit code -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
//        val exitCode = process.awaitExit()
//        return@runCatching ProcessResult(
//            exitCode = exitCode,
//            message = outputStream.await(),
//            errorMessage = errorStream.await()
//        )
//    }.onFailure {
//        return@withContext ProcessResult(
//            exitCode = -1,
//            message = "Error retrieving branch name",
//            errorMessage = it.localizedMessage
//        )
//    }.getOrThrow()
}

private fun readStream(inputStream: InputStream) =
    BufferedReader(InputStreamReader(inputStream)).use { reader ->
        println("reader :" + reader.readText())
        reader.readText()
    }

data class ProcessResult(val exitCode: Int, val message: String, val errorMessage: String)
