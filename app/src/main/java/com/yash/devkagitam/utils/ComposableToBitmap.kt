package com.yash.devkagitam.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.graphics.createBitmap

suspend fun composableToBitmap(context: Context,composable : @Composable () -> Unit) : Bitmap {

    val composeView = ComposeView(context).apply {
        setContent { composable() }
    }

    val widthSpec = View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(400, View.MeasureSpec.AT_MOST)
    composeView.measure(widthSpec, heightSpec)
    composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)

    val bitmap = createBitmap(composeView.measuredWidth, composeView.measuredHeight)
    val canvas = Canvas(bitmap)
    composeView.draw(canvas)
    return bitmap

}