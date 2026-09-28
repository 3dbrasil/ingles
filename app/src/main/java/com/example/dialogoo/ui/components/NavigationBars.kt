package com.example.dialogoo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dialogoo.R
import com.example.dialogoo.model.UserStats
import com.example.dialogoo.ui.Screen
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

@Composable
fun TopBar(
    stats: UserStats,
    onAriaClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(OceanNight)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Logo & Brand Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("app_brand_header")
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(1.dp, OceanBorderHairline, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_dialogoo_logo),
                    contentDescription = "Dialogoo Logo",
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Dialogoo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Speak Native",
                    style = MaterialTheme.typography.labelSmall,
                    color = OceanTealLight
                )
            }
        }

        // Stats Badges (XP & Streak) & Aria Chat Action
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Streak chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x22F59E0B))
                    .border(1.dp, Color(0x44F59E0B), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "🔥", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${stats.streak}d",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFFBBF24),
                    fontWeight = FontWeight.Bold
                )
            }

            // XP chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x2200D4A0))
                    .border(1.dp, Color(0x4400D4A0), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "XP",
                    tint = OceanTeal,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${stats.xp}",
                    style = MaterialTheme.typography.labelLarge,
                    color = OceanTealLight,
                    fontWeight = FontWeight.Bold
                )
            }

            // Quick Aria Button
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(OceanBlue, OceanTeal)))
                    .clickable { onAriaClick() }
                    .padding(8.dp)
                    .testTag("aria_quick_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Aria Tutor",
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = OceanSurface,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = OceanBorderHairline, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .testTag("bottom_nav_bar")
    ) {
        val items = listOf(
            Triple(Screen.MAP, "Trilha", Icons.Default.Explore),
            Triple(Screen.ARENA, "Arena", Icons.Default.FitnessCenter),
            Triple(Screen.REPETITION, "Repetição", Icons.Default.Psychology),
            Triple(Screen.SETTINGS, "Perfil", Icons.Default.Settings)
        )

        items.forEach { (screen, label, icon) ->
            val selected = currentScreen == screen
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OceanNight,
                    selectedTextColor = OceanTealLight,
                    indicatorColor = OceanTeal,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}
