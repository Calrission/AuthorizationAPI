package com.cit.plugins

import com.cit.*
import com.cit.database.controllers.UserController
import com.cit.database.tables.LoginBody
import com.cit.database.tables.SignUpBody
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    val userController = UserController()

    routing {
        post("signIn"){
            try{
                val body = call.receiveAndValidate(LoginBody::class)
                if (body != null) {
                    userController.signIn(body, call)
                }
            }catch (e: Exception){
                call.respondError(e)
            }
        }

        post("signUp"){
            try{
                val body = call.receiveAndValidate(SignUpBody::class)
                if (body != null) {
                    userController.signUp(body, call)
                }
            }catch (e: Exception){
                call.respondError(e)
            }
        }

        get("getUser"){
            try{
                val id = call.request.queryParameters["id"]!!.toInt()
                userController.getUser(id)
            }catch (e: NullPointerException){
                call.respondNullExceptionErrorQueryParameters(e, listOf("id"))
            }catch (e: Exception){
                call.respondError(e)
            }
        }

        get("token/getUser"){
            try{
                val token = call.request.queryParameters["token"]!!.toString()
                userController.getUser(token)
            }catch (e: NullPointerException){
                call.respondNullExceptionErrorQueryParameters(e, listOf("token"))
            }catch (e: Exception){
                call.respondError(e)
            }
        }
    }
}
