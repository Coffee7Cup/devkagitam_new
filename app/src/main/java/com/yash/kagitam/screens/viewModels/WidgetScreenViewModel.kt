package com.yash.kagitam.screens.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.deps.Widget
import com.yash.kagitam.db.widgets.WidgetDB
import com.yash.kagitam.db.widgets.WidgetEntity
import kotlinx.coroutines.launch
import com.yash.kagitam.registries.AppRegistry
import com.yash.kagitam.registries.PaperInstanceRegistry
import java.util.UUID


class WidgetScreenViewModel : ViewModel() {

  val error = mutableStateOf<String?>(null)
  private var _devWidgets: List<Widget>? = null
  private val widgetDao = try {
    WidgetDB.getDatabase(AppRegistry.getAppContext()).widgetDao()
  }catch(e : Exception){
    error.value = e.message
    Log.d("WSVM", "error ${e.message}")
    throw e
  }
  var allWidgets = mutableStateOf<List<WidgetEntity>>(emptyList())
    private set

  init {
    viewModelScope.launch { getWidgets() }
  }

  fun refreshWidgets() {
    viewModelScope.launch { getWidgets() }
  }

  private suspend fun getWidgets() {
    allWidgets.value = widgetDao.getAllWidgets()
  }

  fun addWidget(widget: WidgetEntity) {
    viewModelScope.launch {
      val new = widget.copy(selected = true)
      widgetDao.update(new)
      getWidgets()
    }
  }

  fun removeWidget(widget: WidgetEntity) {
    viewModelScope.launch {
      val new = widget.copy(selected = false)
      widgetDao.update(new)
      getWidgets()
    }
  }

  fun devWidgets(): List<Widget> {
    if (_devWidgets == null) {

      val manifest = DevObject.getManifest(AppRegistry.getAppContext())

      val widgets = manifest.widgets

      val entitis = widgets.map{
        WidgetEntity(
            id = UUID.randomUUID().toString(),
            owner = manifest.name,
            widgetClass = it,
            apkPath = manifest.path,
            selected = true
        )
      }

      _devWidgets = entitis.map {
        PaperInstanceRegistry.getWidgetInstance(AppRegistry.getAppContext(), it)
      }
    }
    return _devWidgets!!
  }
}
