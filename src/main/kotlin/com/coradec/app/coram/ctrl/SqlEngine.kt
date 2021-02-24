package com.coradec.app.coram.ctrl

import com.coradec.app.coram.ctrl.impl.BasicSqlEngine
import com.coradec.app.coram.model.Account
import com.vaadin.flow.data.provider.Query
import java.util.function.Supplier
import java.util.stream.Stream
import kotlin.reflect.KClass

interface SqlEngine<T: Any> {
    val tableName: String

    /** Counts the rows in the table limited by the specified query. */
    fun count(query: Query<T, SqlFilter>): Int
    /** Queries the data in the table limited by the specified query. */
    fun query(query: Query<T, SqlFilter>): Stream<T>
    /** Asserts that the table exists. */
    fun assertTable()
    /** Inserts the specified record to the table; returns the number of affected rows (should be 1). */
    fun insert(entry: T): Int
    /** Deletes the records selected by the specified selection; returns the number of affected rows. */
    fun delete(selection: Selection): Int

    companion object {
        operator fun <T: Any> invoke(klass: KClass<out T>): SqlEngine<T> = BasicSqlEngine(klass)
    }
}
