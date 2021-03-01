package com.coradec.app.coram.model.impl

import com.coradec.app.coram.model.Account
import com.coradec.module.db.annot.Size

data class BasicAccount(override val id: Int, override val name: @Size(128) String): Account
