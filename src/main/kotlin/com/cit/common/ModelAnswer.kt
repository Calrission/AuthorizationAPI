package com.cit.common

import com.cit.common.CodeResponse.Companion.toCodeResponse
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
class ModelAnswer <T>{

    val codeResponse: CodeResponse
    val body: T?

    constructor(httpStatusCode: HttpStatusCode, body: T?=null): this(httpStatusCode.toCodeResponse(), body)
    constructor(body: T): this(HttpStatusCode.OK, body)
    constructor(callbackCodeResponse: CallbackCodeResponse, body: T?=null): this(callbackCodeResponse.codeResponse, body)
    constructor(codeResponse: CodeResponse, body: T?=null){
        this.codeResponse = codeResponse
        this.body = body
    }

    fun <newT> convertToOtherBodyClass(body: newT?=null): ModelAnswer<newT> {
        return ModelAnswer(codeResponse, body)
    }

}