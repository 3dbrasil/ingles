package com.example.dialogoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.dialogoo.ui.MainViewModel
import com.example.dialogoo.ui.Screen
import com.example.dialogoo.ui.components.BottomNavBar
import com.example.dialogoo.ui.components.CelebrationDialog
import com.example.dialogoo.ui.components.TopBar
import com.example.dialogoo.ui.screens.AriaChatScreen
import com.example.dialogoo.ui.screens.CumulativeArenaScreen
import com.example.dialogoo.ui.screens.DialoguePracticeScreen
import com.example.dialogoo.ui.screens.DuolingoMapScreen
import com.example.dialogoo.ui.screens.PhraseRepetitionScreen
import com.example.dialogoo.ui.screens.SettingsScreen
import com.example.dialogoo.ui.theme.DialogooTheme
import com.example.dialogoo.ui.theme.OceanNight

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DialogooTheme {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val dialogues by viewModel.dialogues.collectAsState()
    val stats by viewModel.userStats.collectAsState()
    val selectedDialogue by viewModel.selectedDialogue.collectAsState()
    val celebrationState by viewModel.celebration.collectAsState()

    // Handle back button on secondary screens
    BackHandler(enabled = currentScreen != Screen.MAP) {
        when (currentScreen) {
            Screen.PRACTICE, Screen.ARIA -> {
                viewModel.speechManager.stopSpeaking()
                viewModel.navigateTo(Screen.MAP)
            }
            else -> viewModel.navigateTo(Screen.MAP)
        }
    }

    Scaffold(
        topBar = {
            if (currentScreen != Screen.ARIA && currentScreen != Screen.PRACTICE) {
                TopBar(
                    stats = stats,
                    onAriaClick = { viewModel.openAriaChat() }
                )
            }
        },
        bottomBar = {
            if (currentScreen != Screen.ARIA && currentScreen != Screen.PRACTICE) {
                BottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        },
        containerColor = OceanNight
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OceanNight)
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.MAP -> {
                    DuolingoMapScreen(
                        dialogues = dialogues,
                        stats = stats,
                        onSelectDialogue = { d -> viewModel.selectDialogueForPractice(d) },
                        onOpenAria = { d -> viewModel.openAriaChat(d) }
                    )
                }

                Screen.PRACTICE -> {
                    val dialogue = selectedDialogue ?: dialogues.firstOrNull()
                    if (dialogue != null) {
                        DialoguePracticeScreen(
                            dialogue = dialogue,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateTo(Screen.MAP) },
                            onOpenAria = { viewModel.openAriaChat(dialogue) }
                        )
                    } else {
                        viewModel.navigateTo(Screen.MAP)
                    }
                }

                Screen.ARENA -> {
                    CumulativeArenaScreen(
                        viewModel = viewModel,
                        stats = stats
                    )
                }

                Screen.REPETITION -> {
                    PhraseRepetitionScreen(
                        viewModel = viewModel
                    )
                }

                Screen.SETTINGS -> {
                    SettingsScreen(
                        viewModel = viewModel,
                        stats = stats
                    )
                }

                Screen.ARIA -> {
                    AriaChatScreen(
                        dialogue = selectedDialogue,
                        viewModel = viewModel,
                        onClose = { viewModel.navigateTo(Screen.MAP) }
                    )
                }
            }

            CelebrationDialog(
                state = celebrationState,
                onDismiss = { viewModel.dismissCelebration() }
            )
        }
    }
}
