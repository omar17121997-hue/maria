package com.example.ui.screens.letters

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.LettersActivityId
import com.example.model.LettersCurriculum
import com.example.model.LettersSectionMode
import com.example.ui.FunLearningUiState
import com.example.ui.components.AnimatedLetter3DBlock
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.LetterFingerTracingPad
import com.example.ui.components.LetterWord3DIllustrationCard
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.components.findLetterHitCheckpoints
import com.example.ui.theme.ArabicAccent
import com.example.ui.theme.ArabicCardBg
import com.example.ui.theme.ArabicShadow
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.EnglishAccent
import com.example.ui.theme.EnglishCardBg
import com.example.ui.theme.EnglishShadow
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark

sealed interface LettersWorldRoute {
    data object Hub : LettersWorldRoute
    data class Activity(
        val id: LettersActivityId,
        val initialLetterIndex: Int = 1
    ) : LettersWorldRoute
}

@Composable
fun LettersWorldScreen(
    initialSection: LettersSectionMode,
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onCompleteActivity: (LettersActivityId) -> Unit,
    onMarkLetterLearned: (String, LettersSectionMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf<LettersWorldRoute>(LettersWorldRoute.Hub) }
    var sectionMode by remember(initialSection) { mutableStateOf(initialSection) }

    BackHandler {
        if (currentRoute != LettersWorldRoute.Hub) {
            currentRoute = LettersWorldRoute.Hub
        } else {
            onNavigateHome()
        }
    }

    val learnedIds = if (sectionMode == LettersSectionMode.ARABIC) {
        uiState.settings.learnedArabicLetters
    } else {
        uiState.settings.learnedEnglishLetters
    }

    when (val route = currentRoute) {
        is LettersWorldRoute.Hub -> {
            LettersWorldHubScreen(
                uiState = uiState,
                sectionMode = sectionMode,
                learnedLetterIds = learnedIds,
                onSelectSectionMode = { mode ->
                    sectionMode = mode
                    val greeting = if (mode == LettersSectionMode.ARABIC) {
                        "مرحباً يا بطلة! هيا نتعرف على الحروف العربية من الألف إلى الياء!"
                    } else {
                        "Hello superstar! Let's learn the English letters from A to Z!"
                    }
                    onSpeakLulu(greeting, mode.language)
                },
                onNavigateHome = onNavigateHome,
                onSelectAppLanguage = onSelectLanguage,
                onToggleSound = onToggleSound,
                onSpeakLulu = onSpeakLulu,
                onOpenLetterLesson = { letterIndex ->
                    currentRoute = LettersWorldRoute.Activity(
                        id = LettersActivityId.MEET_LETTER,
                        initialLetterIndex = letterIndex
                    )
                },
                onOpenActivity = { activityId, startIndex ->
                    currentRoute = LettersWorldRoute.Activity(
                        id = activityId,
                        initialLetterIndex = startIndex
                    )
                },
                modifier = modifier
            )
        }

        is LettersWorldRoute.Activity -> {
            when (route.id) {
                LettersActivityId.MEET_LETTER -> {
                    MeetTheLetterActivityScreen(
                        initialLetterIndex = route.initialLetterIndex,
                        sectionMode = sectionMode,
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        learnedLetterIds = learnedIds,
                        isActivityCompleted = LettersActivityId.MEET_LETTER in uiState.settings.completedLettersActivities,
                        onSelectSectionMode = { sectionMode = it },
                        onBackToHub = { currentRoute = LettersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkLetterLearned = { id -> onMarkLetterLearned(id, sectionMode) },
                        onMarkActivityCompleted = { onCompleteActivity(LettersActivityId.MEET_LETTER) },
                        onOpenTracingForLetter = { idx ->
                            currentRoute = LettersWorldRoute.Activity(
                                id = LettersActivityId.LETTER_TRACING,
                                initialLetterIndex = idx
                            )
                        }
                    )
                }

                LettersActivityId.LETTER_TRACING -> {
                    LetterTracingActivityScreen(
                        initialLetterIndex = route.initialLetterIndex,
                        sectionMode = sectionMode,
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isActivityCompleted = LettersActivityId.LETTER_TRACING in uiState.settings.completedLettersActivities,
                        onSelectSectionMode = { sectionMode = it },
                        onBackToHub = { currentRoute = LettersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkLetterLearned = { id -> onMarkLetterLearned(id, sectionMode) },
                        onMarkActivityCompleted = { onCompleteActivity(LettersActivityId.LETTER_TRACING) }
                    )
                }

                LettersActivityId.FIND_LETTER -> {
                    FindTheLetterActivityScreen(
                        sectionMode = sectionMode,
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isActivityCompleted = LettersActivityId.FIND_LETTER in uiState.settings.completedLettersActivities,
                        onSelectSectionMode = { sectionMode = it },
                        onBackToHub = { currentRoute = LettersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkActivityCompleted = { onCompleteActivity(LettersActivityId.FIND_LETTER) }
                    )
                }

                LettersActivityId.MATCH_PICTURE -> {
                    MatchLetterToPictureActivityScreen(
                        sectionMode = sectionMode,
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isActivityCompleted = LettersActivityId.MATCH_PICTURE in uiState.settings.completedLettersActivities,
                        onSelectSectionMode = { sectionMode = it },
                        onBackToHub = { currentRoute = LettersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkActivityCompleted = { onCompleteActivity(LettersActivityId.MATCH_PICTURE) }
                    )
                }

                LettersActivityId.MISSING_LETTER -> {
                    CompleteMissingLetterActivityScreen(
                        sectionMode = sectionMode,
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isActivityCompleted = LettersActivityId.MISSING_LETTER in uiState.settings.completedLettersActivities,
                        onSelectSectionMode = { sectionMode = it },
                        onBackToHub = { currentRoute = LettersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkActivityCompleted = { onCompleteActivity(LettersActivityId.MISSING_LETTER) }
                    )
                }

                LettersActivityId.LETTER_QUIZ -> {
                    LetterQuizActivityScreen(
                        sectionMode = sectionMode,
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isActivityCompleted = LettersActivityId.LETTER_QUIZ in uiState.settings.completedLettersActivities,
                        onSelectSectionMode = { sectionMode = it },
                        onBackToHub = { currentRoute = LettersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkActivityCompleted = { onCompleteActivity(LettersActivityId.LETTER_QUIZ) }
                    )
                }
            }
        }
    }
}

/**
 * MAIN SCREEN: ARABIC & ENGLISH LETTERS LEARNING WORLD HUB
 * - Section 1: الحروف العربية | Arabic Letters (28 isolated letters أ – ي)
 * - Section 2: الحروف الإنجليزية | English Letters (26 letters A a – Z z)
 * - 3D Cartoon Alphabet Blocks illustration
 * - Lulu welcoming the child with voice playback
 * - Organized, attractive grid of all 28 Arabic or 26 English letters
 * - 6 Interactive Letters Activities in a 2-column grid
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LettersWorldHubScreen(
    uiState: FunLearningUiState,
    sectionMode: LettersSectionMode,
    learnedLetterIds: Set<String>,
    onSelectSectionMode: (LettersSectionMode) -> Unit,
    onNavigateHome: () -> Unit,
    onSelectAppLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onOpenLetterLesson: (Int) -> Unit,
    onOpenActivity: (LettersActivityId, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabicApp = uiState.isArabic
    val isArabicSection = sectionMode == LettersSectionMode.ARABIC
    val soundEnabled = uiState.settings.soundEnabled
    val completedActivities = uiState.settings.completedLettersActivities
    val lettersList = LettersCurriculum.lettersForSection(sectionMode)

    val sectionAccent = if (isArabicSection) ArabicAccent else EnglishAccent
    val sectionCardBg = if (isArabicSection) ArabicCardBg else EnglishCardBg

    val welcomeText = if (isArabicSection) {
        "مرحباً يا بطلتي! أهلاً بكِ في عالم الحروف العربية! اختاري أي حرف من الألف إلى الياء لنتعرّف على اسمه وصوته وكلمته المصوّرة!"
    } else {
        if (isArabicApp) {
            "مرحباً! أهلاً بكِ في عالم الحروف الإنجليزية من A إلى Z! اضغطي على أي حرف لسماع نطقه ورؤية صورته!"
        } else {
            "Welcome to Letters World! Tap any letter from A to Z to hear its sound and see its colorful picture word!"
        }
    }

    LaunchedEffect(sectionMode) {
        val voiceText = if (isArabicSection) {
            "مرحباً يا بطلتي! هيا نتعرف على الحروف العربية من الألف إلى الياء!"
        } else {
            "Welcome to English Letters World! Let's learn letters from A to Z!"
        }
        onSpeakLulu(voiceText, sectionMode.language)
    }

    PlayfulSkyBackground(
        modifier = modifier
            .fillMaxSize()
            .testTag("letters_world_hub"),
        accentTint = sectionAccent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 940.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Top Navigation Bar with Home, Language & Sound Controls
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = BorderStroke(2.5.dp, sectionAccent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Bouncy3DButton(
                        text = if (isArabicApp) "الرئيسية" else "Home",
                        onClick = onNavigateHome,
                        containerColor = LuluPink,
                        shadowColor = LuluPinkDark,
                        contentColor = Color.White,
                        icon = Icons.Filled.Home,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        testTagName = "letters_hub_home_button"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageSelectorSwitch(
                            currentLanguage = uiState.settings.language,
                            onSelectLanguage = onSelectAppLanguage
                        )

                        Bouncy3DIconButton(
                            icon = if (soundEnabled) {
                                Icons.AutoMirrored.Filled.VolumeUp
                            } else {
                                Icons.AutoMirrored.Filled.VolumeOff
                            },
                            contentDescription = if (soundEnabled) "Sound On" else "Sound Off",
                            onClick = onToggleSound,
                            containerColor = if (soundEnabled) MeadowGreen else Color(0xFFCFD8DC),
                            shadowColor = if (soundEnabled) MeadowGreenDark else Color(0xFF78909C),
                            iconTint = if (soundEnabled) Color.White else DeepNavyText
                        )
                    }
                }
            }

            // 2. Hero Card: 3D Cartoon Alphabet Blocks + Two Section Buttons + Lulu Welcome
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = sectionCardBg),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 3D Cartoon Alphabet Blocks Illustration Banner
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(2.5.dp, Color.White),
                        shadowElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isArabicSection) {
                                    R.drawable.img_cat_arabic_letters_1794073876696
                                } else {
                                    R.drawable.img_cat_english_letters_1794073878108
                                }
                            ),
                            contentDescription = if (isArabicSection) "الحروف العربية" else "English Letters",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Header + Progress Badges (Learned Letters + Completed Activities)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabicSection) {
                                    if (isArabicApp) "عالم الحروف العربية (٢٨ حرفاً)" else "Arabic Letters World (28 Letters)"
                                } else {
                                    if (isArabicApp) "عالم الحروف الإنجليزية (A – Z)" else "English Letters World (A – Z)"
                                },
                                color = DeepNavyText,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabicApp) 26.sp else 23.sp
                                )
                            )
                            Text(
                                text = if (isArabicSection) {
                                    "تعلّمتِ ${uiState.settings.arabicLettersLearnedCount} من ٢٨ حرفاً عربياً"
                                } else {
                                    if (isArabicApp) {
                                        "تعلّمتِ ${uiState.settings.englishLettersLearnedCount} من ٢٦ حرفاً إنجليزياً"
                                    } else {
                                        "Learned ${uiState.settings.englishLettersLearnedCount} of 26 English letters"
                                    }
                                },
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        // Stars & Activities Progress Pill
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color.White,
                            border = BorderStroke(2.dp, StarGoldBorder),
                            shadowElevation = 3.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = StarGoldBorder,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "${completedActivities.size} / 6",
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.5.dp, sectionAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    ) {
                        val letterProgress = learnedLetterIds.size.toFloat() / lettersList.size.coerceAtLeast(1)
                        val combinedProgress = maxOf(uiState.settings.lettersWorldProgressFraction, letterProgress).coerceAtLeast(0.08f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(combinedProgress.coerceIn(0f, 1f))
                                .height(18.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(sectionAccent, SunnyYellow, MeadowGreen)
                                    )
                                )
                        )
                    }

                    // Lulu Welcome Speech Bubble with Replay Voice Button
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val text = if (isArabicSection) {
                                    "مرحباً يا بطلتي! هيا نتعرف على الحروف العربية من الألف إلى الياء!"
                                } else {
                                    "Let's learn the English letters from A to Z together!"
                                }
                                onSpeakLulu(text, sectionMode.language)
                            },
                        shape = RoundedCornerShape(22.dp),
                        color = Color.White,
                        border = BorderStroke(2.dp, SunnyYellowDark)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(72.dp),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(2.5.dp, LuluPink),
                                color = Color(0xFFCFE9FF)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                                    contentDescription = "Lulu",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = welcomeText,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabicApp) 17.sp else 15.sp
                                    )
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = null,
                                        tint = LuluPink,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isArabicApp) "اضغط لسماع صوت لولو 🔊" else "Tap to hear Lulu's voice 🔊",
                                        color = LuluPink,
                                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
                                    )
                                }
                            }
                        }
                    }

                    // Two Main Section Switch Buttons: 1. الحروف العربية | Arabic Letters  2. الحروف الإنجليزية | English Letters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = "الحروف العربية (أ – ي)",
                            onClick = { onSelectSectionMode(LettersSectionMode.ARABIC) },
                            modifier = Modifier.weight(1f),
                            containerColor = if (isArabicSection) ArabicAccent else Color.White,
                            shadowColor = if (isArabicSection) ArabicShadow else Color(0xFFB0BEC5),
                            contentColor = if (isArabicSection) Color.White else DeepNavyText,
                            fontSize = 15.sp,
                            testTagName = "section_arabic_letters_btn"
                        )

                        Bouncy3DButton(
                            text = "English Letters (A – Z)",
                            onClick = { onSelectSectionMode(LettersSectionMode.ENGLISH) },
                            modifier = Modifier.weight(1f),
                            containerColor = if (!isArabicSection) EnglishAccent else Color.White,
                            shadowColor = if (!isArabicSection) EnglishShadow else Color(0xFFB0BEC5),
                            contentColor = if (!isArabicSection) Color.White else DeepNavyText,
                            fontSize = 15.sp,
                            testTagName = "section_english_letters_btn"
                        )
                    }
                }
            }

            // 3. Organized, Attractive Grid of All 28 Arabic Letters or All 26 English Letters
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                border = BorderStroke(2.5.dp, sectionAccent.copy(alpha = 0.65f)),
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isArabicSection) {
                            if (isArabicApp) {
                                "لوحة الحروف العربية (٢٨ حرفاً) — اضغطي على أي حرف لتعلّمه!"
                            } else {
                                "All 28 Arabic Letters — Tap any letter block to learn it!"
                            }
                        } else {
                            if (isArabicApp) {
                                "لوحة الحروف الإنجليزية (٢٦ حرفاً) — اضغطي على أي حرف لتعلّمه!"
                            } else {
                                "All 26 English Letters (A a – Z z) — Tap any letter block!"
                            }
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isArabicApp) 19.sp else 17.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        maxItemsInEachRow = 7
                    ) {
                        lettersList.forEach { letter ->
                            val isLearned = letter.id in learnedLetterIds
                            Surface(
                                modifier = Modifier
                                    .size(width = 96.dp, height = 98.dp)
                                    .testTag("letter_grid_tile_${letter.id}")
                                    .clickable {
                                        onOpenLetterLesson(letter.index)
                                    },
                                shape = RoundedCornerShape(22.dp),
                                color = letter.cardBgColor,
                                border = BorderStroke(
                                    width = if (isLearned) 2.5.dp else 2.dp,
                                    color = if (isLearned) MeadowGreen else letter.color
                                ),
                                shadowElevation = 4.dp
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp)
                                ) {
                                    if (isLearned) {
                                        Icon(
                                            imageVector = Icons.Filled.CheckCircle,
                                            contentDescription = null,
                                            tint = MeadowGreen,
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(18.dp)
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.align(Alignment.Center),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = letter.color,
                                            border = BorderStroke(1.5.dp, Color.White)
                                        ) {
                                            Text(
                                                text = letter.gridDisplaySymbol,
                                                color = Color.White,
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = if (isArabicSection) 26.sp else 21.sp
                                                ),
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                            )
                                        }

                                        Text(
                                            text = "${letter.emoji} ${letter.primaryWord}",
                                            color = DeepNavyText,
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            ),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. The 6 Interactive Letters World Activities (2-Column Grid)
            Text(
                text = if (isArabicApp) {
                    "أنشطة وألعاب عالم الحروف (٦ أنشطة تفاعلية)"
                } else {
                    "Letters World Activities & Games (6 Interactive Activities)"
                },
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabicApp) 22.sp else 20.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            val activityPairs = LettersActivityId.entries.chunked(2)
            activityPairs.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    rowItems.forEach { activity ->
                        val isDone = activity in completedActivities
                        LettersActivityCard(
                            activity = activity,
                            language = uiState.settings.language,
                            isCompleted = isDone,
                            onClick = { onOpenActivity(activity, 1) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun LettersActivityCard(
    activity: LettersActivityId,
    language: AppLanguage,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == AppLanguage.AR

    Surface(
        modifier = modifier
            .testTag("letters_activity_${activity.key}")
            .shadow(8.dp, RoundedCornerShape(26.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(26.dp),
        color = activity.cardBgColor,
        border = BorderStroke(2.5.dp, Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = activity.accentColor,
                    border = BorderStroke(1.5.dp, Color.White)
                ) {
                    Text(
                        text = activity.badgeEmoji,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = 22.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(1.5.dp, if (isCompleted) StarGoldBorder else Color(0xFFCFD8DC))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = null,
                            tint = if (isCompleted) StarGoldBorder else Color(0xFFB0BEC5),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (isCompleted) {
                                if (isArabic) "مكتمل" else "Done"
                            } else {
                                if (isArabic) "جديد" else "New"
                            },
                            color = DeepNavyText,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                        )
                    }
                }
            }

            Text(
                text = activity.title(language),
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabic) 20.sp else 18.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Text(
                text = activity.subtitle(language),
                color = SoftSlateText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabic) 14.sp else 13.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Bouncy3DButton(
                text = if (isArabic) "ابدأ النشاط" else "Play Now",
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                containerColor = activity.accentColor,
                shadowColor = activity.shadowColor,
                contentColor = Color.White,
                icon = Icons.Filled.PlayArrow,
                fontSize = if (isArabic) 16.sp else 15.sp,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
            )
        }
    }
}

/**
 * Shared Top Bar for all 6 Letters World Activities.
 */
@Composable
fun LettersActivityTopBar(
    title: String,
    accentColor: Color,
    shadowColor: Color,
    isArabic: Boolean,
    soundEnabled: Boolean,
    isActivityCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        color = Color.White,
        border = BorderStroke(2.5.dp, accentColor)
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
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = if (isArabic) "الحروف" else "Letters Hub",
                    onClick = onBackToHub,
                    containerColor = accentColor,
                    shadowColor = shadowColor,
                    iconTint = Color.White,
                    badgeLabel = if (isArabic) "الحروف" else "Letters",
                    testTagName = "back_to_letters_hub_btn"
                )

                Bouncy3DIconButton(
                    icon = Icons.Filled.Home,
                    contentDescription = if (isArabic) "الرئيسية" else "Home",
                    onClick = onNavigateHome,
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    iconTint = Color.White,
                    testTagName = "letters_activity_home_btn"
                )
            }

            Text(
                text = title,
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabic) 20.sp else 18.sp
                ),
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isActivityCompleted) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = StarGoldBorder,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Bouncy3DIconButton(
                    icon = if (soundEnabled) {
                        Icons.AutoMirrored.Filled.VolumeUp
                    } else {
                        Icons.AutoMirrored.Filled.VolumeOff
                    },
                    contentDescription = if (soundEnabled) "Sound On" else "Sound Off",
                    onClick = onToggleSound,
                    containerColor = if (soundEnabled) MeadowGreen else Color(0xFFCFD8DC),
                    shadowColor = if (soundEnabled) MeadowGreenDark else Color(0xFF78909C),
                    iconTint = if (soundEnabled) Color.White else DeepNavyText
                )
            }
        }
    }
}

/**
 * ACTIVITY 1 & DEDICATED LETTER SCREEN: MEET THE LETTER (تعرّف على الحرف)
 * Includes:
 * 1. Very large colorful Arabic letter (or Uppercase + Lowercase English letter)
 * 2. The letter name + clear dot guide for Arabic letters
 * 3. Clear pronunciation via Lulu's voice ("هيا نتعرف على حرف الباء!")
 * 4. Beautiful 3D cartoon illustration of a word beginning with that letter
 * 5. The word written clearly with highlighted initial letter
 * 6. Speaker button to hear the letter and the word
 * 7. Simple entrance animation introducing the letter
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeetTheLetterActivityScreen(
    initialLetterIndex: Int,
    sectionMode: LettersSectionMode,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    learnedLetterIds: Set<String>,
    isActivityCompleted: Boolean,
    onSelectSectionMode: (LettersSectionMode) -> Unit,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkLetterLearned: (String) -> Unit,
    onMarkActivityCompleted: () -> Unit,
    onOpenTracingForLetter: (Int) -> Unit
) {
    val isArabicApp = appLanguage == AppLanguage.AR
    val letters = LettersCurriculum.lettersForSection(sectionMode)
    var currentIndex by remember(sectionMode) {
        mutableIntStateOf(initialLetterIndex.coerceIn(1, letters.size))
    }
    var visitedCount by remember(sectionMode) { mutableIntStateOf(1) }

    val lesson = letters[(currentIndex - 1).coerceIn(0, letters.lastIndex)]
    val isArabicSection = sectionMode == LettersSectionMode.ARABIC

    LaunchedEffect(lesson.id) {
        onSpeakLulu(lesson.luluMeetSentence, sectionMode.language)
        onMarkLetterLearned(lesson.id)
        if (visitedCount >= 3) {
            onMarkActivityCompleted()
        }
    }

    PlayfulSkyBackground(accentTint = lesson.color) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 880.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LettersActivityTopBar(
                title = if (isArabicApp) "تعرّف على الحرف" else "Meet the Letter",
                accentColor = lesson.color,
                shadowColor = lesson.shadowColor,
                isArabic = isArabicApp,
                soundEnabled = soundEnabled,
                isActivityCompleted = isActivityCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Section Switcher (Arabic Letters / English Letters)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = "الحروف العربية (أ – ي)",
                    onClick = { onSelectSectionMode(LettersSectionMode.ARABIC) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (isArabicSection) lesson.color else Color.White,
                    shadowColor = if (isArabicSection) lesson.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
                Bouncy3DButton(
                    text = "English Letters (A – Z)",
                    onClick = { onSelectSectionMode(LettersSectionMode.ENGLISH) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (!isArabicSection) lesson.color else Color.White,
                    shadowColor = if (!isArabicSection) lesson.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (!isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
            }

            // Lulu Voice Introduction Banner
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSpeakLulu(lesson.luluMeetSentence, sectionMode.language)
                    },
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(2.dp, SunnyYellowDark),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(62.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(2.dp, LuluPink),
                        color = Color(0xFFCFE9FF)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                            contentDescription = "Lulu",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = lesson.luluMeetSentence,
                            color = DeepNavyText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isArabicApp) 18.sp else 16.sp
                            )
                        )
                        Text(
                            text = if (isArabicApp) "اضغطي لإعادة سماع لولو 🔊" else "Tap to replay Lulu's voice 🔊",
                            color = LuluPink,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                        )
                    }
                }
            }

            // Main Letter Showcase Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, lesson.color)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Animated Large 3D Letter Block + Name & Dots Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedLetter3DBlock(
                            lesson = lesson,
                            isLearned = lesson.id in learnedLetterIds,
                            onTapSpeak = {
                                onSpeakLulu(lesson.luluPronunciationSentence, sectionMode.language)
                            },
                            modifier = Modifier.testTag("large_letter_block")
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = lesson.letterDisplayName,
                                color = DeepNavyText,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabicSection) 32.sp else 26.sp
                                ),
                                textAlign = TextAlign.Center
                            )

                            // Clear Dots / Case Badge so similar letters are never confused
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = lesson.cardBgColor,
                                border = BorderStroke(1.5.dp, lesson.color)
                            ) {
                                Text(
                                    text = if (isArabicApp) lesson.dotsDescriptionAr else lesson.dotsDescriptionEn,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    textAlign = TextAlign.Center
                                )
                            }

                            Text(
                                text = "${lesson.index} / ${letters.size}",
                                color = SoftSlateText,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }

                    // 2. Beautiful 3D Cartoon Illustration Card of the Word Beginning with that Letter
                    LetterWord3DIllustrationCard(
                        lesson = lesson,
                        appLanguage = appLanguage,
                        onTapSpeakWord = {
                            onSpeakLulu(lesson.luluPronunciationSentence, sectionMode.language)
                        }
                    )

                    // 3. Action Buttons: Hear Pronunciation & Trace This Letter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (isArabicApp) "استمع للنطق 🔊" else "Hear Letter & Word 🔊",
                            onClick = {
                                onSpeakLulu(lesson.luluPronunciationSentence, sectionMode.language)
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = SunnyYellow,
                            shadowColor = SunnyYellowDark,
                            contentColor = DeepNavyText,
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            fontSize = if (isArabicApp) 17.sp else 15.sp,
                            testTagName = "letter_pronunciation_speaker_btn"
                        )

                        Bouncy3DButton(
                            text = if (isArabicApp) "تدرّب على كتابته ✏️" else "Trace Letter ✏️",
                            onClick = {
                                onOpenTracingForLetter(lesson.index)
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = lesson.color,
                            shadowColor = lesson.shadowColor,
                            contentColor = Color.White,
                            icon = Icons.Filled.Edit,
                            fontSize = if (isArabicApp) 17.sp else 15.sp,
                            testTagName = "open_letter_tracing_btn"
                        )
                    }

                    // 4. Previous & Next Letter Navigation Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (isArabicApp) "الحرف السابق" else "Previous",
                            onClick = {
                                currentIndex = if (currentIndex > 1) currentIndex - 1 else letters.size
                                visitedCount++
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = Color(0xFFECEFF1),
                            shadowColor = Color(0xFF90A4AE),
                            contentColor = DeepNavyText,
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            fontSize = 16.sp,
                            testTagName = "prev_letter_btn"
                        )

                        Bouncy3DButton(
                            text = if (isArabicApp) "الحرف التالي" else "Next",
                            onClick = {
                                currentIndex = if (currentIndex < letters.size) currentIndex + 1 else 1
                                visitedCount++
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            fontSize = 16.sp,
                            testTagName = "next_letter_btn"
                        )
                    }
                }
            }

            // Quick Alphabet Strip to Jump to Any Letter
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(2.dp, lesson.color.copy(alpha = 0.4f))
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    letters.forEach { item ->
                        val isSelected = item.index == currentIndex
                        Surface(
                            modifier = Modifier
                                .size(44.dp)
                                .clickable {
                                    currentIndex = item.index
                                    visitedCount++
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) item.color else item.cardBgColor,
                            border = BorderStroke(1.5.dp, if (isSelected) Color.White else item.color)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isArabicSection) item.isolatedSymbol else item.uppercaseSymbol,
                                    color = if (isSelected) Color.White else DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * ACTIVITY 2: LETTER TRACING (كتابة وتتبع الحرف)
 * - Displays a large dotted letter
 * - Allows the child to trace using their finger on a working Canvas drawing surface
 * - Shows a visual writing guide with directional stroke order (Arabic RTL / English LTR)
 * - Includes Clear and Retry buttons
 * - Provides encouraging voice & visual feedback
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LetterTracingActivityScreen(
    initialLetterIndex: Int,
    sectionMode: LettersSectionMode,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isActivityCompleted: Boolean,
    onSelectSectionMode: (LettersSectionMode) -> Unit,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkLetterLearned: (String) -> Unit,
    onMarkActivityCompleted: () -> Unit
) {
    val isArabicApp = appLanguage == AppLanguage.AR
    val letters = LettersCurriculum.lettersForSection(sectionMode)
    var currentIndex by remember(sectionMode) {
        mutableIntStateOf(initialLetterIndex.coerceIn(1, letters.size))
    }
    val lesson = letters[(currentIndex - 1).coerceIn(0, letters.lastIndex)]
    val isArabicSection = sectionMode == LettersSectionMode.ARABIC

    // For English letters, allow tracing Uppercase (A), Lowercase (a), or Both (A a)
    var englishCaseMode by remember { mutableIntStateOf(0) } // 0 = Uppercase, 1 = Lowercase, 2 = Both
    val traceSymbol = if (isArabicSection) {
        lesson.isolatedSymbol
    } else {
        when (englishCaseMode) {
            0 -> lesson.uppercaseSymbol
            1 -> lesson.lowercaseSymbol
            else -> "${lesson.uppercaseSymbol} ${lesson.lowercaseSymbol}"
        }
    }

    var strokes by remember(lesson.id, englishCaseMode) { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var hitCheckpoints by remember(lesson.id, englishCaseMode) { mutableStateOf<Set<Int>>(emptySet()) }
    var showCelebration by remember(lesson.id, englishCaseMode) { mutableStateOf(false) }

    LaunchedEffect(lesson.id) {
        onSpeakLulu(lesson.luluTracePrompt, sectionMode.language)
    }

    PlayfulSkyBackground(accentTint = lesson.color) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 880.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LettersActivityTopBar(
                title = if (isArabicApp) "كتابة وتتبع الحرف" else "Letter Tracing",
                accentColor = lesson.color,
                shadowColor = lesson.shadowColor,
                isArabic = isArabicApp,
                soundEnabled = soundEnabled,
                isActivityCompleted = isActivityCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Section Mode Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = "الحروف العربية (أ – ي)",
                    onClick = { onSelectSectionMode(LettersSectionMode.ARABIC) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (isArabicSection) lesson.color else Color.White,
                    shadowColor = if (isArabicSection) lesson.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
                Bouncy3DButton(
                    text = "English Letters (A – Z)",
                    onClick = { onSelectSectionMode(LettersSectionMode.ENGLISH) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (!isArabicSection) lesson.color else Color.White,
                    shadowColor = if (!isArabicSection) lesson.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (!isArabicSection) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
            }

            // Tracing Stage Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, lesson.color)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header with letter name, word picture, and stroke direction guide
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${lesson.letterDisplayName}  •  ${lesson.emoji} ${lesson.primaryWord}",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabicApp) 22.sp else 20.sp
                                )
                            )
                            Text(
                                text = if (isArabicApp) lesson.strokeDirectionHintAr else lesson.strokeDirectionHintEn,
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Bouncy3DIconButton(
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak",
                            onClick = { onSpeakLulu(lesson.luluTracePrompt, sectionMode.language) },
                            containerColor = SunnyYellow,
                            shadowColor = SunnyYellowDark,
                            iconTint = DeepNavyText
                        )
                    }

                    // For English letters: Uppercase / Lowercase / Both selector
                    if (!isArabicSection) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                0 to "Capital (${lesson.uppercaseSymbol})",
                                1 to "Small (${lesson.lowercaseSymbol})",
                                2 to "Both (${lesson.uppercaseSymbol} ${lesson.lowercaseSymbol})"
                            ).forEach { (modeIdx, label) ->
                                val selected = englishCaseMode == modeIdx
                                Bouncy3DButton(
                                    text = label,
                                    onClick = { englishCaseMode = modeIdx },
                                    modifier = Modifier.weight(1f),
                                    containerColor = if (selected) lesson.color else Color(0xFFECEFF1),
                                    shadowColor = if (selected) lesson.shadowColor else Color(0xFFB0BEC5),
                                    contentColor = if (selected) Color.White else DeepNavyText,
                                    fontSize = 13.sp,
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                                )
                            }
                        }
                    } else {
                        // Clear Arabic dots badge reminder
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = lesson.cardBgColor,
                            border = BorderStroke(1.5.dp, lesson.color)
                        ) {
                            Text(
                                text = "تذكّري النقاط: ${lesson.dotsDescriptionAr}",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // Interactive Finger-Tracing Canvas
                    LetterFingerTracingPad(
                        lesson = lesson,
                        traceSymbol = traceSymbol,
                        strokes = strokes,
                        hitCheckpoints = hitCheckpoints,
                        onStartNewStroke = { pt, canvasSize ->
                            strokes = strokes + listOf(listOf(pt))
                            val updatedHits = findLetterHitCheckpoints(pt, canvasSize, lesson, hitCheckpoints)
                            hitCheckpoints = updatedHits
                            if (!showCelebration && updatedHits.size >= lesson.traceCheckpoints.size) {
                                showCelebration = true
                                onMarkLetterLearned(lesson.id)
                                onMarkActivityCompleted()
                                val praise = if (isArabicSection) {
                                    "ممتاز يا بطلة! كتبتِ حرف ال${lesson.letterNameAr} بشكل رائع!"
                                } else {
                                    "Great job! You wrote letter ${lesson.uppercaseSymbol}!"
                                }
                                onSpeakLulu(praise, sectionMode.language)
                            }
                        },
                        onAddPoint = { pt, canvasSize ->
                            if (strokes.isEmpty()) {
                                strokes = listOf(listOf(pt))
                            } else {
                                val updatedLast = strokes.last() + pt
                                strokes = strokes.dropLast(1) + listOf(updatedLast)
                            }
                            val updatedHits = findLetterHitCheckpoints(pt, canvasSize, lesson, hitCheckpoints)
                            hitCheckpoints = updatedHits
                            if (!showCelebration && updatedHits.size >= lesson.traceCheckpoints.size) {
                                showCelebration = true
                                onMarkLetterLearned(lesson.id)
                                onMarkActivityCompleted()
                                val praise = if (isArabicSection) {
                                    "ممتاز يا بطلة! كتبتِ حرف ال${lesson.letterNameAr} بشكل رائع!"
                                } else {
                                    "Great job! You wrote letter ${lesson.uppercaseSymbol}!"
                                }
                                onSpeakLulu(praise, sectionMode.language)
                            }
                        }
                    )

                    // Encouraging Celebration Banner
                    AnimatedVisibility(
                        visible = showCelebration,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = Color(0xFFE8F5E9),
                            border = BorderStroke(2.dp, MeadowGreen)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = MeadowGreen,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = if (isArabicApp) {
                                        "ممتاز يا بطلة! خطكِ جميل جداً في كتابة الحرف! ⭐"
                                    } else {
                                        "Great job! Your handwriting looks wonderful! ⭐"
                                    },
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Clear & Retry Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (isArabicApp) "مسح اللوحة" else "Clear",
                            onClick = {
                                strokes = emptyList()
                                hitCheckpoints = emptySet()
                                showCelebration = false
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = Color(0xFFFFECEF),
                            shadowColor = LuluPinkDark,
                            contentColor = DeepNavyText,
                            icon = Icons.Filled.DeleteSweep,
                            fontSize = 15.sp,
                            testTagName = "clear_letter_trace_btn"
                        )

                        Bouncy3DButton(
                            text = if (isArabicApp) "إعادة المحاولة" else "Retry",
                            onClick = {
                                strokes = emptyList()
                                hitCheckpoints = emptySet()
                                showCelebration = false
                                onSpeakLulu(lesson.luluTracePrompt, sectionMode.language)
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = SunnyYellow,
                            shadowColor = SunnyYellowDark,
                            contentColor = DeepNavyText,
                            icon = Icons.Filled.Refresh,
                            fontSize = 15.sp,
                            testTagName = "retry_letter_trace_btn"
                        )
                    }

                    // Previous / Next Letter Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (isArabicApp) "الحرف السابق" else "Previous",
                            onClick = {
                                currentIndex = if (currentIndex > 1) currentIndex - 1 else letters.size
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = Color(0xFFECEFF1),
                            shadowColor = Color(0xFF90A4AE),
                            contentColor = DeepNavyText,
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            fontSize = 15.sp
                        )

                        Bouncy3DButton(
                            text = if (isArabicApp) "الحرف التالي" else "Next Letter",
                            onClick = {
                                currentIndex = if (currentIndex < letters.size) currentIndex + 1 else 1
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Quick Selector Bar for All Letters
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(2.dp, lesson.color.copy(alpha = 0.4f))
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    letters.forEach { item ->
                        val isSelected = item.index == currentIndex
                        Surface(
                            modifier = Modifier
                                .size(44.dp)
                                .clickable { currentIndex = item.index },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) item.color else item.cardBgColor,
                            border = BorderStroke(1.5.dp, if (isSelected) Color.White else item.color)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isArabicSection) item.isolatedSymbol else item.uppercaseSymbol,
                                    color = if (isSelected) Color.White else DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
