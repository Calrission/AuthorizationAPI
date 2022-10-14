package com.cit

import com.cit.common.CallbackCodeResponse
import com.cit.common.CodeResponse
import com.cit.common.ModelAnswer
import com.cit.common.Validation
import com.cit.database.DatabaseFactory
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

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
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

suspend fun ApplicationCall.respondExceptionError(e: Exception){
    respondAnswer(ModelAnswer<String>(CodeResponse(500, e.message ?: e.localizedMessage ?: e.toString())))
}

suspend fun ApplicationCall.respondNullExceptionErrorQueryParameters(e: NullPointerException, parameters: List<String>){
    val message = "$e - check this field: $parameters"
    respondAnswer(ModelAnswer<String>(CodeResponse(HttpStatusCode.BadRequest, message)))
}

suspend inline fun <reified T> ApplicationCall.respondAnswer(model: ModelAnswer<T>){
    respond(HttpStatusCode.OK, model)
}

suspend inline fun <reified T> ApplicationCall.respondAnswer(model: T){
    respond(HttpStatusCode.OK, ModelAnswer(model))
}

suspend inline fun ApplicationCall.respondCodeResponse(codeResponse: CodeResponse){
    respondAnswer(ModelAnswer<String>(codeResponse = codeResponse))
}

suspend inline fun ApplicationCall.respondCodeResponse(callbackCodeResponse: CallbackCodeResponse){
    respondCodeResponse(callbackCodeResponse.codeResponse)
}

suspend fun <T : Validation> ApplicationCall.receiveAndValidate(type: KClass<T>): T?{
    try {
        val body = receive(type)
        val validateBody = body.validate()
        if (!validateBody.first) {
            respondCodeResponse(CodeResponse(HttpStatusCode.BadRequest, validateBody.second!!))
            return null
        }
        return body
    }catch (e: java.lang.Exception){
        respondExceptionError(e)
        return null
    }
}