package com.gadgetfactory.app.core.dictionary

interface StringResources {

    operator fun get(key: String): String?

    operator fun get(key: Int): String?

    operator fun get(
        key: Int,
        vararg formatArgs: Any,
    ): String?
}
