package com.yash.devkagitam.utils

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yash.devkagitam.registries.AppRegistry

abstract class PaperEntryPoint {
    @Composable
    abstract fun pluginContent(ctx: Context)

    @Composable
    fun RenderWithHome(ctx: Context) {
        val navController = AppRegistry.getNavController()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) {
            pluginContent(ctx)

            Button(
                onClick = {
                    navController.navigate("App"){
                        popUpTo("App"){inclusive = true}
                        launchSingleTop = true
                    } },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 4.dp)
                    .size(40.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.Red
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(0.5.dp, Color.White)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Home",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}
