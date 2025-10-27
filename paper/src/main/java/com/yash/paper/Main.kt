package com.yash.paper

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.yash.devkagitam.utils.PaperEntryPoint

class Main : PaperEntryPoint() {
    @Composable
    override fun pluginContent(ctx: Context) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text("Hello", fontSize = 20.sp)
        }
    }

}