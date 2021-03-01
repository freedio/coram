package com.coradec.app.coram.ctrl

import com.coradec.coradeck.db.ctrl.Selection
import com.coradec.coradeck.db.ctrl.SqlEngine
import com.coradec.coradeck.db.util.toSqlObjectName
import com.vaadin.flow.data.provider.*
import com.vaadin.flow.shared.Registration
import java.util.stream.Stream
import kotlin.reflect.KClass

open class Records<T: Any>(klass: KClass<out T>) : AbstractBackEndDataProvider<T, SqlFilter>() {
    protected val sql: SqlEngine<T> = SqlEngine(klass)
    private val tableName: String = sql.tableName
    private val listeners = arrayListOf<DataProviderListenerRegistration>()
    private var sorting: List<QuerySortOrder>? = listOf()

    inner class DataProviderListenerRegistration(val listener: DataProviderListener<T>): Registration {
        override fun remove() {
            listeners.remove(this)
        }
    }

    override fun fetch(query: Query<T, SqlFilter>): Stream<T> = fetchFromBackEnd(query)
    override fun fetchFromBackEnd(query: Query<T, SqlFilter>): Stream<T> = sql.query(query.from)
    override fun size(query: Query<T, SqlFilter>): Int = sizeInBackEnd(query)
    override fun sizeInBackEnd(query: Query<T, SqlFilter>): Int = sql.count(query.from)

    override fun addDataProviderListener(listener: DataProviderListener<T>): Registration =
        DataProviderListenerRegistration(listener).also { listeners += it }

    override fun setSortOrders(sortOrders: MutableList<QuerySortOrder>) {
        sorting = sortOrders.toList()
    }

    private val <T, F> Query<T, F>.from: Selection get() = QuerySelection(this)

    class QuerySelection<T1, F>(query: Query<T1, F>) : Selection {
        override val order: String = query.sortOrders.let { if (it.isEmpty()) "" else it.joinToString(" ", " order by ") { "${it.sorted.toSqlObjectName()} ${it.direction.sql}" } }
        override val slice: String =
            "${query.offset.let { if (it == 0) "" else "offset $it" }} ${query.limit.let { if (it == 2147483647) "" else "limit $it" }}"
        override val where: String = ""

        private val SortDirection.sql: String get() = when (this) {
            SortDirection.ASCENDING -> "asc"
            SortDirection.DESCENDING -> "desc"
        }
    }
}
