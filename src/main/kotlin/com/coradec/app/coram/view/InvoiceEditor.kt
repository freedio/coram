package com.coradec.app.coram.view

import com.coradec.coradeck.text.model.LocalText
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route

@Route(value = "accounts-payable-editor", layout = MainView::class)
class InvoiceEditor() : FormLayout(), HasDynamicTitle {
    override fun getPageTitle() = pageName

    val addressView = InvoiceAddressView()
    val detailsView = InvoiceDetailView()
    val itemsView = InvoiceItemsView()
    val paymentView = InvoicePaymentView()
    val invoiceScan = InvoiceScanView()
    val addressEditor = InvoiceAddressEditor()
    val detailsEditor = InvoiceDetailEditor()
    val itemsEditor = InvoiceItemsEditor()
    val paymentEditor = InvoicePaymentEditor()
    val invoiceScanner = InvoiceScanEditor()

    init {
        setId("invoice-editor")
        setSizeFull()
        add(addressEditor, detailsView, itemsView, paymentView, invoiceScan)
    }

    companion object {
        val name = LocalText("PageName")
        val pageName get() = name.content
    }
}
