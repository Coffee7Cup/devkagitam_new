package com.yash.deps

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class PaperEntryPoint {
    @Composable
    abstract fun pluginContent()

    @Composable
    fun RenderWithHome(navController : NavController) {

        var clicked by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        // Animate size with smooth spring
        val scale by animateFloatAsState(
            targetValue = if (clicked) 1.2f else 1f, // 20% bounce
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "homeButtonScale"
        )

        Box(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(WindowInsets.Companion.systemBars.asPaddingValues())
        ) {
            pluginContent()

            Button(
                onClick = {
                    clicked = true
                    scope.launch {
                        delay(100)
                        clicked = false
                        navController.navigate("app") {
                            popUpTo("app") {inclusive = false}
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.Companion
                    .align(Alignment.Companion.BottomEnd)
                    .padding(end = 12.dp, bottom = 80.dp)
                    .size(50.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
                    .clip(RoundedCornerShape(10)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Companion.Black,
                    contentColor = Color.Companion.Red
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(30),
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(0.5.dp, Color.Companion.Red)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "Home",
                    tint = Color.Companion.Red,
                    modifier = Modifier.Companion.size(27.dp)
                )
            }
        }
    }

}