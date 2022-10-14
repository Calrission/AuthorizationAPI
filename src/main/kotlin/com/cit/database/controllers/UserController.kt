package com.cit.database.controllers

import com.cit.common.CallbackCodeResponse
import com.cit.common.ModelAnswer
import com.cit.database.tables.*
import org.jetbrains.exposed.sql.and

class UserController {
    private val daoUser = DAOUsers()

    suspend fun signIn(body: LoginBody): ModelAnswer<Token>{
        var user = daoUser.selectSingle { (Users.email eq body.email) and (Users.password eq body.password) }
        return if (user == null){
            ModelAnswer(CallbackCodeResponse.USER_NOT_FOUND)
        }else{
            user = daoUser.generateNewUUID(user.id)
            return if (user == null){
                ModelAnswer(CallbackCodeResponse.NEW_TOKEN_NOT_INSERT)
            }else {
                ModelAnswer(Token(user.token))
            }
        }
    }

    suspend fun signUp(body: SignUpBody): ModelAnswer<String?>{
        if (daoUser.checkExist { Users.email eq body.email }){
            return ModelAnswer(CallbackCodeResponse.EMAIL_ALREADY_USE)
        }
        val user = daoUser.insert(body.toWithoutIdUserRow())
        return ModelAnswer(if (user != null) "Успешная регистрация" else null)
    }

    suspend fun getUser(id: Int): ModelAnswer<UserSafe?>{
        val user = daoUser.selectSingle { Users.id eq id }
        return ModelAnswer(user?.toSafe())
    }

    suspend fun getUser(token: String): ModelAnswer<UserSafe?>{
        val user = daoUser.selectSingle { Users.token eq token }
        return ModelAnswer(user?.toSafe())
    }
}