package com.example.dialogoo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.RotateRight
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dialogoo.model.SrsLevel
import com.example.dialogoo.model.SrsPhrase
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

@Composable
fun PhraseRepetitionScreen(
    viewModel: MainViewModel
) {
    val phrases by viewModel.srsPhrases.collectAsState()
    var selectedFilter by remember { mutableStateOf<SrsLevel?>(null) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isTranslationRevealed by remember { mutableStateOf(false) }

    val filteredPhrases = remember(phrases, selectedFilter) {
        if (selectedFilter == null) phrases
        else phrases.filter { it.srsLevel == selectedFilter }
    }

    val currentPhrase = filteredPhrases.getOrNull(currentIndex)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanNight)
            .padding(horizontal = 16.dp)
            .testTag("phrase_repetition_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Title Header
        item {
            Column {
                Text(
                    text = "Repetição Espaçada (SRS)",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Fixe vocabulário e padrões gramaticais na memória de longo prazo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Difficulty Filters Bar
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAll = selectedFilter == null
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isAll) OceanTeal else OceanCard)
                            .border(1.dp, if (isAll) OceanTealLight else OceanBorderHairline, RoundedCornerShape(12.dp))
                            .clickable {
                                selectedFilter = null
                                currentIndex = 0
                                isTranslationRevealed = false
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Todas (${phrases.size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAll) OceanNight else TextPrimary
                        )
                    }
                }

                items(SrsLevel.entries) { lvl ->
                    val isSel = selectedFilter == lvl
                    val count = phrases.count { it.srsLevel == lvl }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSel) Color(lvl.colorHex) else OceanCard)
                            .border(1.dp, if (isSel) Color.White else OceanBorderHairline, RoundedCornerShape(12.dp))
                            .clickable {
                                selectedFilter = lvl
                                currentIndex = 0
                                isTranslationRevealed = false
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${lvl.label} ($count)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) Color.White else TextPrimary
                        )
                    }
                }
            }
        }

        // Main Flashcard Container
        if (currentPhrase == null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = OceanCard),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                        .border(1.dp, OceanBorderHairline, RoundedCornerShape(22.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🎉", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Fila de Revisão em Dia!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Você concluiu todas as frases desta categoria.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = OceanCard),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, OceanBorderHairline, RoundedCornerShape(24.dp))
                        .testTag("srs_flashcard")
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Header info: Lesson title + Index counter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${currentPhrase.lessonTitle} · ${currentPhrase.level.code}",
                                style = MaterialTheme.typography.labelSmall,
                                color = OceanTealLight,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Frase ${currentIndex + 1} de ${filteredPhrases.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // English Text
                        Text(
                            text = currentPhrase.text,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            lineHeight = 30.sp
                        )

                        // Pronunciation Phonetics Guide
                        if (currentPhrase.pronunciationGuide.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "🔊 ${currentPhrase.pronunciationGuide}",
                                fontSize = 12.sp,
                                color = Color(0xAAFFFFFF)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Audio button row
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    viewModel.speakText(currentPhrase.text)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OceanSurface),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = OceanTeal,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Ouvir Pronúncia",
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Flip / Reveal Button
                            Button(
                                onClick = { isTranslationRevealed = !isTranslationRevealed },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isTranslationRevealed) OceanTeal else Color(0x222A7FFF)
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RotateRight,
                                    contentDescription = null,
                                    tint = if (isTranslationRevealed) OceanNight else OceanTealLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isTranslationRevealed) "Ocultar" else "Ver Tradução",
                                    fontSize = 12.sp,
                                    color = if (isTranslationRevealed) OceanNight else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Portuguese Translation (Animated reveal)
                        AnimatedVisibility(visible = isTranslationRevealed) {
                            Column(modifier = Modifier.padding(top = 16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(OceanSurface)
                                        .border(1.dp, OceanBorderHairline, RoundedCornerShape(14.dp))
                                        .padding(14.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "Tradução em Português",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = OceanTealLight
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = currentPhrase.translation.ifBlank { "Sem tradução disponível" },
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextPrimary,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Self-Grading SRS Buttons: Fácil, Médio, Difícil
                        Text(
                            text = "Como foi lembrar desta frase?",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Hard (Difícil)
                            Button(
                                onClick = {
                                    viewModel.updateSrsGrade(currentPhrase.id, SrsLevel.HARD, false)
                                    advancePhrase(filteredPhrases.size) { currentIndex = it; isTranslationRevealed = false }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33EF4444)),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("srs_hard_button")
                            ) {
                                Text(text = "Difícil", color = Color(0xFFF87171), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            // Medium (Médio)
                            Button(
                                onClick = {
                                    viewModel.updateSrsGrade(currentPhrase.id, SrsLevel.MEDIUM, true)
                                    advancePhrase(filteredPhrases.size) { currentIndex = it; isTranslationRevealed = false }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33F59E0B)),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("srs_medium_button")
                            ) {
                                Text(text = "Médio", color = Color(0xFFFBBF24), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            // Easy (Fácil)
                            Button(
                                onClick = {
                                    viewModel.updateSrsGrade(currentPhrase.id, SrsLevel.EASY, true)
                                    advancePhrase(filteredPhrases.size) { currentIndex = it; isTranslationRevealed = false }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x3310B981)),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("srs_easy_button")
                            ) {
                                Text(text = "Fácil", color = OceanEmerald, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun advancePhrase(total: Int, update: (Int) -> Unit) {
    if (total <= 1) {
        update(0)
    } else {
        update((0 until total).random())
    }
}
