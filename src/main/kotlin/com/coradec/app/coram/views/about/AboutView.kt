package com.coradec.app.coram.views.about

import com.coradec.app.coram.views.main.MainView
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(value = "about", layout = MainView::class)
@PageTitle("About")
class AboutView : Div() {
    init {
        setId("about-view")
        add(Text("Content placeholder"))
    }
}