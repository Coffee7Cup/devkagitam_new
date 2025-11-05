package com.yash.devkagitam.screens.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.devkagitam.db.api.ApiDB
import com.yash.devkagitam.registries.AppRegistry
import com.yash.devkagitam.registries.PaperInstanceRegistry
import com.yash.devkagitam.db.plugins.MetaDataPluginDB
import com.yash.devkagitam.db.plugins.MetaDataPluginEntity
import com.yash.devkagitam.db.widgets.WidgetDB
import kotlinx.coroutines.launch
import java.io.File

class PaperScreenViewModel : ViewModel(){

    private val pluginDao = MetaDataPluginDB.getDatabase().metaDataPluginDao()
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
               val instance = PaperInstanceRegistry.getPaperInstance(data)

               AppRegistry.setCurrentPlugin(instance,data.name)

               AppRegistry.getNavController().navigate("paper"){
                   launchSingleTop = true
               }
           }catch(e : Error){
                error.value = e.message
           }
        }
    }

    fun deletePaper(){
        viewModelScope.launch {
            try{
                val pluginDao = MetaDataPluginDB.getDatabase().metaDataPluginDao()
                val widgetDao = WidgetDB.getDatabase().widgetDao()
                val apiDao = ApiDB.getDatabase().apiDao()

                if(apiDao.getApiByOwner(data.name ) != null){
                    apiDao.delete(apiDao.getApiByOwner(data.name))
                }

                //TODO : Optimize below code buy adding Quires in Dao
                widgetDao.getWidgetsByOwner(data.name).forEach { widgetDao.delete(it) }
                pluginDao.delete(data)
                val file = File(data.path)
                file.deleteRecursively()
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