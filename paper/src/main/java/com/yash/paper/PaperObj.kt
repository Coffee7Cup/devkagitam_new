package com.yash.paper

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.serialization.Contextual

@SuppressLint("StaticFieldLeak")
object PaperObj {
    private lateinit var paperCtx: Context

    fun getPaperCtx() = paperCtx

    fun setPaperCtx(ctx: Context) {
        paperCtx = ctx
    }
}
