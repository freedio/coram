package com.coradec.app.coram.model.impl

import com.coradec.app.coram.model.Account
import com.coradec.module.db.annot.Size

data class BasicAccount(
    override val id: Int,
    override val name: @Size(value = 128) String,
    override val currency: @Size(value = 4) String,
    override val vatCode: @Size(8) String,
    override val group: Int
): Account
