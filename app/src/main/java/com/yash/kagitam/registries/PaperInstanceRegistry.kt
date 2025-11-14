package com.yash.kagitam.registries

import android.content.Context
import com.yash.deps.PaperEntryPoint
import com.yash.deps.Widget
import com.yash.deps.loadDex
import com.yash.kagitam.db.api.ApiEntity
import com.yash.kagitam.db.plugins.MetaDataPluginEntity
import com.yash.kagitam.db.widgets.WidgetEntity
import kotlin.collections.getOrPut

object PaperInstanceRegistry {
    val mapOfInstances = mutableMapOf<String, Any>()

    fun getPaperInstance(appCtx: Context,plugin: MetaDataPluginEntity) : PaperEntryPoint {
        return mapOfInstances.getOrPut(plugin.name){
            loadDex(appCtx,plugin.path, plugin.entryPoint)
        } as PaperEntryPoint
    }

    fun getWidgetInstance(appCtx: Context, widget: WidgetEntity) : Widget {
        return mapOfInstances.getOrPut(widget.id){
            loadDex(appCtx,"${widget.apkPath}", widget.widgetClass)
        } as Widget
    }

    fun getApiInstance(appCtx: Context, api : ApiEntity) : Any {
        return mapOfInstances.getOrPut(api.id){
            loadDex(appCtx, api.apkPath, api.apiClass)
        }
    }
}