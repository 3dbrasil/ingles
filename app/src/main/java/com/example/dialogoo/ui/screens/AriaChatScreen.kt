package com.example.dialogoo.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.dialogoo.ai.ChatMessage
import com.example.dialogoo.model.Dialogue
import com.example.dialogoo.ui.MainViewModel
import com.example.dialogoo.ui.theme.OceanBlue
import com.example.dialogoo.ui.theme.OceanBorderHairline
import com.example.dialogoo.ui.theme.OceanCard
import com.example.dialogoo.ui.theme.OceanNight
import com.example.dialogoo.ui.theme.OceanSurface
import com.example.dialogoo.ui.theme.OceanTeal
import com.example.dialogoo.ui.theme.OceanTealLight
import com.example.dialogoo.ui.theme.TextMuted
import com.example.dialogoo.ui.theme.TextPrimary
import com.example.dialogoo.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AriaChatScreen(
    dialogue: Dialogue?,
    viewModel: MainViewModel,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val messages by viewModel.ariaMessages.collectAsState()
    val isListening by viewModel.speechManager.isListening.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val revealedTranslations = remember { mutableStateListOf<String>() }
    val listState = rememberLazyListState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.speechManager.startListening { spoken ->
                inputText = spoken
            }
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanNight)
            .testTag("aria_chat_screen")
    ) {
        // Chat Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(OceanSurface)
                .border(1.dp, OceanBorderHairline)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Fechar",
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(OceanBlue, OceanTeal))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Aria AI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = dialogue?.title ?: "Tutora Nativa de Conversação",
                    style = MaterialTheme.typography.labelSmall,
                    color = OceanTealLight
                )
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isAria = !msg.isStudent
                val isRevealed = revealedTranslations.contains(msg.id)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isAria) Arrangement.Start else Arrangement.End
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        horizontalAlignment = if (isAria) Alignment.Start else Alignment.End
                    ) {
                        Text(
                            text = if (isAria) "ARIA AI" else "VOCÊ",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isAria) OceanTealLight else OceanBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 2.dp, start = 4.dp, end = 4.dp)
                        )

                        Card(
                            shape = RoundedCornerShape(
                                topStart = 18.dp,
                                topEnd = 18.dp,
                                bottomStart = if (isAria) 4.dp else 18.dp,
                                bottomEnd = if (isAria) 18.dp else 4.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isAria) OceanCard else Color(0xFF1E3A8A)
                            ),
                            modifier = Modifier.border(
                                1.dp,
                                if (isAria) OceanBorderHairline else Color(0x662A7FFF),
                                RoundedCornerShape(
                                    topStart = 18.dp,
                                    topEnd = 18.dp,
                                    bottomStart = if (isAria) 4.dp else 18.dp,
                                    bottomEnd = if (isAria) 18.dp else 4.dp
                                )
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = msg.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Normal,
                                    color = TextPrimary
                                )

                                if (msg.translation.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isRevealed) msg.translation else "Toque para ver a tradução…",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isRevealed) TextSecondary else OceanTealLight,
                                        fontStyle = if (isRevealed) FontStyle.Normal else FontStyle.Italic,
                                        modifier = Modifier
                                            .clickable {
                                                if (isRevealed) revealedTranslations.remove(msg.id)
                                                else revealedTranslations.add(msg.id)
                                            }
                                            .padding(vertical = 2.dp)
                                    )
                                }

                                if (msg.correction.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22F59E0B))
                                            .border(1.dp, Color(0x44F59E0B), RoundedCornerShape(10.dp))
                                            .padding(8.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Lightbulb,
                                                contentDescription = null,
                                                tint = Color(0xFFFBBF24),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = msg.correction,
                                                fontSize = 11.sp,
                                                color = Color(0xFFFDE68A)
                                            )
                                        }
                                    }
                                }

                                if (isAria) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.speakText(msg.text) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VolumeUp,
                                                contentDescription = "Ouvir",
                                                tint = OceanTeal,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Suggested Replies Chips
                        if (msg.suggestedReplies.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                msg.suggestedReplies.forEach { suggestion ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x332A7FFF))
                                            .border(1.dp, Color(0x662A7FFF), RoundedCornerShape(12.dp))
                                            .clickable {
                                                viewModel.sendStudentMessageToAria(suggestion)
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "💬 $suggestion",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(OceanSurface)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (isListening) {
                        viewModel.speechManager.stopListening()
                    } else {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPermission) {
                            viewModel.speechManager.startListening { spoken ->
                                inputText = spoken
                            }
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isListening) Color(0xFFEF4444) else OceanCard)
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Microfone",
                    tint = if (isListening) TextPrimary else OceanTealLight
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Responda em inglês para a Aria...", color = TextMuted, fontSize = 13.sp) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = OceanCard,
                    unfocusedContainerColor = OceanCard,
                    focusedBorderColor = OceanTeal,
                    unfocusedBorderColor = OceanBorderHairline,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("aria_input_field")
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendStudentMessageToAria(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(OceanTeal),
                enabled = inputText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = OceanNight
                )
            }
        }
    }
}
