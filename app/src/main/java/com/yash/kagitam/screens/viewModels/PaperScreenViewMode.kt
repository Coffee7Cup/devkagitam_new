package com.yash.kagitam.screens.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yash.kagitam.db.api.ApiDB
import com.yash.kagitam.db.plugins.MetaDataPluginDB
import com.yash.kagitam.db.plugins.MetaDataPluginEntity
import com.yash.kagitam.registries.AppRegistry
import com.yash.kagitam.registries.PaperInstanceRegistry
import com.yash.kagitam.db.widgets.WidgetDB
import kotlinx.coroutines.launch
import java.io.File

class PaperScreenViewModel : ViewModel(){

    private val pluginDao = MetaDataPluginDB.getDatabase(AppRegistry.getAppContext()).metaDataPluginDao()
    val allPapers = mutableStateOf<List<MetaDataPluginEntity>>(emptyList())

    init {
        refreshPapers()
    }

    fun refreshPapers(){
       viewModelScope.launch {
           allPapers.value = pluginDao.getAllPlugins()
       }
    }
}

class PaperElementViewModel(val data: MetaDataPluginEntity): ViewModel(){

    val error = mutableStateOf<String?>(null)

    fun loadPaper(){
        viewModelScope.launch {
           try {
               val instance = PaperInstanceRegistry.getPaperInstance(AppRegistry.getAppContext(),data)

               AppRegistry.setCurrentPlugin(instance,data.name)

               AppRegistry.getNavController().navigate("paper"){
                   launchSingleTop = true
               }
           }catch(e : Error){
                error.value = e.message
           }catch(e: Exception){
               error.value = e.message
               Log.e("PaperElementViewModel", e.message, e)
           }
        }
    }

    fun deletePaper(psvm: PaperScreenViewModel){
        viewModelScope.launch {
            try{
                val pluginDao = MetaDataPluginDB.getDatabase(AppRegistry.getAppContext()).metaDataPluginDao()
                val widgetDao = WidgetDB.getDatabase(AppRegistry.getAppContext()).widgetDao()
                val apiDao = ApiDB.getDatabase(AppRegistry.getAppContext()).apiDao()

                apiDao.getApiByOwner(data.name)?.let{
                    apiDao.delete(it)
                }

                widgetDao.getWidgetsByOwner(data.name).forEach { widgetDao.delete(it) }
                pluginDao.delete(data)
                val file = File(data.path)
                file.deleteRecursively()
                psvm.refreshPapers()
            }catch(e: Error){
                error.value = e.message
            }
        }
    }

    fun removeError(){
        error.value = null
    }

}

class PaperElementViewModelFactory(private val data: MetaDataPluginEntity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaperElementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaperElementViewModel(data) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}