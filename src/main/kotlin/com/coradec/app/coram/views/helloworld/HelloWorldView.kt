package com.coradec.app.coram.views.helloworld

import com.coradec.app.coram.views.main.MainView
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.RouteAlias
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route

@Route(value = "hello", layout = MainView::class)
@PageTitle("Hello World")
@CssImport("./styles/views/helloworld/hello-world-view.css")
@RouteAlias(value = "", layout = MainView::class)
class HelloWorldView : HorizontalLayout() {
    private val name: TextField
    private val sayHello: Button

    init {
        setId("hello-world-view")
        name = TextField("Your name")
        sayHello = Button("Say hello")
        add(name, sayHello)
        setVerticalComponentAlignment(FlexComponent.Alignment.END, name, sayHello)
        sayHello.addClickListener { e: ClickEvent<Button?>? -> Notification.show("Hello " + name.value) }
    }
}