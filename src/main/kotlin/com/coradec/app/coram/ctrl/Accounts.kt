package com.coradec.app.coram.ctrl

import com.coradec.app.coram.model.Account
import com.coradec.app.coram.model.impl.BasicAccount
import com.coradec.coradeck.db.ctrl.impl.SqlSelection
import com.vaadin.flow.data.provider.CallbackDataProvider

object Accounts : Records<BasicAccount>(BasicAccount::class) {
    fun clear() = sql.delete(SqlSelection.ALL)
    fun add(account: BasicAccount) = sql.insert(account)
    fun open() = sql.assertTable()

    val query: CallbackDataProvider.FetchCallback<BasicAccount, SqlFilter> = CallbackDataProvider.FetchCallback { fetch(it) }
    val count: CallbackDataProvider.CountCallback<BasicAccount, SqlFilter> = CallbackDataProvider.CountCallback { size(it) }
}