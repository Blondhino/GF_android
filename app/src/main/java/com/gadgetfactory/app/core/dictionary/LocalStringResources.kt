package com.gadgetfactory.app.core.dictionary

import android.content.Context
import android.content.res.Resources

class LocalStringResources(
    private val context: Context,
) : StringResources {

    private val stringPrefix = "R.string."
    private val resourceType = "string"

    private val packageName: String = context.packageName
    private val resources: Resources = context.resources

    override fun get(key: String): String = getFromResources(
        name = key.replace(stringPrefix, ""),
        packageName = packageName,
        resources = resources,
    )

    override fun get(key: Int): String = resources.getString(key)

    override fun get(
        key: Int,
        vararg formatArgs: Any,
    ): String =
        resources.getString(key, *formatArgs)

    private fun getFromResources(
        name: String,
        packageName: String,
        resources: Resources,
    ) = resources.getString(resources.getIdentifier(name, resourceType, packageName))
}
