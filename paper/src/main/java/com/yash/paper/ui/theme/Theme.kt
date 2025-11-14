package com.yash.paper.ui.theme

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yash.paper.PaperObj

val defaultTheme = darkColorScheme(
    error = Color(0xff8e0b13),
    primary = Color(0xff4c4f54),
    secondary = Color(0xff232c2a),
    tertiary = Color(0xffefdfc5),
    onError = Color(0xff380f17),
)


@Composable
fun DevTheme(ctx: Context = PaperObj.getPaperCtx(), content: @Composable () -> Unit) {
    val BitCountFont = remember(ctx) {
        FontFamily(
            Font(
                ctx.resources.getIdentifier("bitcount", "font", ctx.packageName),
                weight = FontWeight.Normal
            )
        )
    }

    val CustomTypography = Typography(
        bodyLarge = TextStyle(fontFamily = BitCountFont, fontSize = 16.sp),
        titleLarge = TextStyle(fontFamily = BitCountFont, fontSize = 22.sp)
    )

    MaterialTheme(
        colorScheme = defaultTheme,
        typography = CustomTypography,
        content = content
    )
}
