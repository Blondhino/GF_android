package com.gadgetfactory.app.core.dictionary

class DictionaryImpl(
    private val stringResources: StringResources,
) : Dictionary {

    override fun getString(key: String): String {
        require(key.isNotBlank()) {
            "Key $key cannot be blank"
        }
        return stringResources[key] ?: key
    }

    override fun getString(key: Int): String {
        require(key != 0) {
            "Key $key cannot be 0"
        }
        return stringResources[key] ?: key.toString()
    }

    override fun getString(
        key: Int,
        vararg formatArgs: Any,
    ): String {
        require(key != 0) {
            "Key $key cannot be 0"
        }
        return stringResources[key, formatArgs] ?: key.toString()
    }
}
