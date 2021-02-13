package com.coradec.app.coram.annot

import kotlin.annotation.AnnotationRetention.*

@Retention(RUNTIME)
annotation class LocalPageTitle(
    val value: String = ""
)
