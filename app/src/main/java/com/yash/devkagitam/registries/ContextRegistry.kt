package com.yash.devkagitam.registries

import com.yash.devkagitam.utils.PaperContextProvider

object ContextRegistry {

    // In-memory cache of plugin contexts
    private val contextMap = mutableMapOf<String, PaperContextProvider>()

    fun getPluginContext(pluginName: String): PaperContextProvider {
        return contextMap.getOrPut(pluginName) {
            PaperContextProvider(pluginName)
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
