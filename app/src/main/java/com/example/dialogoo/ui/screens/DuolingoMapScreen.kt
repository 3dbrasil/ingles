package com.example.dialogoo.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.dialogoo.R
import com.example.dialogoo.model.Dialogue
import com.example.dialogoo.model.Level
import com.example.dialogoo.model.UserStats
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
fun DuolingoMapScreen(
    dialogues: List<Dialogue>,
    stats: UserStats,
    onSelectDialogue: (Dialogue) -> Unit,
    onOpenAria: (Dialogue) -> Unit
) {
    var selectedLevel by remember { mutableStateOf(Level.A1) }
    val context = LocalContext.current

    val completedCount = stats.completedDialogues.size
    val totalCount = dialogues.size
    val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    val filteredDialogues = dialogues.filter { it.level == selectedLevel }.sortedBy { it.order }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanNight)
            .testTag("duolingo_map_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Cinematic Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.map_banner),
                    contentDescription = "Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x330A0F1A),
                                    Color(0xCC020617),
                                    OceanNight
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x33FFFFFF))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Waves,
                            contentDescription = null,
                            tint = OceanTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SUA JORNADA OCEÂNICA",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Domine inglês,\nconversa por conversa",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        lineHeight = 28.sp
                    )
                }
            }
        }

        // Progress Overview Bento Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OceanCard),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Progresso Geral de Fluência",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "$completedCount de $totalCount diálogos dominados",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                        Text(
                            text = "${(progressFraction * 100).toInt()}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = OceanTealLight
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progressFraction },
                        color = OceanTeal,
                        trackColor = OceanSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
            }
        }

        // CEFR Level Tabs Filter
        item {
            Column(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Level.entries) { lvl ->
                        val isSelected = lvl == selectedLevel
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) OceanTeal else OceanCard)
                                .border(
                                    1.dp,
                                    if (isSelected) OceanTealLight else OceanBorderHairline,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedLevel = lvl }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .testTag("level_tab_${lvl.code}")
                        ) {
                            Text(
                                text = "${lvl.code} · ${lvl.title}",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) OceanNight else TextPrimary
                            )
                        }
                    }
                }

                // Level Tagline Description
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x221E293B))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text(
                            text = selectedLevel.subtitle,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = OceanTealLight
                        )
                        Text(
                            text = selectedLevel.tagline,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Aria Interactive Practice Banner
        item {
            val firstLesson = filteredDialogues.firstOrNull() ?: dialogues.firstOrNull()
            if (firstLesson != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0x332A7FFF)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .border(1.dp, Color(0x442A7FFF), RoundedCornerShape(20.dp))
                        .clickable { onOpenAria(firstLesson) }
                        .testTag("aria_promo_card")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Treinar com a Aria AI",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x3300D4A0))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "BETA",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanTealLight
                                    )
                                }
                            }
                            Text(
                                text = "Prática de fala livre e correções imediatas de pronúncia",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Dialogue Lessons List for current Level
        if (filteredDialogues.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum diálogo disponível para este nível ainda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
        } else {
            items(filteredDialogues) { dialogue ->
                val isCompleted = stats.completedDialogues.contains(dialogue.id)
                val isUnlocked = dialogue.level == Level.A1 || dialogue.order == 1 || isCompleted || stats.completedDialogues.isNotEmpty()

                LessonCard(
                    context = context,
                    dialogue = dialogue,
                    isCompleted = isCompleted,
                    isUnlocked = isUnlocked,
                    onStart = { onSelectDialogue(dialogue) }
                )
            }
        }
    }
}

@Composable
private fun LessonCard(
    context: Context,
    dialogue: Dialogue,
    isCompleted: Boolean,
    isUnlocked: Boolean,
    onStart: () -> Unit
) {
    val drawableId = remember(dialogue.coverDrawableRes) {
        if (dialogue.coverDrawableRes.isNotEmpty()) {
            val res = context.resources.getIdentifier(dialogue.coverDrawableRes, "drawable", context.packageName)
            if (res != 0) res else R.drawable.cover_coffee_shop
        } else {
            R.drawable.cover_coffee_shop
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = OceanCard),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                1.dp,
                if (isCompleted) Color(0x5500D4A0) else OceanBorderHairline,
                RoundedCornerShape(22.dp)
            )
            .clickable(enabled = isUnlocked) { onStart() }
            .testTag("dialogue_card_${dialogue.id}")
    ) {
        Column {
            // Lesson Cover Image with situation overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
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
                                    Color.Transparent,
                                    Color(0x99020617),
                                    OceanCard
                                )
                            )
                        )
                )

                // Top Pills (Level & Completion Status)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xAA020617))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${dialogue.level.code} · Lição #${dialogue.order}",
                            style = MaterialTheme.typography.labelSmall,
                            color = OceanTealLight,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isCompleted) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xCC059669))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Concluído",
                                tint = TextPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Dominado",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (!isUnlocked) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xCC1E293B))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Bloqueado",
                                tint = TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Lesson Details and Action Button
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = dialogue.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = dialogue.situation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💬 ${dialogue.lines.size} falas práticas",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextMuted
                    )

                    Button(
                        onClick = onStart,
                        enabled = isUnlocked,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) OceanSurface else OceanTeal,
                            contentColor = if (isCompleted) OceanTealLight else OceanNight,
                            disabledContainerColor = Color(0x331E293B)
                        ),
                        modifier = Modifier.testTag("start_button_${dialogue.id}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCompleted) "Revisar" else "Praticar",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
