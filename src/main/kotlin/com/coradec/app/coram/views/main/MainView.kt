package com.coradec.app.coram.views.main

import com.coradec.coradeck.conf.model.LocalProperty
import com.coradec.coradeck.conf.model.Property
import com.coradec.coradeck.conf.model.impl.ContextProperty
import com.coradec.app.coram.view.AccountView
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.server.PWA
import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.TabsVariant
import com.coradec.app.coram.views.helloworld.HelloWorldView
import com.coradec.app.coram.views.about.AboutView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.RouterLink
import java.util.*

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "Coradec Account Manager", shortName = "CORAM", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
class MainView : AppLayout() {
    private val menu: Tabs
    private var viewTitle: H1? = null
    private val theme: Property<String> = LocalProperty("Theme", "dark")
    private fun createHeaderContent(): Component {
        val layout = HorizontalLayout()
        layout.setId("header")
        layout.themeList["dark"] = true
        layout.setWidthFull()
        layout.isSpacing = false
        layout.alignItems = FlexComponent.Alignment.CENTER
        layout.add(DrawerToggle())
        viewTitle = H1()
        layout.add(viewTitle)
        layout.add(Avatar())
        return layout
    }

    private fun createDrawerContent(menu: Tabs): Component {
        val layout = VerticalLayout()
        layout.setSizeFull()
        layout.themeList["dark"] = true
        layout.isPadding = false
        layout.isSpacing = false
        layout.themeList["spacing-s"] = true
        layout.alignItems = FlexComponent.Alignment.STRETCH
        val coramLayout = VerticalLayout()
        coramLayout.alignItems = FlexComponent.Alignment.START
        coramLayout.add(H1("CorAM"))
        coramLayout.add(Paragraph("Coradec Account Manager"))
        val logoLayout = HorizontalLayout()
        logoLayout.setId("logo")
        logoLayout.alignItems = FlexComponent.Alignment.CENTER
        logoLayout.add(Image("images/logo.png", "Coram Logo"))
        logoLayout.add(coramLayout)
        layout.add(logoLayout, menu)
        return layout
    }

    private fun createMenu(): Tabs {
        val tabs = Tabs()
        tabs.orientation = Tabs.Orientation.VERTICAL
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL)
        tabs.setId("tabs")
        tabs.add(*createMenuItems())
        return tabs
    }

    private fun createMenuItems(): Array<out Component> {
        return arrayOf(
            createTab(AccountView.name.content(UI.getCurrent().locale), AccountView::class.java),
            createTab("Hello World", HelloWorldView::class.java),
            createTab("About", AboutView::class.java)
        )
    }

    override fun afterNavigation() {
        super.afterNavigation()
        getTabForComponent(content).ifPresent { selectedTab: Tab? -> menu.selectedTab = selectedTab }
        val pageTitle = ContextProperty(content.javaClass.name, "PageTitle", "?page title: missing configuration property?")
        viewTitle!!.text = pageTitle.value
    }

    private fun getTabForComponent(component: Component): Optional<Tab> {
        return menu.children.filter { tab: Component? -> ComponentUtil.getData(tab, Class::class.java) == component.javaClass }
            .findFirst().map { obj: Component? -> Tab::class.java.cast(obj) }
    }

    companion object {
        private fun createTab(text: String, navigationTarget: Class<out Component>): Tab {
            val tab = Tab()
            tab.add(RouterLink(text, navigationTarget))
            ComponentUtil.setData(tab, Class::class.java, navigationTarget)
            return tab
        }
    }

    init {
        primarySection = Section.DRAWER
        addToNavbar(true, createHeaderContent())
        menu = createMenu()
        addToDrawer(createDrawerContent(menu))
    }
}