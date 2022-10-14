package com.cit.plugins

import com.cit.*
import com.cit.database.controllers.UserController
import com.cit.database.tables.LoginBody
import com.cit.database.tables.SignUpBody
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {

    val userController = UserController()

    routing {
        post("signIn"){
            try{
                val body = call.receiveAndValidate(LoginBody::class)
                if (body != null) {
                    val result = userController.signIn(body)
                    call.respondAnswer(result)
                }
            }catch (e: Exception){
                call.respondExceptionError(e)
            }
        }

        post("signUp"){
            try{
                val body = call.receiveAndValidate(SignUpBody::class)
                if (body != null) {
                    val result = userController.signUp(body)
                    call.respondAnswer(result)
                }
            }catch (e: Exception){
                call.respondExceptionError(e)
            }
        }

        get("getUser"){
            try{
                val id = call.request.queryParameters["id"]!!.toInt()
                val result = userController.getUser(id)
                call.respondAnswer(result)
            }catch (e: NullPointerException){
                call.respondNullExceptionErrorQueryParameters(e, listOf("id"))
            }catch (e: Exception){
                call.respondExceptionError(e)
            }
        }

        get("token/getUser"){
            try{
                val token = call.request.queryParameters["token"]!!.toString()
                val result = userController.getUser(token)
                call.respondAnswer(result)
            }catch (e: NullPointerException){
                call.respondNullExceptionErrorQueryParameters(e, listOf("token"))
            }catch (e: Exception){
                call.respondExceptionError(e)
            }
        }
    }
}
