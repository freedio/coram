package com.coradec.app.coram.view.component

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class ButtonBar(vararg buttons: Button) : HorizontalLayout() {
    init {
        setSizeFull()
        setAlignSelf(FlexComponent.Alignment.STRETCH)
        buttons.forEach { this@ButtonBar.add(it) }
        if (buttons.isNotEmpty()) buttons[0].element.style.set("margin-left", "auto")
    }
}