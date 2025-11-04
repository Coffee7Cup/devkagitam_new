package com.yash.paper.widgets

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.dev.Widget
import com.yash.paper.ui.theme.DevTheme

class SmallWidget : Widget() {
    @Composable
    override fun Content(modifier: Modifier, i: Context) {
        DevTheme {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E88E5))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ){
                Text("Small", color = Color.White, fontSize = 16.sp)
            }
        }
    }

    @Composable
    override fun OnHold(ctx: Context) {
        Text("Held")
    }

    @Composable
    override fun OnClick(ctx: Context) {
        Text("Clicked")
    }
}

// Medium widget
class MediumWidget : Widget() {
    @Composable
    override fun Content(modifier: Modifier, i: Context) {
        DevTheme {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color(0xFF43A047))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Medium Widget", color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text("More content here", color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Text("Even more text", color = Color.White, fontSize = 14.sp)
            }
        }
    }
    @Composable
    override fun OnHold(ctx: Context) {
        Text("Held")
    }

    @Composable
    override fun OnClick(ctx: Context) {
        Text("Clicked")
    }
}

// Large widget
class LargeWidget : Widget() {
    @Composable
    override fun Content(modifier: Modifier, i: Context) {
        DevTheme {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE53935))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Large Widget", color = Color.White, fontSize = 20.sp)
                Spacer(Modifier.height(12.dp))
                Text("This widget has", color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Text("a lot more", color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Text("content inside it", color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Text("to demonstrate", color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Text("variable heights", color = Color.White, fontSize = 14.sp)
            }
        }
    }
    @Composable
    override fun OnHold(ctx: Context) {
        Text("Held")
    }

    @Composable
    override fun OnClick(ctx: Context) {
        Text("Clicked")
    }
}

// Extra small widget
class TinyWidget : Widget() {
    @Composable
    override fun Content(modifier: Modifier, i: Context) {
        DevTheme {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFB8C00))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ){
                Text("Tiny", color = Color.White, fontSize = 14.sp)
            }
        }
    }
    @Composable
    override fun OnHold(ctx: Context) {
        Text("Held")
    }

    @Composable
    override fun OnClick(ctx: Context) {
        Text("Clicked")
    }
}

// Tall widget
class TallWidget : Widget() {
    @Composable
    override fun Content(modifier: Modifier, i: Context) {
        DevTheme {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color(0xFF8E24AA))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                repeat(8) { index ->
                    Text(
                        "Line ${index + 1}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
    @Composable
    override fun OnHold(ctx: Context) {
        Text("Held")
    }

    @Composable
    override fun OnClick(ctx: Context) {
        Text("Clicked")
    }
}