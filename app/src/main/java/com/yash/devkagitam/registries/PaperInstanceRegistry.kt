package com.yash.devkagitam.registries

import com.yash.devkagitam.db.api.ApiEntity
import com.yash.devkagitam.db.plugins.MetaDataPluginEntity
import com.yash.devkagitam.db.widgets.WidgetEntity
import com.yash.devkagitam.utils.PaperEntryPoint
import com.yash.devkagitam.utils.Widget
import com.yash.devkagitam.utils.loadDex
import kotlin.collections.getOrPut

object PaperInstanceRegistry {
    val mapOfInstances = mutableMapOf<String, Any>()

    fun getPaperInstance(plugin: MetaDataPluginEntity) : PaperEntryPoint {
        return mapOfInstances.getOrPut(plugin.name){
            loadDex(plugin.path, plugin.entryPoint)
        } as PaperEntryPoint
    }

    fun getWidgetInstance(widget: WidgetEntity) : Widget {
        return mapOfInstances.getOrPut(widget.id){
            loadDex(widget.apkPath, widget.widgetClass)
        } as Widget
    }

    fun getApiClassInstance(api : ApiEntity) : Any{
        return mapOfInstances.getOrPut(api.id){
            loadDex(api.apkPath, api.apiClass)
        }
    }

}