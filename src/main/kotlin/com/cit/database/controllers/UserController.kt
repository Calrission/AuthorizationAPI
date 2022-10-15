package com.cit.database.controllers

import com.cit.common.CallbackCodeResponse
import com.cit.database.tables.*
import com.cit.respondError
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.and

class UserController {
    private val daoUser = DAOUsers()

    suspend fun signIn(body: LoginBody, applicationCall: ApplicationCall){
        var user = daoUser.selectSingle { (Users.email eq body.email) and (Users.password eq body.password) }
        return if (user == null){
            applicationCall.respond(Token(error=CallbackCodeResponse.USER_NOT_FOUND.message))
        }else{
            user = daoUser.generateNewUUID(user.id)
            if (user == null){
                applicationCall.respond(Token(error=CallbackCodeResponse.NEW_TOKEN_NOT_INSERT.message))
            }else {
                applicationCall.respond(Token(user.token))
            }
        }
    }

    suspend fun signUp(body: SignUpBody, applicationCall: ApplicationCall){
        if (daoUser.checkExist { Users.email eq body.email }){
            applicationCall.respondError(CallbackCodeResponse.EMAIL_ALREADY_USE)
        }
        val user = daoUser.insert(body.toWithoutIdUserRow())
        if (user != null)
            applicationCall.respondText("Успешная регистрация")
        else
            applicationCall.respondError(CallbackCodeResponse.USER_NOT_CREATED)
    }

    suspend fun getUser(id: Int): UserSafe?{
        val user = daoUser.selectSingle { Users.id eq id }
        return user?.toSafe()
    }

    suspend fun getUser(token: String): UserSafe?{
        val user = daoUser.selectSingle { Users.token eq token }
        return user?.toSafe()
    }
}