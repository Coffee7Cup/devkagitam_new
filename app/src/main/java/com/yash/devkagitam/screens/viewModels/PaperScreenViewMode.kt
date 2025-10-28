package com.yash.devkagitam.screens.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yash.devkagitam.registries.AppRegistry
import com.yash.devkagitam.registries.PaperInstanceRegistry
import com.yash.devkagitam.db.plugins.MetaDataPluginDB
import com.yash.devkagitam.db.plugins.MetaDataPluginEntity
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
           Log.d("PSVM","init ${pluginDao}")
           allPapers.value = pluginDao.getAllPlugins()
           Log.d("PSVM","${allPapers.value.size}")
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