package com.coradec.app.coram.ctrl

import com.coradec.app.coram.model.Account
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider
import com.vaadin.flow.data.provider.DataProviderListener
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.shared.Registration
import java.util.stream.Stream
import kotlin.reflect.KClass

open class Records<T: Any>(klass: KClass<out T>) : AbstractBackEndDataProvider<T, SqlFilter>() {
    private val sql: SqlEngine<T> = SqlEngine(klass)
    private val tableName: String = sql.tableName
    private val listeners = arrayListOf<DataProviderListenerRegistration>()
    private var sorting: List<QuerySortOrder>? = listOf()

    inner class DataProviderListenerRegistration(val listener: DataProviderListener<T>): Registration {
        override fun remove() {
            listeners.remove(this)
        }
    }

    override fun fetch(query: Query<T, SqlFilter>): Stream<T> = fetchFromBackEnd(query)
    override fun fetchFromBackEnd(query: Query<T, SqlFilter>): Stream<T> = sql.query(query)
    override fun size(query: Query<T, SqlFilter>): Int = sizeInBackEnd(query)
    override fun sizeInBackEnd(query: Query<T, SqlFilter>): Int = sql.count(query)

    override fun addDataProviderListener(listener: DataProviderListener<T>): Registration =
        DataProviderListenerRegistration(listener).also { listeners += it }

    override fun setSortOrders(sortOrders: MutableList<QuerySortOrder>) {
        sorting = sortOrders.toList()
    }
}
