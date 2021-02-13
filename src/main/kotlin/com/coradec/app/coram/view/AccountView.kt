package com.coradec.app.coram.view

import com.coradec.app.coram.views.main.MainView
import com.coradec.coradeck.text.model.ConText
import com.coradec.coradeck.text.model.LocalText
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route

@Route(value = "accounts", layout = MainView::class)
class AccountView : VerticalLayout(), HasDynamicTitle {
    override fun getPageTitle(): String = name.content(UI.getCurrent().locale)

    companion object {
        val name: ConText = LocalText("PageName")
    }
}