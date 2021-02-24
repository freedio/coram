package com.coradec.app.coram.view

import com.coradec.app.coram.model.Account
import com.coradec.coradeck.text.model.LocalText
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
    val accountList = VerticalLayout()
    val accountScroller = Scroller(accountList, VERTICAL)
//    val dataProvider: DataProvider<Account, Void> = DataProvider.fromCallbacks(
//
//    )

    init {
        setId("account-view")
        add(accountScroller)

//        setHorizontalComponentAlignment(FlexComponent.Alignment.START, name, sayHello)
//        sayHello.addClickListener { e: ClickEvent<Button?>? -> Notification.show("Hello " + name.value) }
    }

    companion object {
        val name = LocalText("PageName")
    }
}