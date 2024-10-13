package com.gadgetfactory.app.core.dictionary

interface Dictionary {

    fun getString(key: String): String

    fun getString(key: Int): String

    fun getString(
        key: Int,
        vararg formatArgs: Any,
    ): String
}
