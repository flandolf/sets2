import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

class SetTimerViewModel : ViewModel() {
    var isRunning by mutableStateOf(false)
        private set
    var remainingTime by mutableLongStateOf(180L)
        private set

    fun startTimer(duration: Duration) {
        if (!isRunning) {
            isRunning = true
            remainingTime = duration.inWholeSeconds
            viewModelScope.launch {
                while (isRunning && remainingTime > 0) {
                    remainingTime -= 1
                    delay(1000L)
                }
                isRunning = false
            }
        }
    }

    fun resetTimer(duration: Duration) {
        isRunning = false
        remainingTime = duration.inWholeSeconds
    }
}