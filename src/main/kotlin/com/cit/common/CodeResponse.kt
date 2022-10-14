package com.cit.common

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
class CodeResponse {
    val code: Int
    val description: String

    constructor(code: Int): this(code, "")
    constructor(httpStatusCode: HttpStatusCode, description: String): this(httpStatusCode.value, description)
    constructor(httpStatusCode: HttpStatusCode, codeTitle: CodeTitle): this(httpStatusCode.value, codeTitle)
    constructor(code: Int, codeTitle: CodeTitle): this(code, codeTitle.toString())
    constructor(httpStatusCode: HttpStatusCode): this(httpStatusCode.value, httpStatusCode.description)
    constructor(code: Int, description: String){
        this.description = description
        this.code = code
    }

    fun convertToHttpsStatusCode(): HttpStatusCode{
        return HttpStatusCode(code, description)
    }

    fun <T> toModelAnswer(body: T?=null): ModelAnswer<T> {
        return ModelAnswer(this, body)
    }

    companion object {
        fun HttpStatusCode.toCodeResponse(): CodeResponse {
            return CodeResponse(this)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is HttpStatusCode) return code == other.value
        return super.equals(other)
    }
    /*
    Было созданно автоматически IDEей
     */
    override fun hashCode(): Int {
        var result = code
        result = 31 * result + description.hashCode()
        return result
    }
}

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

enum class CallbackCodeResponse(val codeResponse: CodeResponse){

    NEW_TOKEN_NOT_INSERT(CodeResponse(1000, CodeTitle.NEW_TOKEN_NOT_INSERT)),
    TOKEN_NOT_CORRECT(CodeResponse(HttpStatusCode.BadRequest, CodeTitle.TOKEN_NOT_CORRECT)),
    USER_NOT_CREATED(CodeResponse(1200, CodeTitle.USER_NOT_CREATED)),
    USER_NOT_FOUND(CodeResponse(HttpStatusCode.NotFound, CodeTitle.USER_NOT_FOUND)),
    LOGIN_ALREADY_EXIST(CodeResponse(HttpStatusCode.NotAcceptable, CodeTitle.LOGIN_ALREADY_EXIST)),
    PASSWORD_NOT_CORRECT_8(CodeResponse(HttpStatusCode.BadRequest, CodeTitle.PASSWORD_8)),
    EMAIL_NOT_CORRECT(CodeResponse(HttpStatusCode.BadRequest, CodeTitle.EMAIL_NOT_CORRECT)),
    EMAIL_ALREADY_USE(CodeResponse(HttpStatusCode.BadRequest, CodeTitle.EMAIL_ALREADY_USE)),
}
