package com.yash.devkagitam.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yash.devkagitam.db.colorSchemes.ColorSchemeEntity
import com.yash.devkagitam.R

val BitcountFont = FontFamily(
    Font(R.font.bitcount)
)
val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = BitcountFont,  // use your font here
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = BitcountFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

lateinit var selectedColorScheme : ColorScheme

val defaultTheme = darkColorScheme(
    error = Color(0xff8e0b13),
    primary = Color(0xff4c4f54),
    secondary = Color.Black,
    onSecondary = Color(0xff232c2a),
    tertiary = Color(0xffefdfc5),
    onError = Color(0xff380f17),
)

fun setColorScheme(scheme: ColorSchemeEntity){

}


@Composable
fun DevKagitamTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = defaultTheme,
        typography = CustomTypography,
        content = content
    )
}