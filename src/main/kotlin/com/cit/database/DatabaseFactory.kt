package com.cit.database

import com.cit.database.tables.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory{
    fun initDataBase(){
        val driverClassName = "org.postgresql.Driver"
        val url = "jdbc:postgresql://localhost:5432/UsersForTestAuth"
        val user = "postgres"
        val password = "QwE123asd"
        val database = Database.connect(url, driverClassName, user, password)

        createTablesIfNotExist(database)
    }

    private fun createTablesIfNotExist(database: Database){
        transaction(database){
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> pushQuery(block: suspend () -> T): T{
        return newSuspendedTransaction(Dispatchers.IO) { block() }
    }
}