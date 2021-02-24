package com.coradec.app.coram.ctrl.impl

import com.coradec.app.coram.ctrl.Selection

data class SqlSelection(val expr: String) : Selection {
    override val where: String
        get() {
            val orig = StringBuilder(expr)
            Regex("@(\\w+)")
                .findAll(orig)
                .forEach {
                    it.groups[1]?.let { element ->
                        orig.replace(element.range.first-1, element.range.last+1, element.value.toSqlObjectName())
                    }
                }
            return orig.toString().let { if (it.isBlank()) "" else " where $it" }
        }
}
