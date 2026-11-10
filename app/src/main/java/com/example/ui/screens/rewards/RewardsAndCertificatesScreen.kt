package com.example.ui.screens.rewards

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.AchievementBadge
import com.example.model.AppLanguage
import com.example.model.CategoryId
import com.example.model.EducationalLevel
import com.example.model.RewardsProgressCalculator
import com.example.model.RewardsTab
import com.example.model.SurpriseRewardItem
import com.example.model.WorldCertificateType
import com.example.ui.FunLearningUiState
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MagicPurple
import com.example.ui.theme.MagicPurpleDark
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.MintGreen
import com.example.ui.theme.SkyCyan
import com.example.ui.theme.SkyCyanDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark
import com.example.util.PdfAndPrintHelper
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun RewardsAndCertificatesScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onSelectCategory: (CategoryId) -> Unit,
    onOpenSurpriseBox: (SurpriseRewardItem) -> Unit,
    onEquipAccessory: (String) -> Unit,
    onUpdateChildName: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onNavigateHome)

    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR
    val layoutDirection = if (isAr) LayoutDirection.Rtl else LayoutDirection.Ltr

    var selectedTab by rememberSaveable { mutableStateOf(RewardsTab.STAR_BOARD) }
    var selectedCertificate by rememberSaveable { mutableStateOf(WorldCertificateType.NUMBERS_CERT) }
    var exportStatusBanner by remember { mutableStateOf<String?>(null) }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("rewards_world_screen"),
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = Color.Transparent
        ) { innerPadding ->
            PlayfulSkyBackground {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Top Navigation Header
                    item {
                        RewardsTopBar(
                            language = lang,
                            soundEnabled = settings.soundEnabled,
                            totalStars = settings.totalEarnedStars,
                            onNavigateHome = onNavigateHome,
                            onSelectLanguage = onSelectLanguage,
                            onToggleSound = onToggleSound,
                            modifier = Modifier.widthIn(max = 920.dp)
                        )
                    }

                    // 2. Lulu Level & Accessory Hero Card
                    item {
                        LuluRewardsHeroBanner(
                            uiState = uiState,
                            onSpeakLulu = { text -> onSpeakLulu(text, lang) },
                            modifier = Modifier.widthIn(max = 920.dp)
                        )
                    }

                    // 3. 4-Tab Selector (Star Board, Badges, Surprise Box, Certificates)
                    item {
                        RewardsTabSelectorRow(
                            selectedTab = selectedTab,
                            language = lang,
                            unopenedSurpriseCount = RewardsProgressCalculator.availableSurpriseBoxesToOpen(settings).size,
                            onSelectTab = { tab ->
                                selectedTab = tab
                                exportStatusBanner = null
                            },
                            modifier = Modifier.widthIn(max = 920.dp)
                        )
                    }

                    // 4. Active Tab Content
                    when (selectedTab) {
                        RewardsTab.STAR_BOARD -> {
                            item {
                                StarBoardAndLevelsSection(
                                    uiState = uiState,
                                    onSelectCategory = onSelectCategory,
                                    onSpeakLulu = { text -> onSpeakLulu(text, lang) },
                                    modifier = Modifier.widthIn(max = 920.dp)
                                )
                            }
                        }

                        RewardsTab.BADGES -> {
                            item {
                                AchievementsAndBadgesSection(
                                    uiState = uiState,
                                    onSpeakLulu = { text -> onSpeakLulu(text, lang) },
                                    modifier = Modifier.widthIn(max = 920.dp)
                                )
                            }
                        }

                        RewardsTab.SURPRISE_BOX -> {
                            item {
                                SurpriseBoxSection(
                                    uiState = uiState,
                                    onOpenSurpriseBox = onOpenSurpriseBox,
                                    onEquipAccessory = onEquipAccessory,
                                    onSpeakLulu = { text -> onSpeakLulu(text, lang) },
                                    modifier = Modifier.widthIn(max = 920.dp)
                                )
                            }
                        }

                        RewardsTab.CERTIFICATES -> {
                            item {
                                EducationalCertificatesSection(
                                    uiState = uiState,
                                    selectedCertificate = selectedCertificate,
                                    onSelectCertificate = {
                                        selectedCertificate = it
                                        exportStatusBanner = null
                                    },
                                    exportStatusBanner = exportStatusBanner,
                                    onUpdateStatusBanner = { exportStatusBanner = it },
                                    onUpdateChildName = onUpdateChildName,
                                    onSpeakLulu = { text -> onSpeakLulu(text, lang) },
                                    modifier = Modifier.widthIn(max = 920.dp)
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RewardsTopBar(
    language: AppLanguage,
    soundEnabled: Boolean,
    totalStars: Int,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.AR
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        border = BorderStroke(3.dp, StarGoldBorder),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Bouncy3DIconButton(
                    icon = Icons.Filled.Home,
                    contentDescription = if (isAr) "الرئيسية" else "Home",
                    onClick = onNavigateHome,
                    containerColor = SkyCyan,
                    shadowColor = SkyCyanDark,
                    iconTint = Color.White,
                    badgeLabel = if (isAr) "الرئيسية" else "Home",
                    testTagName = "rewards_home_button"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = StarGoldBorder,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = if (isAr) "عالم المكافآت والشهادات" else "Rewards & Certificates",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isAr) 21.sp else 18.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF8E1),
                    border = BorderStroke(2.dp, StarGoldBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = StarGoldBorder,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "$totalStars",
                            color = DeepNavyText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LanguageSelectorSwitch(
                    currentLanguage = language,
                    onSelectLanguage = onSelectLanguage
                )

                Bouncy3DIconButton(
                    icon = if (soundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                    contentDescription = if (soundEnabled) "Sound On" else "Sound Off",
                    onClick = onToggleSound,
                    containerColor = if (soundEnabled) Color(0xFF2E7D32) else Color(0xFFCFD8DC),
                    shadowColor = if (soundEnabled) Color(0xFF1B5E20) else Color(0xFF78909C),
                    iconTint = if (soundEnabled) Color.White else DeepNavyText,
                    testTagName = "rewards_sound_toggle"
                )
            }
        }
    }
}

@Composable
private fun LuluRewardsHeroBanner(
    uiState: FunLearningUiState,
    onSpeakLulu: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR
    val currentLevel = RewardsProgressCalculator.computeCurrentLevel(settings)
    val equippedAccessory = SurpriseRewardItem.entries.firstOrNull { it.id == settings.equippedLuluAccessory }

    val infiniteTransition = rememberInfiniteTransition(label = "lulu_star_pulse")
    val starScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_pulse"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSpeakLulu(currentLevel.luluMessage(lang)) },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(3.dp, currentLevel.badgeColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFFF8E1),
                            Color(0xFFFFF0F6),
                            Color(0xFFE1F5FE)
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Lulu Portrait + Equipped Accessory Overlay Badge
            Box(contentAlignment = Alignment.TopEnd) {
                Surface(
                    modifier = Modifier.size(96.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFE1F5FE),
                    border = BorderStroke(3.dp, SunnyYellow),
                    shadowElevation = 6.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                        contentDescription = if (isAr) "لولو" else "Lulu",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                if (equippedAccessory != null) {
                    Surface(
                        modifier = Modifier
                            .offset(x = 6.dp, y = (-6).dp)
                            .scale(starScale),
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(2.dp, equippedAccessory.accentColor),
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = equippedAccessory.emoji,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = currentLevel.badgeColor
                    ) {
                        Text(
                            text = "${currentLevel.emoji} ${
                                if (isAr) "المستوى ${currentLevel.levelNumber}: ${currentLevel.titleAr}"
                                else "Level ${currentLevel.levelNumber}: ${currentLevel.titleEn}"
                            }",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isAr) 15.sp else 13.sp
                            )
                        )
                    }
                }

                Text(
                    text = currentLevel.luluMessage(lang),
                    color = DeepNavyText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isAr) 16.sp else 15.sp,
                        lineHeight = 22.sp
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        tint = Color(0xFFC2185B),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isAr) "اضغط لسماع تشجيع لولو بصوتها!" else "Tap to hear Lulu's encouragement!",
                        color = Color(0xFFC2185B),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardsTabSelectorRow(
    selectedTab: RewardsTab,
    language: AppLanguage,
    unopenedSurpriseCount: Int,
    onSelectTab: (RewardsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        items(RewardsTab.entries) { tab ->
            val isSelected = tab == selectedTab
            Surface(
                modifier = Modifier
                    .testTag("rewards_tab_${tab.name.lowercase()}")
                    .clickable { onSelectTab(tab) },
                shape = RoundedCornerShape(22.dp),
                color = if (isSelected) MagicPurpleDark else Color.White,
                border = BorderStroke(
                    width = 2.5.dp,
                    color = if (isSelected) SunnyYellow else Color(0xFFB39DDB)
                ),
                shadowElevation = if (isSelected) 6.dp else 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = tab.emoji, fontSize = 20.sp)
                    Text(
                        text = tab.title(language),
                        color = if (isSelected) Color.White else DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (language == AppLanguage.AR) 17.sp else 15.sp
                        )
                    )
                    if (tab == RewardsTab.SURPRISE_BOX && unopenedSurpriseCount > 0) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD81B60)
                        ) {
                            Text(
                                text = "$unopenedSurpriseCount",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// TAB 1: STAR BOARD & EDUCATIONAL LEVELS
// =====================================================================

@Composable
private fun StarBoardAndLevelsSection(
    uiState: FunLearningUiState,
    onSelectCategory: (CategoryId) -> Unit,
    onSpeakLulu: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR

    val totalStars = settings.totalEarnedStars
    val completedActivities = RewardsProgressCalculator.totalCompletedActivitiesCount(settings)
    val maxActivities = RewardsProgressCalculator.maxActivitiesCount()
    val exploredWorlds = RewardsProgressCalculator.exploredWorldsCount(settings)
    val unlockedBadgesCount = RewardsProgressCalculator.unlockedBadges(settings).size
    val currentLevel = RewardsProgressCalculator.computeCurrentLevel(settings)
    val (nextLevel, nextLevelFraction) = RewardsProgressCalculator.computeNextLevelProgress(settings)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. 4 Key Metric Cards Grid (Stars, Completed Activities, Explored Worlds, Unlocked Badges)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(3.dp, StarGoldBorder),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (isAr) "⭐ لوحة النجوم والإنجازات" else "⭐ Star Board & Milestones",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isAr) 22.sp else 20.sp
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StarStatCard(
                        emoji = "🌟",
                        valueText = "$totalStars",
                        labelText = if (isAr) "إجمالي النجوم" else "Total Stars",
                        bgColor = Color(0xFFFFF8E1),
                        borderColor = StarGoldBorder,
                        modifier = Modifier.weight(1f)
                    )
                    StarStatCard(
                        emoji = "✅",
                        valueText = "$completedActivities / $maxActivities",
                        labelText = if (isAr) "الأنشطة المكتملة" else "Activities Done",
                        bgColor = Color(0xFFE8F5E9),
                        borderColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StarStatCard(
                        emoji = "🌍",
                        valueText = "$exploredWorlds / 8",
                        labelText = if (isAr) "العوالم المستكشفة" else "Worlds Explored",
                        bgColor = Color(0xFFE1F5FE),
                        borderColor = Color(0xFF0288D1),
                        modifier = Modifier.weight(1f)
                    )
                    StarStatCard(
                        emoji = "🏅",
                        valueText = "$unlockedBadgesCount / ${AchievementBadge.entries.size}",
                        labelText = if (isAr) "الشارات المفتوحة" else "Unlocked Badges",
                        bgColor = Color(0xFFF3E5F5),
                        borderColor = Color(0xFF8E24AA),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 2. Progressive Educational Levels Ladder (5 Levels)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(3.dp, Color(0xFF81D4FA)),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isAr) "🚀 المستويات التعليمية الخمسة" else "🚀 5 Educational Levels",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isAr) 21.sp else 19.sp
                    )
                )

                if (nextLevel != null) {
                    val animatedProg by animateFloatAsState(
                        targetValue = nextLevelFraction,
                        animationSpec = tween(600),
                        label = "next_lvl_prog"
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = if (isAr) {
                                "التقدم نحو المستوى التالي: ${nextLevel.titleAr} (${(nextLevelFraction * 100).toInt()}%)"
                            } else {
                                "Progress to next level: ${nextLevel.titleEn} (${(nextLevelFraction * 100).toInt()}%)"
                            },
                            color = SoftSlateText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        LinearProgressIndicator(
                            progress = { animatedProg },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = nextLevel.badgeColor,
                            trackColor = Color(0xFFE0E0E0)
                        )
                    }
                }

                EducationalLevel.entries.forEach { level ->
                    val isReached = level.levelNumber <= currentLevel.levelNumber
                    val isCurrent = level == currentLevel
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(level.luluMessage(lang)) },
                        shape = RoundedCornerShape(20.dp),
                        color = when {
                            isCurrent -> level.badgeColor.copy(alpha = 0.14f)
                            isReached -> Color(0xFFF1F8E9)
                            else -> Color(0xFFF5F5F5)
                        },
                        border = BorderStroke(
                            width = if (isCurrent) 2.5.dp else 1.5.dp,
                            color = when {
                                isCurrent -> level.badgeColor
                                isReached -> Color(0xFF81C784)
                                else -> Color(0xFFBDBDBD)
                            }
                        )
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
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isReached) level.badgeColor else Color(0xFFBDBDBD),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = level.emoji, fontSize = 22.sp)
                                    }
                                }
                                Column {
                                    Text(
                                        text = "${level.levelNumber}. ${level.title(lang)}",
                                        color = if (isReached) DeepNavyText else Color(0xFF757575),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = if (isAr) 18.sp else 16.sp
                                        )
                                    )
                                    Text(
                                        text = level.subtitle(lang),
                                        color = SoftSlateText,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            if (isCurrent) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = level.badgeColor
                                ) {
                                    Text(
                                        text = if (isAr) "مستواك الحالي" else "Current",
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            } else if (isReached) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(26.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Stars Per Educational World (Interactive Jump Cards)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(3.dp, Color(0xFFFF80AB)),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isAr) "🌟 نجومي في العوالم الثمانية (اضغط للانتقال)" else "🌟 My Stars Across All 8 Worlds (Tap to Play)",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isAr) 19.sp else 17.sp
                    )
                )

                uiState.categories.forEach { category ->
                    val earned = settings.categoryStars[category.id] ?: 1
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectCategory(category.id) },
                        shape = RoundedCornerShape(18.dp),
                        color = category.cardBgColor.copy(alpha = 0.65f),
                        border = BorderStroke(1.5.dp, category.accentColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = category.title(lang),
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isAr) 17.sp else 15.sp
                                )
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                repeat(category.maxStars) { idx ->
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = if (idx < earned) StarGoldBorder else Color(0xFFB0BEC5),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StarStatCard(
    emoji: String,
    valueText: String,
    labelText: String,
    bgColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        color = bgColor,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Text(
                text = valueText,
                color = DeepNavyText,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
            Text(
                text = labelText,
                color = SoftSlateText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

// =====================================================================
// TAB 2: 10 ACHIEVEMENTS & BADGES
// =====================================================================

@Composable
private fun AchievementsAndBadgesSection(
    uiState: FunLearningUiState,
    onSpeakLulu: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR
    val unlockedList = RewardsProgressCalculator.unlockedBadges(settings)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            color = Color.White,
            border = BorderStroke(3.dp, Color(0xFFBA68C8)),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (isAr) {
                        "🏅 الإنجازات والشارات (${unlockedList.size} من ${AchievementBadge.entries.size} مفتوحة)"
                    } else {
                        "🏅 Achievements & Badges (${unlockedList.size} of ${AchievementBadge.entries.size} Unlocked)"
                    },
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isAr) 21.sp else 19.sp
                    )
                )
                Text(
                    text = if (isAr) {
                        "اضغط على أي شارة لسماع تشجيع لولو ومعرفة كيفية الفوز بها!"
                    } else {
                        "Tap any badge to hear Lulu's message and see how to unlock it!"
                    },
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // 2-Column Responsive Grid of all 10 Badges
        AchievementBadge.entries.chunked(2).forEach { rowBadges ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowBadges.forEach { badge ->
                    val unlocked = badge.isUnlocked(settings)
                    BadgeCardItem(
                        badge = badge,
                        unlocked = unlocked,
                        language = lang,
                        onClick = {
                            val speech = if (unlocked) {
                                badge.luluCelebrate(lang)
                            } else {
                                if (isAr) "${badge.titleAr}: ${badge.requirementAr}"
                                else "${badge.titleEn}: ${badge.requirementEn}"
                            }
                            onSpeakLulu(speech)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowBadges.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BadgeCardItem(
    badge: AchievementBadge,
    unlocked: Boolean,
    language: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.AR

    Surface(
        modifier = modifier
            .testTag("badge_card_${badge.id}")
            .alpha(if (unlocked) 1f else 0.62f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(26.dp),
        color = if (unlocked) badge.cardBgColor else Color(0xFFECEFF1),
        border = BorderStroke(
            width = if (unlocked) 3.dp else 2.dp,
            color = if (unlocked) badge.accentColor else Color(0xFFB0BEC5)
        ),
        shadowElevation = if (unlocked) 7.dp else 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = CircleShape,
                    color = if (unlocked) Color.White else Color(0xFFCFD8DC),
                    border = BorderStroke(2.5.dp, if (unlocked) StarGoldBorder else Color(0xFF90A4AE)),
                    modifier = Modifier.size(68.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = badge.emoji,
                            fontSize = 32.sp
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = if (unlocked) Color(0xFF2E7D32) else Color(0xFF78909C),
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (unlocked) Icons.Filled.CheckCircle else Icons.Filled.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Text(
                text = badge.title(language),
                color = DeepNavyText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isAr) 18.sp else 16.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (unlocked) badge.description(language) else badge.requirement(language),
                color = if (unlocked) badge.accentColor else SoftSlateText,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isAr) 13.sp else 12.sp,
                    lineHeight = 17.sp
                ),
                textAlign = TextAlign.Center,
                minLines = 2,
                maxLines = 3
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (unlocked) badge.accentColor else Color(0xFF90A4AE)
            ) {
                Text(
                    text = if (unlocked) {
                        if (isAr) "مفتوحة ★" else "Unlocked ★"
                    } else {
                        if (isAr) "قيد التقدم 🔒" else "Locked 🔒"
                    },
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// =====================================================================
// TAB 3: SURPRISE BOX (صندوق المفاجآت)
// =====================================================================

@Composable
private fun SurpriseBoxSection(
    uiState: FunLearningUiState,
    onOpenSurpriseBox: (SurpriseRewardItem) -> Unit,
    onEquipAccessory: (String) -> Unit,
    onSpeakLulu: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR
    val completedCount = RewardsProgressCalculator.totalCompletedActivitiesCount(settings)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(3.dp, Color(0xFFF57C00)),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Redeem,
                        contentDescription = null,
                        tint = Color(0xFFF57C00),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = if (isAr) "🎁 صندوق المفاجآت والهدايا" else "🎁 Surprise Box & Gifts",
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isAr) 21.sp else 19.sp
                        )
                    )
                }
                Text(
                    text = if (isAr) {
                        "أكملت $completedCount أنشطة تعليمية! افتح الصناديق المضيئة لربح نجوم إضافية وإكسسوارات مرحة للولو!"
                    } else {
                        "You completed $completedCount learning activities! Open unlocked chests for bonus stars and Lulu accessories!"
                    },
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        SurpriseRewardItem.entries.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    val isOpened = item.id in settings.openedSurpriseIds
                    val canOpen = completedCount >= item.requiredCompletedActivities && !isOpened
                    val isEquipped = settings.equippedLuluAccessory == item.id

                    SurpriseChestCard(
                        item = item,
                        isOpened = isOpened,
                        canOpen = canOpen,
                        isEquipped = isEquipped,
                        completedCount = completedCount,
                        language = lang,
                        onOpenChest = { onOpenSurpriseBox(item) },
                        onEquip = {
                            val next = if (isEquipped) "" else item.id
                            onEquipAccessory(next)
                            val msg = if (isAr) "يا سلام! لولو ترتدي الآن ${item.titleAr}!" else "Yay! Lulu is now wearing ${item.titleEn}!"
                            onSpeakLulu(msg)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SurpriseChestCard(
    item: SurpriseRewardItem,
    isOpened: Boolean,
    canOpen: Boolean,
    isEquipped: Boolean,
    completedCount: Int,
    language: AppLanguage,
    onOpenChest: () -> Unit,
    onEquip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.AR

    Surface(
        modifier = modifier.testTag("surprise_box_${item.id}"),
        shape = RoundedCornerShape(26.dp),
        color = when {
            isOpened -> Color(0xFFFFFDE7)
            canOpen -> Color(0xFFFFF0F6)
            else -> Color(0xFFECEFF1)
        },
        border = BorderStroke(
            width = 3.dp,
            color = when {
                isOpened -> item.accentColor
                canOpen -> Color(0xFFD81B60)
                else -> Color(0xFFB0BEC5)
            }
        ),
        shadowElevation = if (isOpened || canOpen) 6.dp else 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(2.dp, item.accentColor),
                modifier = Modifier.size(66.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isOpened) item.emoji else if (canOpen) "🎁" else "🔒",
                        fontSize = 32.sp
                    )
                }
            }

            Text(
                text = if (isOpened) item.title(language) else if (isAr) "صندوق مفاجأة سحري" else "Magic Surprise Chest",
                color = DeepNavyText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isAr) 17.sp else 15.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (isOpened) {
                    item.subtitle(language)
                } else {
                    if (isAr) "يتطلب إكمال ${item.requiredCompletedActivities} أنشطة ($completedCount/${item.requiredCompletedActivities})"
                    else "Requires ${item.requiredCompletedActivities} activities ($completedCount/${item.requiredCompletedActivities})"
                },
                color = SoftSlateText,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center,
                minLines = 2
            )

            when {
                canOpen -> {
                    Bouncy3DButton(
                        text = if (isAr) "افتح الصندوق! 🎁" else "Open Box! 🎁",
                        onClick = onOpenChest,
                        containerColor = Color(0xFFD81B60),
                        shadowColor = Color(0xFF880E4F),
                        fontSize = 14.sp,
                        cornerRadius = 18.dp,
                        lipHeight = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                isOpened && item.isLuluAccessory -> {
                    Bouncy3DButton(
                        text = if (isEquipped) {
                            if (isAr) "ترتديه لولو ✓" else "Equipped ✓"
                        } else {
                            if (isAr) "إلباس لولو ✨" else "Equip on Lulu ✨"
                        },
                        onClick = onEquip,
                        containerColor = if (isEquipped) MeadowGreenDark else item.accentColor,
                        shadowColor = DeepNavyText,
                        fontSize = 14.sp,
                        cornerRadius = 18.dp,
                        lipHeight = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                isOpened -> {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MintGreen
                    ) {
                        Text(
                            text = if (isAr) "تم فتح الهدية (+${item.bonusStars}⭐)" else "Claimed (+${item.bonusStars}⭐)",
                            color = DeepNavyText,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

// =====================================================================
// TAB 4: EDUCATIONAL CERTIFICATES (الشهادات التعليمية)
// =====================================================================

@Composable
private fun EducationalCertificatesSection(
    uiState: FunLearningUiState,
    selectedCertificate: WorldCertificateType,
    onSelectCertificate: (WorldCertificateType) -> Unit,
    exportStatusBanner: String?,
    onUpdateStatusBanner: (String) -> Unit,
    onUpdateChildName: (String) -> Unit,
    onSpeakLulu: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings = uiState.settings
    val lang = settings.language
    val isAr = lang == AppLanguage.AR

    var childNameInput by remember(settings.childName) { mutableStateOf(settings.childName) }
    val isSelectedCertUnlocked = selectedCertificate.isUnlocked(settings)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Child Name Customization Card for Certificates
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            color = Color.White,
            border = BorderStroke(2.5.dp, Color(0xFF4FC3F7)),
            shadowElevation = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isAr) "✍️ اسم الطفل على الشهادات التعليمية:" else "✍️ Child's Name on Certificates:",
                    color = DeepNavyText,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = childNameInput,
                        onValueChange = { childNameInput = it },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("certificate_child_name_input"),
                        placeholder = {
                            Text(if (isAr) "اكتب اسم البطل أو البطلة..." else "Enter child's name...")
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    Bouncy3DButton(
                        text = if (isAr) "حفظ الاسم" else "Save Name",
                        onClick = { onUpdateChildName(childNameInput) },
                        containerColor = SkyCyanDark,
                        shadowColor = DeepNavyText,
                        fontSize = 14.sp,
                        cornerRadius = 16.dp,
                        lipHeight = 4.dp
                    )
                }
            }
        }

        // 2. Horizontal Selector of All 7 Certificates
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(WorldCertificateType.entries) { cert ->
                val unlocked = cert.isUnlocked(settings)
                val selected = cert == selectedCertificate
                Surface(
                    modifier = Modifier
                        .testTag("cert_chip_${cert.id}")
                        .clickable { onSelectCertificate(cert) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selected) cert.accentColor else Color.White,
                    border = BorderStroke(2.dp, cert.accentColor),
                    shadowElevation = if (selected) 5.dp else 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = if (unlocked) cert.emoji else "🔒", fontSize = 18.sp)
                        Text(
                            text = cert.title(lang),
                            color = if (selected) Color.White else DeepNavyText,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isAr) 15.sp else 13.sp
                            )
                        )
                    }
                }
            }
        }

        // 3. Electronic Certificate Preview Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("certificate_preview_card"),
            shape = RoundedCornerShape(30.dp),
            color = Color(0xFFFFFDF7),
            border = BorderStroke(5.dp, selectedCertificate.accentColor),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(2.5.dp, StarGoldBorder, RoundedCornerShape(22.dp))
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelectedCertUnlocked) Color(0xFF2E7D32) else Color(0xFFF57C00)
                    ) {
                        Text(
                            text = if (isSelectedCertUnlocked) {
                                if (isAr) "شهادة مكتسبة ★" else "Unlocked Certificate ★"
                            } else {
                                if (isAr) "معاينة تشجيعية" else "Preview Mode"
                            },
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Text(
                        text = "عالم المرح والتعلّم | Fun Learning World",
                        color = MagicPurpleDark,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Lulu + World Illustration Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        border = BorderStroke(3.dp, SunnyYellow),
                        shadowElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                            contentDescription = "Lulu",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(2.5.dp, selectedCertificate.borderColor),
                        shadowElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(id = selectedCertificate.bannerDrawableRes),
                            contentDescription = selectedCertificate.title(lang),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Text(
                    text = selectedCertificate.title(lang),
                    color = DeepNavyText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isAr) 24.sp else 20.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isAr) "تُمنح هذه الشهادة بكل فخر واعتزاز إلى:" else "Proudly presented to our shining star:",
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                // Child's Name Golden Banner
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF8E1),
                    border = BorderStroke(2.dp, StarGoldBorder)
                ) {
                    Text(
                        text = settings.childName,
                        color = Color(0xFFC2185B),
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Text(
                    text = selectedCertificate.worldName(lang),
                    color = selectedCertificate.accentColor,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isAr) 18.sp else 16.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = selectedCertificate.message(lang),
                    color = DeepNavyText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                if (!isSelectedCertUnlocked) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFF3E0),
                        border = BorderStroke(1.dp, Color(0xFFFFB74D))
                    ) {
                        Text(
                            text = "💡 ${selectedCertificate.requirement(lang)}",
                            color = Color(0xFFE65100),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${if (isAr) "التاريخ:" else "Date:"} ${PdfAndPrintHelper.currentFormattedDate()}",
                        color = SoftSlateText,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isAr) "⭐ ختم لولو الذهبي" else "⭐ Lulu's Golden Seal",
                        color = Color(0xFFC2185B),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // 4. Action Buttons: Download PDF, Print Certificate, and Hear Lulu Praise
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Bouncy3DButton(
                text = if (isAr) "تنزيل الشهادة PDF" else "Download PDF",
                onClick = {
                    val status = PdfAndPrintHelper.exportCertificateAsPdf(
                        context = context,
                        childName = settings.childName,
                        certificate = selectedCertificate,
                        language = lang
                    )
                    onUpdateStatusBanner(status)
                },
                icon = Icons.Filled.Download,
                containerColor = Color(0xFF0288D1),
                shadowColor = Color(0xFF01579B),
                fontSize = 15.sp,
                modifier = Modifier.weight(1f),
                testTagName = "download_certificate_pdf_btn"
            )

            Bouncy3DButton(
                text = if (isAr) "طباعة الشهادة" else "Print Certificate",
                onClick = {
                    val status = PdfAndPrintHelper.printCertificate(
                        context = context,
                        childName = settings.childName,
                        certificate = selectedCertificate,
                        language = lang
                    )
                    onUpdateStatusBanner(status)
                },
                icon = Icons.Filled.Print,
                containerColor = Color(0xFF2E7D32),
                shadowColor = Color(0xFF1B5E20),
                fontSize = 15.sp,
                modifier = Modifier.weight(1f),
                testTagName = "print_certificate_btn"
            )
        }

        Bouncy3DButton(
            text = if (isAr) "اسمع تهنئة لولو بهذه الشهادة 🔊" else "Hear Lulu's Certificate Praise 🔊",
            onClick = {
                val praise = if (isAr) {
                    "مبارك لـ ${settings.childName}! ${selectedCertificate.titleAr}! ${selectedCertificate.messageAr}"
                } else {
                    "Congratulations to ${settings.childName}! ${selectedCertificate.titleEn}! ${selectedCertificate.messageEn}"
                }
                onSpeakLulu(praise)
            },
            containerColor = LuluPink,
            shadowColor = LuluPinkDark,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = exportStatusBanner != null,
            enter = fadeIn() + scaleIn()
        ) {
            exportStatusBanner?.let { statusText ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(2.dp, Color(0xFF2E7D32))
                ) {
                    Text(
                        text = statusText,
                        color = Color(0xFF1B5E20),
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// =====================================================================
// ONE-TIME ACHIEVEMENT CELEBRATION MODAL (الاحتفال بالإنجاز)
// =====================================================================

@Composable
fun AchievementCelebrationDialog(
    badge: AchievementBadge,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onOpenRewardsWorld: () -> Unit
) {
    val isAr = language == AppLanguage.AR

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("achievement_celebration_dialog"),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            border = BorderStroke(4.dp, StarGoldBorder),
            shadowElevation = 14.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFFFF8E1),
                                Color.White,
                                badge.cardBgColor
                            )
                        )
                    )
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "🌟 🎉 🌟",
                    fontSize = 30.sp
                )

                Text(
                    text = if (isAr) "إنجاز جديد مفتوح!" else "New Achievement Unlocked!",
                    color = Color(0xFFC2185B),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(4.dp, badge.accentColor),
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(92.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = badge.emoji, fontSize = 46.sp)
                    }
                }

                Text(
                    text = badge.title(language),
                    color = DeepNavyText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = badge.luluCelebrate(language),
                    color = SoftSlateText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Bouncy3DButton(
                        text = if (isAr) "رائع! تابع اللعب" else "Awesome! Continue",
                        onClick = onDismiss,
                        containerColor = MeadowGreenDark,
                        shadowColor = Color(0xFF1B5E20),
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f)
                    )

                    Bouncy3DButton(
                        text = if (isAr) "لوحة الجوائز" else "View Rewards",
                        onClick = {
                            onDismiss()
                            onOpenRewardsWorld()
                        },
                        containerColor = badge.accentColor,
                        shadowColor = DeepNavyText,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
