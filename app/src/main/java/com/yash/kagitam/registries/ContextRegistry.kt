package com.yash.kagitam.registries

import com.yash.deps.PaperContext
import com.yash.kagitam.utils.PaperContextImpl

object ContextRegistry {

    // In-memory cache of plugin contexts
    private val contextMap = mutableMapOf<String, PaperContext>()

    fun getPluginContext(pluginName: String): PaperContext {
        return contextMap.getOrPut(pluginName) {
            PaperContextImpl(pluginName, AppRegistry.getAppContext())
        }
    }

    fun removePluginContext(pluginName: String) {
        contextMap.remove(pluginName)
    }

    fun clearAll() {
        contextMap.clear()
    }


    fun hasContext(pluginName: String): Boolean {
        return contextMap.containsKey(pluginName)
    }
}
