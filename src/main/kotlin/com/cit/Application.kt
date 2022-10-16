package com.cit

import com.cit.common.CallbackCodeResponse
import com.cit.common.Validation
import com.cit.database.DatabaseFactory
import com.cit.database.PATH_LOCAL_PROPERTY
import com.cit.database.getLocalProperty
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.cit.plugins.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import org.slf4j.event.Level
import kotlin.reflect.KClass

fun main(args: Array<String>) {

    PATH_LOCAL_PROPERTY = if (args.isNotEmpty()) args[0] else "local.properties"

    embeddedServer(Netty, port = (getLocalProperty("port") as String).toInt(), host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        install(DoubleReceive)
        install(CallLogging){
            level = Level.INFO
            format {
                it.url()
            }
        }
        DatabaseFactory.initDataBase()
        configureRouting()
    }.start(wait = true)
}

suspend fun ApplicationCall.respondError(message: String){
    respondError(200, message)
}

suspend fun ApplicationCall.respondError(code: Int, message: String){
    respond(HttpStatusCode(code, "Error"), message)
}

suspend fun ApplicationCall.respondError(e: Exception){
    respondError(e.localizedMessage ?: e.message ?: "Unknown error")
}

suspend fun ApplicationCall.respondError(callbackCodeResponse: CallbackCodeResponse){
    respondError(callbackCodeResponse.message)
}

suspend fun <T : Validation> ApplicationCall.receiveAndValidate(type: KClass<T>): T?{
    try {
        val body = receive(type)
        val validateBody = body.validate()
        if (!validateBody.first) {
            respondError(validateBody.second!!)
            return null
        }
        return body
    }catch (e: java.lang.Exception){
        respondError(e.message!!)
        return null
    }
}

suspend fun ApplicationCall.respondNullExceptionErrorQueryParameters(e: Exception, params: List<String>) {
    respondError("${e.message ?: "Unknown"} - check ${params.joinToString(" , "){ it }}")
}


