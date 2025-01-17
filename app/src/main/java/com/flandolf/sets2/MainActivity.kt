package com.flandolf.sets2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flandolf.sets2.ui.theme.Sets2Theme
import com.flandolf.sets2.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themeViewModel: ThemeViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    Sets2Theme (
                        darkTheme = themeViewModel.darkTheme,
                        dynamicColor = themeViewModel.dynamicColor
                    ) {
                        Navigation(themeViewModel)
                    }
                }
            }
        }
    }
}
@Composable
fun Navigation(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController, themeViewModel)
        }
    }
}



@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { MainAppBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val sets = remember { mutableIntStateOf(1) }
            var restDuration by remember { mutableStateOf(3.minutes) }
            CountdownTimer(sets, restDuration) { newDuration ->
                restDuration = newDuration
            }
            SetCounter(sets)
            WorkoutSession()

        }
    }
}

@Composable
fun WorkoutSession() {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    var showWorkoutCompletedDialog by remember { mutableStateOf(false) }
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L) // Wait for 1 second
            elapsedTime += 1
        }
    }
    val formattedTime = remember(elapsedTime) {
        val minutes = elapsedTime / 60
        val seconds = elapsedTime % 60
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
    if (showWorkoutCompletedDialog) {
        WorkoutCompletedDialog(
            onDismissRequest = { showWorkoutCompletedDialog = false },
            onConfirmation = {
                showWorkoutCompletedDialog = false
                elapsedTime = 0
            },
            duration = formattedTime
        )
    }


    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Workout Session Icon")
                Text("Workout Session", style = Typography.titleMedium)
            }
            Text(formattedTime, style = Typography.headlineMedium)
            Row (
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledTonalButton(onClick = {
                    isRunning=true
                }, enabled = !isRunning) {
                    Text("Start Session")
                }
                ElevatedButton(onClick = {
                    isRunning=false
                    showWorkoutCompletedDialog=true
                }, enabled = isRunning) {
                    Text("End Session")
                }
            }
        }
    }
}

@Composable
fun SetCounter(sets: MutableState<Int>) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Set Counter Icon")
                Text("Set Counter", style = Typography.titleMedium)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Spread items to opposite ends
                verticalAlignment = Alignment.CenterVertically // Center items vertically
            ) {
                // Left side: Text displaying sets count
                Text(
                    text = "Sets: ${sets.value}",
                    style = Typography.headlineMedium
                )

                // Right side: Column with buttons
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp),
                ) {
                    ElevatedButton(
                        onClick = { sets.value++ },
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        ) // Flat bottom
                    ) {
                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increment Sets")
                    }

                    ElevatedButton(
                        onClick = { sets.value = 0 },
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        ) // Flat top
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Reset Sets")
                    }
                }
            }
        }


    }
}

@Composable
fun CountdownTimer(sets: MutableState<Int>, restDuration: Duration, onDurationChange: (Duration) -> Unit) {
    var remainingTime by remember { mutableLongStateOf(restDuration.inWholeSeconds) }
    var isRunning by remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }

    // Countdown logic
    LaunchedEffect(isRunning) {
        if (isRunning) {
            sets.value++
            while (remainingTime > 0) {
                delay(1000L) // Wait for 1 second
                remainingTime -= 1
            }
            isRunning = false // Stop the timer when it finishes
        }
    }

    // Format the remaining time as "MM:SS"
    val formattedTime = remember(remainingTime) {
        val minutes = remainingTime / 60
        val seconds = remainingTime % 60
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    if (openAlertDialog.value) {
        ChangeTimerAlertDialog(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = { newDuration ->
                remainingTime = newDuration.inWholeSeconds
                onDurationChange(newDuration) // Update the rest duration
                isRunning = false
                openAlertDialog.value = false
            },
            currentDuration = restDuration
        )
    }

    // UI
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = { openAlertDialog.value = true }) {
            Text(text = formattedTime, style = Typography.headlineLarge)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FilledTonalButton(
                onClick = { isRunning = true },
                enabled = !isRunning && remainingTime > 0
            ) {
                Text(text = if (remainingTime > 0) "Start Rest!" else "Time's Up!")
            }

            ElevatedButton(
                onClick = {
                    remainingTime = restDuration.inWholeSeconds // Reset using the updated rest duration
                    isRunning = false
                }
            ) {
                Text(text = "Reset Timer")
            }
        }
    }
}

@Composable
fun WorkoutCompletedDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    duration: String
) {
    AlertDialog(
        title = {
            Text(text = "Workout Completed!")
        },
        text = {
            Column {
                Text(text = "You've completed a workout session!")
                Text(text = "Duration: $duration")
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("OK")
            }
        },
    )
}

@Composable
fun ChangeTimerAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (Duration) -> Unit, // Pass the new timer value
    currentDuration: Duration, // The current duration to be displayed and edited
) {
    var newDuration by remember { mutableStateOf(currentDuration) } // State to track the new duration
    val formattedTime = remember(newDuration) {
        val minutes = newDuration.inWholeMinutes
        val seconds = newDuration.inWholeSeconds % 60
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
    AlertDialog(
        title = {
            Text(text = "Change Timer")
        },
        text = {
            Column {
                Text(text = "Set New Rest Period:")
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(), // Ensure the row spans the entire width
                    horizontalArrangement = Arrangement.SpaceBetween, // Spread items to opposite ends
                    verticalAlignment = Alignment.CenterVertically // Center items vertically
                ) {
                    Text(text = formattedTime, style = Typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ))
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp),
                    ) {
                        ElevatedButton(
                            onClick = {
                                newDuration += 15.seconds
                            },
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            ) // Flat bottom
                        ) {
                            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increment Rest")
                        }

                        ElevatedButton(
                            onClick = { newDuration -= 15.seconds },
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            ) // Flat top
                        ) {
                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrement Rest")
                        }
                    }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(newDuration) // Pass the new duration to the confirmation handler
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = "Sets 2")
        },
        actions = {
            IconButton(onClick = {
                navController.navigate("settings")
            }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = "Settings")
        },
        actions = {
            IconButton(onClick = {
                navController.navigate("home")
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

