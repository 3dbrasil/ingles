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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dialogoo.model.Level
import com.example.dialogoo.model.UserStats
import com.example.dialogoo.ui.MainViewModel
import com.example.dialogoo.ui.theme.OceanBlue
import com.example.dialogoo.ui.theme.OceanBorderHairline
import com.example.dialogoo.ui.theme.OceanCard
import com.example.dialogoo.ui.theme.OceanEmerald
import com.example.dialogoo.ui.theme.OceanNight
import com.example.dialogoo.ui.theme.OceanPurple
import com.example.dialogoo.ui.theme.OceanSurface
import com.example.dialogoo.ui.theme.OceanTeal
import com.example.dialogoo.ui.theme.OceanTealLight
import com.example.dialogoo.ui.theme.TextMuted
import com.example.dialogoo.ui.theme.TextPrimary
import com.example.dialogoo.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    stats: UserStats
) {
    var showImportDialog by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    var importTitle by remember { mutableStateOf("") }
    var importSituation by remember { mutableStateOf("") }
    var importText by remember { mutableStateOf("") }

    var currentRate by remember(stats.speechRate) { mutableFloatStateOf(stats.speechRate) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanNight)
            .padding(horizontal = 16.dp)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Profile Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OceanCard),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(22.dp))
                    .testTag("user_profile_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (stats.isAdmin) OceanPurple else OceanBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (stats.isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                contentDescription = null,
                                tint = TextPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stats.studentName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (stats.isAdmin) "Acesso de Administrador Ativo" else "Modo Estudante Autônomo (Offline)",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (stats.isAdmin) OceanPurple else OceanTealLight
                            )
                        }

                        // Admin Bypass Toggle Switch
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (stats.isAdmin) "Admin" else "Aluno",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Switch(
                                checked = stats.isAdmin,
                                onCheckedChange = { viewModel.toggleAdminRole() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = TextPrimary,
                                    checkedTrackColor = OceanPurple,
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = OceanSurface
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Acesso local é validado instantaneamente ao iniciar o app, garantindo funcionamento ininterrupto mesmo sem conexão à rede.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Stats Overview Bento
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OceanCard),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(22.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Suas Conquistas de Fluência",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Total XP
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(OceanSurface)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(text = "Pontos XP", fontSize = 11.sp, color = TextMuted)
                                Text(
                                    text = "${stats.xp}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanTealLight
                                )
                            }
                        }

                        // Streak
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(OceanSurface)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(text = "Ofensiva", fontSize = 11.sp, color = TextMuted)
                                Text(
                                    text = "${stats.streak} dias",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFBBF24)
                                )
                            }
                        }

                        // Completed
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(OceanSurface)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(text = "Diálogos", fontSize = 11.sp, color = TextMuted)
                                Text(
                                    text = "${stats.completedDialogues.size}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanBlue
                                )
                            }
                        }
                    }

                    // Badges Collection
                    if (stats.badges.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Medalhas Desbloqueadas",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            stats.badges.forEach { badge ->
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x221E293B))
                                        .border(1.dp, OceanBorderHairline, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = badge.iconEmoji, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = badge.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Voice & Pronunciation Speed Controls
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OceanCard),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(22.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = OceanTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Velocidade da Fala Nativa (TTS)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0.6f to "0.6x (Lento)", 0.85f to "1.0x (Padrão)", 1.2f to "1.3x (Rápido)").forEach { (rate, label) ->
                            val isSel = currentRate == rate
                            Button(
                                onClick = {
                                    currentRate = rate
                                    viewModel.setSpeechRate(rate)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSel) OceanTeal else OceanSurface,
                                    contentColor = if (isSel) OceanNight else TextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Import Custom Dialogue (For Admin & Self-Study)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OceanCard),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OceanBorderHairline, RoundedCornerShape(22.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = null,
                                tint = OceanBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Importar Diálogo Customizado",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Button(
                            onClick = { showImportDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = "Abrir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Adicione novos diálogos para treinar situações personalizadas de trabalho, viagens ou exames.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // Danger Zone: Reset
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x22EF4444)),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x44EF4444), RoundedCornerShape(22.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Reiniciar Progresso",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFCA5A5)
                        )
                        Text(
                            text = "Zera todos os pontos XP, ofensivas e fila de revisão.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = { showResetConfirm = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Resetar", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
            }
        }
    }

    // Custom Dialogue Import Modal
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = {
                Text(
                    text = "Importar Diálogo",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = importTitle,
                        onValueChange = { importTitle = it },
                        label = { Text("Título da Lição", color = TextMuted) },
                        placeholder = { Text("Ex: Check-in no Hotel", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = importSituation,
                        onValueChange = { importSituation = it },
                        label = { Text("Situação / Contexto", color = TextMuted) },
                        placeholder = { Text("Ex: Conversando na recepção...", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = importText,
                        onValueChange = { importText = it },
                        label = { Text("Falas (Formato: Personagem | Inglês | Tradução)", color = TextMuted) },
                        placeholder = { Text("Host | Welcome! | Bem-vindo!\nYou | Thank you | Obrigado", color = TextMuted) },
                        minLines = 3,
                        maxLines = 6,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (importText.isNotBlank()) {
                            viewModel.importCustomDialogue(
                                title = importTitle,
                                situation = importSituation,
                                level = Level.A1,
                                linesText = importText
                            )
                            showImportDialog = false
                            importTitle = ""
                            importSituation = ""
                            importText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OceanTeal)
                ) {
                    Text(text = "Salvar e Importar", color = OceanNight, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text(text = "Cancelar", color = TextMuted)
                }
            },
            containerColor = OceanCard
        )
    }

    // Reset Progress Confirmation Dialog
    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = {
                Text(
                    text = "Deseja mesmo reiniciar?",
                    color = Color(0xFFFCA5A5),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Todo o histórico de lições completadas, ofensiva e pontos de XP serão apagados.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetProgress()
                        showResetConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(text = "Sim, Reiniciar", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text(text = "Cancelar", color = TextMuted)
                }
            },
            containerColor = OceanCard
        )
    }
}
