package com.yash.kagitam.__dev__

import com.google.gson.Gson
import com.yash.kagitam.db.plugins.MetaDataPluginEntity
import java.io.File
import java.util.UUID

fun readManifest(manifestFile: File): MetaDataPluginEntity {
    val json = manifestFile.readText()

    val partial = Gson().fromJson(json, MetaDataPluginEntity::class.java)

    return MetaDataPluginEntity(
        name = partial.name,
        author = partial.author,
        version = partial.version,
        entryPoint = partial.entryPoint,
        widgets = partial.widgets ?: emptyList(),
        apiClass = partial.apiClass,
        id = partial.id ?: UUID.randomUUID().toString(), // <-- FIX
        path = "${manifestFile.parent}/paper.apk"
    )
}
