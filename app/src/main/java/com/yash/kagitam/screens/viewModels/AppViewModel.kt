package com.yash.kagitam.screens.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    val currentPage = mutableStateOf(0)
}