package com.cit.database.tables

import com.cit.database.DAOTable
import com.cit.database.DatabaseFactory.pushQuery
import org.jetbrains.exposed.sql.*
import java.util.UUID

class DAOUsers: DAOTable<UserRow, WithoutIdUserRow> {
    override fun resultRowToModel(row: ResultRow): UserRow {
        return UserRow(
            id = row[Users.id],
            login = row[Users.login],
            email = row[Users.email],
            password = row[Users.password],
            token = row[Users.token]
        )
    }

    override suspend fun selectAll(): List<UserRow> {
        return pushQuery{
            Users.selectAll().map(::resultRowToModel)
        }
    }

    override suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): UserRow? {
        return pushQuery{
            Users.select(where).map(::resultRowToModel).singleOrNull()
        }
    }

    override suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<UserRow> {
        return pushQuery {
            Users.select(where).map(::resultRowToModel)
        }
    }

    override suspend fun delete(where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Users.deleteWhere (op=where) > 0
        }
    }

    override suspend fun insert(model: WithoutIdUserRow): UserRow? {
        return pushQuery {
            Users.insert {
                it[login] = model.login
                it[password] = model.password
                it[email] = model.email
                it[token] = if (model.token != "") model.token  else UUID.randomUUID().toString()
            }.resultedValues?.singleOrNull()?.let(::resultRowToModel)
        }
    }

    override suspend fun edit(model: WithoutIdUserRow, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return pushQuery {
            Users.update(where){
                it[login] = model.login
                it[email] = model.email
                it[password] = model.password
                it[token] = model.token
            } > 0
        }
    }

    suspend fun generateNewUUID(id: Int): UserRow?{
        pushQuery {
            Users.update(where = {
                Users.id eq id
            }){
                it[token] = UUID.randomUUID().toString()
            }
        }
        return selectSingle { Users.id eq id }
    }

}