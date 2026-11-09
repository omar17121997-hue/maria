package com.example.ui.screens.writing

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoFixNormal
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.model.ColoringTemplateTheme
import com.example.model.LettersCurriculum
import com.example.model.NumbersCurriculum
import com.example.model.WritingDrawingActivityId
import com.example.model.WritingDrawingCurriculum
import com.example.ui.FunLearningUiState
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.LetterFingerTracingPad
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.components.findLetterHitCheckpoints
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.DrawingAccent
import com.example.ui.theme.DrawingCardBg
import com.example.ui.theme.DrawingShadow
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark
import com.example.util.SoundEffectsHelper
import kotlin.math.hypot

private sealed interface WritingSubScreen {
    data object Hub : WritingSubScreen
    data class Activity(val activityId: WritingDrawingActivityId) : WritingSubScreen
}

data class FreeStrokeData(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
    val isEraser: Boolean = false
)

@Composable
fun WritingDrawingWorldScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onCompleteActivity: (WritingDrawingActivityId) -> Unit,
    onMarkItemPracticed: (String) -> Unit,
    onSaveDrawing: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSubScreen by remember { mutableStateOf<WritingSubScreen>(WritingSubScreen.Hub) }
    val appLanguage = uiState.settings.language
    val layoutDirection = if (appLanguage == AppLanguage.AR) LayoutDirection.Rtl else LayoutDirection.Ltr

    BackHandler {
        if (currentSubScreen != WritingSubScreen.Hub) {
            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
            currentSubScreen = WritingSubScreen.Hub
        } else {
            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
            onNavigateHome()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        PlayfulSkyBackground(
            modifier = modifier.fillMaxSize(),
            accentTint = DrawingAccent
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                containerColor = Color.Transparent,
                topBar = {
                    WritingWorldTopBar(
                        appLanguage = appLanguage,
                        soundEnabled = uiState.settings.soundEnabled,
                        completedCount = uiState.settings.completedWritingActivities.size,
                        inSubActivity = currentSubScreen != WritingSubScreen.Hub,
                        onBackOrHome = {
                            SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
                            if (currentSubScreen != WritingSubScreen.Hub) {
                                currentSubScreen = WritingSubScreen.Hub
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
                    label = "writing_subscreen"
                ) { subScreen ->
                    when (subScreen) {
                        is WritingSubScreen.Hub -> {
                            WritingDrawingHubScreen(
                                appLanguage = appLanguage,
                                soundEnabled = uiState.settings.soundEnabled,
                                completedActivities = uiState.settings.completedWritingActivities,
                                practicedItemsCount = uiState.settings.practicedWritingItems.size,
                                savedDrawings = uiState.settings.savedDrawingsTitles,
                                onSpeakLulu = onSpeakLulu,
                                onOpenActivity = { activityId ->
                                    SoundEffectsHelper.playPopChime(uiState.settings.soundEnabled)
                                    currentSubScreen = WritingSubScreen.Activity(activityId)
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }

                        is WritingSubScreen.Activity -> {
                            when (subScreen.activityId) {
                                WritingDrawingActivityId.WRITE_ARABIC_LETTERS -> {
                                    WriteArabicLettersActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        practicedItems = uiState.settings.practicedWritingItems,
                                        onSpeakLulu = onSpeakLulu,
                                        onMarkItemPracticed = onMarkItemPracticed,
                                        onComplete = {
                                            onCompleteActivity(WritingDrawingActivityId.WRITE_ARABIC_LETTERS)
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                WritingDrawingActivityId.WRITE_ENGLISH_LETTERS -> {
                                    WriteEnglishLettersActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        practicedItems = uiState.settings.practicedWritingItems,
                                        onSpeakLulu = onSpeakLulu,
                                        onMarkItemPracticed = onMarkItemPracticed,
                                        onComplete = {
                                            onCompleteActivity(WritingDrawingActivityId.WRITE_ENGLISH_LETTERS)
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                WritingDrawingActivityId.WRITE_NUMBERS -> {
                                    WriteNumbersActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        practicedItems = uiState.settings.practicedWritingItems,
                                        onSpeakLulu = onSpeakLulu,
                                        onMarkItemPracticed = onMarkItemPracticed,
                                        onComplete = {
                                            onCompleteActivity(WritingDrawingActivityId.WRITE_NUMBERS)
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                WritingDrawingActivityId.FREE_DRAWING_STUDIO -> {
                                    FreeDrawingStudioActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        savedDrawings = uiState.settings.savedDrawingsTitles,
                                        onSpeakLulu = onSpeakLulu,
                                        onSaveDrawing = { title ->
                                            onSaveDrawing(title)
                                            onCompleteActivity(WritingDrawingActivityId.FREE_DRAWING_STUDIO)
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                WritingDrawingActivityId.INTERACTIVE_COLORING_BOOK -> {
                                    InteractiveColoringBookActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onSaveDrawing = onSaveDrawing,
                                        onComplete = {
                                            onCompleteActivity(WritingDrawingActivityId.INTERACTIVE_COLORING_BOOK)
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    )
                                }

                                WritingDrawingActivityId.DRAW_AND_LEARN_CHALLENGE -> {
                                    DrawAndLearnChallengeActivityScreen(
                                        appLanguage = appLanguage,
                                        soundEnabled = uiState.settings.soundEnabled,
                                        onSpeakLulu = onSpeakLulu,
                                        onComplete = {
                                            onCompleteActivity(WritingDrawingActivityId.DRAW_AND_LEARN_CHALLENGE)
                                        },
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
private fun WritingWorldTopBar(
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
        border = BorderStroke(2.5.dp, DrawingAccent)
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
                        text = if (appLanguage == AppLanguage.AR) "عالم الكتابة والرسم ✍️🎨" else "Writing & Drawing ✍️🎨",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText
                    )
                    Text(
                        text = if (appLanguage == AppLanguage.AR) "كتابة الحروف والأرقام والرسم والتلوين" else "Write Letters, Numbers, Draw & Color",
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
private fun WritingDrawingHubScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    completedActivities: Set<WritingDrawingActivityId>,
    practicedItemsCount: Int,
    savedDrawings: Set<String>,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onOpenActivity: (WritingDrawingActivityId) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = completedActivities.size
    val progressFraction = (completedCount / 6f).coerceIn(0f, 1f)

    val welcomeMessage = if (appLanguage == AppLanguage.AR) {
        "أهلاً بكِ في عالم الكتابة والرسم! هيا نتدرّب بإصبعنا على كتابة الحروف العربية والإنجليزية والأرقام، ونرسم ونلوّن أجمل اللوحات!"
    } else {
        "Welcome to Writing & Drawing World! Let's trace Arabic & English letters and numbers with our finger, draw freely, and color!"
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
                    .testTag("writing_hub_hero_card"),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = DrawingCardBg),
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
                                .border(2.5.dp, DrawingAccent, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_card_art_shapes_1794072210824),
                                contentDescription = if (appLanguage == AppLanguage.AR) "الكتابة والرسم" else "Writing & Drawing",
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
                                    text = if (appLanguage == AppLanguage.AR) "لولو الفنانة 🔊" else "Artist Lulu 🔊",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = DrawingShadow
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
                                        "تم تدريب $practicedItemsCount حرفاً ورقماً • رسومات محفوظة: ${savedDrawings.size}"
                                    } else {
                                        "Practiced $practicedItemsCount items • Saved drawings: ${savedDrawings.size}"
                                    },
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = DeepNavyText
                                )
                                Text(
                                    text = "$completedCount / 6 ⭐",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = DrawingShadow
                                )
                            }
                            LinearProgressIndicator(
                                progress = { progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = MeadowGreen,
                                trackColor = Color(0xFFB2DFDB)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = if (appLanguage == AppLanguage.AR) "اختاري نشاط الكتابة أو الرسم (٦ أنشطة):" else "Choose a Writing or Drawing Activity (6 Activities):",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = DeepNavyText,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .padding(top = 4.dp)
            )
        }

        val activityPairs = WritingDrawingActivityId.entries.chunked(2)
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
                            .height(182.dp)
                            .shadow(7.dp, RoundedCornerShape(26.dp))
                            .clickable { onOpenActivity(activity) }
                            .testTag("writing_activity_card_${activity.key}"),
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
// 1. WRITE ARABIC LETTERS (28 LETTERS + FORMS + STEP-BY-STEP TRACING)
// =====================================================================

@Composable
private fun WriteArabicLettersActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    practicedItems: Set<String>,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkItemPracticed: (String) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val arabicLetters = LettersCurriculum.arabicLetters
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentLetter = arabicLetters[currentIndex]
    val forms = remember(currentLetter.isolatedSymbol) {
        WritingDrawingCurriculum.getArabicLetterForms(currentLetter.isolatedSymbol)
    }

    val strokes = remember(currentLetter.id) { mutableStateListOf<List<Offset>>() }
    var hitCheckpoints by remember(currentLetter.id) { mutableStateOf(emptySet<Int>()) }
    var encouragementBanner by remember(currentLetter.id) { mutableStateOf<String?>(null) }

    LaunchedEffect(currentIndex, appLanguage) {
        val msg = if (appLanguage == AppLanguage.AR) {
            "هيا نكتب ${currentLetter.letterNameAr} (${currentLetter.isolatedSymbol})! تتبّعي المسار من الرقم ١ إلى ٤ بإصبعك!"
        } else {
            "Let's write Arabic letter ${currentLetter.letterNameEn} (${currentLetter.isolatedSymbol})! Trace from 1 to 4 with your finger!"
        }
        onSpeakLulu(msg, appLanguage)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 28 Arabic Letters Selector Bar
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(arabicLetters.size) { idx ->
                    val item = arabicLetters[idx]
                    val isSelected = idx == currentIndex
                    val isPracticed = practicedItems.contains("write_ar_${item.id}")
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when {
                            isSelected -> item.color
                            isPracticed -> Color(0xFFE8F5E9)
                            else -> Color.White
                        },
                        border = BorderStroke(
                            2.dp,
                            if (isSelected) SunnyYellowDark else item.color
                        ),
                        modifier = Modifier
                            .size(54.dp)
                            .clickable {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                currentIndex = idx
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = item.isolatedSymbol,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = if (isSelected) Color.White else DeepNavyText
                            )
                        }
                    }
                }
            }
        }

        // Letter Header + Positional Forms (أول الكلمة • وسط الكلمة • آخر الكلمة)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(6.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = currentLetter.cardBgColor),
                border = BorderStroke(2.5.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = currentLetter.color,
                                border = BorderStroke(2.dp, Color.White)
                            ) {
                                Text(
                                    text = currentLetter.isolatedSymbol,
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "${currentLetter.letterNameAr} • ${currentLetter.emoji} ${currentLetter.wordAr}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = DeepNavyText
                                )
                                Text(
                                    text = if (appLanguage == AppLanguage.AR) currentLetter.strokeDirectionHintAr else currentLetter.strokeDirectionHintEn,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = SoftSlateText
                                )
                            }
                        }

                        Bouncy3DIconButton(
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak",
                            onClick = {
                                onSpeakLulu(currentLetter.luluPronunciationSentence, appLanguage)
                            },
                            containerColor = currentLetter.color,
                            shadowColor = currentLetter.shadowColor
                        )
                    }

                    // Positional Forms Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val formItems = listOf(
                            (if (appLanguage == AppLanguage.AR) "منفصل" else "Isolated") to forms.isolated,
                            (if (appLanguage == AppLanguage.AR) "أول الكلمة" else "Initial") to forms.initial,
                            (if (appLanguage == AppLanguage.AR) "وسط الكلمة" else "Medial") to forms.medial,
                            (if (appLanguage == AppLanguage.AR) "آخر الكلمة" else "Final") to forms.finalForm
                        )
                        formItems.forEach { (label, symbol) ->
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White,
                                border = BorderStroke(1.5.dp, currentLetter.color.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = symbol,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                        color = currentLetter.color
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        color = SoftSlateText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Interactive Finger Tracing Canvas
        item {
            LetterFingerTracingPad(
                lesson = currentLetter,
                traceSymbol = currentLetter.isolatedSymbol,
                strokes = strokes,
                hitCheckpoints = hitCheckpoints,
                onStartNewStroke = { pt, size ->
                    strokes.add(listOf(pt))
                    val updated = findLetterHitCheckpoints(pt, size, currentLetter, hitCheckpoints)
                    hitCheckpoints = updated
                },
                onAddPoint = { pt, size ->
                    if (strokes.isNotEmpty()) {
                        val lastIdx = strokes.lastIndex
                        strokes[lastIdx] = strokes[lastIdx] + pt
                    } else {
                        strokes.add(listOf(pt))
                    }
                    val updated = findLetterHitCheckpoints(pt, size, currentLetter, hitCheckpoints)
                    if (updated.size > hitCheckpoints.size && updated.size >= 2) {
                        // Gentle simplified evaluation: encourage as soon as child traces 2+ checkpoints
                        onMarkItemPracticed("write_ar_${currentLetter.id}")
                        onComplete()
                        encouragementBanner = if (appLanguage == AppLanguage.AR) {
                            "خط رائع يا بطلة! أحسنتِ كتابة ${currentLetter.letterNameAr} 🌟"
                        } else {
                            "Wonderful writing! Great job tracing ${currentLetter.letterNameEn} 🌟"
                        }
                    }
                    hitCheckpoints = updated
                },
                modifier = Modifier.widthIn(max = 640.dp)
            )
        }

        if (encouragementBanner != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(2.dp, MeadowGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = encouragementBanner.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF1B5E20),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Controls: Clear/Retry, Evaluate & Encourage, Next/Prev
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "السابق" else "Prev",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        currentIndex = if (currentIndex > 0) currentIndex - 1 else arabicLetters.lastIndex
                    },
                    containerColor = Color(0xFF29B6F6),
                    shadowColor = Color(0xFF0288D1),
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    modifier = Modifier.weight(1f)
                )

                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "مسح وإعادة" else "Clear",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        strokes.clear()
                        hitCheckpoints = emptySet()
                        encouragementBanner = null
                    },
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    icon = Icons.Filled.Refresh,
                    modifier = Modifier.weight(1f),
                    testTagName = "write_arabic_clear_btn"
                )

                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "التالي" else "Next",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        currentIndex = (currentIndex + 1) % arabicLetters.size
                    },
                    containerColor = MeadowGreen,
                    shadowColor = MeadowGreenDark,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// =====================================================================
// 2. WRITE ENGLISH LETTERS (A–Z UPPERCASE & LOWERCASE)
// =====================================================================

@Composable
private fun WriteEnglishLettersActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    practicedItems: Set<String>,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkItemPracticed: (String) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val englishLetters = LettersCurriculum.englishLetters
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var caseMode by rememberSaveable { mutableIntStateOf(0) } // 0 = Both "A a", 1 = Uppercase "A", 2 = Lowercase "a"
    val currentLetter = englishLetters[currentIndex]

    val traceSymbol = when (caseMode) {
        1 -> currentLetter.uppercaseSymbol
        2 -> currentLetter.lowercaseSymbol
        else -> "${currentLetter.uppercaseSymbol} ${currentLetter.lowercaseSymbol}"
    }

    val strokes = remember(currentLetter.id, caseMode) { mutableStateListOf<List<Offset>>() }
    var hitCheckpoints by remember(currentLetter.id, caseMode) { mutableStateOf(emptySet<Int>()) }
    var encouragementBanner by remember(currentLetter.id, caseMode) { mutableStateOf<String?>(null) }

    LaunchedEffect(currentIndex, appLanguage) {
        val msg = if (appLanguage == AppLanguage.AR) {
            "هيا نكتب حرف ${currentLetter.uppercaseSymbol} الكبير و ${currentLetter.lowercaseSymbol} الصغير!"
        } else {
            "Let's write Uppercase ${currentLetter.uppercaseSymbol} and Lowercase ${currentLetter.lowercaseSymbol}!"
        }
        onSpeakLulu(msg, appLanguage)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(englishLetters.size) { idx ->
                    val item = englishLetters[idx]
                    val isSelected = idx == currentIndex
                    val isPracticed = practicedItems.contains("write_en_${item.id}")
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when {
                            isSelected -> item.color
                            isPracticed -> Color(0xFFE8F5E9)
                            else -> Color.White
                        },
                        border = BorderStroke(2.dp, if (isSelected) SunnyYellowDark else item.color),
                        modifier = Modifier
                            .size(56.dp)
                            .clickable {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                currentIndex = idx
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${item.uppercaseSymbol}${item.lowercaseSymbol}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = if (isSelected) Color.White else DeepNavyText
                            )
                        }
                    }
                }
            }
        }

        // Case selector (A a / A / a)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(6.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = currentLetter.cardBgColor),
                border = BorderStroke(2.5.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${currentLetter.uppercaseSymbol} ${currentLetter.lowercaseSymbol} • ${currentLetter.emoji} ${currentLetter.wordEn}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = DeepNavyText
                        )
                        Bouncy3DIconButton(
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak",
                            onClick = {
                                onSpeakLulu(currentLetter.luluPronunciationSentence, appLanguage)
                            },
                            containerColor = currentLetter.color,
                            shadowColor = currentLetter.shadowColor
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val modes = listOf(
                            0 to (if (appLanguage == AppLanguage.AR) "الكبير والصغير (${currentLetter.uppercaseSymbol} ${currentLetter.lowercaseSymbol})" else "Both (${currentLetter.uppercaseSymbol} ${currentLetter.lowercaseSymbol})"),
                            1 to (if (appLanguage == AppLanguage.AR) "الحرف الكبير (${currentLetter.uppercaseSymbol})" else "Uppercase (${currentLetter.uppercaseSymbol})"),
                            2 to (if (appLanguage == AppLanguage.AR) "الحرف الصغير (${currentLetter.lowercaseSymbol})" else "Lowercase (${currentLetter.lowercaseSymbol})")
                        )
                        modes.forEach { (modeIdx, label) ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        SoundEffectsHelper.playPopChime(soundEnabled)
                                        caseMode = modeIdx
                                    },
                                shape = RoundedCornerShape(14.dp),
                                color = if (caseMode == modeIdx) currentLetter.color else Color.White,
                                border = BorderStroke(1.5.dp, currentLetter.color)
                            ) {
                                Text(
                                    text = label,
                                    color = if (caseMode == modeIdx) Color.White else DeepNavyText,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            LetterFingerTracingPad(
                lesson = currentLetter,
                traceSymbol = traceSymbol,
                strokes = strokes,
                hitCheckpoints = hitCheckpoints,
                onStartNewStroke = { pt, size ->
                    strokes.add(listOf(pt))
                    hitCheckpoints = findLetterHitCheckpoints(pt, size, currentLetter, hitCheckpoints)
                },
                onAddPoint = { pt, size ->
                    if (strokes.isNotEmpty()) {
                        val lastIdx = strokes.lastIndex
                        strokes[lastIdx] = strokes[lastIdx] + pt
                    } else {
                        strokes.add(listOf(pt))
                    }
                    val updated = findLetterHitCheckpoints(pt, size, currentLetter, hitCheckpoints)
                    if (updated.size > hitCheckpoints.size && updated.size >= 2) {
                        onMarkItemPracticed("write_en_${currentLetter.id}")
                        onComplete()
                        encouragementBanner = if (appLanguage == AppLanguage.AR) {
                            "ممتاز! كتابة جميلة لحرف ${currentLetter.uppercaseSymbol} 🌟"
                        } else {
                            "Awesome! Great job writing letter ${currentLetter.uppercaseSymbol} 🌟"
                        }
                    }
                    hitCheckpoints = updated
                },
                modifier = Modifier.widthIn(max = 640.dp)
            )
        }

        if (encouragementBanner != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(2.dp, MeadowGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = encouragementBanner.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF1B5E20),
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "السابق" else "Prev",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        currentIndex = if (currentIndex > 0) currentIndex - 1 else englishLetters.lastIndex
                    },
                    containerColor = Color(0xFF29B6F6),
                    shadowColor = Color(0xFF0288D1),
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    modifier = Modifier.weight(1f)
                )

                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "مسح وإعادة" else "Clear",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        strokes.clear()
                        hitCheckpoints = emptySet()
                        encouragementBanner = null
                    },
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    icon = Icons.Filled.Refresh,
                    modifier = Modifier.weight(1f)
                )

                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "التالي" else "Next",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        currentIndex = (currentIndex + 1) % englishLetters.size
                    },
                    containerColor = MeadowGreen,
                    shadowColor = MeadowGreenDark,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// =====================================================================
// 3. WRITE NUMBERS 1–10 (ARABIC ١–١٠ & ENGLISH 1–10)
// =====================================================================

@Composable
private fun WriteNumbersActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    practicedItems: Set<String>,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkItemPracticed: (String) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val numberLessons = NumbersCurriculum.lessons
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var useArabicNumeral by rememberSaveable { mutableStateOf(appLanguage == AppLanguage.AR) }
    val currentNumber = numberLessons[currentIndex]

    val traceSymbol = if (useArabicNumeral) currentNumber.symbolAr else currentNumber.symbolEn
    val strokes = remember(currentNumber.value, useArabicNumeral) { mutableStateListOf<List<Offset>>() }
    var encouragementBanner by remember(currentNumber.value, useArabicNumeral) { mutableStateOf<String?>(null) }

    LaunchedEffect(currentIndex, useArabicNumeral, appLanguage) {
        val msg = if (appLanguage == AppLanguage.AR) {
            "هيا نكتب الرقم $traceSymbol (${currentNumber.wordAr}) بإصبعنا!"
        } else {
            "Let's write number $traceSymbol (${currentNumber.wordEn}) with our finger!"
        }
        onSpeakLulu(msg, appLanguage)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(numberLessons.size) { idx ->
                    val lesson = numberLessons[idx]
                    val isSelected = idx == currentIndex
                    val isPracticed = practicedItems.contains("write_num_${lesson.value}")
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when {
                            isSelected -> lesson.color
                            isPracticed -> Color(0xFFE8F5E9)
                            else -> Color.White
                        },
                        border = BorderStroke(2.dp, if (isSelected) SunnyYellowDark else lesson.color),
                        modifier = Modifier
                            .size(54.dp)
                            .clickable {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                currentIndex = idx
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (useArabicNumeral) lesson.symbolAr else lesson.symbolEn,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = if (isSelected) Color.White else DeepNavyText
                            )
                        }
                    }
                }
            }
        }

        // Arabic (١–١٠) vs English (1–10) Numeral Switch + Model Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(6.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.5.dp, currentNumber.color)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$traceSymbol • ${currentNumber.wordAr} / ${currentNumber.wordEn}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = DeepNavyText
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (useArabicNumeral) currentNumber.color else Color.White,
                                border = BorderStroke(1.5.dp, currentNumber.color),
                                modifier = Modifier.clickable {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    useArabicNumeral = true
                                }
                            ) {
                                Text(
                                    text = "١ ٢ ٣",
                                    color = if (useArabicNumeral) Color.White else DeepNavyText,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (!useArabicNumeral) currentNumber.color else Color.White,
                                border = BorderStroke(1.5.dp, currentNumber.color),
                                modifier = Modifier.clickable {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    useArabicNumeral = false
                                }
                            ) {
                                Text(
                                    text = "1 2 3",
                                    color = if (!useArabicNumeral) Color.White else DeepNavyText,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Number Tracing Canvas
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .height(280.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFFFFDF7))
                    .border(3.dp, currentNumber.color, RoundedCornerShape(28.dp))
                    .testTag("write_numbers_canvas")
                    .pointerInput(currentNumber.value, useArabicNumeral) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                strokes.add(listOf(offset))
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                if (strokes.isNotEmpty()) {
                                    val lastIdx = strokes.lastIndex
                                    strokes[lastIdx] = strokes[lastIdx] + change.position
                                }
                                if (strokes.sumOf { it.size } >= 12 && encouragementBanner == null) {
                                    onMarkItemPracticed("write_num_${currentNumber.value}")
                                    onComplete()
                                    encouragementBanner = if (appLanguage == AppLanguage.AR) {
                                        "أحسنتِ كتابة الرقم $traceSymbol (${currentNumber.wordAr})! 🌟"
                                    } else {
                                        "Great job writing number $traceSymbol (${currentNumber.wordEn})! 🌟"
                                    }
                                }
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // School lines
                    drawLine(Color(0xFFE1F5FE), Offset(24f, h * 0.2f), Offset(w - 24f, h * 0.2f), strokeWidth = 3f)
                    drawLine(Color(0xFF81D4FA), Offset(24f, h * 0.55f), Offset(w - 24f, h * 0.55f), strokeWidth = 4f)
                    drawLine(Color(0xFFE1F5FE), Offset(24f, h * 0.84f), Offset(w - 24f, h * 0.84f), strokeWidth = 3f)

                    // Dotted Number Guide
                    drawContext.canvas.nativeCanvas.apply {
                        val textY = h * 0.72f
                        val fontSizePx = h * 0.62f

                        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = currentNumber.color.copy(alpha = 0.14f).toArgb()
                            textSize = fontSizePx
                            textAlign = Paint.Align.CENTER
                            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        }
                        drawText(traceSymbol, w / 2f, textY, fillPaint)

                        val dottedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = currentNumber.color.copy(alpha = 0.78f).toArgb()
                            style = Paint.Style.STROKE
                            strokeWidth = 9f
                            pathEffect = DashPathEffect(floatArrayOf(18f, 14f), 0f)
                            textSize = fontSizePx
                            textAlign = Paint.Align.CENTER
                            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        }
                        drawText(traceSymbol, w / 2f, textY, dottedPaint)
                    }

                    // Child's traced strokes
                    strokes.forEach { pts ->
                        if (pts.size > 1) {
                            val path = Path().apply {
                                moveTo(pts.first().x, pts.first().y)
                                for (i in 1 until pts.size) {
                                    lineTo(pts[i].x, pts[i].y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = SunnyYellow.copy(alpha = 0.55f),
                                style = Stroke(width = 46f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                            drawPath(
                                path = path,
                                color = currentNumber.color,
                                style = Stroke(width = 34f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                        }
                    }
                }
            }
        }

        if (encouragementBanner != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(2.dp, MeadowGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = encouragementBanner.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF1B5E20),
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "السابق" else "Prev",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        currentIndex = if (currentIndex > 0) currentIndex - 1 else numberLessons.lastIndex
                    },
                    containerColor = Color(0xFF29B6F6),
                    shadowColor = Color(0xFF0288D1),
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    modifier = Modifier.weight(1f)
                )

                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "مسح وإعادة" else "Clear",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        strokes.clear()
                        encouragementBanner = null
                    },
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    icon = Icons.Filled.Refresh,
                    modifier = Modifier.weight(1f)
                )

                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "التالي" else "Next",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        currentIndex = (currentIndex + 1) % numberLessons.size
                    },
                    containerColor = MeadowGreen,
                    shadowColor = MeadowGreenDark,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// =====================================================================
// 4. FREE DRAWING STUDIO | مساحة الرسم الحر
// =====================================================================

@Composable
private fun FreeDrawingStudioActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    savedDrawings: Set<String>,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onSaveDrawing: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val palette = WritingDrawingCurriculum.drawingPaletteColors
    var selectedColor by remember { mutableStateOf(palette.first().second) }
    var isEraser by remember { mutableStateOf(false) }
    var selectedBrushSize by remember { mutableFloatStateOf(24f) } // 14f = Small, 24f = Medium, 38f = Large
    val strokes = remember { mutableStateListOf<FreeStrokeData>() }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(appLanguage) {
        val welcome = if (appLanguage == AppLanguage.AR) {
            "هذه لوحة الرسم الحر! اختاري اللون وحجم القلم وارسمي ما تحبين، ويمكنكِ حفظ الرسم أو تنزيله على الجهاز!"
        } else {
            "Welcome to the Free Drawing Studio! Pick a color and brush size, draw freely, and save or download your art!"
        }
        onSpeakLulu(welcome, appLanguage)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Palette + Brush Size + Eraser Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .shadow(6.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(palette.size) { idx ->
                            val (_, color) = palette[idx]
                            val isSelected = !isEraser && selectedColor == color
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 46.dp else 40.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.5.dp else 2.dp,
                                        color = if (isSelected) SunnyYellowDark else Color.LightGray,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        SoundEffectsHelper.playPopChime(soundEnabled)
                                        isEraser = false
                                        selectedColor = color
                                    }
                            )
                        }
                    }

                    // Brush Sizes + Pen/Eraser Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val brushSizes = listOf(
                                14f to (if (appLanguage == AppLanguage.AR) "صغير" else "S"),
                                24f to (if (appLanguage == AppLanguage.AR) "متوسط" else "M"),
                                38f to (if (appLanguage == AppLanguage.AR) "كبير" else "L")
                            )
                            brushSizes.forEach { (sizePx, label) ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (selectedBrushSize == sizePx) DrawingAccent else Color(0xFFF5F5F5),
                                    border = BorderStroke(1.5.dp, DrawingAccent),
                                    modifier = Modifier.clickable {
                                        SoundEffectsHelper.playPopChime(soundEnabled)
                                        selectedBrushSize = sizePx
                                    }
                                ) {
                                    Text(
                                        text = label,
                                        color = if (selectedBrushSize == sizePx) Color.White else DeepNavyText,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (!isEraser) MeadowGreen else Color(0xFFF5F5F5),
                                border = BorderStroke(1.5.dp, MeadowGreenDark),
                                modifier = Modifier.clickable {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    isEraser = false
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Brush,
                                        contentDescription = null,
                                        tint = if (!isEraser) Color.White else DeepNavyText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = if (appLanguage == AppLanguage.AR) "قلم" else "Pen",
                                        color = if (!isEraser) Color.White else DeepNavyText,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isEraser) LuluPink else Color(0xFFF5F5F5),
                                border = BorderStroke(1.5.dp, LuluPinkDark),
                                modifier = Modifier
                                    .clickable {
                                        SoundEffectsHelper.playPopChime(soundEnabled)
                                        isEraser = true
                                    }
                                    .testTag("free_drawing_eraser_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoFixNormal,
                                        contentDescription = null,
                                        tint = if (isEraser) Color.White else DeepNavyText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = if (appLanguage == AppLanguage.AR) "ممحاة" else "Eraser",
                                        color = if (isEraser) Color.White else DeepNavyText,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Interactive Freehand Drawing Canvas
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .height(330.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .border(3.dp, DrawingAccent, RoundedCornerShape(28.dp))
                    .testTag("free_drawing_canvas")
                    .pointerInput(selectedColor, isEraser, selectedBrushSize) {
                        detectTapGestures { offset ->
                            strokes.add(
                                FreeStrokeData(
                                    points = listOf(offset),
                                    color = if (isEraser) Color.White else selectedColor,
                                    strokeWidth = if (isEraser) 44f else selectedBrushSize,
                                    isEraser = isEraser
                                )
                            )
                        }
                    }
                    .pointerInput(selectedColor, isEraser, selectedBrushSize) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                strokes.add(
                                    FreeStrokeData(
                                        points = listOf(offset),
                                        color = if (isEraser) Color.White else selectedColor,
                                        strokeWidth = if (isEraser) 44f else selectedBrushSize,
                                        isEraser = isEraser
                                    )
                                )
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                if (strokes.isNotEmpty()) {
                                    val lastIdx = strokes.lastIndex
                                    val current = strokes[lastIdx]
                                    strokes[lastIdx] = current.copy(points = current.points + change.position)
                                }
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    strokes.forEach { stroke ->
                        val pts = stroke.points
                        if (pts.size == 1) {
                            drawCircle(
                                color = stroke.color,
                                radius = stroke.strokeWidth / 2f,
                                center = pts.first()
                            )
                        } else if (pts.size > 1) {
                            val path = Path().apply {
                                moveTo(pts.first().x, pts.first().y)
                                for (i in 1 until pts.size) {
                                    lineTo(pts[i].x, pts[i].y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = stroke.color,
                                style = Stroke(
                                    width = stroke.strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                }
            }
        }

        if (statusMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(2.dp, MeadowGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 680.dp)
                ) {
                    Text(
                        text = statusMessage.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF1B5E20),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Action Buttons: Undo, Clear, Save Drawing, Download to Device
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Bouncy3DButton(
                        text = if (appLanguage == AppLanguage.AR) "تراجع" else "Undo",
                        onClick = {
                            SoundEffectsHelper.playPopChime(soundEnabled)
                            if (strokes.isNotEmpty()) {
                                strokes.removeAt(strokes.lastIndex)
                            }
                        },
                        containerColor = Color(0xFF29B6F6),
                        shadowColor = Color(0xFF0288D1),
                        icon = Icons.AutoMirrored.Filled.Undo,
                        modifier = Modifier.weight(1f),
                        testTagName = "free_drawing_undo_btn"
                    )

                    Bouncy3DButton(
                        text = if (appLanguage == AppLanguage.AR) "مسح اللوحة" else "Clear All",
                        onClick = {
                            SoundEffectsHelper.playPopChime(soundEnabled)
                            strokes.clear()
                            statusMessage = null
                        },
                        containerColor = LuluPink,
                        shadowColor = LuluPinkDark,
                        icon = Icons.Filled.DeleteSweep,
                        modifier = Modifier.weight(1f),
                        testTagName = "free_drawing_clear_btn"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Bouncy3DButton(
                        text = if (appLanguage == AppLanguage.AR) "حفظ الرسم ⭐" else "Save Drawing ⭐",
                        onClick = {
                            val title = if (appLanguage == AppLanguage.AR) {
                                "لوحة فنية رقم ${savedDrawings.size + 1}"
                            } else {
                                "Artwork #${savedDrawings.size + 1}"
                            }
                            onSaveDrawing(title)
                            statusMessage = if (appLanguage == AppLanguage.AR) {
                                "تم حفظ رسمتك الجميلة في معرض الطفل! 🌟"
                            } else {
                                "Your lovely drawing has been saved to your gallery! 🌟"
                            }
                            onSpeakLulu(statusMessage.orEmpty(), appLanguage)
                        },
                        containerColor = MeadowGreen,
                        shadowColor = MeadowGreenDark,
                        icon = Icons.Filled.Save,
                        modifier = Modifier.weight(1f),
                        testTagName = "free_drawing_save_btn"
                    )

                    Bouncy3DButton(
                        text = if (appLanguage == AppLanguage.AR) "تنزيل على الجهاز" else "Download PNG",
                        onClick = {
                            val exported = exportStrokesToDeviceGallery(context, strokes)
                            val title = if (appLanguage == AppLanguage.AR) {
                                "رسمة منزّلة ${savedDrawings.size + 1}"
                            } else {
                                "Downloaded Art #${savedDrawings.size + 1}"
                            }
                            onSaveDrawing(title)
                            statusMessage = if (exported) {
                                if (appLanguage == AppLanguage.AR) {
                                    "تم تنزيل الرسمة بنجاح في صور الجهاز! 📥🎨"
                                } else {
                                    "Drawing downloaded to your device's Pictures! 📥🎨"
                                }
                            } else {
                                if (appLanguage == AppLanguage.AR) {
                                    "تم حفظ الرسمة بنجاح داخل التطبيق! 🌟"
                                } else {
                                    "Drawing saved inside the app! 🌟"
                                }
                            }
                            onSpeakLulu(statusMessage.orEmpty(), appLanguage)
                        },
                        containerColor = Color(0xFF8E24AA),
                        shadowColor = Color(0xFF4A148C),
                        icon = Icons.Filled.Download,
                        modifier = Modifier.weight(1f),
                        testTagName = "free_drawing_download_btn"
                    )
                }
            }
        }
    }
}

private fun exportStrokesToDeviceGallery(
    context: Context,
    strokes: List<FreeStrokeData>
): Boolean {
    return try {
        val width = 800
        val height = 600
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        strokes.forEach { stroke ->
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = stroke.color.toArgb()
                strokeWidth = stroke.strokeWidth
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
            }
            val pts = stroke.points
            if (pts.size == 1) {
                paint.style = Paint.Style.FILL
                canvas.drawCircle(pts[0].x, pts[0].y, stroke.strokeWidth / 2f, paint)
            } else if (pts.size > 1) {
                val path = android.graphics.Path().apply {
                    moveTo(pts[0].x, pts[0].y)
                    for (i in 1 until pts.size) {
                        lineTo(pts[i].x, pts[i].y)
                    }
                }
                canvas.drawPath(path, paint)
            }
        }

        val filename = "FunLearningWorld_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/FunLearningWorld")
            }
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            true
        } else {
            false
        }
    } catch (_: Throwable) {
        false
    }
}

// =====================================================================
// 5. INTERACTIVE COLORING BOOK | التلوين التفاعلي (الحيوانات، الفواكه، السيارات، الزهور، المنازل، لولو)
// =====================================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InteractiveColoringBookActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onSaveDrawing: (String) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val templates = ColoringTemplateTheme.entries
    var currentTemplateIdx by rememberSaveable { mutableIntStateOf(0) }
    val currentTemplate = templates[currentTemplateIdx]

    val palette = WritingDrawingCurriculum.drawingPaletteColors
    var selectedColor by remember { mutableStateOf(palette.first().second) }
    val regionColors = remember(currentTemplate.id) {
        mutableStateMapOf<Int, Color>()
    }
    var statusText by remember(currentTemplate.id) { mutableStateOf<String?>(null) }

    LaunchedEffect(currentTemplateIdx, appLanguage) {
        val msg = if (appLanguage == AppLanguage.AR) {
            "هيا نلوّن ${currentTemplate.titleAr} (${currentTemplate.categoryAr})! اختاري لوناً ثم اضغطي على أجزاء الصورة!"
        } else {
            "Let's color ${currentTemplate.titleEn} (${currentTemplate.categoryEn})! Pick a color and tap each part!"
        }
        onSpeakLulu(msg, appLanguage)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 6 Cartoon Templates Selector (Animals, Fruits, Cars, Flowers, Houses, Cartoon Characters)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(templates.size) { idx ->
                    val tpl = templates[idx]
                    val isSelected = idx == currentTemplateIdx
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) Color(0xFFFB8C00) else Color.White,
                        border = BorderStroke(2.dp, Color(0xFFFB8C00)),
                        modifier = Modifier.clickable {
                            SoundEffectsHelper.playPopChime(soundEnabled)
                            currentTemplateIdx = idx
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = tpl.emoji, fontSize = 22.sp)
                            Text(
                                text = tpl.title(appLanguage),
                                color = if (isSelected) Color.White else DeepNavyText,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold)
                            )
                        }
                    }
                }
            }
        }

        // Color Palette
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(5.dp, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                LazyRow(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(palette.size) { idx ->
                        val (_, color) = palette[idx]
                        val isSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 46.dp else 40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.5.dp else 2.dp,
                                    color = if (isSelected) SunnyYellowDark else Color.LightGray,
                                    shape = CircleShape
                                )
                                .clickable {
                                    SoundEffectsHelper.playPopChime(soundEnabled)
                                    selectedColor = color
                                }
                        )
                    }
                }
            }
        }

        // Interactive Cartoon Coloring Preview + Tap-to-Color Regions
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(7.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF7)),
                border = BorderStroke(3.dp, Color(0xFFFB8C00))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "${currentTemplate.emoji} ${currentTemplate.title(appLanguage)}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText
                    )

                    // Custom Canvas Illustration that updates dynamically as each region is colored
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.White)
                            .border(2.dp, Color(0xFFFFE0B2), RoundedCornerShape(22.dp))
                    ) {
                        val w = size.width
                        val h = size.height
                        val c0 = regionColors[0] ?: Color(0xFFECEFF1)
                        val c1 = regionColors[1] ?: Color(0xFFF5F5F5)
                        val c2 = regionColors[2] ?: Color(0xFFEEEEEE)
                        val c3 = regionColors[3] ?: Color(0xFFE0E0E0)

                        // Ground / Base region (c3)
                        drawRoundRect(
                            color = c3,
                            topLeft = Offset(w * 0.1f, h * 0.72f),
                            size = Size(w * 0.8f, h * 0.2f),
                            cornerRadius = CornerRadius(24f, 24f)
                        )
                        // Main body / shape (c2)
                        drawRoundRect(
                            color = c2,
                            topLeft = Offset(w * 0.28f, h * 0.42f),
                            size = Size(w * 0.44f, h * 0.34f),
                            cornerRadius = CornerRadius(32f, 32f)
                        )
                        // Outer / Crown / Roof / Mane (c0)
                        drawCircle(
                            color = c0,
                            radius = h * 0.25f,
                            center = Offset(w * 0.5f, h * 0.32f)
                        )
                        // Inner Face / Center / Window (c1)
                        drawCircle(
                            color = c1,
                            radius = h * 0.16f,
                            center = Offset(w * 0.5f, h * 0.32f)
                        )
                    }

                    Text(
                        text = if (appLanguage == AppLanguage.AR) {
                            "اضغطي على أي جزء من الصورة لتلوينه باللون المختار:"
                        } else {
                            "Tap any part of the picture to color it with your chosen color:"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = SoftSlateText
                    )

                    val regionLabels = if (appLanguage == AppLanguage.AR) {
                        currentTemplate.regionNamesAr
                    } else {
                        currentTemplate.regionNamesEn
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        regionLabels.forEachIndexed { rIdx, rName ->
                            val filledColor = regionColors[rIdx]
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .clickable {
                                        SoundEffectsHelper.playPopChime(soundEnabled)
                                        regionColors[rIdx] = selectedColor
                                        if (regionColors.size >= 3) {
                                            SoundEffectsHelper.playStarCollectChime(soundEnabled)
                                            onComplete()
                                            statusText = if (appLanguage == AppLanguage.AR) {
                                                "تلوين مبهج وجميل جداً! 🌟🎨"
                                            } else {
                                                "Such joyful and colorful artwork! 🌟🎨"
                                            }
                                            onSpeakLulu(statusText.orEmpty(), appLanguage)
                                        }
                                    }
                                    .testTag("coloring_region_btn_$rIdx"),
                                shape = RoundedCornerShape(16.dp),
                                color = filledColor ?: Color.White,
                                border = BorderStroke(2.dp, Color(0xFFFB8C00))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = rName,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                                        color = if (filledColor != null && filledColor != Color(0xFFFDD835)) Color.White else DeepNavyText,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    if (statusText != null) {
                        Text(
                            text = statusText.orEmpty(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = MeadowGreenDark,
                            textAlign = TextAlign.Center
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (appLanguage == AppLanguage.AR) "مسح الألوان" else "Reset Colors",
                            onClick = {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                regionColors.clear()
                                statusText = null
                            },
                            containerColor = LuluPink,
                            shadowColor = LuluPinkDark,
                            icon = Icons.Filled.Refresh,
                            modifier = Modifier.weight(1f)
                        )

                        Bouncy3DButton(
                            text = if (appLanguage == AppLanguage.AR) "حفظ التلوين ⭐" else "Save Coloring ⭐",
                            onClick = {
                                onSaveDrawing(currentTemplate.title(appLanguage))
                                onComplete()
                                statusText = if (appLanguage == AppLanguage.AR) {
                                    "تم حفظ تلوين ${currentTemplate.titleAr} بنجاح! 🌟"
                                } else {
                                    "Saved your ${currentTemplate.titleEn} coloring! 🌟"
                                }
                                onSpeakLulu(statusText.orEmpty(), appLanguage)
                            },
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            icon = Icons.Filled.Save,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// =====================================================================
// 6. DRAW & LEARN CHALLENGE | تحدي ارسم وتعلّم
// =====================================================================

@Composable
private fun DrawAndLearnChallengeActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val challenges = WritingDrawingCurriculum.drawAndLearnChallenges
    var currentChallengeIdx by rememberSaveable { mutableIntStateOf(0) }
    val challenge = challenges[currentChallengeIdx]

    val palette = WritingDrawingCurriculum.drawingPaletteColors
    var selectedColor by remember(currentChallengeIdx) { mutableStateOf(challenge.targetColor) }
    val strokes = remember(currentChallengeIdx) { mutableStateListOf<FreeStrokeData>() }
    var feedbackBanner by remember(currentChallengeIdx) { mutableStateOf<String?>(null) }

    LaunchedEffect(currentChallengeIdx, appLanguage) {
        onSpeakLulu(challenge.instruction(appLanguage), appLanguage)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .shadow(6.dp, RoundedCornerShape(26.dp))
                    .clickable { onSpeakLulu(challenge.instruction(appLanguage), appLanguage) },
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                border = BorderStroke(2.5.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${currentChallengeIdx + 1} / ${challenges.size} • ${challenge.emoji}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MeadowGreenDark
                    )
                    Text(
                        text = challenge.title(appLanguage),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = DeepNavyText,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = challenge.instruction(appLanguage),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = SoftSlateText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Color Palette Strip
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(palette.size) { idx ->
                    val (_, color) = palette[idx]
                    val isSelected = selectedColor == color
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 46.dp else 38.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.5.dp else 2.dp,
                                color = if (isSelected) SunnyYellowDark else Color.White,
                                shape = CircleShape
                            )
                            .clickable {
                                SoundEffectsHelper.playPopChime(soundEnabled)
                                selectedColor = color
                            }
                    )
                }
            }
        }

        // Guided Challenge Canvas
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .height(270.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .border(3.dp, challenge.targetColor, RoundedCornerShape(28.dp))
                    .testTag("draw_and_learn_canvas")
                    .pointerInput(currentChallengeIdx, selectedColor) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                strokes.add(FreeStrokeData(listOf(offset), selectedColor, 28f))
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                if (strokes.isNotEmpty()) {
                                    val lastIdx = strokes.lastIndex
                                    val cur = strokes[lastIdx]
                                    strokes[lastIdx] = cur.copy(points = cur.points + change.position)
                                }
                                if (strokes.sumOf { it.points.size } >= 10 && feedbackBanner == null) {
                                    SoundEffectsHelper.playHappyMelody(soundEnabled)
                                    onComplete()
                                    feedbackBanner = if (appLanguage == AppLanguage.AR) {
                                        "رسمة رائعة يا فنانة! لولو فخورة بكِ جداً! 🌟👏"
                                    } else {
                                        "Wonderful drawing, little artist! Lulu is so proud of you! 🌟👏"
                                    }
                                    onSpeakLulu(feedbackBanner.orEmpty(), appLanguage)
                                }
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val guideStroke = Stroke(
                        width = 7f,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(18f, 14f), 0f)
                    )

                    // Draw dotted guide outline for the challenge
                    when (challenge.guideShapeType) {
                        "circle", "apple", "sun" -> {
                            drawCircle(
                                color = challenge.targetColor.copy(alpha = 0.45f),
                                radius = minOf(w, h) * 0.30f,
                                center = Offset(w / 2f, h / 2f),
                                style = guideStroke
                            )
                        }
                        "square" -> {
                            val side = minOf(w, h) * 0.56f
                            drawRoundRect(
                                color = challenge.targetColor.copy(alpha = 0.45f),
                                topLeft = Offset((w - side) / 2f, (h - side) / 2f),
                                size = Size(side, side),
                                cornerRadius = CornerRadius(18f, 18f),
                                style = guideStroke
                            )
                        }
                        "tree" -> {
                            drawRoundRect(
                                color = Color(0xFF6D4C41).copy(alpha = 0.45f),
                                topLeft = Offset(w * 0.44f, h * 0.55f),
                                size = Size(w * 0.12f, h * 0.32f),
                                style = guideStroke
                            )
                            drawCircle(
                                color = challenge.targetColor.copy(alpha = 0.45f),
                                radius = minOf(w, h) * 0.26f,
                                center = Offset(w / 2f, h * 0.38f),
                                style = guideStroke
                            )
                        }
                    }

                    // Child's drawn strokes
                    strokes.forEach { stroke ->
                        val pts = stroke.points
                        if (pts.size > 1) {
                            val path = Path().apply {
                                moveTo(pts.first().x, pts.first().y)
                                for (i in 1 until pts.size) {
                                    lineTo(pts[i].x, pts[i].y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = stroke.color,
                                style = Stroke(width = stroke.strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                        }
                    }
                }
            }
        }

        if (feedbackBanner != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(2.dp, MeadowGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Text(
                        text = feedbackBanner.orEmpty(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF1B5E20),
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "مسح وإعادة" else "Clear",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        strokes.clear()
                        feedbackBanner = null
                    },
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    icon = Icons.Filled.Refresh,
                    modifier = Modifier.weight(1f)
                )

                Bouncy3DButton(
                    text = if (appLanguage == AppLanguage.AR) "التحدي التالي" else "Next Challenge",
                    onClick = {
                        SoundEffectsHelper.playPopChime(soundEnabled)
                        currentChallengeIdx = (currentChallengeIdx + 1) % challenges.size
                    },
                    containerColor = MeadowGreen,
                    shadowColor = MeadowGreenDark,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    modifier = Modifier.weight(1f),
                    testTagName = "draw_and_learn_next_btn"
                )
            }
        }
    }
}
