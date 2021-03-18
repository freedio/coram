package com.coradec.app.coram.ctrl

import com.coradec.app.coram.model.impl.BasicInvoice
import com.coradec.coradeck.db.ctrl.impl.SqlSelection
import com.vaadin.flow.data.provider.CallbackDataProvider

object Invoices : Records<BasicInvoice>(BasicInvoice::class) {
    fun clear() = sql.delete(SqlSelection.ALL)
    fun add(invoice: BasicInvoice) = sql.insert(invoice)
    fun open() = sql.assertTable()

    val query: CallbackDataProvider.FetchCallback<BasicInvoice, SqlFilter> = CallbackDataProvider.FetchCallback { fetch(it) }
    val count: CallbackDataProvider.CountCallback<BasicInvoice, SqlFilter> = CallbackDataProvider.CountCallback { size(it) }
}