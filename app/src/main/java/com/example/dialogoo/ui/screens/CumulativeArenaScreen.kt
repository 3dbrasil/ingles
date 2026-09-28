package com.example.dialogoo.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.dialogoo.R
import com.example.dialogoo.model.UserStats
import com.example.dialogoo.ui.MainViewModel
import com.example.dialogoo.ui.theme.OceanBlue
import com.example.dialogoo.ui.theme.OceanBorderHairline
import com.example.dialogoo.ui.theme.OceanCard
import com.example.dialogoo.ui.theme.OceanEmerald
import com.example.dialogoo.ui.theme.OceanNight
import com.example.dialogoo.ui.theme.OceanSurface
import com.example.dialogoo.ui.theme.OceanTeal
import com.example.dialogoo.ui.theme.OceanTealLight
import com.example.dialogoo.ui.theme.TextMuted
import com.example.dialogoo.ui.theme.TextPrimary
import com.example.dialogoo.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CumulativeArenaScreen(
    viewModel: MainViewModel,
    stats: UserStats
) {
    val context = LocalContext.current
    val challenge by viewModel.currentArenaChallenge.collectAsState()
    val feedback by viewModel.arenaFeedback.collectAsState()
    val isClaimed by viewModel.arenaClaimed.collectAsState()
    val isListening by viewModel.speechManager.isListening.collectAsState()
    val liveTranscript by viewModel.speechManager.speechRecognitionResult.collectAsState()

    var manualInput by remember { mutableStateOf("") }
    var showManualInput by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.speechManager.startListening { spoken ->
                viewModel.evaluateArenaSpeech(spoken)
            }
        } else {
            showManualInput = true
        }
    }

    val currentChallenge = challenge ?: return

    val drawableId = remember(currentChallenge.coverDrawableRes) {
        if (currentChallenge.coverDrawableRes.isNotEmpty()) {
            val res = context.resources.getIdentifier(currentChallenge.coverDrawableRes, "drawable", context.packageName)
            if (res != 0) res else R.drawable.cover_coffee_shop
        } else {
            R.drawable.cover_coffee_shop
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanNight)
            .padding(horizontal = 16.dp)
            .testTag("cumulative_arena_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Arena Header Title & Subtitle
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Arena de Pronúncia",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Desafios situacionais aleatórios com análise fonética",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                IconButton(
                    onClick = {
                        manualInput = ""
                        viewModel.generateNewArenaChallenge()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(OceanCard)
                        .border(1.dp, OceanBorderHairline, CircleShape)
                        .testTag("refresh_challenge_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Novo Desafio",
                        tint = OceanTealLight
                    )
                }
            }
        }

        // Challenge Bento Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OceanCard),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(22.dp))
                    .testTag("arena_challenge_card")
            ) {
                Column {
                    // Cover Header Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {
                        Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = currentChallenge.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color(0x99020617),
                                            OceanCard
                                        )
                                    )
                                )
                        )

                        // Level badge pill
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xCC020617))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${currentChallenge.level.code} · ${currentChallenge.title}",
                                style = MaterialTheme.typography.labelSmall,
                                color = OceanTealLight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = currentChallenge.situation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Target Line Quote Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = OceanSurface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, OceanBorderHairline, RoundedCornerShape(16.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "\"${currentChallenge.targetLine}\"",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        modifier = Modifier.weight(1f)
                                    )

                                    IconButton(
                                        onClick = {
                                            viewModel.speakText(currentChallenge.targetLine)
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0x3300D4A0))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Ouvir frase",
                                            tint = OceanTeal,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = currentChallenge.translation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Interaction controls (Record Mic vs Type)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
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
                                                viewModel.evaluateArenaSpeech(spoken)
                                            }
                                        } else {
                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isListening) Color(0xFFEF4444) else OceanTeal,
                                    contentColor = if (isListening) TextPrimary else OceanNight
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("record_speech_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isListening) "Gravando… (Parar)" else "Falar com Microfone",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            IconButton(
                                onClick = { showManualInput = !showManualInput },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(OceanSurface)
                                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(14.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Keyboard,
                                    contentDescription = "Digitar manualmente",
                                    tint = if (showManualInput) OceanTealLight else TextMuted
                                )
                            }
                        }

                        // Optional Manual Keyboard Input for testing
                        AnimatedVisibility(visible = showManualInput) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                OutlinedTextField(
                                    value = manualInput,
                                    onValueChange = { manualInput = it },
                                    placeholder = { Text("Ou digite a frase para avaliar...", color = TextMuted, fontSize = 13.sp) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = OceanSurface,
                                        unfocusedContainerColor = OceanSurface,
                                        focusedBorderColor = OceanTeal,
                                        unfocusedBorderColor = OceanBorderHairline,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = {
                                        if (manualInput.isNotBlank()) {
                                            IconButton(onClick = {
                                                viewModel.evaluateArenaSpeech(manualInput)
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Send,
                                                    contentDescription = "Enviar",
                                                    tint = OceanTeal
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        // Live transcript indicator
                        if (liveTranscript.isNotBlank() || isListening) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x221E293B))
                                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = if (liveTranscript.isBlank()) "Ouvindo sua pronúncia em inglês..." else "Você disse: \"$liveTranscript\"",
                                    fontSize = 12.sp,
                                    color = if (liveTranscript.isBlank()) OceanTealLight else TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Pronunciation Analysis & Feedback
        feedback?.let { fb ->
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = OceanCard),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (fb.score >= 80) Color(0x6610B981) else Color(0x66F59E0B),
                            RoundedCornerShape(20.dp)
                        )
                        .testTag("pronunciation_feedback_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Avaliação de Precisão Fonética",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = fb.verdict,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }

                            Text(
                                text = "${fb.score}%",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (fb.score >= 80) OceanEmerald else Color(0xFFFBBF24)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Word-by-word highlights
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            fb.words.forEach { w ->
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (w.isCorrect) Color(0x2210B981) else Color(0x22EF4444))
                                        .border(
                                            1.dp,
                                            if (w.isCorrect) Color(0x5510B981) else Color(0x55EF4444),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = w.word,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (w.isCorrect) OceanEmerald else Color(0xFFF87171)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = if (w.isCorrect) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (w.isCorrect) OceanEmerald else Color(0xFFF87171),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        // Claim XP Button
                        if (fb.score >= 50 && !isClaimed) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.claimArenaXp() },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OceanEmerald),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .testTag("claim_xp_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = OceanNight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Reivindicar +20 XP de Precisão",
                                        fontWeight = FontWeight.Bold,
                                        color = OceanNight,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        } else if (isClaimed) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = OceanEmerald,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Pontos de XP reivindicados com sucesso!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OceanEmerald,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
