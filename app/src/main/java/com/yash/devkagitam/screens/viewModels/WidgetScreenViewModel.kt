package com.yash.devkagitam.screens.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.dev.Widget
import com.yash.devkagitam.__dev__.DevPaperEntity
import com.yash.devkagitam.db.widgets.WidgetDB
import com.yash.devkagitam.db.widgets.WidgetEntity
import kotlinx.coroutines.launch

class WidgetScreenViewModel : ViewModel() {
  private val widgetDao = WidgetDB.getDatabase().widgetDao()
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

  fun devWidgets() : List<Widget>{

    val widgets = DevPaperEntity.widgets

    val instances: List<Widget> = widgets.map{
      val cls =Class.forName(it)
      val inst = cls.getDeclaredConstructor().newInstance() as Widget
      inst
    }
    return instances
  }

}
