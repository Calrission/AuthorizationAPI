package com.cit.database

import com.cit.database.tables.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

var PATH_LOCAL_PROPERTY: String? = null
object DatabaseFactory{
    fun initDataBase(){
        val driverClassName = "org.postgresql.Driver"
        val host = getLocalProperty("host_db")
        val port = getLocalProperty("port_db")
        val password = getLocalProperty("password_db") as String
        val databaseName = getLocalProperty("database")
        val user = getLocalProperty("user_db") as String

        val url = "jdbc:postgresql://$host:$port/$databaseName"
        val database = Database.connect(url, driverClassName, user, password)

        createTablesIfNotExist(database)
    }

    private fun createTablesIfNotExist(database: Database){
        transaction(db=database){
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> pushQuery(block: suspend () -> T): T{
        return newSuspendedTransaction(Dispatchers.IO) { block() }
    }
}

fun getLocalProperty(key: String): Any {
    val properties = java.util.Properties()
    val localProperties = File(PATH_LOCAL_PROPERTY!!)
    if (localProperties.isFile) {
        java.io.InputStreamReader(java.io.FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else error("File from not found")

    return properties.getProperty(key)
}