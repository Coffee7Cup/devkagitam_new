package com.yash.paper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yash.deps.PaperEntryPoint
import com.yash.deps.PaperContext
import com.yash.paper.R


class Main : PaperEntryPoint(){
    @Composable
    override fun pluginContent() {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            App()
        }
    }

}

@Composable
fun App(){

    val ctx: PaperContext = LocalContext.current as PaperContext

    Column {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text("Local ctx : $LocalContext, \n Accessing R.string = ${ctx.getString(R.string.app_name)} \n ctx.Current = ${ctx}")
        }
    }
}