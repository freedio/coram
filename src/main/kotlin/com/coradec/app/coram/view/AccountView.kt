package com.coradec.app.coram.view

import com.coradec.app.coram.ctrl.Accounts
import com.coradec.app.coram.model.Account
import com.coradec.app.coram.model.impl.BasicAccount
import com.coradec.coradeck.text.model.LocalText
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection.VERTICAL
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import java.util.*

@Route(value = "accounts", layout = MainView::class)
class AccountView : VerticalLayout(), HasDynamicTitle {
    override fun getPageTitle(): String = name.content(Locale.getDefault())
    val accounts = DataProvider.fromFilteringCallbacks(Accounts.query, Accounts.count)
    val accountTable = Grid(BasicAccount::class.java).apply { dataProvider = accounts }
//    val accountScroller = Scroller(accountTable, VERTICAL)
//    val accountEditor = AccountEditor()

    init {
        Accounts.open()
        Accounts.add(1, "Aktiven")
        Accounts.add(1000, "Kasse")

        setId("account-view")
        setSizeFull()
        configureAccountTable()
//        add(accountScroller)
//        add(accountEditor)
        add(accountTable)

//        setHorizontalComponentAlignment(FlexComponent.Alignment.START, name, sayHello)
//        sayHello.addClickListener { e: ClickEvent<Button?>? -> Notification.show("Hello " + name.value) }
    }

    private fun configureAccountTable() {
//        accountTable.addColumn(Account::id)
//            .setHeader("ID")
//            .setSortProperty("id");
//        accountTable.addColumn(Account::name)
//            .setHeader("Name")
//            .setSortProperty("name");
        accountTable.addClassName("account-table")
        accountTable.setSizeFull()
        accountTable.setColumns("id", "name")
    }

    companion object {
        val name = LocalText("PageName")
    }
}