package com.coradec.app.coram.view

import com.coradec.app.coram.ctrl.AccountXlsReader
import com.coradec.app.coram.ctrl.Accounts
import com.coradec.app.coram.model.impl.BasicAccount
import com.coradec.app.coram.view.component.ButtonBar
import com.coradec.app.coram.view.component.Icons
import com.coradec.coradeck.core.model.ClassPathResource
import com.coradec.coradeck.text.model.LocalText
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.progressbar.ProgressBar
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
    val buttonBar = ButtonBar(Button("Import", Icons.import, ::importClickListener))
    val progressBar = ProgressBar()

    init {
        Accounts.open()
        setId("account-view")
        setSizeFull()
        configureAccountTable()
//        add(accountScroller)
//        add(accountEditor)
        add(accountTable)
        add(progressBar)
        add(buttonBar)

//        setHorizontalComponentAlignment(FlexComponent.Alignment.START, name, sayHello)
//        sayHello.addClickListener { e: ClickEvent<Button?>? -> Notification.show("Hello " + name.value) }
    }

    private fun importClickListener(clickEvent: ClickEvent<Button>) {
        progressBar.value = 0.0
        AccountXlsReader(ClassPathResource("accountplan.xlsx").location).run()
        progressBar.value = 1.0

    }

    private fun configureAccountTable() {
        accountTable.addClassName("account-table")
        accountTable.setSizeFull()
        accountTable.setColumns("id", "name", "currency", "vatCode", "agroup")
    }

    companion object {
        val name = LocalText("PageName")
    }
}