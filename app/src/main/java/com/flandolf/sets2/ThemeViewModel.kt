import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flandolf.sets2.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val themePreferences: ThemePreferences) : ViewModel() {
    val darkTheme: StateFlow<String> = themePreferences.themeFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, "System")

    val dynamicColor: StateFlow<Boolean> = themePreferences.dynamicColorFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun updateDarkTheme(theme: String) {
        viewModelScope.launch {
            themePreferences.saveThemeSetting(theme)
        }
    }

    fun updateDynamicColor(dynamic: Boolean) {
        viewModelScope.launch {
            themePreferences.saveDynamicColorSetting(dynamic)
        }
    }
}