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
        val host = "95.31.130.149"
        val port = "5432"
        val password = "Aa!135"
        val databaseName = "postgres"
        val user = "lyubimov"

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