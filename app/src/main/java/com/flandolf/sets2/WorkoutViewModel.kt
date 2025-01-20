import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WorkoutViewModel : ViewModel() {
    var isRunning by mutableStateOf(false)
        private set
    var elapsedTime by mutableLongStateOf(0L)
        private set

    fun startTimer() {
        if (!isRunning) {
            isRunning = true
            viewModelScope.launch {
                while (isRunning) {
                    elapsedTime += 1
                    delay(1000L)

                }
            }
        }
    }

    fun stopTimer() {
        isRunning = false
        elapsedTime = 0
    }

    fun resetTimer() {
        isRunning = false
        elapsedTime = 0
    }
}