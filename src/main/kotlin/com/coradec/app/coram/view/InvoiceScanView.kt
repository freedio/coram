package com.coradec.app.coram.view

import com.coradec.coradeck.text.model.LocalText
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Tag
import com.vaadin.flow.router.HasDynamicTitle

@Tag(Tag.DIV)
class InvoiceScanView: Component(), HasDynamicTitle {
    override fun getPageTitle() = pageName

    companion object {
        val name = LocalText("PageName")
        val pageName get() = name.content
    }
}
