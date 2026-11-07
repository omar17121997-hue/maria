package com.example.ui.screens.numbers

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
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.width
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
import com.example.model.NumbersActivityId
import com.example.model.NumbersCurriculum
import com.example.ui.FunLearningUiState
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.NumberFingerTracingPad
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.components.QuantityObjectsGrid
import com.example.ui.components.findHitCheckpoints
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MagicPurple
import com.example.ui.theme.MagicPurpleDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.NumbersAccent
import com.example.ui.theme.NumbersCardBg
import com.example.ui.theme.NumbersShadow
import com.example.ui.theme.SkyCyanDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark

sealed interface NumbersWorldRoute {
    data object Hub : NumbersWorldRoute
    data class Activity(val id: NumbersActivityId, val initialNumber: Int = 1) : NumbersWorldRoute
}

@Composable
fun NumbersWorldScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onCompleteActivity: (NumbersActivityId) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf<NumbersWorldRoute>(NumbersWorldRoute.Hub) }
    // Numeral mode (Arabic ١-١٠ vs English 1-10); defaults to current app language and can be switched with the Arabic/English Numbers buttons
    var numeralLang by remember(uiState.settings.language) {
        mutableStateOf(uiState.settings.language)
    }

    BackHandler {
        if (currentRoute != NumbersWorldRoute.Hub) {
            currentRoute = NumbersWorldRoute.Hub
        } else {
            onNavigateHome()
        }
    }

    when (val route = currentRoute) {
        is NumbersWorldRoute.Hub -> {
            NumbersWorldHubScreen(
                uiState = uiState,
                numeralLang = numeralLang,
                onSelectNumeralLang = { lang ->
                    numeralLang = lang
                    val msg = if (lang == AppLanguage.AR) {
                        "مرحباً يا بطلتي! هيا نتعلم الأرقام العربية من واحد إلى عشرة."
                    } else {
                        "Let's learn English numbers from one to ten together!"
                    }
                    onSpeakLulu(msg, lang)
                },
                onNavigateHome = onNavigateHome,
                onSelectAppLanguage = onSelectLanguage,
                onToggleSound = onToggleSound,
                onSpeakLulu = onSpeakLulu,
                onOpenActivity = { activityId, startNum ->
                    currentRoute = NumbersWorldRoute.Activity(activityId, startNum)
                },
                modifier = modifier
            )
        }

        is NumbersWorldRoute.Activity -> {
            when (route.id) {
                NumbersActivityId.LEARN -> {
                    LearnNumbersActivityScreen(
                        initialNumber = route.initialNumber,
                        appLanguage = uiState.settings.language,
                        numeralLanguage = numeralLang,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = NumbersActivityId.LEARN in uiState.settings.completedNumbersActivities,
                        onSelectNumeralLanguage = { numeralLang = it },
                        onBackToHub = { currentRoute = NumbersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(NumbersActivityId.LEARN) }
                    )
                }

                NumbersActivityId.TRACING -> {
                    NumberTracingActivityScreen(
                        initialNumber = route.initialNumber,
                        appLanguage = uiState.settings.language,
                        numeralLanguage = numeralLang,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = NumbersActivityId.TRACING in uiState.settings.completedNumbersActivities,
                        onSelectNumeralLanguage = { numeralLang = it },
                        onBackToHub = { currentRoute = NumbersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(NumbersActivityId.TRACING) }
                    )
                }

                NumbersActivityId.COUNTING -> {
                    CountObjectsActivityScreen(
                        appLanguage = uiState.settings.language,
                        numeralLanguage = numeralLang,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = NumbersActivityId.COUNTING in uiState.settings.completedNumbersActivities,
                        onBackToHub = { currentRoute = NumbersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(NumbersActivityId.COUNTING) }
                    )
                }

                NumbersActivityId.MATCHING -> {
                    MatchNumberActivityScreen(
                        appLanguage = uiState.settings.language,
                        numeralLanguage = numeralLang,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = NumbersActivityId.MATCHING in uiState.settings.completedNumbersActivities,
                        onBackToHub = { currentRoute = NumbersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(NumbersActivityId.MATCHING) }
                    )
                }

                NumbersActivityId.QUIZ -> {
                    NumberQuizActivityScreen(
                        appLanguage = uiState.settings.language,
                        numeralLanguage = numeralLang,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = NumbersActivityId.QUIZ in uiState.settings.completedNumbersActivities,
                        onBackToHub = { currentRoute = NumbersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(NumbersActivityId.QUIZ) }
                    )
                }

                NumbersActivityId.ORDER -> {
                    NumberOrderActivityScreen(
                        appLanguage = uiState.settings.language,
                        numeralLanguage = numeralLang,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = NumbersActivityId.ORDER in uiState.settings.completedNumbersActivities,
                        onBackToHub = { currentRoute = NumbersWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(NumbersActivityId.ORDER) }
                    )
                }
            }
        }
    }
}

/**
 * FIRST SCREEN: NUMBERS WORLD HUB
 * Features:
 * - Clear Home button + Language & Sound controls
 * - Large colorful 3D illustration of numbers
 * - Lulu welcoming the child with voice playback
 * - Arabic numbers button (١–١٠) & English numbers button (1–10)
 * - Interactive Numbers from 1 to 10 strip
 * - Real Progress Indicator for the 6 Numbers World activities
 * - 6 Activity Cards in a 2-column grid
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NumbersWorldHubScreen(
    uiState: FunLearningUiState,
    numeralLang: AppLanguage,
    onSelectNumeralLang: (AppLanguage) -> Unit,
    onNavigateHome: () -> Unit,
    onSelectAppLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onOpenActivity: (NumbersActivityId, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = uiState.isArabic
    val soundEnabled = uiState.settings.soundEnabled
    val completedSet = uiState.settings.completedNumbersActivities

    val welcomeText = if (isArabic) {
        "مرحباً يا بطلتي! هيا نتعلم الأرقام معاً من ١ إلى ١٠ ونجمع النجوم الذهبية!"
    } else {
        "Hello little explorer! Let's learn numbers from 1 to 10 together and collect golden stars!"
    }

    LaunchedEffect(Unit) {
        onSpeakLulu(welcomeText, uiState.settings.language)
    }

    PlayfulSkyBackground(
        modifier = modifier
            .fillMaxSize()
            .testTag("numbers_world_hub"),
        accentTint = NumbersAccent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 920.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Top Navigation Bar with Clear Home Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = BorderStroke(2.5.dp, NumbersAccent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Bouncy3DButton(
                        text = if (isArabic) "الرئيسية" else "Home",
                        onClick = onNavigateHome,
                        containerColor = LuluPink,
                        shadowColor = LuluPinkDark,
                        contentColor = Color.White,
                        icon = Icons.Filled.Home,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        testTagName = "numbers_hub_home_button"
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

            // 2. Hero Card: Large 3D Numbers Illustration + Lulu Welcome + Progress Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = NumbersCardBg),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Large Colorful 3D Illustration of Numbers
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(2.5.dp, Color.White),
                        shadowElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_card_numbers_letters_1794072209030),
                            contentDescription = if (isArabic) "عالم الأرقام" else "Numbers World",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Title + Progress Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "عالم الأرقام الممتع" else "Numbers Learning World",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabic) 27.sp else 24.sp
                                )
                            )
                            Text(
                                text = if (isArabic) "تعلّم الأرقام من ١ إلى ١٠ مع لولو" else "Learn Numbers 1 to 10 with Lulu",
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        // Stars & Progress Pill
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
                                    text = "${completedSet.size} / 6",
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
                            .border(1.5.dp, NumbersAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    ) {
                        val fraction = uiState.settings.numbersWorldProgressFraction.coerceAtLeast(0.08f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .height(18.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(NumbersAccent, SunnyYellow, MeadowGreen)
                                    )
                                )
                        )
                    }

                    // Lulu Welcome Speech Bubble with Replay Voice Button
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(welcomeText, uiState.settings.language) },
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
                                        fontSize = if (isArabic) 18.sp else 16.sp
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
                                        text = if (isArabic) "اضغط لسماع صوت لولو 🔊" else "Tap to hear Lulu's voice 🔊",
                                        color = LuluPink,
                                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
                                    )
                                }
                            }
                        }
                    }

                    // Arabic Numbers Button & English Numbers Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val isArNum = numeralLang == AppLanguage.AR
                        Bouncy3DButton(
                            text = "الأرقام العربية (١–١٠)",
                            onClick = { onSelectNumeralLang(AppLanguage.AR) },
                            modifier = Modifier.weight(1f),
                            containerColor = if (isArNum) NumbersAccent else Color.White,
                            shadowColor = if (isArNum) NumbersShadow else Color(0xFFB0BEC5),
                            contentColor = if (isArNum) Color.White else DeepNavyText,
                            fontSize = 15.sp,
                            testTagName = "arabic_numbers_mode_btn"
                        )

                        Bouncy3DButton(
                            text = "English Numbers (1–10)",
                            onClick = { onSelectNumeralLang(AppLanguage.EN) },
                            modifier = Modifier.weight(1f),
                            containerColor = if (!isArNum) SkyCyanDark else Color.White,
                            shadowColor = if (!isArNum) Color(0xFF01579B) else Color(0xFFB0BEC5),
                            contentColor = if (!isArNum) Color.White else DeepNavyText,
                            fontSize = 15.sp,
                            testTagName = "english_numbers_mode_btn"
                        )
                    }
                }
            }

            // 3. Interactive Numbers 1 to 10 Bar (Tap any number to hear & learn it!)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = BorderStroke(2.dp, StarGoldBorder),
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isArabic) {
                            "الأرقام من ١ إلى ١٠ (اضغط على أي رقم لسماعه وتعلّمه!)"
                        } else {
                            "Numbers 1 to 10 (Tap any number to hear & learn it!)"
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isArabic) 18.sp else 16.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 5
                    ) {
                        NumbersCurriculum.lessons.forEach { lesson ->
                            Surface(
                                modifier = Modifier
                                    .size(58.dp)
                                    .testTag("hub_number_tile_${lesson.value}")
                                    .clickable {
                                        onSpeakLulu(lesson.spokenSentence(numeralLang), numeralLang)
                                        onOpenActivity(NumbersActivityId.LEARN, lesson.value)
                                    },
                                shape = RoundedCornerShape(18.dp),
                                color = lesson.color,
                                border = BorderStroke(2.dp, Color.White),
                                shadowElevation = 4.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = lesson.symbol(numeralLang),
                                        color = Color.White,
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. The 6 Interactive Numbers World Activities (2-Column Grid)
            Text(
                text = if (isArabic) "أنشطة وألعاب عالم الأرقام (٦ أنشطة تفاعلية)" else "Numbers World Activities & Games (6 Activities)",
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabic) 22.sp else 20.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            val activityPairs = NumbersActivityId.entries.chunked(2)
            activityPairs.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    rowItems.forEach { activity ->
                        val isDone = activity in completedSet
                        NumbersActivityCard(
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
private fun NumbersActivityCard(
    activity: NumbersActivityId,
    language: AppLanguage,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == AppLanguage.AR

    Surface(
        modifier = modifier
            .testTag("numbers_activity_${activity.key}")
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
 * Shared Top Bar for all 6 Numbers World Activities.
 */
@Composable
fun NumbersActivityTopBar(
    title: String,
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
        border = BorderStroke(2.5.dp, NumbersAccent)
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
                    contentDescription = if (isArabic) "عالم الأرقام" else "Numbers Hub",
                    onClick = onBackToHub,
                    containerColor = NumbersAccent,
                    shadowColor = NumbersShadow,
                    iconTint = Color.White,
                    badgeLabel = if (isArabic) "الأرقام" else "Numbers",
                    testTagName = "back_to_numbers_hub_btn"
                )

                Bouncy3DIconButton(
                    icon = Icons.Filled.Home,
                    contentDescription = if (isArabic) "الرئيسية" else "Home",
                    onClick = onNavigateHome,
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    iconTint = Color.White,
                    testTagName = "activity_home_btn"
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
 * ACTIVITY 1: LET'S LEARN NUMBERS (١ to ١٠ / 1 to 10)
 * - Displays large colorful 3D number
 * - Written name in Arabic or English
 * - Attractive 3D-styled illustrations representing the exact quantity (tap each item to count!)
 * - Speaker button to hear pronunciation
 * - Next and Previous buttons + 1-10 quick selector
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LearnNumbersActivityScreen(
    initialNumber: Int,
    appLanguage: AppLanguage,
    numeralLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onSelectNumeralLanguage: (AppLanguage) -> Unit,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    var currentNumber by remember { mutableIntStateOf(initialNumber.coerceIn(1, 10)) }
    var visitedNumbers by remember { mutableStateOf(setOf(currentNumber)) }

    val lesson = NumbersCurriculum.byValue(currentNumber)

    LaunchedEffect(currentNumber, numeralLanguage) {
        onSpeakLulu(lesson.spokenSentence(numeralLanguage), numeralLanguage)
    }

    PlayfulSkyBackground(accentTint = lesson.color) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 860.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NumbersActivityTopBar(
                title = if (isArabic) "هيا نتعلّم الأرقام" else "Let's Learn Numbers",
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isActivityCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Arabic / English Numerals Mode Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val isArMode = numeralLanguage == AppLanguage.AR
                Bouncy3DButton(
                    text = "الأرقام العربية (١–١٠)",
                    onClick = { onSelectNumeralLanguage(AppLanguage.AR) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (isArMode) lesson.color else Color.White,
                    shadowColor = if (isArMode) lesson.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (isArMode) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
                Bouncy3DButton(
                    text = "English (1–10)",
                    onClick = { onSelectNumeralLanguage(AppLanguage.EN) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (!isArMode) lesson.color else Color.White,
                    shadowColor = if (!isArMode) lesson.shadowColor else Color(0xFFB0BEC5),
                    contentColor = if (!isArMode) Color.White else DeepNavyText,
                    fontSize = 15.sp
                )
            }

            // Main Number Stage Card
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Giant Colorful 3D Number + Written Name Side-by-Side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Giant 3D Number Tile
                        Surface(
                            modifier = Modifier
                                .size(135.dp)
                                .clickable {
                                    onSpeakLulu(lesson.spokenSentence(numeralLanguage), numeralLanguage)
                                },
                            shape = RoundedCornerShape(30.dp),
                            color = lesson.color,
                            border = BorderStroke(4.dp, SunnyYellow),
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = lesson.symbol(numeralLanguage),
                                    color = Color.White,
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 68.sp
                                    )
                                )
                            }
                        }

                        // Written Number Name + Speaker Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = lesson.color.copy(alpha = 0.14f),
                                border = BorderStroke(2.dp, lesson.color)
                            ) {
                                Text(
                                    text = lesson.word(numeralLanguage),
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 34.sp
                                    )
                                )
                            }

                            Bouncy3DButton(
                                text = if (isArabic) "استمع للنطق 🔊" else "Hear Pronunciation 🔊",
                                onClick = {
                                    onSpeakLulu(lesson.spokenSentence(numeralLanguage), numeralLanguage)
                                },
                                containerColor = SunnyYellow,
                                shadowColor = SunnyYellowDark,
                                contentColor = DeepNavyText,
                                icon = Icons.AutoMirrored.Filled.VolumeUp,
                                fontSize = if (isArabic) 16.sp else 15.sp,
                                testTagName = "speak_number_btn"
                            )
                        }
                    }

                    // Quantity Objects Showcase Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xFFFFFBF2),
                        border = BorderStroke(2.dp, SunnyYellowDark.copy(alpha = 0.45f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = lesson.spokenSentence(numeralLanguage),
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabic) 19.sp else 17.sp
                                ),
                                textAlign = TextAlign.Center
                            )

                            QuantityObjectsGrid(
                                count = lesson.value,
                                objectType = lesson.objectType,
                                numeralLanguage = numeralLanguage,
                                itemSize = if (lesson.value <= 5) 72.dp else 58.dp,
                                onObjectTapped = { tappedCount ->
                                    val countItem = NumbersCurriculum.byValue(tappedCount)
                                    onSpeakLulu(countItem.word(numeralLanguage), numeralLanguage)
                                }
                            )

                            Text(
                                text = if (isArabic) {
                                    "💡 اضغط على كل رسمة لعدّها مع لولو!"
                                } else {
                                    "💡 Tap each picture to count it out loud with Lulu!"
                                },
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    // Previous & Next Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (isArabic) "السابق" else "Previous",
                            onClick = {
                                val prev = if (currentNumber > 1) currentNumber - 1 else 10
                                currentNumber = prev
                                visitedNumbers = visitedNumbers + prev
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = MagicPurple,
                            shadowColor = MagicPurpleDark,
                            contentColor = Color.White,
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            testTagName = "learn_prev_btn"
                        )

                        Bouncy3DButton(
                            text = if (currentNumber < 10) {
                                if (isArabic) "الرقم التالي" else "Next Number"
                            } else {
                                if (isArabic) "أنهيت التعلّم ⭐" else "Finish & Earn Star ⭐"
                            },
                            onClick = {
                                if (currentNumber < 10) {
                                    val next = currentNumber + 1
                                    currentNumber = next
                                    val updated = visitedNumbers + next
                                    visitedNumbers = updated
                                    if (updated.size >= 3) {
                                        onMarkCompleted()
                                    }
                                } else {
                                    onMarkCompleted()
                                    val congrats = if (isArabic) {
                                        "أحسنتِ يا بطلتي! لقد تعلّمتِ الأرقام من واحد إلى عشرة!"
                                    } else {
                                        "Great job! You learned the numbers from 1 to 10!"
                                    }
                                    onSpeakLulu(congrats, appLanguage)
                                    currentNumber = 1
                                }
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            testTagName = "learn_next_btn"
                        )
                    }
                }
            }

            // Quick 1-10 Number Selector Strip
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NumbersCurriculum.lessons.forEach { item ->
                    val selected = item.value == currentNumber
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                currentNumber = item.value
                                val updated = visitedNumbers + item.value
                                visitedNumbers = updated
                                if (updated.size >= 3) {
                                    onMarkCompleted()
                                }
                            },
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) item.color else Color.White,
                        border = BorderStroke(2.dp, item.color),
                        shadowElevation = if (selected) 4.dp else 1.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = item.symbol(numeralLanguage),
                                color = if (selected) Color.White else DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * ACTIVITY 2: NUMBER TRACING
 * - Displays a large dotted number (١–١٠ or 1–10)
 * - Finger tracing canvas with visual guide checkpoints
 * - Clear and Retry buttons
 * - Encouraging celebration feedback from Lulu upon completion
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberTracingActivityScreen(
    initialNumber: Int,
    appLanguage: AppLanguage,
    numeralLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onSelectNumeralLanguage: (AppLanguage) -> Unit,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    var currentNumber by remember { mutableIntStateOf(initialNumber.coerceIn(1, 10)) }
    val lesson = NumbersCurriculum.byValue(currentNumber)

    var strokes by remember(currentNumber, numeralLanguage) {
        mutableStateOf<List<List<Offset>>>(emptyList())
    }
    var hitCheckpoints by remember(currentNumber, numeralLanguage) {
        mutableStateOf<Set<Int>>(emptySet())
    }
    var showCelebration by remember(currentNumber, numeralLanguage) {
        mutableStateOf(false)
    }

    LaunchedEffect(currentNumber, numeralLanguage) {
        val prompt = if (isArabic) {
            "تتبّعي النقاط بإصبعكِ لكتابة الرقم ${lesson.wordAr}!"
        } else {
            "Trace the dotted lines with your finger to write number ${lesson.wordEn}!"
        }
        onSpeakLulu(prompt, appLanguage)
    }

    fun checkTracingSuccess(updatedHits: Set<Int>, totalPointsCount: Int) {
        val requiredHits = (lesson.traceCheckpoints.size - 1).coerceAtLeast(3)
        if (!showCelebration && (updatedHits.size >= requiredHits || totalPointsCount >= 28)) {
            showCelebration = true
            onMarkCompleted()
            val praise = if (isArabic) {
                "أحسنتِ! خط جميل جداً! هذا هو الرقم ${lesson.wordAr}."
            } else {
                "Great job! Beautiful writing! This is number ${lesson.wordEn}."
            }
            onSpeakLulu(praise, appLanguage)
        }
    }

    PlayfulSkyBackground(accentTint = lesson.color) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 860.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NumbersActivityTopBar(
                title = if (isArabic) "كتابة وتتبع الأرقام" else "Number Tracing",
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isActivityCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Lulu Guide & Numeral Mode Row
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(2.dp, lesson.color)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = CircleShape,
                            border = BorderStroke(2.dp, SunnyYellow)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                                contentDescription = "Lulu",
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Text(
                                text = if (isArabic) {
                                    "اكتبي الرقم (${lesson.symbol(numeralLanguage)}) - ${lesson.word(numeralLanguage)}"
                                } else {
                                    "Trace Number (${lesson.symbol(numeralLanguage)}) - ${lesson.word(numeralLanguage)}"
                                },
                                color = DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabic) 19.sp else 17.sp
                                )
                            )
                            Text(
                                text = if (isArabic) {
                                    "ابدئي من النقطة الصفراء ومرّري إصبعكِ على النقاط"
                                } else {
                                    "Start at the yellow dot and trace along the dotted number"
                                },
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                            )
                        }
                    }

                    Bouncy3DIconButton(
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Speak",
                        onClick = {
                            onSpeakLulu(lesson.spokenSentence(numeralLanguage), numeralLanguage)
                        },
                        containerColor = SunnyYellow,
                        shadowColor = SunnyYellowDark,
                        iconTint = DeepNavyText
                    )
                }
            }

            // Interactive Finger Tracing Canvas
            NumberFingerTracingPad(
                lesson = lesson,
                numeralLanguage = numeralLanguage,
                strokes = strokes,
                hitCheckpoints = hitCheckpoints,
                onStartNewStroke = { pt, canvasSize ->
                    val updatedStrokes = strokes + listOf(listOf(pt))
                    strokes = updatedStrokes
                    val updatedHits = findHitCheckpoints(pt, canvasSize, lesson, hitCheckpoints)
                    hitCheckpoints = updatedHits
                    checkTracingSuccess(updatedHits, updatedStrokes.sumOf { it.size })
                },
                onAddPoint = { pt, canvasSize ->
                    val currentList = strokes.lastOrNull().orEmpty() + pt
                    val updatedStrokes = if (strokes.isEmpty()) {
                        listOf(currentList)
                    } else {
                        strokes.dropLast(1) + listOf(currentList)
                    }
                    strokes = updatedStrokes
                    val updatedHits = findHitCheckpoints(pt, canvasSize, lesson, hitCheckpoints)
                    hitCheckpoints = updatedHits
                    checkTracingSuccess(updatedHits, updatedStrokes.sumOf { it.size })
                }
            )

            // Encouraging Celebration Feedback Banner
            AnimatedVisibility(
                visible = showCelebration,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(2.5.dp, MeadowGreen)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = MeadowGreen,
                                modifier = Modifier.size(32.dp)
                            )
                            Column {
                                Text(
                                    text = if (isArabic) "رائع! خطكِ جميل جداً! 🌟" else "Wonderful! Great tracing! 🌟",
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (isArabic) "حصلتِ على نجمة ذهبية من لولو!" else "You earned a golden star from Lulu!",
                                    color = MeadowGreenDark,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = StarGoldBorder,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }

            // Clear, Retry, and Next Number Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = if (isArabic) "مسح" else "Clear",
                    onClick = {
                        strokes = emptyList()
                        hitCheckpoints = emptySet()
                        showCelebration = false
                    },
                    modifier = Modifier.weight(1f),
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    contentColor = Color.White,
                    icon = Icons.Filled.DeleteSweep,
                    testTagName = "tracing_clear_btn"
                )

                Bouncy3DButton(
                    text = if (isArabic) "إعادة المحاولة" else "Retry",
                    onClick = {
                        strokes = emptyList()
                        hitCheckpoints = emptySet()
                        showCelebration = false
                        val retryMsg = if (isArabic) "هيا نحاول كتابة الرقم مرة أخرى!" else "Let's try tracing the number again!"
                        onSpeakLulu(retryMsg, appLanguage)
                    },
                    modifier = Modifier.weight(1f),
                    containerColor = MagicPurple,
                    shadowColor = MagicPurpleDark,
                    contentColor = Color.White,
                    icon = Icons.Filled.Refresh,
                    testTagName = "tracing_retry_btn"
                )

                Bouncy3DButton(
                    text = if (isArabic) "الرقم التالي" else "Next",
                    onClick = {
                        currentNumber = if (currentNumber < 10) currentNumber + 1 else 1
                    },
                    modifier = Modifier.weight(1f),
                    containerColor = MeadowGreen,
                    shadowColor = MeadowGreenDark,
                    contentColor = Color.White,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    testTagName = "tracing_next_btn"
                )
            }

            // 1 to 10 Number Selector Strip
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NumbersCurriculum.lessons.forEach { item ->
                    val selected = item.value == currentNumber
                    Surface(
                        modifier = Modifier
                            .size(46.dp)
                            .clickable { currentNumber = item.value },
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) item.color else Color.White,
                        border = BorderStroke(2.dp, item.color)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = item.symbol(numeralLanguage),
                                color = if (selected) Color.White else DeepNavyText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}
