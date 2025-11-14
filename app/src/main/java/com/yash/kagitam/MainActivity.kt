package com.yash.kagitam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yash.kagitam.registries.AppRegistry
import com.yash.kagitam.ui.theme.DevKagitamTheme

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