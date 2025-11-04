package com.yash.devkagitam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yash.devkagitam.registries.AppRegistry
import com.yash.devkagitam.screens.viewModels.ZIP_FILES
import com.yash.devkagitam.ui.theme.DevKagitamTheme
import com.yash.devkagitam.utils.activeDownloads
import com.yash.devkagitam.utils.cancelOkhttpDownload

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

    //remove all the downloads before removing app
//    override fun onDestroy() {
//        super.onDestroy()
//        val downloading = activeDownloads
//        for((link,call) in downloading ){
//            cancelOkhttpDownload(
//                link = link,
//                savedFileLoc = "${this.applicationContext}/$ZIP_FILES/${}",
//                onComplete = {}
//            )
//        }
//    }
}