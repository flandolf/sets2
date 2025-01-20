package com.flandolf.sets2

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferences(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "theme_preferences")

    private val themePreferenceKey = stringPreferencesKey("dark_theme")
    private val dynamicColorEnabledKey = booleanPreferencesKey("dynamic_color")

    val themeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[themePreferenceKey] ?: "System"
        }

    val dynamicColorFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[dynamicColorEnabledKey] ?: true
        }

    suspend fun saveThemeSetting(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[themePreferenceKey] = theme
        }
    }

    suspend fun saveDynamicColorSetting(dynamic: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[dynamicColorEnabledKey] = dynamic
        }
    }
}