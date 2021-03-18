package com.coradec.app.coram.view

import com.coradec.app.coram.ctrl.Accounts
import com.coradec.app.coram.ctrl.Invoices
import com.coradec.app.coram.model.impl.BasicInvoice
import com.coradec.app.coram.view.component.ButtonBar
import com.coradec.app.coram.view.component.Icons
import com.coradec.coradeck.text.model.LocalText
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.renderer.NativeButtonRenderer
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route

@Route(value = "invoices", layout = MainView::class)
class InvoiceListView : VerticalLayout(), HasDynamicTitle {
    override fun getPageTitle(): String = pageName
    val invoices = DataProvider.fromFilteringCallbacks(Invoices.query, Invoices.count)
    val invoiceTable = Grid(BasicInvoice::class.java).apply { dataProvider = invoices }
    val buttonBar = ButtonBar(Button("Import", Icons.import, ::importClickListener))

    init {
        Invoices.open()
        setId("invoice-list-view")
        setSizeFull()
        configureInvoiceTable()
        add(invoiceTable)
        add(buttonBar)

//        setHorizontalComponentAlignment(FlexComponent.Alignment.START, name, sayHello)
//        sayHello.addClickListener { e: ClickEvent<Button?>? -> Notification.show("Hello " + name.value) }
    }

    private fun importClickListener(clickEvent: ClickEvent<Button>) {
        UI.getCurrent().page.reload()
    }

    private fun configureInvoiceTable() {
        invoiceTable.addClassName("invoice-table")
        invoiceTable.setSizeFull()
        invoiceTable.setColumns("id")
        invoiceTable.addColumn(NativeButtonRenderer("⊙") { itemToEdit ->
            println("Editing item ${itemToEdit.id}")
            UI.getCurrent().page.reload()
        })
        invoiceTable.addColumn(NativeButtonRenderer("⊖") { itemToDelete ->
            println("Removing item ${itemToDelete.id}")
            Accounts.remove(itemToDelete.id)
            invoiceTable.setSizeFull()
            UI.getCurrent().page.reload()
        })
    }

    companion object {
        val name = LocalText("PageName")
        val pageName get() = name.content
    }

}