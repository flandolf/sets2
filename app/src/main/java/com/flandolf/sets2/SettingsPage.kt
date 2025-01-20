package com.flandolf.sets2

import ThemeViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val darkTheme by themeViewModel.darkTheme.collectAsState()
    val dynamicColor by themeViewModel.dynamicColor.collectAsState()

    Scaffold(
        topBar = { SettingsAppBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val darkOptions = listOf("Dark", "Light", "System")
                val darkSelectedIndex = when (darkTheme) {
                    "Dark" -> 0
                    "Light" -> 1
                    "System" -> 2
                    else -> 2
                }

                Text("Theme Select")
                SingleChoiceSegmentedButtonRow {
                    darkOptions.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = darkOptions.size
                            ),
                            onClick = {
                                themeViewModel.updateDarkTheme(label)
                            },
                            selected = index == darkSelectedIndex,
                            label = { Text(label) }
                        )
                    }
                }

                val dynamicColorSelectedIndex = if (dynamicColor) 0 else 1
                val dynamicColorOptions = listOf("Dynamic", "Static")

                Text("Dynamic Colour")
                SingleChoiceSegmentedButtonRow {
                    dynamicColorOptions.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = dynamicColorOptions.size
                            ),
                            onClick = {
                                themeViewModel.updateDynamicColor(index == 0)
                            },
                            selected = index == dynamicColorSelectedIndex,
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    }
}