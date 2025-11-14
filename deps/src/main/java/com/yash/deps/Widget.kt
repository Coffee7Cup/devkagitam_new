package com.yash.deps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

abstract class Widget() {
    /** Each widget defines its UI here. */
    @Composable
    protected abstract fun Content(modifier: Modifier)
    @Composable
    abstract fun OnHold()
    @Composable
    abstract fun OnClick()


    /** Entry point for rendering. Allows natural height for staggered grid. */
    @SuppressLint("UnusedBoxWithConstraintsScope")
    @Composable
    fun Render() {
        Box(
            modifier = Modifier.Companion
                .wrapContentHeight()  // ✅ Natural height
        ) {
            Content(Modifier.Companion.fillMaxWidth())
        }
    }
}