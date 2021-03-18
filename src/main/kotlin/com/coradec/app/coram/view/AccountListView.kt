package com.coradec.app.coram.view

import com.coradec.app.coram.ctrl.AccountXlsReader
import com.coradec.app.coram.ctrl.Accounts
import com.coradec.app.coram.model.Account
import com.coradec.app.coram.model.impl.BasicAccount
import com.coradec.app.coram.view.component.ButtonBar
import com.coradec.app.coram.view.component.Icons
import com.coradec.coradeck.core.model.ClassPathResource
import com.coradec.coradeck.text.model.LocalText
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.renderer.NativeButtonRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route

@Route(value = "accounts", layout = MainView::class)
class AccountListView : VerticalLayout(), HasDynamicTitle {
    override fun getPageTitle(): String = pageName

    val accounts = DataProvider.fromFilteringCallbacks(Accounts.query, Accounts.count)
    val accountTable = Grid(BasicAccount::class.java).apply { dataProvider = accounts }
    val buttonBar = ButtonBar(Button("Import", Icons.import, ::importClickListener))

    init {
        Accounts.open()
        setId("account-list-view")
        setSizeFull()
        configureAccountTable()
        add(accountTable)
        add(buttonBar)

//        setHorizontalComponentAlignment(FlexComponent.Alignment.START, name, sayHello)
//        sayHello.addClickListener { e: ClickEvent<Button?>? -> Notification.show("Hello " + name.value) }
    }

    private fun importClickListener(clickEvent: ClickEvent<Button>) {
        AccountXlsReader(ClassPathResource("accountplan.xlsx").location).run()
        UI.getCurrent().page.reload()
    }

    private fun configureAccountTable() {
        accountTable.addClassName("account-table")
        accountTable.setSizeFull()
        accountTable.setColumns("id", "name", "currency", "vatCode", "agroup")
        accountTable.addColumn(NativeButtonRenderer("⊙") { itemToEdit ->
            println("Editing item ${itemToEdit.id}")
            UI.getCurrent().page.reload()
        })
        accountTable.addColumn(NativeButtonRenderer("⊖") { itemToDelete ->
            println("Removing item ${itemToDelete.id}")
            Accounts.remove(itemToDelete.id)
            accountTable.setSizeFull()
            UI.getCurrent().page.reload()
        })
    }

    class EditRenderer(any: Any): Renderer<Account>() {

    }

    companion object {
        val name = LocalText("PageName")
        val pageName get() = name.content
    }
}