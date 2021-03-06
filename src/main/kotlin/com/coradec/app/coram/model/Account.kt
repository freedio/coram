package com.coradec.app.coram.model

import com.coradec.app.coram.model.impl.BasicAccount
import com.coradec.module.db.annot.Size

interface Account {
    val id: Int
    val name: @Size(128) String
    val currency: @Size(value = 4) String
    val vatCode: @Size(8) String?
    val agroup: Int

    companion object {
        operator fun invoke(
            nr: Int,
            name: String,
            currency: String,
            vatCode: String,
            group: Int
        ): Account = BasicAccount(nr, name, currency, vatCode, group)
    }
}
