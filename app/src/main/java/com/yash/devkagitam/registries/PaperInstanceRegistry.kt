package com.yash.devkagitam.registries

import com.yash.dev.PaperEntryPoint
import com.yash.dev.Widget
import com.yash.dev.loadDex
import com.yash.devkagitam.db.api.ApiEntity
import com.yash.devkagitam.db.plugins.MetaDataPluginEntity
import com.yash.devkagitam.db.widgets.WidgetEntity
import kotlin.collections.getOrPut

object PaperInstanceRegistry {
    val mapOfInstances = mutableMapOf<String, Any>()

    fun getPaperInstance(plugin: MetaDataPluginEntity) : PaperEntryPoint {
        val ctx = AppRegistry.getAppContext()
        return mapOfInstances.getOrPut(plugin.name){
            loadDex(ctx,plugin.path, plugin.entryPoint)
        } as PaperEntryPoint
    }

    fun getWidgetInstance(widget: WidgetEntity) : Widget {
        val ctx = AppRegistry.getAppContext()
        return mapOfInstances.getOrPut(widget.id){
            loadDex(ctx,widget.apkPath, widget.widgetClass)
        } as Widget
    }

    fun getApiClassInstance(api : ApiEntity) : Any{
        val ctx = AppRegistry.getAppContext()
        return mapOfInstances.getOrPut(api.id){
            loadDex(ctx,api.apkPath, api.apiClass)
        }
    }
}