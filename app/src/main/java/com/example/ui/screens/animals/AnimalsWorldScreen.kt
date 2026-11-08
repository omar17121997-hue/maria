package com.example.ui.screens.animals

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
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.example.model.AnimalHabitat
import com.example.model.AnimalsActivityId
import com.example.model.AnimalsCurriculum
import com.example.model.AppLanguage
import com.example.ui.FunLearningUiState
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.theme.AnimalsAccent
import com.example.ui.theme.AnimalsCardBg
import com.example.ui.theme.AnimalsShadow
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

private sealed interface AnimalsSubScreen {
    data object Hub : AnimalsSubScreen
    data class Activity(val activityId: AnimalsActivityId, val initialAnimalIndex: Int = 0) : AnimalsSubScreen
}

@Composable
fun AnimalsWorldScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onCompleteActivity: (AnimalsActivityId) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSubScreen by remember { mutableStateOf<AnimalsSubScreen>(AnimalsSubScreen.Hub) }
    val appLanguage = uiState.settings.language
    val layoutDirection = if (appLanguage == AppLanguage.AR) LayoutDirection.Rtl else LayoutDirection.Ltr

    BackHandler {
        if (currentSubScreen != AnimalsSubScreen.Hub) {
            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
            currentSubScreen = AnimalsSubScreen.Hub
        } else {
            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
            onNavigateHome()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        PlayfulSkyBackground(
            modifier = modifier.fillMaxSize(),
            accentTint = AnimalsAccent
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                containerColor = Color.Transparent,
                topBar = {
                    AnimalsWorldTopBar(
                        appLanguage = appLanguage,
                        soundEnabled = uiState.settings.soundEnabled,
                        completedCount = uiState.settings.completedAnimalsActivities.size,
                        inSubActivity = currentSubScreen != AnimalsSubScreen.Hub,
                        onBackOrHome = {
                            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
                            if (currentSubScreen != AnimalsSubScreen.Hub) {
                                currentSubScreen = AnimalsSubScreen.Hub
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
                    label = "animals_subscreen"
                ) { subScreen ->
                    when (subScreen) {
                        is AnimalsSubScreen.Hub -> {
                            AnimalsWorldHubScreen(
                                appLanguage = appLanguage,
                                soundEnabled = uiState.settings.soundEnabled,
                                completedActivities = uiState.settings.completedAnimalsActivities,
                                onSpeakLulu = onSpeakLulu,
                                onOpenActivity = { activityId, animalIndex ->
                                    SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
                                    currentSubScreen = AnimalsSubScreen.Activity(activityId, animalIndex)
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }

                        is AnimalsSubScreen.Activity -> {
                            when (subScreen.activityId) {
                                AnimalsActivityId.MEET_ANIMALS -> {
                                    MeetAnimalsActivityScreen(
                                        initialIndex = subScreen.initialAnimalIndex,
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = { onCompleteActivity(AnimalsActivityId.MEET_ANIMALS) },
                                        onBackToHub = { currentSubScreen = AnimalsSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                AnimalsActivityId.GUESS_ANIMAL -> {
                                    GuessAnimalActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = { onCompleteActivity(AnimalsActivityId.GUESS_ANIMAL) },
                                        onBackToHub = { currentSubScreen = AnimalsSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                AnimalsActivityId.MATCH_ANIMAL_NAME -> {
                                    MatchAnimalToNameActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = { onCompleteActivity(AnimalsActivityId.MATCH_ANIMAL_NAME) },
                                        onBackToHub = { currentSubScreen = AnimalsSubScreen.Hub },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                AnimalsActivityId.ANIMAL_HABITATS -> {
                                    AnimalHabitatsActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = { onCompleteActivity(AnimalsActivityId.ANIMAL_HABITATS) },
                                        onBackToHub = { currentSubScreen = AnimalsSubScreen.Hub },
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
private fun AnimalsWorldTopBar(
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
        border = BorderStroke(2.5.dp, AnimalsAccent)
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
                        text = if (appLanguage == AppLanguage.AR) "عالم الحيوانات 🦁" else "Animals World 🦁",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = DeepNavyText
                    )
                    Text(
                        text = if (appLanguage == AppLanguage.AR) "١٥ حيواناً وأصواتها وبيئاتها" else "15 Animals, Sounds & Habitats",
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
                            text = "$completedCount / 4",
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
private fun AnimalsWorldHubScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    completedActivities: Set<AnimalsActivityId>,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onOpenActivity: (AnimalsActivityId, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = completedActivities.size
    val progressFraction = (completedCount / 4f).coerceIn(0f, 1f)

    val welcomeMessage = if (appLanguage == AppLanguage.AR) {
        "أهلاً بكِ في عالم الحيوانات! هيا نتعرف على ١٥ حيواناً لطيفاً ونسمع أصواتها ونكتشف أين تعيش!"
    } else {
        "Welcome to Animals World! Let's meet 15 cute animals, hear their sounds, and discover where they live!"
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
        // Hero Banner with Lulu & Cute Cartoon Animals
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
                    .testTag("animals_hub_hero_card"),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = AnimalsCardBg),
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
                                .border(2.5.dp, AnimalsAccent, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_card_animals_nature_1794072210949),
                                contentDescription = if (appLanguage == AppLanguage.AR) "حيوانات كرتونية" else "Cartoon Animals",
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
                                    text = if (appLanguage == AppLanguage.AR) "لولو تقول 🔊" else "Lulu says 🔊",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = AnimalsShadow
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

                    // Progress indicator
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
                                    text = if (appLanguage == AppLanguage.AR) "تقدّم أنشطة الحيوانات" else "Animals World Progress",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = DeepNavyText
                                )
                                Text(
                                    text = "$completedCount / 4 ⭐",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = AnimalsShadow
                                )
                            }
                            LinearProgressIndicator(
                                progress = { progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = MeadowGreen,
                                trackColor = Color(0xFFFFE0B2)
                            )
                        }
                    }
                }
            }
        }

        // Quick 15 Animals Grid
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .shadow(6.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.5.dp, AnimalsAccent.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (appLanguage == AppLanguage.AR) {
                            "اضغطي على أي حيوان لسماع صوته والتعرف عليه (١٥ حيواناً):"
                        } else {
                            "Tap any animal to hear its sound & learn about it (15 Animals):"
                        },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText
                    )

                    val rows = AnimalsCurriculum.allAnimals.chunked(5)
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { animal ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(84.dp)
                                        .shadow(4.dp, RoundedCornerShape(18.dp))
                                        .clickable {
                                            SoundEffectsHelper.playAnimalSoundEffect(animal.id, soundEnabled)
                                            onSpeakLulu(animal.spokenMeet(appLanguage), appLanguage)
                                            onOpenActivity(AnimalsActivityId.MEET_ANIMALS, animal.index - 1)
                                        }
                                        .testTag("animals_hub_quick_${animal.id}"),
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = animal.cardBgColor),
                                    border = BorderStroke(2.dp, animal.color.copy(alpha = 0.6f))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(text = animal.emoji, fontSize = 26.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = animal.name(appLanguage),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 11.sp
                                            ),
                                            color = DeepNavyText,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4 Interactive Activities Header
        item {
            Text(
                text = if (appLanguage == AppLanguage.AR) "اختاري نشاطاً ممتعاً:" else "Choose a Fun Activity:",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = DeepNavyText,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .padding(top = 4.dp)
            )
        }

        val activityPairs = AnimalsActivityId.entries.chunked(2)
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
                            .height(176.dp)
                            .shadow(7.dp, RoundedCornerShape(26.dp))
                            .clickable { onOpenActivity(activity, 0) }
                            .testTag("animals_activity_card_${activity.key}"),
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
// ACTIVITY 1: MEET THE ANIMALS | تعرّف على الحيوانات
// =====================================================================

@Composable
private fun MeetAnimalsActivityScreen(
    initialIndex: Int,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allAnimals = AnimalsCurriculum.allAnimals
    var currentIndex by rememberSaveable { mutableIntStateOf(initialIndex.coerceIn(0, allAnimals.lastIndex)) }
    val visitedIndices = remember { mutableStateListOf<Int>() }
    var hasAwardedCompletion by rememberSaveable { mutableStateOf(false) }

    val currentAnimal = allAnimals[currentIndex]

    val infiniteTransition = rememberInfiniteTransition(label = "animal_bounce")
    val bounceY by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animal_y"
    )

    LaunchedEffect(currentIndex, appLanguage) {
        if (!visitedIndices.contains(currentIndex)) {
            visitedIndices.add(currentIndex)
        }
        SoundEffectsHelper.playAnimalSoundEffect(currentAnimal.id, soundEnabled)
        onSpeakLulu(currentAnimal.spokenMeet(appLanguage), appLanguage)
        if (visitedIndices.size >= 4 && !hasAwardedCompletion) {
            hasAwardedCompletion = true
            onComplete()
        }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(8.dp, RoundedCornerShape(30.dp))
                    .testTag("meet_animals_main_card"),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = currentAnimal.cardBgColor),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White
                        ) {
                            Text(
                                text = "${currentAnimal.index} / ${allAnimals.size}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = currentAnimal.shadowColor,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        // Habitat Tag
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = currentAnimal.habitat.cardBgColor,
                            border = BorderStroke(1.5.dp, currentAnimal.habitat.color)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = currentAnimal.habitat.emoji, fontSize = 18.sp)
                                Text(
                                    text = currentAnimal.habitat.title(appLanguage),
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = currentAnimal.habitat.shadowColor
                                )
                            }
                        }
                    }

                    // 3D Cartoon Animal Showcase with Habitat Backdrop
                    Box(
                        modifier = Modifier
                            .size(164.dp)
                            .offset(y = bounceY.dp)
                            .clip(RoundedCornerShape(36.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color.White, currentAnimal.habitat.cardBgColor)
                                )
                            )
                            .border(4.dp, currentAnimal.color, RoundedCornerShape(36.dp))
                            .clickable {
                                SoundEffectsHelper.playAnimalSoundEffect(currentAnimal.id, soundEnabled)
                                onSpeakLulu(currentAnimal.spokenMeet(appLanguage), appLanguage)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = currentAnimal.color.copy(alpha = 0.14f),
                                radius = size.minDimension * 0.42f,
                                center = Offset(size.width * 0.5f, size.height * 0.54f)
                            )
                        }
                        Text(text = currentAnimal.emoji, fontSize = 86.sp)
                    }

                    // Bilingual Names
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentAnimal.nameAr,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 36.sp
                            ),
                            color = DeepNavyText
                        )
                        Text(
                            text = currentAnimal.nameEn,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = currentAnimal.shadowColor
                        )
                    }

                    // Two Interactive Audio Buttons: Animal Sound + Lulu Explanation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (appLanguage == AppLanguage.AR) {
                                "صوت: ${currentAnimal.soundLabel(appLanguage)}"
                            } else {
                                "Sound: ${currentAnimal.soundLabel(appLanguage)}"
                            },
                            onClick = {
                                SoundEffectsHelper.playAnimalSoundEffect(currentAnimal.id, soundEnabled)
                                onSpeakLulu(
                                    if (appLanguage == AppLanguage.AR) {
                                        "${currentAnimal.nameAr}! ${currentAnimal.soundOnomatopoeiaAr}!"
                                    } else {
                                        "${currentAnimal.nameEn}! ${currentAnimal.soundOnomatopoeiaEn}!"
                                    },
                                    appLanguage
                                )
                            },
                            containerColor = currentAnimal.color,
                            shadowColor = currentAnimal.shadowColor,
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            modifier = Modifier.weight(1f),
                            testTagName = "meet_animals_sound_btn"
                        )

                        Bouncy3DButton(
                            text = if (appLanguage == AppLanguage.AR) "اسمعي لولو" else "Hear Lulu",
                            onClick = {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                onSpeakLulu(currentAnimal.spokenMeet(appLanguage), appLanguage)
                            },
                            containerColor = LuluPink,
                            shadowColor = LuluPinkDark,
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            modifier = Modifier.weight(1f),
                            testTagName = "meet_animals_lulu_btn"
                        )
                    }

                    // Educational Fact Card
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = Color.White,
                        shadowElevation = 3.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_lulu_mascot_1794072207514),
                                contentDescription = "Lulu",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, currentAnimal.color, CircleShape)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (appLanguage == AppLanguage.AR) "معلومة ممتعة 💡" else "Fun Fact 💡",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = currentAnimal.shadowColor
                                )
                                Text(
                                    text = currentAnimal.fact(appLanguage),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 22.sp
                                    ),
                                    color = DeepNavyText
                                )
                            }
                        }
                    }

                    // Next & Previous Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (appLanguage == AppLanguage.AR) "السابق" else "Previous",
                            onClick = {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                currentIndex = if (currentIndex > 0) currentIndex - 1 else allAnimals.lastIndex
                            },
                            containerColor = Color(0xFF29B6F6),
                            shadowColor = Color(0xFF0288D1),
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            modifier = Modifier.weight(1f),
                            testTagName = "meet_animals_prev_btn"
                        )

                        Bouncy3DButton(
                            text = if (appLanguage == AppLanguage.AR) "التالي" else "Next",
                            onClick = {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                if (currentIndex == allAnimals.lastIndex && !hasAwardedCompletion) {
                                    hasAwardedCompletion = true
                                    onComplete()
                                }
                                currentIndex = (currentIndex + 1) % allAnimals.size
                            },
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            modifier = Modifier.weight(1f),
                            testTagName = "meet_animals_next_btn"
                        )
                    }
                }
            }
        }

        // All 15 Animals Strip
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(5.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.dp, AnimalsAccent.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (appLanguage == AppLanguage.AR) "جميع الحيوانات (١٥):" else "All Animals (15):",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText
                    )
                    val rows = allAnimals.chunked(5)
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { item ->
                                val selected = item.index - 1 == currentIndex
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(64.dp)
                                        .shadow(if (selected) 5.dp else 2.dp, RoundedCornerShape(16.dp))
                                        .clickable {
                                            SoundEffectsHelper.playPopChime(soundEnabled)
                                            currentIndex = item.index - 1
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selected) item.color else item.cardBgColor
                                    ),
                                    border = BorderStroke(
                                        width = if (selected) 2.5.dp else 1.5.dp,
                                        color = if (selected) SunnyYellow else Color.White
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(text = item.emoji, fontSize = 22.sp)
                                        Text(
                                            text = item.name(appLanguage),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 10.sp
                                            ),
                                            color = if (selected) Color.White else DeepNavyText,
                                            maxLines = 1
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
}

// =====================================================================
// ACTIVITY 2: GUESS THE ANIMAL | خمّن صوت الحيوان
// =====================================================================

@Composable
private fun GuessAnimalActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allAnimals = AnimalsCurriculum.allAnimals
    var roundIndex by rememberSaveable { mutableIntStateOf(0) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var isQuizFinished by rememberSaveable { mutableStateOf(false) }
    var feedbackBanner by remember { mutableStateOf<String?>(null) }
    var isPositiveFeedback by remember { mutableStateOf(true) }

    val totalRounds = 8
    val targetAnimal = allAnimals[(roundIndex * 2) % allAnimals.size]

    val options = remember(roundIndex) {
        val distractors = allAnimals.filter { it.id != targetAnimal.id }.shuffled().take(3)
        (distractors + targetAnimal).shuffled()
    }

    LaunchedEffect(roundIndex, appLanguage, isQuizFinished) {
        if (!isQuizFinished) {
            feedbackBanner = null
            SoundEffectsHelper.playAnimalSoundEffect(targetAnimal.id, soundEnabled)
            onSpeakLulu(targetAnimal.spokenGuessClue(appLanguage), appLanguage)
        }
    }

    if (isQuizFinished) {
        AnimalsCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "أحسنتِ في تخمين أصوات الحيوانات! 🦁" else "Awesome Job Guessing the Animals! 🦁",
            subtitle = if (appLanguage == AppLanguage.AR) "لقد تعرّفتِ على أصوات الحيوانات بنجاح!" else "You recognized all the animal sounds!",
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                roundIndex = 0
                score = 0
                isQuizFinished = false
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
                    .clickable {
                        SoundEffectsHelper.playAnimalSoundEffect(targetAnimal.id, soundEnabled)
                        onSpeakLulu(targetAnimal.spokenGuessClue(appLanguage), appLanguage)
                    }
                    .testTag("guess_animal_prompt_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (appLanguage == AppLanguage.AR) {
                                "السؤال ${roundIndex + 1} من $totalRounds"
                            } else {
                                "Question ${roundIndex + 1} of $totalRounds"
                            },
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = SoftSlateText
                        )
                        Text(
                            text = "⭐ $score",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = AnimalsShadow
                        )
                    }

                    // Sound Mystery Box
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🔊🐾", fontSize = 48.sp)
                            Text(
                                text = if (appLanguage == AppLanguage.AR) {
                                    "من صاحب هذا الصوت؟"
                                } else {
                                    "Which animal makes this sound?"
                                },
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = DeepNavyText,
                                textAlign = TextAlign.Center
                            )
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFFFF8E1),
                                border = BorderStroke(1.5.dp, AnimalsAccent)
                            ) {
                                Text(
                                    text = targetAnimal.soundLabel(appLanguage),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = AnimalsShadow,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Bouncy3DButton(
                        text = if (appLanguage == AppLanguage.AR) "اسمعي صوت الحيوان مرة أخرى" else "Play Animal Sound Again",
                        onClick = {
                            SoundEffectsHelper.playAnimalSoundEffect(targetAnimal.id, soundEnabled)
                            onSpeakLulu(targetAnimal.spokenGuessClue(appLanguage), appLanguage)
                        },
                        containerColor = MeadowGreen,
                        shadowColor = MeadowGreenDark,
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        testTagName = "guess_animal_replay_btn"
                    )
                }
            }
        }

        if (feedbackBanner != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isPositiveFeedback) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    border = BorderStroke(
                        2.dp,
                        if (isPositiveFeedback) MeadowGreen else LuluPink
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = feedbackBanner.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (isPositiveFeedback) Color(0xFF1B5E20) else Color(0xFFC62828),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 2x2 Grid of Animal Options
        val optionRows = options.chunked(2)
        items(optionRows.size) { rowIdx ->
            val rowOptions = optionRows[rowIdx]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowOptions.forEach { option ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(158.dp)
                            .shadow(7.dp, RoundedCornerShape(26.dp))
                            .clickable {
                                if (option.id == targetAnimal.id) {
                                    SoundEffectsHelper.playHappyMelody(soundEnabled)
                                    isPositiveFeedback = true
                                    feedbackBanner = if (appLanguage == AppLanguage.AR) {
                                        "إجابة صحيحة! هذا صوت ${option.nameAr} 🌟"
                                    } else {
                                        "Correct! That's the ${option.nameEn} 🌟"
                                    }
                                    onSpeakLulu(
                                        if (appLanguage == AppLanguage.AR) {
                                            "أحسنتِ! هذا هو ${option.nameAr}!"
                                        } else {
                                            "Great job! That is the ${option.nameEn}!"
                                        },
                                        appLanguage
                                    )
                                    score += 1
                                    if (roundIndex + 1 >= totalRounds) {
                                        SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                        onComplete()
                                        isQuizFinished = true
                                    } else {
                                        roundIndex += 1
                                    }
                                } else {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    isPositiveFeedback = false
                                    feedbackBanner = if (appLanguage == AppLanguage.AR) {
                                        "هذا ${option.nameAr}، استمعي للصوت وحاولي مرة أخرى!"
                                    } else {
                                        "That's a ${option.nameEn}, listen and try again!"
                                    }
                                    onSpeakLulu(
                                        if (appLanguage == AppLanguage.AR) {
                                            "حاولي مرة أخرى يا بطلة!"
                                        } else {
                                            "Try again, superstar!"
                                        },
                                        appLanguage
                                    )
                                }
                            }
                            .testTag("guess_animal_option_${option.id}"),
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = option.cardBgColor),
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
                                text = option.name(appLanguage),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = DeepNavyText
                            )
                            Text(
                                text = option.secondaryName(appLanguage),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = SoftSlateText
                            )
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// ACTIVITY 3: MATCH ANIMAL TO NAME | طابق الحيوان باسمه
// =====================================================================

@Composable
private fun MatchAnimalToNameActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allAnimals = AnimalsCurriculum.allAnimals
    var roundIndex by rememberSaveable { mutableIntStateOf(0) }
    var isCompletedAll by rememberSaveable { mutableStateOf(false) }

    val batchAnimals = remember(roundIndex) {
        val start = (roundIndex * 5) % allAnimals.size
        (0 until 5).map { offset -> allAnimals[(start + offset) % allAnimals.size] }
    }
    val shuffledNames = remember(roundIndex) {
        batchAnimals.shuffled()
    }

    var selectedAnimalId by remember { mutableStateOf<String?>(null) }
    val matchedIds = remember(roundIndex) { mutableStateListOf<String>() }
    var statusText by remember(roundIndex, appLanguage) {
        mutableStateOf(
            if (appLanguage == AppLanguage.AR) {
                "اضغطي على صورة الحيوان ثم اختاري اسمه الصحيح!"
            } else {
                "Tap an animal picture, then tap its matching name!"
            }
        )
    }

    LaunchedEffect(roundIndex, appLanguage) {
        onSpeakLulu(statusText, appLanguage)
    }

    if (isCompletedAll) {
        AnimalsCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "بطلة مطابقة أسماء الحيوانات! 🏷️" else "Animal Name Matching Champion! 🏷️",
            subtitle = if (appLanguage == AppLanguage.AR) "لقد طابقتِ الحيوانات بأسمائها الصحيحة!" else "You matched the animals to their names!",
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                roundIndex = 0
                isCompletedAll = false
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
                    .shadow(6.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                border = BorderStroke(2.5.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (appLanguage == AppLanguage.AR) {
                            "الجولة ${roundIndex + 1} من 3"
                        } else {
                            "Round ${roundIndex + 1} of 3"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF6A1B9A)
                    )
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        items(batchAnimals.size) { idx ->
            val leftAnimal = batchAnimals[idx]
            val rightNameAnimal = shuffledNames[idx]
            val isLeftMatched = matchedIds.contains(leftAnimal.id)
            val isRightMatched = matchedIds.contains(rightNameAnimal.id)
            val isLeftSelected = selectedAnimalId == leftAnimal.id

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animal Picture Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(86.dp)
                        .shadow(5.dp, RoundedCornerShape(22.dp))
                        .clickable {
                            if (!isLeftMatched) {
                                SoundEffectsHelper.playAnimalSoundEffect(leftAnimal.id, soundEnabled)
                                selectedAnimalId = leftAnimal.id
                                onSpeakLulu(leftAnimal.name(appLanguage), appLanguage)
                            }
                        }
                        .testTag("match_animal_pic_${leftAnimal.id}"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isLeftMatched -> Color(0xFFE8F5E9)
                            isLeftSelected -> Color(0xFFFFF8E1)
                            else -> leftAnimal.cardBgColor
                        }
                    ),
                    border = BorderStroke(
                        width = if (isLeftSelected) 3.dp else 2.dp,
                        color = if (isLeftSelected) SunnyYellowDark else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = leftAnimal.emoji, fontSize = 42.sp)
                        if (isLeftMatched) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = MeadowGreenDark,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }

                // Animal Name Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(86.dp)
                        .shadow(5.dp, RoundedCornerShape(22.dp))
                        .clickable {
                            if (!isRightMatched) {
                                val chosenId = selectedAnimalId
                                if (chosenId == null) {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    statusText = if (appLanguage == AppLanguage.AR) {
                                        "اضغطي على صورة الحيوان أولاً!"
                                    } else {
                                        "Tap an animal picture first!"
                                    }
                                } else if (chosenId == rightNameAnimal.id) {
                                    SoundEffectsHelper.playHappyMelody(soundEnabled)
                                    matchedIds.add(chosenId)
                                    selectedAnimalId = null
                                    statusText = if (appLanguage == AppLanguage.AR) {
                                        "رائع! هذا اسم ${rightNameAnimal.nameAr} 🌟"
                                    } else {
                                        "Awesome! That's ${rightNameAnimal.nameEn} 🌟"
                                    }
                                    onSpeakLulu(
                                        if (appLanguage == AppLanguage.AR) {
                                            "أحسنتِ! ${rightNameAnimal.nameAr}!"
                                        } else {
                                            "Great match! ${rightNameAnimal.nameEn}!"
                                        },
                                        appLanguage
                                    )
                                    if (matchedIds.size == batchAnimals.size) {
                                        if (roundIndex + 1 >= 3) {
                                            SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                            onComplete()
                                            isCompletedAll = true
                                        } else {
                                            roundIndex += 1
                                        }
                                    }
                                } else {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    statusText = if (appLanguage == AppLanguage.AR) {
                                        "حاولي مرة أخرى! ابحثي عن الاسم الصحيح."
                                    } else {
                                        "Try again! Look for the matching animal name."
                                    }
                                }
                            }
                        }
                        .testTag("match_animal_name_${rightNameAnimal.id}"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isRightMatched) Color(0xFFE8F5E9) else Color.White
                    ),
                    border = BorderStroke(2.dp, rightNameAnimal.color.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = rightNameAnimal.nameAr,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = DeepNavyText
                        )
                        Text(
                            text = rightNameAnimal.nameEn,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = rightNameAnimal.shadowColor
                        )
                    }
                }
            }
        }
    }
}

// =====================================================================
// ACTIVITY 4: WHERE DOES IT LIVE? | أين يعيش الحيوان؟
// =====================================================================

@Composable
private fun AnimalHabitatsActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allAnimals = AnimalsCurriculum.allAnimals
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var isCompleted by rememberSaveable { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var isFeedbackPositive by remember { mutableStateOf(true) }

    val totalQuestions = 8
    val currentAnimal = allAnimals[(currentIndex * 2) % allAnimals.size]

    LaunchedEffect(currentIndex, appLanguage, isCompleted) {
        if (!isCompleted) {
            feedbackMessage = null
            val prompt = if (appLanguage == AppLanguage.AR) {
                "أين يعيش ${currentAnimal.nameAr}؟ في المزرعة، الغابة، البحر، أم الأدغال؟"
            } else {
                "Where does the ${currentAnimal.nameEn} live? Farm, Forest, Sea, or Jungle?"
            }
            onSpeakLulu(prompt, appLanguage)
        }
    }

    if (isCompleted) {
        AnimalsCelebrationCard(
            title = if (appLanguage == AppLanguage.AR) "بطلة بيئات الحيوانات! 🏡🌊🌴" else "Animal Habitats Explorer! 🏡🌊🌴",
            subtitle = if (appLanguage == AppLanguage.AR) {
                "لقد ساعدتِ كل حيوان على الوصول إلى مكانه الصحيح!"
            } else {
                "You helped every animal find its right home!"
            },
            appLanguage = appLanguage,
            onPlayAgain = {
                SoundEffectsHelper.playPopChime(soundEnabled)
                currentIndex = 0
                score = 0
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
                    .clickable {
                        SoundEffectsHelper.playAnimalSoundEffect(currentAnimal.id, soundEnabled)
                        val prompt = if (appLanguage == AppLanguage.AR) {
                            "أين يعيش ${currentAnimal.nameAr}؟"
                        } else {
                            "Where does the ${currentAnimal.nameEn} live?"
                        }
                        onSpeakLulu(prompt, appLanguage)
                    }
                    .testTag("animal_habitat_question_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = currentAnimal.cardBgColor),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${currentIndex + 1} / $totalQuestions",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = SoftSlateText
                        )
                        Text(
                            text = "⭐ $score",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = AnimalsShadow
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(112.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.White)
                            .border(3.dp, currentAnimal.color, RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentAnimal.emoji, fontSize = 64.sp)
                    }

                    Text(
                        text = if (appLanguage == AppLanguage.AR) {
                            "أين يعيش ${currentAnimal.nameAr}؟"
                        } else {
                            "Where does the ${currentAnimal.nameEn} live?"
                        },
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${currentAnimal.nameAr} • ${currentAnimal.nameEn}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = currentAnimal.shadowColor
                    )
                }
            }
        }

        if (feedbackMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isFeedbackPositive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    border = BorderStroke(
                        2.dp,
                        if (isFeedbackPositive) MeadowGreen else LuluPink
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = feedbackMessage.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (isFeedbackPositive) Color(0xFF1B5E20) else Color(0xFFC62828),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 2x2 Grid of 4 Habitats: Farm, Forest, Sea, Jungle
        val habitatPairs = AnimalHabitat.entries.chunked(2)
        items(habitatPairs.size) { rowIdx ->
            val rowHabitats = habitatPairs[rowIdx]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowHabitats.forEach { habitat ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(148.dp)
                            .shadow(7.dp, RoundedCornerShape(26.dp))
                            .clickable {
                                if (habitat == currentAnimal.habitat) {
                                    SoundEffectsHelper.playHappyMelody(soundEnabled)
                                    isFeedbackPositive = true
                                    feedbackMessage = if (appLanguage == AppLanguage.AR) {
                                        "أحسنتِ! ${currentAnimal.nameAr} يعيش في ${habitat.nameAr} 🌟"
                                    } else {
                                        "Correct! The ${currentAnimal.nameEn} lives in the ${habitat.nameEn} 🌟"
                                    }
                                    onSpeakLulu(
                                        if (appLanguage == AppLanguage.AR) {
                                            "ممتاز! ${currentAnimal.nameAr} يعيش في ${habitat.nameAr}!"
                                        } else {
                                            "Wonderful! The ${currentAnimal.nameEn} lives in the ${habitat.nameEn}!"
                                        },
                                        appLanguage
                                    )
                                    score += 1
                                    if (currentIndex + 1 >= totalQuestions) {
                                        SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                        onComplete()
                                        isCompleted = true
                                    } else {
                                        currentIndex += 1
                                    }
                                } else {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    isFeedbackPositive = false
                                    feedbackMessage = if (appLanguage == AppLanguage.AR) {
                                        "ليست ${habitat.nameAr}، فكّري أين يعيش ${currentAnimal.nameAr}!"
                                    } else {
                                        "Not the ${habitat.nameEn}, think about where the ${currentAnimal.nameEn} lives!"
                                    }
                                }
                            }
                            .testTag("habitat_option_${habitat.key}"),
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = habitat.cardBgColor),
                        border = BorderStroke(2.5.dp, Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = habitat.emoji, fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = habitat.nameAr,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = DeepNavyText
                            )
                            Text(
                                text = habitat.nameEn,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = habitat.shadowColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimalsCelebrationCard(
    title: String,
    subtitle: String,
    appLanguage: AppLanguage,
    onPlayAgain: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "animals_celebration")
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
                    text = "🌟🦁🌟",
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
                        containerColor = Color(0xFF29B6F6),
                        shadowColor = Color(0xFF0288D1),
                        icon = Icons.Filled.Refresh,
                        modifier = Modifier.weight(1f)
                    )
                    Bouncy3DButton(
                        text = if (appLanguage == AppLanguage.AR) "أنشطة الحيوانات" else "Animals Hub",
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
