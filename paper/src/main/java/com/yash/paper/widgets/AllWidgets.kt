package com.yash.paper.widgets

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.deps.Widget

class SmallWidget : Widget(){
    @Composable
    override fun Content(modifier: Modifier) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Magenta)
                .padding(50.dp),
            contentAlignment = Alignment.Center
        ){
            Text("AMMAMMA, I LOVE YOUUUU", color = Color.White, fontSize = 16.sp)
        }
    }

    @Composable
    override fun OnHold() {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text("LOVE YOU MOREEE AMMAMMA", color = Color.White, fontSize = 16.sp)
        }
    }

    @Composable
    override fun OnClick() {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text("LOVE YOU EVEN MOREE AMMAMMA", color = Color.White, fontSize = 16.sp)
        }
    }

}