package com.yash.paper.__dev__

import java.util.UUID


object DevPaperEntity {
    val name: String = "DevTest"
    val author: String = "Yash"
    val version: String = "1.0.0"
    val id: String = UUID.randomUUID().toString()
    val entryPoint: String = "com.yash.paper.Main"
    val widgets: List<String> =
        listOf(
        "com.yash.paper.widgets.TinyWidget",
        "com.yash.paper.widgets.SmallWidget",
        "com.yash.paper.widgets.MediumWidget",
        "com.yash.paper.widgets.LargeWidget",
        "com.yash.paper.widgets.SmallWidget",
        "com.yash.paper.widgets.TinyWidget",
        "com.yash.paper.widgets.MediumWidget",
        "com.yash.paper.widgets.LargeWidget",
        "com.yash.paper.widgets.TinyWidget",
        "com.yash.paper.widgets.LargeWidget",
        "com.yash.paper.widgets.SmallWidget",
        "com.yash.paper.widgets.TinyWidget",
        "com.yash.paper.widgets.MediumWidget",
        "com.yash.paper.widgets.LargeWidget",
        "com.yash.paper.widgets.TinyWidget"
    )
    val apiClass: Any = ""
}