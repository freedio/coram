package com.coradec.app.coram.ctrl

import com.coradec.app.coram.model.Account
import com.coradec.app.coram.model.impl.BasicAccount
import com.vaadin.flow.data.provider.CallbackDataProvider

object Accounts : Records<BasicAccount>(BasicAccount::class) {
    fun add(id: Int, name: String) = sql.insert(BasicAccount(id, name))
    fun open() = sql.assertTable()

    val query: CallbackDataProvider.FetchCallback<BasicAccount, SqlFilter> = CallbackDataProvider.FetchCallback { fetch(it) }
    val count: CallbackDataProvider.CountCallback<BasicAccount, SqlFilter> = CallbackDataProvider.CountCallback { size(it) }
}