package com.example.dialogoo.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dialogoo.R
import com.example.dialogoo.model.Dialogue
import com.example.dialogoo.model.DialogueLine
import com.example.dialogoo.ui.MainViewModel
import com.example.dialogoo.ui.theme.OceanBlue
import com.example.dialogoo.ui.theme.OceanBorderHairline
import com.example.dialogoo.ui.theme.OceanCard
import com.example.dialogoo.ui.theme.OceanNight
import com.example.dialogoo.ui.theme.OceanPurple
import com.example.dialogoo.ui.theme.OceanSurface
import com.example.dialogoo.ui.theme.OceanTeal
import com.example.dialogoo.ui.theme.OceanTealLight
import com.example.dialogoo.ui.theme.TextMuted
import com.example.dialogoo.ui.theme.TextPrimary
import com.example.dialogoo.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialoguePracticeScreen(
    dialogue: Dialogue,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onOpenAria: () -> Unit
) {
    val context = LocalContext.current
    val speakingId by viewModel.speechManager.isSpeaking.collectAsState()
    val revealedTranslations = remember { mutableStateListOf<String>() }
    var showVocabDrawer by remember { mutableStateOf(false) }
    var isAutoplayActive by remember { mutableStateOf(false) }
    var speechRate by remember { mutableFloatStateOf(0.85f) }
    val learnedIds = remember { mutableStateListOf<String>() }

    // Autoplay coroutine
    LaunchedEffect(isAutoplayActive) {
        if (isAutoplayActive) {
            for (line in dialogue.lines) {
                if (!isAutoplayActive) break
                viewModel.speechManager.speak(line.text, utteranceId = line.id, rate = speechRate)
                // approximate audio duration delay
                val durationMs = (line.text.split(" ").size * 450L / speechRate).toLong().coerceAtLeast(1500L)
                delay(durationMs)
            }
            isAutoplayActive = false
        }
    }

    val drawableId = remember(dialogue.coverDrawableRes) {
        if (dialogue.coverDrawableRes.isNotEmpty()) {
            val res = context.resources.getIdentifier(dialogue.coverDrawableRes, "drawable", context.packageName)
            if (res != 0) res else R.drawable.cover_coffee_shop
        } else {
            R.drawable.cover_coffee_shop
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanNight)
            .testTag("dialogue_practice_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Lesson Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = dialogue.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x66020617),
                                    Color(0xDD020617),
                                    OceanNight
                                )
                            )
                        )
                )

                // Back Button
                IconButton(
                    onClick = {
                        viewModel.speechManager.stopSpeaking()
                        onBack()
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, start = 12.dp)
                        .clip(CircleShape)
                        .background(Color(0xAA020617))
                        .testTag("practice_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextPrimary
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "${dialogue.level.code} · ${dialogue.level.title.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = OceanTealLight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = dialogue.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = dialogue.situation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // Audio & Playback Controls Bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OceanCard),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Line segment indicator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    ) {
                        dialogue.lines.forEach { line ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .background(if (line.isStudent) OceanBlue else OceanTeal)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Autoplay toggle
                        Button(
                            onClick = { isAutoplayActive = !isAutoplayActive },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAutoplayActive) OceanTeal else OceanSurface,
                                contentColor = if (isAutoplayActive) OceanNight else TextPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("autoplay_button")
                        ) {
                            Icon(
                                imageVector = if (isAutoplayActive) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isAutoplayActive) "Parar" else "Auto-play",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Speed selector
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(OceanSurface)
                                .padding(2.dp)
                        ) {
                            listOf(0.6f to "0.6x", 0.85f to "1x", 1.2f to "1.3x").forEach { (rate, label) ->
                                val isSel = speechRate == rate
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) OceanBlue else Color.Transparent)
                                        .clickable {
                                            speechRate = rate
                                            viewModel.setSpeechRate(rate)
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }

                        // Vocab toggle
                        Button(
                            onClick = { showVocabDrawer = !showVocabDrawer },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (showVocabDrawer) OceanTeal else OceanSurface,
                                contentColor = if (showVocabDrawer) OceanNight else TextPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Vocab", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Vocabulary Card Dropdown
        item {
            AnimatedVisibility(visible = showVocabDrawer) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = OceanCard),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .border(1.dp, OceanBorderHairline, RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Vocabulário-Chave da Lição",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = OceanTealLight
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val allVocab = dialogue.lines.flatMap { it.keyVocabulary }.distinctBy { it.word }
                            allVocab.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(OceanSurface)
                                        .border(1.dp, OceanBorderHairline, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.word,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = OceanTealLight
                                    )
                                    Text(
                                        text = " → ${item.translation}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialogue Lines Bubbles
        items(dialogue.lines) { line ->
            val isSpeakingThis = speakingId == line.id
            val isRevealed = revealedTranslations.contains(line.id)
            val isLearned = learnedIds.contains(line.id)

            DialogueBubbleItem(
                line = line,
                isSpeaking = isSpeakingThis,
                isRevealed = isRevealed,
                isLearned = isLearned,
                onPlay = {
                    viewModel.speechManager.speak(line.text, utteranceId = line.id, rate = speechRate)
                },
                onToggleReveal = {
                    if (isRevealed) revealedTranslations.remove(line.id)
                    else revealedTranslations.add(line.id)
                },
                onToggleLearned = {
                    if (isLearned) learnedIds.remove(line.id)
                    else {
                        learnedIds.add(line.id)
                        viewModel.markPhraseLearned(line.id, true)
                    }
                }
            )
        }

        // Bottom CTAs: Aria AI & Complete Lesson
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Aria Free Chat Prompt
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0x332A7FFF)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0x552A7FFF), RoundedCornerShape(20.dp))
                        .clickable { onOpenAria() }
                        .testTag("aria_practice_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(OceanBlue, OceanTeal))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Aria",
                                tint = TextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Praticar com a Aria AI",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Conversa livre sobre esta lição com retorno imediato",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Complete Lesson Button
                Button(
                    onClick = {
                        viewModel.completeDialoguePractice(dialogue.id, scoreAvg = 90)
                    },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanTeal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("finish_lesson_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Concluir Lição e Ganhar XP",
                            fontWeight = FontWeight.Bold,
                            color = OceanNight,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = OceanNight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogueBubbleItem(
    line: DialogueLine,
    isSpeaking: Boolean,
    isRevealed: Boolean,
    isLearned: Boolean,
    onPlay: () -> Unit,
    onToggleReveal: () -> Unit,
    onToggleLearned: () -> Unit
) {
    val isStu = line.isStudent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = if (isStu) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isStu) {
            SpeakerAvatar(speaker = line.speaker, isSpeaking = isSpeaking)
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Bubble Content
        Column(
            horizontalAlignment = if (isStu) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            // Speaker label
            Text(
                text = if (isStu) "🎙️ VOCÊ" else line.speaker.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = if (isStu) OceanTealLight else TextSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 2.dp, start = 4.dp, end = 4.dp)
            )

            Card(
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (isStu) 18.dp else 4.dp,
                    bottomEnd = if (isStu) 4.dp else 18.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isStu) Color(0xFF1E3A8A) else OceanCard
                ),
                modifier = Modifier
                    .border(
                        1.dp,
                        if (isSpeaking) OceanTeal else OceanBorderHairline,
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isStu) 18.dp else 4.dp,
                            bottomEnd = if (isStu) 4.dp else 18.dp
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // English original text
                    Text(
                        text = line.text,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Portuguese translation (Tap to reveal or toggle)
                    Text(
                        text = if (isRevealed) line.translation else "Toque para ver a tradução…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isRevealed) TextSecondary else OceanTealLight,
                        fontStyle = if (isRevealed) FontStyle.Normal else FontStyle.Italic,
                        modifier = Modifier
                            .clickable { onToggleReveal() }
                            .padding(vertical = 2.dp)
                    )

                    // Pronunciation Phonetics Guide
                    if (line.pronunciationGuide.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🔊 ${line.pronunciationGuide}",
                            fontSize = 11.sp,
                            color = Color(0x99FFFFFF),
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Line Actions: Play TTS & Mark as Learned
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onToggleLearned,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isLearned) Icons.Default.CheckCircle else Icons.Default.Check,
                                contentDescription = "Marcar como dominado",
                                tint = if (isLearned) OceanTealLight else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = onPlay,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isSpeaking) OceanTeal else OceanSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Ouvir frase",
                                tint = if (isSpeaking) OceanNight else TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        if (isStu) {
            Spacer(modifier = Modifier.width(8.dp))
            SpeakerAvatar(speaker = "You", isSpeaking = isSpeaking)
        }
    }
}

@Composable
private fun SpeakerAvatar(speaker: String, isSpeaking: Boolean) {
    val initial = speaker.firstOrNull()?.uppercaseChar()?.toString() ?: "U"
    val avatarColor = when {
        speaker.contains("you", ignoreCase = true) -> OceanBlue
        speaker.contains("barista", ignoreCase = true) -> OceanTeal
        speaker.contains("waiter", ignoreCase = true) -> OceanPurple
        speaker.contains("doctor", ignoreCase = true) -> OceanTeal
        speaker.contains("manager", ignoreCase = true) -> OceanTeal
        else -> Color(0xFF6366F1)
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(avatarColor)
            .border(
                2.dp,
                if (isSpeaking) TextPrimary else Color.Transparent,
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}
