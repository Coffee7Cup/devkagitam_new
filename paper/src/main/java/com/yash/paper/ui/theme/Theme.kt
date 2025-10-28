package com.yash.paper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yash.paper.R

val BitCountFont = FontFamily(
    Font(R.font.bitcount)
)

val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = BitCountFont,  // use your font here
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = BitCountFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)

val defaultTheme = darkColorScheme(
    error = Color(0xff8e0b13),
    primary = Color(0xff4c4f54),
    secondary = Color(0xff232c2a),
    tertiary = Color(0xffefdfc5),
    onError = Color(0xff380f17),
)


@Composable
fun DevTheme(
    content : @Composable () -> Unit
){
    MaterialTheme(
        colorScheme = defaultTheme,
        typography = CustomTypography
    ){
        content()
    }
}