package com.coradec.app.coram.model

import com.coradec.app.coram.model.impl.BasicAccount
import com.coradec.module.db.annot.Size

interface Account {
    val id: Int
    val name: @Size(128) String

    companion object {
        operator fun invoke(id: Int, name: String): Account = BasicAccount(id, name)
    }
}
