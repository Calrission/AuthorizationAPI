package com.cit.common

import io.ktor.http.*
import kotlinx.serialization.Serializable

enum class CodeTitle(private val title: String){
    USER_NOT_FOUND("User not found"),
    PASSWORD_8("Password not correct (min 8 length)"),
    LOGIN_ALREADY_EXIST("This login already exist"),
    LOGIN_EMPTY("Login empty"),
    USER_NOT_CREATED("User not created"),
    NEW_TOKEN_NOT_INSERT("New token not created"),
    TOKEN_NOT_CORRECT("Token not correct"),
    EMAIL_NOT_CORRECT("Email not correct"),
    EMAIL_ALREADY_USE("Email already use");

    override fun toString(): String {
        return title
    }
}

enum class CallbackCodeResponse(val code: Int, val message: String){

    NEW_TOKEN_NOT_INSERT(1000, CodeTitle.NEW_TOKEN_NOT_INSERT.toString()),
    TOKEN_NOT_CORRECT(HttpStatusCode.BadRequest.value, CodeTitle.TOKEN_NOT_CORRECT.toString()),
    USER_NOT_CREATED(1200, CodeTitle.USER_NOT_CREATED.toString()),
    USER_NOT_FOUND(HttpStatusCode.NotFound.value, CodeTitle.USER_NOT_FOUND.toString()),
    LOGIN_ALREADY_EXIST(HttpStatusCode.NotAcceptable.value, CodeTitle.LOGIN_ALREADY_EXIST.toString()),
    PASSWORD_NOT_CORRECT_8(HttpStatusCode.BadRequest.value, CodeTitle.PASSWORD_8.toString()),
    EMAIL_NOT_CORRECT(HttpStatusCode.BadRequest.value, CodeTitle.EMAIL_NOT_CORRECT.toString()),
    EMAIL_ALREADY_USE(HttpStatusCode.BadRequest.value, CodeTitle.EMAIL_ALREADY_USE.toString()),
}
