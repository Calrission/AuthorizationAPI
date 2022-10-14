package com.cit.database

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder

interface DAOTable <T, R> {

    fun resultRowToModel(row: ResultRow): T
    suspend fun selectAll(): List<T>
    suspend fun selectSingle(where: SqlExpressionBuilder.() -> Op<Boolean>): T?
    suspend fun selectMany(where: SqlExpressionBuilder.() -> Op<Boolean>): List<T>
    suspend fun edit(model: R, where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean
    suspend fun insert(model: R): T?
    suspend fun delete(where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean
    suspend fun checkExist(where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
        return selectSingle(where) != null
    }
}