package com.example.ui.screens.brain

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.BrainGameActivityId
import com.example.model.BrainGamesCurriculum
import com.example.model.MemoryCardThemeItem
import com.example.model.NumbersCurriculum
import com.example.model.SizeOrderDirection
import com.example.ui.FunLearningUiState
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.theme.BrainAccent
import com.example.ui.theme.BrainCardBg
import com.example.ui.theme.BrainShadow
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark
import com.example.util.SoundEffectsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private sealed interface BrainSubScreen {
    data object Hub : BrainSubScreen
    data class Activity(val activityId: BrainGameActivityId) : BrainSubScreen
}

@Composable
fun BrainGamesWorldScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onCompleteActivity: (BrainGameActivityId, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSubScreen by remember { mutableStateOf<BrainSubScreen>(BrainSubScreen.Hub) }
    val appLanguage = uiState.settings.language
    val layoutDirection = if (appLanguage == AppLanguage.AR) LayoutDirection.Rtl else LayoutDirection.Ltr

    BackHandler {
        if (currentSubScreen != BrainSubScreen.Hub) {
            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
            currentSubScreen = BrainSubScreen.Hub
        } else {
            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
            onNavigateHome()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        PlayfulSkyBackground(
            modifier = modifier.fillMaxSize(),
            accentTint = BrainAccent
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                containerColor = Color.Transparent,
                topBar = {
                    BrainGamesTopBar(
                        appLanguage = appLanguage,
                        soundEnabled = uiState.settings.soundEnabled,
                        completedCount = uiState.settings.completedBrainActivities.size,
                        inSubActivity = currentSubScreen != BrainSubScreen.Hub,
                        onBackOrHome = {
                            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
                            if (currentSubScreen != BrainSubScreen.Hub) {
                                currentSubScreen = BrainSubScreen.Hub
                            } else {
                                onNavigateHome()
                            }
                        },
                        onGoHomeDirect = {
                            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
                            onNavigateHome()
                        },
                        onSelectLanguage = { lang ->
                            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
                            onSelectLanguage(lang)
                        },
                        onToggleSound = onToggleSound
                    )
                }
            ) { innerPadding ->
                AnimatedContent(
                    targetState = currentSubScreen,
                    transitionSpec = {
                        (fadeIn(tween(240)) + scaleIn(initialScale = 0.96f, animationSpec = tween(240)))
                            .togetherWith(fadeOut(tween(180)))
                    },
                    label = "brain_subscreen"
                ) { subScreen ->
                    when (subScreen) {
                        is BrainSubScreen.Hub -> {
                            BrainGamesHubScreen(
                                appLanguage = appLanguage,
                                soundEnabled = uiState.settings.soundEnabled,
                                completedActivities = uiState.settings.completedBrainActivities,
                                maxLevelReached = uiState.settings.brainMaxLevelReached,
                                onSpeakLulu = onSpeakLulu,
                                onOpenActivity = { activityId ->
                                    SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
                                    currentSubScreen = BrainSubScreen.Activity(activityId)
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }

                        is BrainSubScreen.Activity -> {
                            when (subScreen.activityId) {
                                BrainGameActivityId.MEMORY_MATCH -> {
                                    MemoryMatchActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = { level ->
                                            onCompleteActivity(BrainGameActivityId.MEMORY_MATCH, level)
                                        },
                                        onBackToHub = { currentSubScreen = BrainSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                BrainGameActivityId.ODD_ONE_OUT -> {
                                    OddOneOutActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = {
                                            onCompleteActivity(BrainGameActivityId.ODD_ONE_OUT, 2)
                                        },
                                        onBackToHub = { currentSubScreen = BrainSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                BrainGameActivityId.COMPLETE_PATTERN -> {
                                    CompletePatternActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = {
                                            onCompleteActivity(BrainGameActivityId.COMPLETE_PATTERN, 3)
                                        },
                                        onBackToHub = { currentSubScreen = BrainSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                BrainGameActivityId.ORDER_BY_SIZE -> {
                                    OrderBySizeActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = {
                                            onCompleteActivity(BrainGameActivityId.ORDER_BY_SIZE, 2)
                                        },
                                        onBackToHub = { currentSubScreen = BrainSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                BrainGameActivityId.PICTURE_PUZZLES -> {
                                    PicturePuzzlesActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = { level ->
                                            onCompleteActivity(BrainGameActivityId.PICTURE_PUZZLES, level)
                                        },
                                        onBackToHub = { currentSubScreen = BrainSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                BrainGameActivityId.COUNT_AND_THINK -> {
                                    CountAndThinkActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = {
                                            onCompleteActivity(BrainGameActivityId.COUNT_AND_THINK, 3)
                                        },
                                        onBackToHub = { currentSubScreen = BrainSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
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
private fun BrainGamesTopBar(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    completedCount: Int,
    inSubActivity: Boolean,
    onBackOrHome: () -> Unit,
    onGoHomeDirect: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        color = Color.White,
        border = BorderStroke(2.5.dp, BrainAccent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Bouncy3DIconButton(
                    icon = if (inSubActivity) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Home,
                    contentDescription = if (appLanguage == AppLanguage.AR) "رجوع" else "Back",
                    onClick = onBackOrHome,
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    iconTint = Color.White
                )

                if (inSubActivity) {
                    Bouncy3DIconButton(
                        icon = Icons.Filled.Home,
                        contentDescription = if (appLanguage == AppLanguage.AR) "الرئيسية" else "Home",
                        onClick = onGoHomeDirect,
                        containerColor = MeadowGreen,
                        shadowColor = MeadowGreenDark,
                        iconTint = Color.White
                    )
                }

                Column {
                    Text(
                        text = if (appLanguage == AppLanguage.AR) "ألعاب الذكاء والذاكرة 🧩" else "Brain & Memory Games 🧩",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText
                    )
                    Text(
                        text = if (appLanguage == AppLanguage.AR) "٦ ألعاب ذكاء وتفكير ممتعة" else "6 Fun Logic & Memory Games",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = SoftSlateText
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF8E1),
                    border = BorderStroke(1.5.dp, StarGoldBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
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
                            text = "$completedCount / 6",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = DeepNavyText
                        )
                    }
                }

                LanguageSelectorSwitch(
                    currentLanguage = appLanguage,
                    onSelectLanguage = onSelectLanguage
                )

                Bouncy3DIconButton(
                    icon = if (soundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                    contentDescription = "Sound",
                    onClick = onToggleSound,
                    containerColor = if (soundEnabled) MeadowGreen else Color(0xFFCFD8DC),
                    shadowColor = if (soundEnabled) MeadowGreenDark else Color(0xFF78909C),
                    iconTint = if (soundEnabled) Color.White else DeepNavyText
                )
            }
        }
    }
}

@Composable
private fun BrainGamesHubScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    completedActivities: Set<BrainGameActivityId>,
    maxLevelReached: Int,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onOpenActivity: (BrainGameActivityId) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = completedActivities.size
    val progressFraction = (completedCount / 6f).coerceIn(0f, 1f)

    val welcomeMessage = if (appLanguage == AppLanguage.AR) {
        "مرحباً يا بطل الذكاء! في عالم ألعاب الذكاء والذاكرة سنلعب تطابق البطاقات، الأنماط، الألغاز المصورة، والعدّ!"
    } else {
        "Welcome, Smart Explorer! In Brain & Memory Games we'll play Memory Match, Patterns, Jigsaw Puzzles, and Counting!"
    }

    LaunchedEffect(appLanguage) {
        onSpeakLulu(welcomeMessage, appLanguage)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .shadow(8.dp, RoundedCornerShape(30.dp))
                    .clickable {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        onSpeakLulu(welcomeMessage, appLanguage)
                    }
                    .testTag("brain_hub_hero_card"),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = BrainCardBg),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White)
                                .border(2.5.dp, BrainAccent, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_cat_brain_games_1794073881867),
                                contentDescription = if (appLanguage == AppLanguage.AR) "ألعاب الذكاء" else "Brain Games",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                                    contentDescription = "Lulu",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, LuluPink, CircleShape)
                                )
                                Text(
                                    text = if (appLanguage == AppLanguage.AR) "لولو تشجعكِ 🔊" else "Lulu cheers you on 🔊",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = BrainShadow
                                )
                            }

                            Text(
                                text = welcomeMessage,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 22.sp
                                ),
                                color = DeepNavyText
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (appLanguage == AppLanguage.AR) {
                                        "التقدّم في ألعاب الذكاء (المستوى $maxLevelReached)"
                                    } else {
                                        "Brain Games Progress (Level $maxLevelReached)"
                                    },
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = DeepNavyText
                                )
                                Text(
                                    text = "$completedCount / 6 ⭐",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = BrainShadow
                                )
                            }
                            LinearProgressIndicator(
                                progress = { progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = MeadowGreen,
                                trackColor = Color(0xFFD1C4E9)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = if (appLanguage == AppLanguage.AR) "اختاري لعبة ذكاء ممتعة (٦ ألعاب):" else "Pick a Fun Brain Game (6 Games):",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = DeepNavyText,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .padding(top = 4.dp)
            )
        }

        val activityPairs = BrainGameActivityId.entries.chunked(2)
        items(activityPairs.size) { rowIndex ->
            val pair = activityPairs[rowIndex]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                pair.forEach { activity ->
                    val isDone = completedActivities.contains(activity)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(180.dp)
                            .shadow(7.dp, RoundedCornerShape(26.dp))
                            .clickable { onOpenActivity(activity) }
                            .testTag("brain_activity_card_${activity.key}"),
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = activity.cardBgColor),
                        border = BorderStroke(2.5.dp, Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    shadowElevation = 2.dp
                                ) {
                                    Text(
                                        text = activity.badgeEmoji,
                                        fontSize = 28.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }

                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = MeadowGreenDark,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = activity.title(appLanguage),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold
                                    ),
                                    color = DeepNavyText
                                )
                                Text(
                                    text = activity.subtitle(appLanguage),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = SoftSlateText,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

// =====================================================================
// 1. MEMORY MATCH GAME | لعبة تطابق الذاكرة
// =====================================================================

private data class MemoryCardSlot(
    val slotIndex: Int,
    val item: MemoryCardThemeItem
)

@Composable
private fun MemoryMatchActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: (Int) -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Level 1 = 4 cards (2 pairs), Level 2 = 6 cards (3 pairs), Level 3 = 8 cards (4 pairs)
    var currentLevel by rememberSaveable { mutableIntStateOf(1) }
    var isAllLevelsComplete by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val pairCount = when (currentLevel) {
        1 -> 2
        2 -> 3
        else -> 4
    }

    val deck = remember(currentLevel) {
        val chosenItems = BrainGamesCurriculum.memoryPool.take(pairCount)
        (chosenItems + chosenItems)
            .shuffled()
            .mapIndexed { idx, themeItem -> MemoryCardSlot(idx, themeItem) }
    }

    val matchedItemIds = remember(currentLevel) { mutableStateListOf<String>() }
    val flippedSlotIndices = remember(currentLevel) { mutableStateListOf<Int>() }
    var isBusyChecking by remember(currentLevel) { mutableStateOf(false) }

    LaunchedEffect(currentLevel, appLanguage) {
        val prompt = if (appLanguage == AppLanguage.AR) {
            "المستوى $currentLevel: اقلبي بطاقتين لتجدي الصورتين المتطابقتين!"
        } else {
            "Level $currentLevel: Tap two cards to find matching pictures!"
        }
        onSpeakLulu(prompt, appLanguage)
    }

    if (isAllLevelsComplete) {
        BrainCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "ذاكرة خارقة يا بطلة! 🃏🌟" else "Super Memory Champion! 🃏🌟",
            subtitle = if (appLanguage == AppLanguage.AR) {
                "لقد أكملتِ جميع مستويات لعبة تطابق الذاكرة بنجاح!"
            } else {
                "You completed all levels of Memory Match!"
            },
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                currentLevel = 1
                isAllLevelsComplete = false
            },
            onBackToHub = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                onBackToHub()
            },
            modifier = modifier
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(7.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                border = BorderStroke(2.5.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (appLanguage == AppLanguage.AR) {
                                "المستوى $currentLevel (${deck.size} بطاقات)"
                            } else {
                                "Level $currentLevel (${deck.size} Cards)"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = BrainShadow
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(1 to "4", 2 to "6", 3 to "8").forEach { (lvl, label) ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (currentLevel == lvl) BrainAccent else Color.White,
                                    border = BorderStroke(1.5.dp, BrainAccent),
                                    modifier = Modifier.clickable {
                                        SoundEffectsHelper.playPopChime(soundEnabled)
                                        currentLevel = lvl
                                    }
                                ) {
                                    Text(
                                        text = if (appLanguage == AppLanguage.AR) "$label بطاقات" else "$label Cards",
                                        color = if (currentLevel == lvl) Color.White else BrainShadow,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = if (appLanguage == AppLanguage.AR) {
                            "اضغطي على بطاقتين لكشف الصور المتشابهة!"
                        } else {
                            "Tap two cards to reveal and match identical pictures!"
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        val cols = if (deck.size == 4) 2 else if (deck.size == 6) 3 else 4
        val cardRows = deck.chunked(cols)
        items(cardRows.size) { rowIdx ->
            val rowCards = cardRows[rowIdx]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowCards.forEach { slot ->
                    val isMatched = matchedItemIds.contains(slot.item.id)
                    val isFlipped = isMatched || flippedSlotIndices.contains(slot.slotIndex)

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(136.dp)
                            .shadow(6.dp, RoundedCornerShape(24.dp))
                            .clickable(enabled = !isFlipped && !isBusyChecking) {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                flippedSlotIndices.add(slot.slotIndex)
                                onSpeakLulu(slot.item.name(appLanguage), appLanguage)

                                if (flippedSlotIndices.size == 2) {
                                    val firstIdx = flippedSlotIndices[0]
                                    val secondIdx = flippedSlotIndices[1]
                                    val firstItem = deck[firstIdx].item
                                    val secondItem = deck[secondIdx].item

                                    if (firstItem.id == secondItem.id) {
                                        SoundEffectsHelper.playHappyMelody(soundEnabled)
                                        matchedItemIds.add(firstItem.id)
                                        flippedSlotIndices.clear()
                                        if (matchedItemIds.size == pairCount) {
                                            SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                            onComplete(currentLevel)
                                            coroutineScope.launch {
                                                delay(700)
                                                if (currentLevel < 3) {
                                                    currentLevel += 1
                                                } else {
                                                    isAllLevelsComplete = true
                                                }
                                            }
                                        }
                                    } else {
                                        isBusyChecking = true
                                        coroutineScope.launch {
                                            delay(850)
                                            flippedSlotIndices.clear()
                                            isBusyChecking = false
                                        }
                                    }
                                }
                            }
                            .testTag("memory_card_slot_${slot.slotIndex}"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isMatched -> Color(0xFFE8F5E9)
                                isFlipped -> slot.item.bgColor
                                else -> BrainAccent
                            }
                        ),
                        border = BorderStroke(
                            width = 3.dp,
                            color = if (isMatched) MeadowGreen else Color.White
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFlipped) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = slot.item.emoji, fontSize = 46.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = slot.item.name(appLanguage),
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                        color = DeepNavyText
                                    )
                                }
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "🌟", fontSize = 36.sp)
                                    Text(
                                        text = "?",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                                        color = Color.White
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

// =====================================================================
// 2. ODD ONE OUT GAME | لعبة اكتشف العنصر المختلف
// =====================================================================

@Composable
private fun OddOneOutActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = BrainGamesCurriculum.oddOneOutQuestions
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var isCompleted by rememberSaveable { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf<String?>(null) }
    var isFeedbackPositive by remember { mutableStateOf(true) }

    val currentQuestion = questions[currentIndex]

    LaunchedEffect(currentIndex, appLanguage, isCompleted) {
        if (!isCompleted) {
            feedbackText = null
            onSpeakLulu(currentQuestion.prompt(appLanguage), appLanguage)
        }
    }

    if (isCompleted) {
        BrainCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "قوة ملاحظة رائعة! 🔍🌟" else "Sharp Eyes & Smart Thinking! 🔍🌟",
            subtitle = if (appLanguage == AppLanguage.AR) {
                "لقد اكتشفتِ جميع الصور المختلفة بذكاء!"
            } else {
                "You spotted every odd picture out!"
            },
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                currentIndex = 0
                isCompleted = false
            },
            onBackToHub = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                onBackToHub()
            },
            modifier = modifier
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(7.dp, RoundedCornerShape(28.dp))
                    .clickable { onSpeakLulu(currentQuestion.prompt(appLanguage), appLanguage) },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1)),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${currentIndex + 1} / ${questions.size}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF004D40)
                    )
                    Text(
                        text = currentQuestion.prompt(appLanguage),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (feedbackText != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isFeedbackPositive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    border = BorderStroke(2.dp, if (isFeedbackPositive) MeadowGreen else LuluPink),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = feedbackText.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (isFeedbackPositive) Color(0xFF1B5E20) else Color(0xFFC62828),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        val pairs = currentQuestion.items.mapIndexed { idx, item -> idx to item }.chunked(2)
        items(pairs.size) { rowIdx ->
            val rowItems = pairs[rowIdx]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowItems.forEach { (idx, option) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(152.dp)
                            .shadow(6.dp, RoundedCornerShape(26.dp))
                            .clickable {
                                if (idx == currentQuestion.oddIndex) {
                                    SoundEffectsHelper.playHappyMelody(soundEnabled)
                                    isFeedbackPositive = true
                                    feedbackText = currentQuestion.explanation(appLanguage)
                                    onSpeakLulu(currentQuestion.explanation(appLanguage), appLanguage)
                                    if (currentIndex + 1 >= questions.size) {
                                        SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                        onComplete()
                                        isCompleted = true
                                    } else {
                                        currentIndex += 1
                                    }
                                } else {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    isFeedbackPositive = false
                                    feedbackText = if (appLanguage == AppLanguage.AR) {
                                        "هذه ${option.labelAr} تشبه البقية، ابحثي عن الصورة المختلفة!"
                                    } else {
                                        "The ${option.labelEn} belongs to the group, find the different one!"
                                    }
                                    onSpeakLulu(feedbackText.orEmpty(), appLanguage)
                                }
                            }
                            .testTag("odd_one_out_option_$idx"),
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = option.bgColor),
                        border = BorderStroke(2.5.dp, Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = option.emoji, fontSize = 54.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = option.label(appLanguage),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = DeepNavyText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// 3. COMPLETE THE PATTERN GAME | لعبة أكمل النمط
// =====================================================================

@Composable
private fun CompletePatternActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = BrainGamesCurriculum.patternQuestions
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var isCompleted by rememberSaveable { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf<String?>(null) }
    var isFeedbackPositive by remember { mutableStateOf(true) }

    val currentQuestion = questions[currentIndex]

    LaunchedEffect(currentIndex, appLanguage, isCompleted) {
        if (!isCompleted) {
            feedbackText = null
            onSpeakLulu(currentQuestion.prompt(appLanguage), appLanguage)
        }
    }

    if (isCompleted) {
        BrainCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "بطلة الأنماط الذكية! 🔄🌟" else "Pattern Master Champion! 🔄🌟",
            subtitle = if (appLanguage == AppLanguage.AR) {
                "لقد أكملتِ جميع الأنماط بذكاء وترتيب رائع!"
            } else {
                "You solved all the sequence patterns!"
            },
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                currentIndex = 0
                isCompleted = false
            },
            onBackToHub = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                onBackToHub()
            },
            modifier = modifier
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(7.dp, RoundedCornerShape(28.dp))
                    .clickable { onSpeakLulu(currentQuestion.prompt(appLanguage), appLanguage) },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (appLanguage == AppLanguage.AR) {
                            "المستوى ${currentQuestion.level} • النمط ${currentIndex + 1} من ${questions.size}"
                        } else {
                            "Level ${currentQuestion.level} • Pattern ${currentIndex + 1} of ${questions.size}"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF0D47A1)
                    )

                    Text(
                        text = currentQuestion.prompt(appLanguage),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )

                    // Pattern train display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        currentQuestion.sequenceEmojis.forEachIndexed { idx, emoji ->
                            val label = if (appLanguage == AppLanguage.AR) {
                                currentQuestion.sequenceLabelsAr[idx]
                            } else {
                                currentQuestion.sequenceLabelsEn[idx]
                            }
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, Color(0xFF90CAF9)),
                                shadowElevation = 3.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = emoji, fontSize = 30.sp)
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = DeepNavyText
                                    )
                                }
                            }
                        }

                        // Mystery ? slot
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFFFFF8E1),
                            border = BorderStroke(2.5.dp, SunnyYellowDark),
                            shadowElevation = 4.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(58.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "؟",
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = Color(0xFFE65100)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (feedbackText != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isFeedbackPositive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    border = BorderStroke(2.dp, if (isFeedbackPositive) MeadowGreen else LuluPink),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = feedbackText.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (isFeedbackPositive) Color(0xFF1B5E20) else Color(0xFFC62828),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentQuestion.options.forEachIndexed { idx, option ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(142.dp)
                            .shadow(6.dp, RoundedCornerShape(24.dp))
                            .clickable {
                                if (idx == currentQuestion.correctOptionIndex) {
                                    SoundEffectsHelper.playHappyMelody(soundEnabled)
                                    isFeedbackPositive = true
                                    feedbackText = if (appLanguage == AppLanguage.AR) {
                                        "رائع! ${option.labelAr} يكمل النمط بشكل صحيح! 🌟"
                                    } else {
                                        "Awesome! ${option.labelEn} completes the pattern! 🌟"
                                    }
                                    onSpeakLulu(feedbackText.orEmpty(), appLanguage)
                                    if (currentIndex + 1 >= questions.size) {
                                        SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                        onComplete()
                                        isCompleted = true
                                    } else {
                                        currentIndex += 1
                                    }
                                } else {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    isFeedbackPositive = false
                                    feedbackText = if (appLanguage == AppLanguage.AR) {
                                        "انظري إلى الترتيب وحاولي مرة أخرى يا بطلة!"
                                    } else {
                                        "Look closely at the sequence and try again!"
                                    }
                                    onSpeakLulu(feedbackText.orEmpty(), appLanguage)
                                }
                            }
                            .testTag("pattern_option_$idx"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = option.bgColor),
                        border = BorderStroke(2.5.dp, Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = option.emoji, fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = option.label(appLanguage),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = DeepNavyText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// 4. ORDER BY SIZE GAME | لعبة الترتيب حسب الحجم
// =====================================================================

@Composable
private fun OrderBySizeActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val challenges = BrainGamesCurriculum.sizeOrderChallenges
    var currentChallengeIdx by rememberSaveable { mutableIntStateOf(0) }
    var isCompleted by rememberSaveable { mutableStateOf(false) }

    val challenge = challenges[currentChallengeIdx]
    val expectedOrder = remember(currentChallengeIdx) {
        if (challenge.direction == SizeOrderDirection.SMALLEST_TO_LARGEST) {
            challenge.items.sortedBy { it.sizeRank }
        } else {
            challenge.items.sortedByDescending { it.sizeRank }
        }
    }
    val shuffledChoices = remember(currentChallengeIdx) {
        challenge.items.shuffled()
    }
    val pickedIds = remember(currentChallengeIdx) { mutableStateListOf<String>() }
    var feedbackBanner by remember(currentChallengeIdx) { mutableStateOf<String?>(null) }

    LaunchedEffect(currentChallengeIdx, appLanguage, isCompleted) {
        if (!isCompleted) {
            onSpeakLulu(challenge.prompt(appLanguage), appLanguage)
        }
    }

    if (isCompleted) {
        BrainCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "بطلة الترتيب حسب الحجم! 📏🌟" else "Size Ordering Champion! 📏🌟",
            subtitle = if (appLanguage == AppLanguage.AR) {
                "لقد رتّبتِ جميع العناصر من الأصغر للأكبر ومن الأكبر للأصغر!"
            } else {
                "You ordered everything from smallest to largest and largest to smallest!"
            },
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                currentChallengeIdx = 0
                isCompleted = false
            },
            onBackToHub = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                onBackToHub()
            },
            modifier = modifier
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(7.dp, RoundedCornerShape(28.dp))
                    .clickable { onSpeakLulu(challenge.prompt(appLanguage), appLanguage) },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = challenge.cardColor),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "${currentChallengeIdx + 1} / ${challenges.size}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFFE65100)
                    )
                    Text(
                        text = challenge.prompt(appLanguage),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )

                    // Ordered Slots Display (1 -> 2 -> 3 -> 4)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (slotIdx in 0 until 4) {
                            val pickedId = pickedIds.getOrNull(slotIdx)
                            val pickedItem = challenge.items.firstOrNull { it.id == pickedId }
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(92.dp),
                                shape = RoundedCornerShape(18.dp),
                                color = if (pickedItem != null) Color(0xFFE8F5E9) else Color.White,
                                border = BorderStroke(
                                    2.dp,
                                    if (pickedItem != null) MeadowGreen else Color(0xFFFFB74D)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    if (pickedItem != null) {
                                        Text(
                                            text = challenge.emoji,
                                            fontSize = (36f * pickedItem.scaleFactor).sp
                                        )
                                        Text(
                                            text = pickedItem.sizeLabel(appLanguage),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = DeepNavyText
                                        )
                                    } else {
                                        Text(
                                            text = "${slotIdx + 1}",
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                            color = Color(0xFFFFB74D)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (pickedIds.isNotEmpty()) {
                        Bouncy3DButton(
                            text = if (appLanguage == AppLanguage.AR) "إعادة ترتيب" else "Reset Order",
                            onClick = {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                pickedIds.clear()
                                feedbackBanner = null
                            },
                            containerColor = Color(0xFFFB8C00),
                            shadowColor = Color(0xFFE65100),
                            icon = Icons.Filled.Refresh
                        )
                    }
                }
            }
        }

        if (feedbackBanner != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF8E1),
                    border = BorderStroke(2.dp, SunnyYellowDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = feedbackBanner.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Tap-to-pick cards
        val pairs = shuffledChoices.chunked(2)
        items(pairs.size) { rowIdx ->
            val rowChoices = pairs[rowIdx]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowChoices.forEach { item ->
                    val alreadyPicked = pickedIds.contains(item.id)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(145.dp)
                            .shadow(if (alreadyPicked) 2.dp else 6.dp, RoundedCornerShape(24.dp))
                            .clickable(enabled = !alreadyPicked) {
                                val nextExpected = expectedOrder[pickedIds.size]
                                if (item.id == nextExpected.id) {
                                    SoundEffectsHelper.playHappyMelody(soundEnabled)
                                    pickedIds.add(item.id)
                                    feedbackBanner = if (appLanguage == AppLanguage.AR) {
                                        "أحسنتِ! اختاري الحجم التالي 🌟"
                                    } else {
                                        "Great job! Tap the next size 🌟"
                                    }
                                    if (pickedIds.size == 4) {
                                        if (currentChallengeIdx + 1 >= challenges.size) {
                                            SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                            onComplete()
                                            isCompleted = true
                                        } else {
                                            currentChallengeIdx += 1
                                        }
                                    }
                                } else {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    feedbackBanner = if (appLanguage == AppLanguage.AR) {
                                        if (challenge.direction == SizeOrderDirection.SMALLEST_TO_LARGEST) {
                                            "ابحثي عن الحجم الأصغر من البقية أولاً!"
                                        } else {
                                            "ابحثي عن الحجم الأكبر من البقية أولاً!"
                                        }
                                    } else {
                                        if (challenge.direction == SizeOrderDirection.SMALLEST_TO_LARGEST) {
                                            "Look for the smallest remaining item first!"
                                        } else {
                                            "Look for the largest remaining item first!"
                                        }
                                    }
                                    onSpeakLulu(feedbackBanner.orEmpty(), appLanguage)
                                }
                            }
                            .testTag("size_order_item_${item.id}"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (alreadyPicked) Color(0xFFE8F5E9) else Color.White
                        ),
                        border = BorderStroke(
                            2.5.dp,
                            if (alreadyPicked) MeadowGreen else Color(0xFFFFB74D)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = challenge.emoji,
                                fontSize = (48f * item.scaleFactor).sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.sizeLabel(appLanguage),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = DeepNavyText
                            )
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// 5. PICTURE JIGSAW PUZZLES | لعبة الألغاز المصورة (4, 6, 9 قطع)
// =====================================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PicturePuzzlesActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: (Int) -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val puzzles = BrainGamesCurriculum.picturePuzzles
    var puzzleIndex by rememberSaveable { mutableIntStateOf(0) }
    var isAllPuzzlesDone by rememberSaveable { mutableStateOf(false) }

    val currentPuzzle = puzzles[puzzleIndex]
    val totalPieces = currentPuzzle.pieceCount

    val placedPieces = remember(puzzleIndex) { mutableStateListOf<Int>() }
    val trayPieces = remember(puzzleIndex) {
        (0 until totalPieces).shuffled()
    }
    var selectedTrayPiece by remember(puzzleIndex) { mutableStateOf<Int?>(null) }

    LaunchedEffect(puzzleIndex, appLanguage, isAllPuzzlesDone) {
        if (!isAllPuzzlesDone) {
            val msg = if (appLanguage == AppLanguage.AR) {
                "${currentPuzzle.titleAr}: اضغطي على القطعة المناسبة أو المكان الصحيح لتركيب الصورة!"
            } else {
                "${currentPuzzle.titleEn}: Tap the matching piece or board slot to assemble the puzzle!"
            }
            onSpeakLulu(msg, appLanguage)
        }
    }

    if (isAllPuzzlesDone) {
        BrainCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "بطلة الألغاز المصوّرة! 🧩🌟" else "Picture Puzzle Master! 🧩🌟",
            subtitle = if (appLanguage == AppLanguage.AR) {
                "لقد ركّبتِ ألغاز ٤ و ٦ و ٩ قطع بنجاح!"
            } else {
                "You assembled the 4, 6, and 9 piece puzzles!"
            },
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                puzzleIndex = 0
                isAllPuzzlesDone = false
            },
            onBackToHub = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                onBackToHub()
            },
            modifier = modifier
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(7.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC)),
                border = BorderStroke(2.5.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        puzzles.forEachIndexed { idx, p ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (puzzleIndex == idx) p.themeColor else Color.White,
                                border = BorderStroke(1.5.dp, p.themeColor),
                                modifier = Modifier.clickable {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    puzzleIndex = idx
                                }
                            ) {
                                Text(
                                    text = if (appLanguage == AppLanguage.AR) "${p.pieceCount} قطع" else "${p.pieceCount} Pieces",
                                    color = if (puzzleIndex == idx) Color.White else DeepNavyText,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = currentPuzzle.title(appLanguage),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = if (appLanguage == AppLanguage.AR) {
                            "اضغطي على القطعة من الأسفل ثم ضعيها في مكانها المماثل (${placedPieces.size} / $totalPieces)"
                        } else {
                            "Tap a piece below, then tap its matching spot on the board (${placedPieces.size} / $totalPieces)"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftSlateText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Puzzle Board Grid
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .shadow(8.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, currentPuzzle.themeColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val slotRows = (0 until totalPieces).chunked(currentPuzzle.cols)
                    slotRows.forEach { rowIndices ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowIndices.forEach { slotIdx ->
                                val isPlaced = placedPieces.contains(slotIdx)
                                val emoji = currentPuzzle.tileEmojis[slotIdx]
                                val tileColor = currentPuzzle.tileColors[slotIdx]

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(if (currentPuzzle.rows == 3) 86.dp else 104.dp)
                                        .clickable(enabled = !isPlaced) {
                                            val chosen = selectedTrayPiece
                                            if (chosen == null || chosen == slotIdx) {
                                                // Easy touch for young children: if they selected the matching piece OR tap the slot directly to place its matching piece
                                                SoundEffectsHelper.playHappyMelody(soundEnabled)
                                                placedPieces.add(slotIdx)
                                                selectedTrayPiece = null
                                                if (placedPieces.size == totalPieces) {
                                                    SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                                    onComplete(puzzleIndex + 1)
                                                    if (puzzleIndex + 1 < puzzles.size) {
                                                        puzzleIndex += 1
                                                    } else {
                                                        isAllPuzzlesDone = true
                                                    }
                                                }
                                            } else {
                                                SoundEffectsHelper.playPopChime(soundEnabled)
                                                onSpeakLulu(
                                                    if (appLanguage == AppLanguage.AR) "ابحثي عن المربع الذي يحمل نفس الرسمة!"
                                                    else "Look for the slot with the matching picture!",
                                                    appLanguage
                                                )
                                            }
                                        }
                                        .testTag("puzzle_board_slot_$slotIdx"),
                                    shape = RoundedCornerShape(18.dp),
                                    color = if (isPlaced) tileColor else tileColor.copy(alpha = 0.28f),
                                    border = BorderStroke(
                                        width = if (isPlaced) 2.5.dp else 2.dp,
                                        color = if (isPlaced) MeadowGreen else currentPuzzle.themeColor.copy(alpha = 0.5f)
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = emoji,
                                            fontSize = if (isPlaced) 40.sp else 28.sp,
                                            modifier = Modifier.scale(if (isPlaced) 1f else 0.78f)
                                        )
                                        if (!isPlaced) {
                                            Text(
                                                text = "${slotIdx + 1}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                                color = DeepNavyText.copy(alpha = 0.6f),
                                                modifier = Modifier
                                                    .align(Alignment.TopStart)
                                                    .padding(6.dp)
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

        // Remaining Puzzle Pieces Tray
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(5.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (appLanguage == AppLanguage.AR) "قطع اللغز المتبقية:" else "Remaining Puzzle Pieces:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        trayPieces.filter { !placedPieces.contains(it) }.forEach { pieceIdx ->
                            val isSelected = selectedTrayPiece == pieceIdx
                            Surface(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clickable {
                                        SoundEffectsHelper.playPopChime(soundEnabled)
                                        selectedTrayPiece = pieceIdx
                                    }
                                    .testTag("puzzle_tray_piece_$pieceIdx"),
                                shape = RoundedCornerShape(18.dp),
                                color = currentPuzzle.tileColors[pieceIdx],
                                border = BorderStroke(
                                    width = if (isSelected) 3.5.dp else 2.dp,
                                    color = if (isSelected) SunnyYellowDark else currentPuzzle.themeColor
                                ),
                                shadowElevation = if (isSelected) 6.dp else 2.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = currentPuzzle.tileEmojis[pieceIdx], fontSize = 32.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// 6. COUNT & THINK GAME | لعبة العدّ والتفكير (مرتبطة بعالم الأرقام)
// =====================================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CountAndThinkActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = BrainGamesCurriculum.countAndThinkQuestions
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var isCompleted by rememberSaveable { mutableStateOf(false) }
    var feedbackBanner by remember { mutableStateOf<String?>(null) }
    var isFeedbackPositive by remember { mutableStateOf(true) }

    val currentQuestion = questions[currentIndex]

    LaunchedEffect(currentIndex, appLanguage, isCompleted) {
        if (!isCompleted) {
            feedbackBanner = null
            onSpeakLulu(currentQuestion.prompt(appLanguage), appLanguage)
        }
    }

    if (isCompleted) {
        BrainCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "عبقرية العدّ والتفكير! 🔢🌟" else "Counting & Logic Genius! 🔢🌟",
            subtitle = if (appLanguage == AppLanguage.AR) {
                "لقد عددتِ المجموعات واخترتِ الأكبر والأصغر ببراعة!"
            } else {
                "You counted the groups and picked the right answers!"
            },
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                currentIndex = 0
                isCompleted = false
            },
            onBackToHub = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                onBackToHub()
            },
            modifier = modifier
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(7.dp, RoundedCornerShape(28.dp))
                    .clickable { onSpeakLulu(currentQuestion.prompt(appLanguage), appLanguage) },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${currentIndex + 1} / ${questions.size}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MeadowGreenDark
                    )
                    Text(
                        text = currentQuestion.prompt(appLanguage),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (feedbackBanner != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isFeedbackPositive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    border = BorderStroke(2.dp, if (isFeedbackPositive) MeadowGreen else LuluPink),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = feedbackBanner.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (isFeedbackPositive) Color(0xFF1B5E20) else Color(0xFFC62828),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        items(currentQuestion.groups.size) { groupIdx ->
            val group = currentQuestion.groups[groupIdx]
            val numberLesson = NumbersCurriculum.byValue(group.count.coerceIn(1, 10))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(6.dp, RoundedCornerShape(24.dp))
                    .clickable {
                        if (groupIdx == currentQuestion.correctGroupIndex) {
                            SoundEffectsHelper.playHappyMelody(soundEnabled)
                            isFeedbackPositive = true
                            val numberWord = numberLesson.word(appLanguage)
                            feedbackBanner = if (appLanguage == AppLanguage.AR) {
                                "أحسنتِ! هذه المجموعة فيها ${group.labelAr} ($numberWord) 🌟"
                            } else {
                                "Correct! This group has ${group.labelEn} ($numberWord) 🌟"
                            }
                            onSpeakLulu(feedbackBanner.orEmpty(), appLanguage)
                            if (currentIndex + 1 >= questions.size) {
                                SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                onComplete()
                                isCompleted = true
                            } else {
                                currentIndex += 1
                            }
                        } else {
                            SoundEffectsHelper.playPopChime(soundEnabled)
                            isFeedbackPositive = false
                            feedbackBanner = if (appLanguage == AppLanguage.AR) {
                                "هذه المجموعة فيها ${group.labelAr}، عدّي المجموعات الأخرى وحاولي مرة ثانية!"
                            } else {
                                "This group has ${group.labelEn}, count the others and try again!"
                            }
                            onSpeakLulu(feedbackBanner.orEmpty(), appLanguage)
                        }
                    }
                    .testTag("count_think_group_$groupIdx"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = group.cardColor),
                border = BorderStroke(2.5.dp, Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FlowRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(group.count) {
                            Text(text = group.emoji, fontSize = 34.sp)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color.White,
                        border = BorderStroke(2.dp, numberLesson.color)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = group.numberSymbol(appLanguage),
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = numberLesson.color
                            )
                            Text(
                                text = numberLesson.word(appLanguage),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = DeepNavyText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BrainCelebrationCard(
    title: String,
    subtitle: String,
    appLanguage: AppLanguage,
    onPlayAgain: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "brain_celebration")
    val starScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 520.dp)
                .shadow(9.dp, RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
            border = BorderStroke(3.dp, Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "🌟🧩🌟",
                    fontSize = 52.sp,
                    modifier = Modifier.scale(starScale)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = DeepNavyText,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = SoftSlateText,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Bouncy3DButton(
                        text = if (appLanguage == AppLanguage.AR) "العبي مرة أخرى" else "Play Again",
                        onClick = onPlayAgain,
                        containerColor = BrainAccent,
                        shadowColor = BrainShadow,
                        icon = Icons.Filled.Refresh,
                        modifier = Modifier.weight(1f)
                    )
                    Bouncy3DButton(
                        text = if (appLanguage == AppLanguage.AR) "ألعاب الذكاء" else "Brain Hub",
                        onClick = onBackToHub,
                        containerColor = MeadowGreen,
                        shadowColor = MeadowGreenDark,
                        icon = Icons.Filled.Home,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
