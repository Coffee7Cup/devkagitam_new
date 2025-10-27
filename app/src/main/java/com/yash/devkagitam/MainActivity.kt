package com.yash.devkagitam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yash.devkagitam.registries.AppRegistry
import com.yash.devkagitam.ui.theme.DevKagitamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppRegistry.setAppContext(this.applicationContext)
        setContent {
            DevKagitamTheme {
                NavigationMain()
            }
        }
    }
}