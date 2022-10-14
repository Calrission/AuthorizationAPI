package com.cit.common

fun String.validateEmail(): Boolean{
    return ("@" in this) && (count { it == '.' } == 1)
}

fun String.validatePassword(): Boolean{
    return this.length >= 8
}

fun String.validateDate(pattern: String): Boolean{
    return try {
        toPatternDate(pattern)
        true
    }catch (e: Exception){
        false
    }
}

fun String.validateTime(pattern: String): Boolean{
    return try {
        toPatternTime(pattern)
        true
    }catch (e: Exception){
        false
    }
}

interface Validation{
    fun validate(): Pair<Boolean, String?>
}