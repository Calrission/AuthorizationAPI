package com.cit.database.tables

import com.cit.common.CodeTitle
import com.cit.common.Validation
import com.cit.common.validateEmail
import com.cit.common.validatePassword
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
@Serializable
data class LoginBody(
    val email: String,
    val password: String
): Validation{
    override fun validate(): Pair<Boolean, String?> {
        if (!email.validateEmail())
            return Pair(false, CodeTitle.EMAIL_NOT_CORRECT.toString())
        if (!password.validatePassword())
            return Pair(false, CodeTitle.PASSWORD_8.toString())
        return Pair(true, null)
    }
}
@Serializable
data class SignUpBody(
    val login: String,
    val password: String,
    val email: String
): Validation{

    override fun validate(): Pair<Boolean, String?> {
        if (!email.validateEmail())
            return Pair(false, CodeTitle.EMAIL_NOT_CORRECT.toString())
        if (!password.validatePassword())
            return Pair(false, CodeTitle.PASSWORD_8.toString())
        if (login.isEmpty())
            return Pair(false, CodeTitle.LOGIN_EMPTY.toString())
        return Pair(true, null)
    }

    fun toWithoutIdUserRow(): WithoutIdUserRow{
        return WithoutIdUserRow(login, password, email, "")
    }
}
@Serializable
data class WithoutIdUserRow(
    val login: String,
    val password: String,
    val email: String,
    val token: String
)

data class UserRow(
    val id: Int,
    val login: String,
    val email: String,
    val password: String,
    val token: String
){
    fun toSafe(): UserSafe = UserSafe(id, login)
}
@Serializable
data class Token(
    val token: String
)
@Serializable
data class UserSafe(
    val id: Int,
    val login: String
)

object Users: Table() {
    val id = integer("id").autoIncrement()
    val login = varchar("login", 1000)
    val email = varchar("email", 100)
    val password = varchar("password", 1000)
    val token = varchar("token", 120)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}