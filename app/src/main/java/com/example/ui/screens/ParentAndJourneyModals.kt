package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.AppLanguage
import com.example.model.CategoryId
import com.example.ui.FunLearningUiState
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MagicPurple
import com.example.ui.theme.MagicPurpleDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SkyCyan
import com.example.ui.theme.SkyCyanDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark

/**
 * Parent Section Dialog:
 * Allows parents and educators to review child progress across the 8 categories,
 * set daily learning time goals, switch language/sound, and reset or restore stars.
 */
@Composable
fun ParentSectionDialog(
    uiState: FunLearningUiState,
    onDismiss: () -> Unit,
    onSetDailyGoal: (Int) -> Unit,
    onResetStars: () -> Unit,
    onRestoreSampleStars: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit
) {
    val isArabic = uiState.isArabic
    val settings = uiState.settings

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("parent_section_dialog"),
            shape = RoundedCornerShape(30.dp),
            color = Color.White,
            border = BorderStroke(3.dp, MagicPurple),
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MagicPurple,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SupervisorAccount,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isArabic) "ركن الأهل والمربين" else "Parents & Educators Corner",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                            Text(
                                text = if (isArabic) "إعدادات التطبيق ومتابعة تقدّم الطفل" else "App settings & child progress overview",
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Bouncy3DIconButton(
                        icon = Icons.Filled.Close,
                        contentDescription = if (isArabic) "إغلاق" else "Close",
                        onClick = onDismiss,
                        containerColor = LuluPink,
                        shadowColor = LuluPinkDark,
                        iconTint = Color.White,
                        testTagName = "close_parent_dialog_button"
                    )
                }

                // Daily Learning Goal Selector
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF5F2FF),
                    border = BorderStroke(1.5.dp, MagicPurple.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = null,
                                tint = MagicPurple
                            )
                            Text(
                                text = if (isArabic) "هدف التعلّم اليومي (دقائق):" else "Daily Learning Goal (Minutes):",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(15, 20, 30, 45).forEach { mins ->
                                val selected = settings.dailyGoalMinutes == mins
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onSetDailyGoal(mins) },
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (selected) MagicPurple else Color.White,
                                    border = BorderStroke(1.5.dp, MagicPurple)
                                ) {
                                    Text(
                                        text = if (isArabic) "$mins د" else "${mins}m",
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        color = if (selected) Color.White else DeepNavyText,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                // Progress Summary across the 8 Categories
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFFFDF5),
                    border = BorderStroke(1.5.dp, SunnyYellowDark.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isArabic) {
                                "مجموع النجوم المكتسبة: ${settings.totalEarnedStars} من ${settings.maxPossibleStars} ⭐"
                            } else {
                                "Total Stars Earned: ${settings.totalEarnedStars} of ${settings.maxPossibleStars} ⭐"
                            },
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        uiState.categories.forEach { cat ->
                            val stars = settings.categoryStars[cat.id] ?: 0
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${cat.numberIndex}. ${cat.title(settings.language)}",
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Row {
                                    repeat(3) { i ->
                                        Icon(
                                            imageVector = if (i < stars) Icons.Filled.Star else Icons.Filled.StarBorder,
                                            contentDescription = null,
                                            tint = if (i < stars) StarGoldBorder else Color(0xFFBDBDBD),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Action Buttons: Reset or Restore Starter Stars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Bouncy3DButton(
                        text = if (isArabic) "تصفير النجوم" else "Reset Stars",
                        onClick = onResetStars,
                        modifier = Modifier.weight(1f),
                        containerColor = SkyCyan,
                        shadowColor = SkyCyanDark,
                        contentColor = Color.White,
                        icon = Icons.Filled.Refresh,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
                    )

                    Bouncy3DButton(
                        text = if (isArabic) "نجوم تجريبية" else "Sample Stars",
                        onClick = onRestoreSampleStars,
                        modifier = Modifier.weight(1f),
                        containerColor = MeadowGreen,
                        shadowColor = MeadowGreenDark,
                        contentColor = Color.White,
                        icon = Icons.Filled.Star,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
                    )
                }

                Bouncy3DButton(
                    text = if (isArabic) "حفظ وإغلاق" else "Save & Close",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MagicPurple,
                    shadowColor = MagicPurpleDark,
                    contentColor = Color.White
                )
            }
        }
    }
}

/**
 * My Learning Journey Modal:
 * Interactive reward map showing Lulu cheering on the child's progress across all 8 worlds.
 */
@Composable
fun LearningJourneyModal(
    uiState: FunLearningUiState,
    onDismiss: () -> Unit,
    onSelectCategory: (CategoryId) -> Unit
) {
    val isArabic = uiState.isArabic
    val settings = uiState.settings

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("learning_journey_dialog"),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            border = BorderStroke(3.dp, SunnyYellow),
            shadowElevation = 14.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(52.dp),
                            shape = CircleShape,
                            border = BorderStroke(2.5.dp, SunnyYellow)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                                contentDescription = "Lulu",
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Text(
                                text = if (isArabic) "رحلتي التعليمية مع لولو" else "My Learning Journey with Lulu",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp
                                )
                            )
                            Text(
                                text = if (isArabic) {
                                    "لقد جمعت ${settings.totalEarnedStars} نجمة ذهبية! 🌟"
                                } else {
                                    "You collected ${settings.totalEarnedStars} golden stars! 🌟"
                                },
                                color = LuluPinkDark,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Bouncy3DIconButton(
                        icon = Icons.Filled.Close,
                        contentDescription = if (isArabic) "إغلاق" else "Close",
                        onClick = onDismiss,
                        containerColor = LuluPink,
                        shadowColor = LuluPinkDark,
                        iconTint = Color.White,
                        testTagName = "close_journey_dialog_button"
                    )
                }

                // List of 8 Learning Worlds with clickable jump action
                uiState.categories.forEach { category ->
                    val earned = settings.categoryStars[category.id] ?: 0
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDismiss()
                                onSelectCategory(category.id)
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = category.cardBgColor,
                        border = BorderStroke(1.5.dp, category.accentColor.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = category.accentColor
                                ) {
                                    Icon(
                                        imageVector = category.icon,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(20.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = category.title(settings.language),
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = category.secondaryTitle(settings.language),
                                        color = SoftSlateText,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                    )
                                }
                            }

                            Row {
                                repeat(category.maxStars) { idx ->
                                    Icon(
                                        imageVector = if (idx < earned) Icons.Filled.Star else Icons.Filled.StarBorder,
                                        contentDescription = null,
                                        tint = if (idx < earned) StarGoldBorder else Color(0xFFBDBDBD),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Bouncy3DButton(
                    text = if (isArabic) "متابعة اللعب! 🚀" else "Keep Playing! 🚀",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = SunnyYellow,
                    shadowColor = SunnyYellowDark,
                    contentColor = DeepNavyText,
                    icon = Icons.Filled.EmojiEvents
                )
            }
        }
    }
}
