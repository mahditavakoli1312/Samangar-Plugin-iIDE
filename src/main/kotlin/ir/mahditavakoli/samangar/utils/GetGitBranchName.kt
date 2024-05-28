package ir.mahditavakoli.samangar.utils

import com.intellij.util.io.awaitExit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

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
suspend fun getHeadBranchName() : String {
    return executeCommand(listOf("git", "symbolic-ref", "--short", "HEAD")).message
}

suspend fun executeCommand(commandArgs: List<String>): ProcessResult = withContext(
    Dispatchers.IO
) {
    runCatching {
        val process = ProcessBuilder(commandArgs).start()
        val outputStream = async {
            println("Context for output stream -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
            readStream(process.inputStream) }
        val errorStream = async {
            println("Context for error stream -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
            readStream(process.errorStream)
        }
        println("Context for exit code -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
        val exitCode =  process.awaitExit()
        ProcessResult(
            exitCode = exitCode,
            message = outputStream.await(),
            errorMessage = errorStream.await()
        )
    }.onFailure{
        ProcessResult(
            exitCode = -1,
            message = "Error retrieving branch name",
            errorMessage = it.localizedMessage
        )
    }.getOrThrow()
}

private fun readStream(inputStream: InputStream) =
    BufferedReader(InputStreamReader(inputStream)).use { reader ->
        println("reader :"+reader.readText()    )
        reader.readText() }

data class ProcessResult(val exitCode: Int, val message: String, val errorMessage: String)
