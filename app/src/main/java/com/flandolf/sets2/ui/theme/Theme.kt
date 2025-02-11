package com.flandolf.sets2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext

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
@Composable
fun AppTheme(
    darkTheme: String = "System",
    dynamicColor: State<Boolean>,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor.value -> {
            val context = LocalContext.current
            if (darkTheme == "System") {
                if (isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else if (darkTheme == "Dark") {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        darkTheme == "Dark" -> DarkColorScheme
        darkTheme == "Light" -> LightColorScheme
        darkTheme == "System" -> {
            if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        }
        else -> {
            if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}