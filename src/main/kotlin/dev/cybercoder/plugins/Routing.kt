package dev.cybercoder.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit

fun Application.configureRouting(args: Array<String>) {
    routing {
        get("/") {
            val response = if (args.isEmpty())
                getGitLog(".")
            else getGitLog(args[0])

            if (response.isFailure) {
                val message = response.exceptionOrNull()!!.message ?: "Unexpected error occurred"
                call.respond(HttpStatusCode.BadRequest, message)
            } else
                call.respond(HttpStatusCode.OK, response.getOrNull()!!)
        }
    }
}

fun getGitLog(path: String): Result<String> {
    val filePath = File(path)
    if (!filePath.exists()) {
        return Result.failure(Exception("$path does not exist!"))
    }
    if (!filePath.isDirectory) {
        return Result.failure(Exception("$path is not a directory!"))
    }
    if (!File("$path/.git").exists()) {
        return Result.failure(Exception("$path is not a git repository!"))
    }

    val process = ProcessBuilder("git", "log")
        .directory(filePath)
        .start()

    val sb = StringBuilder("")
    InputStreamReader(process.inputStream).use {
        Scanner(it).use { scanner ->
            while (scanner.hasNextLine()) {
                sb.appendLine(scanner.nextLine())
            }
        }
    }

    process.waitFor(5L, TimeUnit.SECONDS)
    return Result.success(sb.toString())
}
