package com.flandolf.sets2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    var darkTheme by mutableStateOf("system")
        private set

    fun updateDarkTheme(theme: String) {
        darkTheme = theme
    }

    var dynamicColor by mutableStateOf(true)
        private set

    fun updateDynamicColor(dynamic: Boolean) {
        dynamicColor = dynamic
    }
}
