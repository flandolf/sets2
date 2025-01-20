import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SetCounterViewModel : ViewModel() {
    var counter by mutableIntStateOf(1)
        private set

    fun increment() {
        counter++
    }

    fun reset() {
        counter = 1
    }
}