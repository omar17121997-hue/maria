package com.example.ui.screens.colors

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoFixNormal
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.ColorLessonItem
import com.example.model.ColorsActivityId
import com.example.model.ColorsCurriculum
import com.example.ui.FunLearningUiState
import com.example.ui.components.Bouncy3DButton
import com.example.ui.components.Bouncy3DIconButton
import com.example.ui.components.LanguageSelectorSwitch
import com.example.ui.components.PlayfulSkyBackground
import com.example.ui.theme.ColorsAccent
import com.example.ui.theme.ColorsCardBg
import com.example.ui.theme.ColorsShadow
import com.example.ui.theme.DeepNavyText
import com.example.ui.theme.LuluPink
import com.example.ui.theme.LuluPinkDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.SoftSlateText
import com.example.ui.theme.StarGoldBorder
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark
import kotlin.math.hypot

sealed interface ColorsWorldRoute {
    data object Hub : ColorsWorldRoute
    data class Activity(
        val id: ColorsActivityId,
        val initialColorIndex: Int = 1
    ) : ColorsWorldRoute
}

@Composable
fun ColorsWorldScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onCompleteActivity: (ColorsActivityId) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf<ColorsWorldRoute>(ColorsWorldRoute.Hub) }

    BackHandler {
        if (currentRoute != ColorsWorldRoute.Hub) {
            currentRoute = ColorsWorldRoute.Hub
        } else {
            onNavigateHome()
        }
    }

    when (val route = currentRoute) {
        is ColorsWorldRoute.Hub -> {
            ColorsWorldHubScreen(
                uiState = uiState,
                onNavigateHome = onNavigateHome,
                onSelectLanguage = onSelectLanguage,
                onToggleSound = onToggleSound,
                onSpeakLulu = onSpeakLulu,
                onOpenActivity = { activityId, colorIdx ->
                    currentRoute = ColorsWorldRoute.Activity(activityId, colorIdx)
                },
                modifier = modifier
            )
        }

        is ColorsWorldRoute.Activity -> {
            when (route.id) {
                ColorsActivityId.MEET_COLORS -> {
                    MeetColorsActivityScreen(
                        initialColorIndex = route.initialColorIndex,
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = ColorsActivityId.MEET_COLORS in uiState.settings.completedColorsActivities,
                        onBackToHub = { currentRoute = ColorsWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(ColorsActivityId.MEET_COLORS) }
                    )
                }

                ColorsActivityId.FIND_COLOR -> {
                    FindColorActivityScreen(
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = ColorsActivityId.FIND_COLOR in uiState.settings.completedColorsActivities,
                        onBackToHub = { currentRoute = ColorsWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(ColorsActivityId.FIND_COLOR) }
                    )
                }

                ColorsActivityId.MATCH_COLOR -> {
                    ColorMatchingActivityScreen(
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = ColorsActivityId.MATCH_COLOR in uiState.settings.completedColorsActivities,
                        onBackToHub = { currentRoute = ColorsWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(ColorsActivityId.MATCH_COLOR) }
                    )
                }

                ColorsActivityId.COLORING -> {
                    InteractiveColoringActivityScreen(
                        appLanguage = uiState.settings.language,
                        soundEnabled = uiState.settings.soundEnabled,
                        isCompleted = ColorsActivityId.COLORING in uiState.settings.completedColorsActivities,
                        onBackToHub = { currentRoute = ColorsWorldRoute.Hub },
                        onNavigateHome = onNavigateHome,
                        onToggleSound = onToggleSound,
                        onSpeakLulu = onSpeakLulu,
                        onMarkCompleted = { onCompleteActivity(ColorsActivityId.COLORING) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorsWorldHubScreen(
    uiState: FunLearningUiState,
    onNavigateHome: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onOpenActivity: (ColorsActivityId, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = uiState.isArabic
    val soundEnabled = uiState.settings.soundEnabled
    val completedSet = uiState.settings.completedColorsActivities

    val welcomeText = if (isArabic) {
        "هيا نتعلم الألوان معاً! اختاري أي لون جميل من لوحة الألوان لنتعرّف على اسمه والأشياء الملونة!"
    } else {
        "Let's learn colors together! Tap any bright color on the palette to hear its name and see colorful objects!"
    }

    LaunchedEffect(uiState.settings.language) {
        onSpeakLulu(
            if (isArabic) "هيا نتعلم الألوان معاً!" else "Let's learn colors together!",
            uiState.settings.language
        )
    }

    PlayfulSkyBackground(
        modifier = modifier
            .fillMaxSize()
            .testTag("colors_world_hub"),
        accentTint = ColorsAccent
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
            // Top Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = BorderStroke(2.5.dp, ColorsAccent)
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
                        testTagName = "colors_hub_home_button"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageSelectorSwitch(
                            currentLanguage = uiState.settings.language,
                            onSelectLanguage = onSelectLanguage
                        )

                        Bouncy3DIconButton(
                            icon = if (soundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = if (soundEnabled) "Sound On" else "Sound Off",
                            onClick = onToggleSound,
                            containerColor = if (soundEnabled) MeadowGreen else Color(0xFFCFD8DC),
                            shadowColor = if (soundEnabled) MeadowGreenDark else Color(0xFF78909C),
                            iconTint = if (soundEnabled) Color.White else DeepNavyText
                        )
                    }
                }
            }

            // Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = ColorsCardBg),
                border = BorderStroke(3.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(2.5.dp, Color.White),
                        shadowElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_cat_colors_1794073880642),
                            contentDescription = if (isArabic) "عالم الألوان" else "Colors World",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "عالم الألوان المبهج" else "Colors Learning World",
                                color = DeepNavyText,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isArabic) 27.sp else 24.sp
                                )
                            )
                            Text(
                                text = if (isArabic) "١٠ ألوان أساسية بالعربية والإنجليزية" else "10 Basic Colors in Arabic & English",
                                color = SoftSlateText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

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
                                    text = "${completedSet.size} / 4",
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
                            .border(1.5.dp, ColorsAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    ) {
                        val fraction = uiState.settings.colorsWorldProgressFraction.coerceAtLeast(0.08f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .height(18.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFE53935), SunnyYellow, MeadowGreen, Color(0xFF1E88E5), ColorsAccent)
                                    )
                                )
                        )
                    }

                    // Lulu Welcome Speech Bubble
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
                                modifier = Modifier.size(70.dp),
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
                                        fontSize = if (isArabic) 17.sp else 15.sp
                                    )
                                )
                                Text(
                                    text = if (isArabic) "اضغطي لسماع صوت لولو 🔊" else "Tap to hear Lulu's voice 🔊",
                                    color = LuluPink,
                                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
                                )
                            }
                        }
                    }
                }
            }

            // 10 Interactive Color Swatches Grid
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = BorderStroke(2.dp, ColorsAccent.copy(alpha = 0.55f)),
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
                            "لوحة الألوان العشرة (اضغطي على أي لون لتعلّمه!)"
                        } else {
                            "All 10 Colors Palette (Tap any color to learn it!)"
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
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        maxItemsInEachRow = 5
                    ) {
                        ColorsCurriculum.allColors.forEach { item ->
                            Surface(
                                modifier = Modifier
                                    .size(width = 112.dp, height = 88.dp)
                                    .testTag("hub_color_tile_${item.id}")
                                    .clickable {
                                        onOpenActivity(ColorsActivityId.MEET_COLORS, item.index)
                                    },
                                shape = RoundedCornerShape(20.dp),
                                color = item.colorValue,
                                border = BorderStroke(
                                    width = 2.5.dp,
                                    color = if (item.isLightColor) Color(0xFF90A4AE) else Color.White
                                ),
                                shadowElevation = 4.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = item.mainEmoji,
                                        fontSize = 24.sp
                                    )
                                    Text(
                                        text = "${item.nameAr} • ${item.nameEn}",
                                        color = if (item.isLightColor) DeepNavyText else Color.White,
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

            // 4 Interactive Colors World Activities (2-Column Grid)
            Text(
                text = if (isArabic) "أنشطة وألعاب عالم الألوان (٤ أنشطة تفاعلية)" else "Colors World Activities (4 Interactive Activities)",
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabic) 22.sp else 20.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            ColorsActivityId.entries.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    rowItems.forEach { activity ->
                        val isDone = activity in completedSet
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("colors_activity_${activity.key}")
                                .shadow(8.dp, RoundedCornerShape(26.dp))
                                .clickable { onOpenActivity(activity, 1) },
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
                                        border = BorderStroke(1.5.dp, if (isDone) StarGoldBorder else Color(0xFFCFD8DC))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isDone) Icons.Filled.Star else Icons.Filled.StarBorder,
                                                contentDescription = null,
                                                tint = if (isDone) StarGoldBorder else Color(0xFFB0BEC5),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = if (isDone) (if (isArabic) "مكتمل" else "Done") else (if (isArabic) "جديد" else "New"),
                                                color = DeepNavyText,
                                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = activity.title(uiState.settings.language),
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 20.sp else 18.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )

                                Text(
                                    text = activity.subtitle(uiState.settings.language),
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
                                    onClick = { onOpenActivity(activity, 1) },
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
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ColorsActivityTopBar(
    title: String,
    accentColor: Color,
    shadowColor: Color,
    isArabic: Boolean,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit
) {
    Surface(
        modifier = Modifier
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
                    contentDescription = if (isArabic) "الألوان" else "Colors Hub",
                    onClick = onBackToHub,
                    containerColor = accentColor,
                    shadowColor = shadowColor,
                    iconTint = Color.White,
                    badgeLabel = if (isArabic) "الألوان" else "Colors",
                    testTagName = "back_to_colors_hub_btn"
                )

                Bouncy3DIconButton(
                    icon = Icons.Filled.Home,
                    contentDescription = if (isArabic) "الرئيسية" else "Home",
                    onClick = onNavigateHome,
                    containerColor = LuluPink,
                    shadowColor = LuluPinkDark,
                    iconTint = Color.White
                )
            }

            Text(
                text = title,
                color = DeepNavyText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isArabic) 20.sp else 18.sp
                ),
                maxLines = 1
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = StarGoldBorder,
                        modifier = Modifier.size(28.dp)
                    )
                }

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

/**
 * ACTIVITY 1: MEET THE COLORS (تعرّف على الألوان)
 * - Displays large 3D glossy color jar/splash
 * - Shows both Arabic and English names
 * - Plays pronunciation via Lulu
 * - Shows 3 matching colorful objects
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MeetColorsActivityScreen(
    initialColorIndex: Int,
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    val allColors = ColorsCurriculum.allColors
    var currentIndex by remember { mutableIntStateOf(initialColorIndex.coerceIn(1, allColors.size)) }
    var visitedCount by remember { mutableIntStateOf(1) }

    val colorLesson = allColors[(currentIndex - 1).coerceIn(0, allColors.lastIndex)]
    val topBarAccent = if (colorLesson.isLightColor) ColorsAccent else colorLesson.colorValue
    val topBarShadow = if (colorLesson.isLightColor) ColorsShadow else colorLesson.shadowColor

    LaunchedEffect(colorLesson.id, appLanguage) {
        onSpeakLulu(colorLesson.luluIntro(appLanguage), appLanguage)
        if (visitedCount >= 3) {
            onMarkCompleted()
        }
    }

    PlayfulSkyBackground(accentTint = topBarAccent) {
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
            ColorsActivityTopBar(
                title = if (isArabic) "تعرّف على الألوان" else "Meet the Colors",
                accentColor = topBarAccent,
                shadowColor = topBarShadow,
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Main Color Stage Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, topBarAccent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Giant 3D Glossy Color Splash + Arabic & English Names
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 3D Glossy Color Jar / Splash
                        Box(
                            modifier = Modifier
                                .size(152.dp)
                                .clickable { onSpeakLulu(colorLesson.luluIntro(appLanguage), appLanguage) },
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val r = size.minDimension * 0.44f
                                drawCircle(
                                    color = colorLesson.shadowColor.copy(alpha = 0.35f),
                                    radius = r,
                                    center = Offset(size.width / 2f, size.height / 2f + 10f)
                                )
                                drawCircle(
                                    color = colorLesson.colorValue,
                                    radius = r,
                                    center = Offset(size.width / 2f, size.height / 2f)
                                )
                                drawCircle(
                                    color = if (colorLesson.isLightColor) Color(0xFF90A4AE) else Color.White,
                                    radius = r,
                                    center = Offset(size.width / 2f, size.height / 2f),
                                    style = Stroke(width = 8f)
                                )
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.48f),
                                    radius = r * 0.28f,
                                    center = Offset(size.width * 0.36f, size.height * 0.34f)
                                )
                            }
                            Text(
                                text = colorLesson.mainEmoji,
                                fontSize = 54.sp
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = colorLesson.nameAr,
                                color = DeepNavyText,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 38.sp
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = colorLesson.cardBgColor,
                                border = BorderStroke(1.5.dp, topBarAccent)
                            ) {
                                Text(
                                    text = colorLesson.nameEn,
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                                )
                            }

                            Bouncy3DButton(
                                text = if (isArabic) "استمع للنطق 🔊" else "Hear Color 🔊",
                                onClick = {
                                    onSpeakLulu(
                                        "${colorLesson.nameAr}. ${colorLesson.nameEn}. ${colorLesson.luluIntro(appLanguage)}",
                                        appLanguage
                                    )
                                },
                                containerColor = SunnyYellow,
                                shadowColor = SunnyYellowDark,
                                contentColor = DeepNavyText,
                                icon = Icons.AutoMirrored.Filled.VolumeUp,
                                fontSize = 15.sp,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // 3 Matching Colorful Objects
                    Text(
                        text = if (isArabic) {
                            "أشياء جميلة باللون ال${colorLesson.nameAr} (اضغطي عليها!):"
                        } else {
                            "Things that are ${colorLesson.nameEn} (Tap them!):"
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        colorLesson.matchingObjects.forEach { obj ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        val phrase = if (isArabic) {
                                            "${obj.nameAr}! لونه ${colorLesson.nameAr}."
                                        } else {
                                            "${obj.nameEn}! It is ${colorLesson.nameEn}."
                                        }
                                        onSpeakLulu(phrase, appLanguage)
                                    },
                                shape = RoundedCornerShape(22.dp),
                                color = colorLesson.cardBgColor,
                                border = BorderStroke(2.dp, topBarAccent),
                                shadowElevation = 4.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = obj.emoji, fontSize = 44.sp)
                                    Text(
                                        text = obj.nameAr,
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = obj.nameEn,
                                        color = SoftSlateText,
                                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Previous / Next Color Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (isArabic) "اللون السابق" else "Previous",
                            onClick = {
                                currentIndex = if (currentIndex > 1) currentIndex - 1 else allColors.size
                                visitedCount++
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = Color(0xFFECEFF1),
                            shadowColor = Color(0xFF90A4AE),
                            contentColor = DeepNavyText,
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            fontSize = 16.sp
                        )

                        Bouncy3DButton(
                            text = if (isArabic) "اللون التالي" else "Next Color",
                            onClick = {
                                currentIndex = if (currentIndex < allColors.size) currentIndex + 1 else 1
                                visitedCount++
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Quick 10-Color Palette Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(2.dp, topBarAccent.copy(alpha = 0.45f))
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allColors.forEach { item ->
                        val isSelected = item.index == currentIndex
                        Surface(
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    currentIndex = item.index
                                    visitedCount++
                                },
                            shape = CircleShape,
                            color = item.colorValue,
                            border = BorderStroke(
                                width = if (isSelected) 3.5.dp else 2.dp,
                                color = if (isSelected) DeepNavyText else Color(0xFF90A4AE)
                            ),
                            shadowElevation = if (isSelected) 6.dp else 2.dp
                        ) {}
                    }
                }
            }
        }
    }
}

/**
 * ACTIVITY 2: FIND THE COLOR (أين هو اللون؟)
 * Asks "أين اللون الأحمر؟" / "Where is the color Red?" and displays 4 colored objects.
 */
@Composable
private fun FindColorActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    val allColors = ColorsCurriculum.allColors

    var currentRound by remember { mutableIntStateOf(0) }
    var starsInSession by remember { mutableIntStateOf(0) }
    var selectedId by remember(currentRound) { mutableStateOf<String?>(null) }
    var isCorrect by remember(currentRound) { mutableStateOf<Boolean?>(null) }

    val targetColor = allColors[currentRound % allColors.size]
    val options: List<ColorLessonItem> = remember(targetColor.id) {
        val distractors = allColors.filter { it.id != targetColor.id }.shuffled().take(3)
        (distractors + targetColor).shuffled()
    }

    LaunchedEffect(targetColor.id, appLanguage) {
        onSpeakLulu(targetColor.findQuestion(appLanguage), appLanguage)
    }

    val meta = ColorsActivityId.FIND_COLOR

    PlayfulSkyBackground(accentTint = meta.accentColor) {
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
            ColorsActivityTopBar(
                title = meta.title(appLanguage),
                accentColor = meta.accentColor,
                shadowColor = meta.shadowColor,
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, meta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSpeakLulu(targetColor.findQuestion(appLanguage), appLanguage) },
                        shape = RoundedCornerShape(24.dp),
                        color = meta.cardBgColor,
                        border = BorderStroke(2.dp, meta.accentColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(64.dp),
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

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = targetColor.findQuestion(appLanguage),
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isArabic) 26.sp else 22.sp
                                    )
                                )
                                Text(
                                    text = "${targetColor.nameAr} • ${targetColor.nameEn}",
                                    color = SoftSlateText,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Replay",
                                tint = meta.accentColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    // 4 Colored Choices (2x2 Grid)
                    options.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowItems.forEach { item ->
                                val isSel = selectedId == item.id
                                val isTarget = item.id == targetColor.id

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(126.dp)
                                        .testTag("find_color_option_${item.id}")
                                        .shadow(8.dp, RoundedCornerShape(26.dp))
                                        .clickable {
                                            selectedId = item.id
                                            if (isTarget) {
                                                if (isCorrect != true) {
                                                    starsInSession++
                                                    if (starsInSession >= 2) {
                                                        onMarkCompleted()
                                                    }
                                                }
                                                isCorrect = true
                                                onSpeakLulu(targetColor.luluIntro(appLanguage), appLanguage)
                                            } else {
                                                isCorrect = false
                                                val retry = if (isArabic) {
                                                    "حاولي مرة أخرى يا بطلة! ابحثي عن اللون ال${targetColor.nameAr}."
                                                } else {
                                                    "Try again superstar! Look for the color ${targetColor.nameEn}."
                                                }
                                                onSpeakLulu(retry, appLanguage)
                                            }
                                        },
                                    shape = RoundedCornerShape(26.dp),
                                    color = item.colorValue,
                                    border = BorderStroke(
                                        width = if (isSel) 4.dp else 2.5.dp,
                                        color = when {
                                            isSel && isTarget -> MeadowGreen
                                            isSel && !isTarget -> Color(0xFFE53935)
                                            item.isLightColor -> Color(0xFF90A4AE)
                                            else -> Color.White
                                        }
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(text = item.mainEmoji, fontSize = 44.sp)
                                        Text(
                                            text = if (isArabic) item.nameAr else item.nameEn,
                                            color = if (item.isLightColor) DeepNavyText else Color.White,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = isCorrect != null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        val right = isCorrect == true
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = if (right) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                            border = BorderStroke(2.dp, if (right) MeadowGreen else SunnyYellowDark)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (right) {
                                        if (isArabic) "أحسنتِ! هذا اللون ال${targetColor.nameAr} ⭐" else "Great job! That is ${targetColor.nameEn} ⭐"
                                    } else {
                                        if (isArabic) "قريب جداً! جربي لوناً آخر 💪" else "Almost! Try tapping another color 💪"
                                    },
                                    color = DeepNavyText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.weight(1f)
                                )

                                if (right) {
                                    Bouncy3DButton(
                                        text = if (isArabic) "التالي" else "Next",
                                        onClick = { currentRound = (currentRound + 1) % allColors.size },
                                        containerColor = MeadowGreen,
                                        shadowColor = MeadowGreenDark,
                                        contentColor = Color.White,
                                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
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

/**
 * ACTIVITY 3: COLOR MATCHING (طابق الألوان)
 * Match colorful objects with their corresponding colors by tapping.
 */
@Composable
private fun ColorMatchingActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    var roundBatch by remember { mutableIntStateOf(0) }

    val batchColors = remember(roundBatch) {
        val offset = (roundBatch * 4) % ColorsCurriculum.allColors.size
        (0 until 4).map { idx ->
            ColorsCurriculum.allColors[(offset + idx) % ColorsCurriculum.allColors.size]
        }
    }
    val shuffledObjects = remember(roundBatch) { batchColors.shuffled() }

    var selectedColorId by remember(roundBatch) { mutableStateOf<String?>(null) }
    var matchedIds by remember(roundBatch) { mutableStateOf<Set<String>>(emptySet()) }

    val meta = ColorsActivityId.MATCH_COLOR

    LaunchedEffect(roundBatch) {
        val prompt = if (isArabic) {
            "اضغطي على اللون ثم اضغطي على الشكل الذي يحمل نفس اللون!"
        } else {
            "Tap a color jar, then tap the object with the matching color!"
        }
        onSpeakLulu(prompt, appLanguage)
    }

    PlayfulSkyBackground(accentTint = meta.accentColor) {
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
            ColorsActivityTopBar(
                title = meta.title(appLanguage),
                accentColor = meta.accentColor,
                shadowColor = meta.shadowColor,
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, meta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isArabic) {
                            "طابقي كل لون مع الشكل المناسب له (${matchedIds.size} / 4)"
                        } else {
                            "Match each color to its object (${matchedIds.size} / 4)"
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )

                    batchColors.indices.forEach { rowIdx ->
                        val colorItem = batchColors[rowIdx]
                        val objectItem = shuffledObjects[rowIdx]
                        val isColorMatched = colorItem.id in matchedIds
                        val isObjectMatched = objectItem.id in matchedIds
                        val isColorSelected = selectedColorId == colorItem.id

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Color Swatch Button
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(82.dp)
                                    .clickable(enabled = !isColorMatched) {
                                        selectedColorId = colorItem.id
                                        onSpeakLulu(colorItem.name(appLanguage), appLanguage)
                                    },
                                shape = RoundedCornerShape(22.dp),
                                color = if (isColorMatched) Color(0xFFE8F5E9) else colorItem.colorValue,
                                border = BorderStroke(
                                    width = if (isColorSelected) 4.dp else 2.5.dp,
                                    color = when {
                                        isColorMatched -> MeadowGreen
                                        isColorSelected -> SunnyYellowDark
                                        colorItem.isLightColor -> Color(0xFF90A4AE)
                                        else -> Color.White
                                    }
                                ),
                                shadowElevation = 4.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (isColorMatched) {
                                            "✓ ${colorItem.name(appLanguage)}"
                                        } else {
                                            "${colorItem.nameAr} • ${colorItem.nameEn}"
                                        },
                                        color = if (isColorMatched || colorItem.isLightColor) DeepNavyText else Color.White,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    )
                                }
                            }

                            // Object Card Button
                            val objExample = objectItem.matchingObjects.first()
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(82.dp)
                                    .clickable(enabled = !isObjectMatched) {
                                        val sel = selectedColorId
                                        if (sel == null) {
                                            selectedColorId = objectItem.id
                                            onSpeakLulu(objExample.name(appLanguage), appLanguage)
                                        } else if (sel == objectItem.id) {
                                            val updated = matchedIds + objectItem.id
                                            matchedIds = updated
                                            selectedColorId = null
                                            if (updated.size >= 4) {
                                                onMarkCompleted()
                                                onSpeakLulu(
                                                    if (isArabic) "ممتاز يا بطلة! طابقتِ كل الألوان بنجاح!" else "Awesome! You matched all colors!",
                                                    appLanguage
                                                )
                                            } else {
                                                onSpeakLulu(
                                                    if (isArabic) "أحسنتِ! ${objExample.nameAr}" else "Great job! ${objExample.nameEn}",
                                                    appLanguage
                                                )
                                            }
                                        } else {
                                            onSpeakLulu(
                                                if (isArabic) "حاولي مرة أخرى! اختاري الشكل الذي له نفس اللون." else "Try again! Pick the matching colored object.",
                                                appLanguage
                                            )
                                        }
                                    },
                                shape = RoundedCornerShape(22.dp),
                                color = if (isObjectMatched) Color(0xFFE8F5E9) else objectItem.cardBgColor,
                                border = BorderStroke(
                                    width = 2.5.dp,
                                    color = if (isObjectMatched) MeadowGreen else objectItem.colorValue
                                ),
                                shadowElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(text = objExample.emoji, fontSize = 34.sp)
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(
                                        text = objExample.name(appLanguage),
                                        color = DeepNavyText,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    if (matchedIds.size >= 4) {
                        Bouncy3DButton(
                            text = if (isArabic) "مجموعة ألوان جديدة 🌟" else "More Colors 🌟",
                            onClick = { roundBatch++ },
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.Filled.Refresh
                        )
                    }
                }
            }
        }
    }
}

private data class ColoredStroke(
    val points: List<Offset>,
    val color: Color,
    val isEraser: Boolean
)

/**
 * ACTIVITY 4: INTERACTIVE COLORING STUDIO (مرسم التلوين المرح)
 * - Allows the child to color simple drawings (Butterfly, Cozy House, Happy Flower)
 *   both by tapping regions AND by finger painting on the Canvas
 * - Uses all 10 colors in a touch-friendly palette
 * - Includes Eraser, Clear button, and Reset option
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InteractiveColoringActivityScreen(
    appLanguage: AppLanguage,
    soundEnabled: Boolean,
    isCompleted: Boolean,
    onBackToHub: () -> Unit,
    onNavigateHome: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakLulu: (String, AppLanguage) -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.AR
    val allColors = ColorsCurriculum.allColors

    var drawingTemplateIdx by remember { mutableIntStateOf(0) } // 0 = Butterfly, 1 = House, 2 = Flower
    var selectedColor by remember { mutableStateOf(allColors.first()) }
    var isEraserActive by remember { mutableStateOf(false) }

    // Tap-to-fill region colors (5 regions per template)
    val regionColors = remember(drawingTemplateIdx) {
        mutableStateMapOf<Int, Color>()
    }
    // Freehand finger strokes on the canvas
    var strokes by remember(drawingTemplateIdx) {
        mutableStateOf<List<ColoredStroke>>(emptyList())
    }
    var hasAwardedCompletion by remember { mutableStateOf(false) }

    val meta = ColorsActivityId.COLORING

    LaunchedEffect(Unit) {
        onSpeakLulu(
            if (isArabic) "أمسكي الفرشاة ولوّني الرسمة بأجمل الألوان!" else "Pick a color from the palette and color the drawing!",
            appLanguage
        )
    }

    PlayfulSkyBackground(accentTint = meta.accentColor) {
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
            ColorsActivityTopBar(
                title = meta.title(appLanguage),
                accentColor = meta.accentColor,
                shadowColor = meta.shadowColor,
                isArabic = isArabic,
                soundEnabled = soundEnabled,
                isCompleted = isCompleted,
                onBackToHub = onBackToHub,
                onNavigateHome = onNavigateHome,
                onToggleSound = onToggleSound
            )

            // Template Selector Row (Butterfly, House, Flower)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    0 to (if (isArabic) "🦋 فراشة" else "🦋 Butterfly"),
                    1 to (if (isArabic) "🏠 بيت جميل" else "🏠 Cozy House"),
                    2 to (if (isArabic) "🌸 زهرة" else "🌸 Flower")
                ).forEach { (idx, label) ->
                    val selected = drawingTemplateIdx == idx
                    Bouncy3DButton(
                        text = label,
                        onClick = { drawingTemplateIdx = idx },
                        modifier = Modifier.weight(1f),
                        containerColor = if (selected) meta.accentColor else Color.White,
                        shadowColor = if (selected) meta.shadowColor else Color(0xFFB0BEC5),
                        contentColor = if (selected) Color.White else DeepNavyText,
                        fontSize = 15.sp,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, meta.accentColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isArabic) {
                            "اضغطي على أجزاء الرسمة أو اسحبي بإصبعكِ للتلوين!"
                        } else {
                            "Tap drawing parts to fill or drag your finger to paint!"
                        },
                        color = DeepNavyText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )

                    // Interactive Coloring Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(290.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color(0xFFFFFDF9))
                            .border(3.dp, meta.accentColor, RoundedCornerShape(26.dp))
                            .testTag("coloring_canvas")
                            .pointerInput(drawingTemplateIdx, selectedColor.id, isEraserActive) {
                                detectTapGestures { offset ->
                                    val w = size.width.toFloat()
                                    val h = size.height.toFloat()
                                    val hitRegion = findColoringRegion(drawingTemplateIdx, offset, Size(w, h))
                                    if (hitRegion != null) {
                                        if (isEraserActive) {
                                            regionColors.remove(hitRegion)
                                        } else {
                                            regionColors[hitRegion] = selectedColor.colorValue
                                            if (!hasAwardedCompletion && regionColors.size >= 2) {
                                                hasAwardedCompletion = true
                                                onMarkCompleted()
                                                onSpeakLulu(
                                                    if (isArabic) "لوحة فنية رائعة يا بطلة!" else "What a beautiful masterpiece!",
                                                    appLanguage
                                                )
                                            }
                                        }
                                    } else {
                                        val paintCol = if (isEraserActive) Color(0xFFFFFDF9) else selectedColor.colorValue
                                        strokes = strokes + ColoredStroke(listOf(offset), paintCol, isEraserActive)
                                    }
                                }
                            }
                            .pointerInput(drawingTemplateIdx, selectedColor.id, isEraserActive) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        val paintCol = if (isEraserActive) Color(0xFFFFFDF9) else selectedColor.colorValue
                                        strokes = strokes + ColoredStroke(listOf(offset), paintCol, isEraserActive)
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()
                                        if (strokes.isNotEmpty()) {
                                            val last = strokes.last()
                                            val updated = last.copy(points = last.points + change.position)
                                            strokes = strokes.dropLast(1) + updated
                                            if (!hasAwardedCompletion && strokes.size >= 2) {
                                                hasAwardedCompletion = true
                                                onMarkCompleted()
                                            }
                                        }
                                    }
                                )
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val cx = w / 2f
                            val cy = h / 2f
                            val outlineStroke = Stroke(width = 7f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            val outlineColor = Color(0xFF263238)

                            // 1. Draw Template Regions (Tap-to-Fill)
                            when (drawingTemplateIdx) {
                                0 -> {
                                    // Butterfly: 0=TopLeftWing, 1=TopRightWing, 2=BottomLeftWing, 3=BottomRightWing, 4=Body
                                    val r0 = regionColors[0] ?: Color.White
                                    val r1 = regionColors[1] ?: Color.White
                                    val r2 = regionColors[2] ?: Color.White
                                    val r3 = regionColors[3] ?: Color.White
                                    val r4 = regionColors[4] ?: Color(0xFFFFF8E1)

                                    drawCircle(r0, radius = h * 0.24f, center = Offset(cx - h * 0.26f, cy - h * 0.14f))
                                    drawCircle(outlineColor, radius = h * 0.24f, center = Offset(cx - h * 0.26f, cy - h * 0.14f), style = outlineStroke)

                                    drawCircle(r1, radius = h * 0.24f, center = Offset(cx + h * 0.26f, cy - h * 0.14f))
                                    drawCircle(outlineColor, radius = h * 0.24f, center = Offset(cx + h * 0.26f, cy - h * 0.14f), style = outlineStroke)

                                    drawCircle(r2, radius = h * 0.18f, center = Offset(cx - h * 0.20f, cy + h * 0.20f))
                                    drawCircle(outlineColor, radius = h * 0.18f, center = Offset(cx - h * 0.20f, cy + h * 0.20f), style = outlineStroke)

                                    drawCircle(r3, radius = h * 0.18f, center = Offset(cx + h * 0.20f, cy + h * 0.20f))
                                    drawCircle(outlineColor, radius = h * 0.18f, center = Offset(cx + h * 0.20f, cy + h * 0.20f), style = outlineStroke)

                                    drawRoundRect(
                                        color = r4,
                                        topLeft = Offset(cx - 22f, cy - h * 0.30f),
                                        size = Size(44f, h * 0.62f),
                                        cornerRadius = CornerRadius(22f, 22f)
                                    )
                                    drawRoundRect(
                                        color = outlineColor,
                                        topLeft = Offset(cx - 22f, cy - h * 0.30f),
                                        size = Size(44f, h * 0.62f),
                                        cornerRadius = CornerRadius(22f, 22f),
                                        style = outlineStroke
                                    )
                                }

                                1 -> {
                                    // Cozy House: 0=Roof, 1=Walls, 2=Door, 3=Sun, 4=Window
                                    val roofCol = regionColors[0] ?: Color.White
                                    val wallCol = regionColors[1] ?: Color.White
                                    val doorCol = regionColors[2] ?: Color.White
                                    val sunCol = regionColors[3] ?: Color.White

                                    drawCircle(sunCol, radius = 38f, center = Offset(w * 0.18f, h * 0.22f))
                                    drawCircle(outlineColor, radius = 38f, center = Offset(w * 0.18f, h * 0.22f), style = outlineStroke)

                                    drawRect(
                                        color = wallCol,
                                        topLeft = Offset(cx - 110f, cy - 10f),
                                        size = Size(220f, 150f)
                                    )
                                    drawRect(
                                        color = outlineColor,
                                        topLeft = Offset(cx - 110f, cy - 10f),
                                        size = Size(220f, 150f),
                                        style = outlineStroke
                                    )

                                    val roofPath = Path().apply {
                                        moveTo(cx, cy - 115f)
                                        lineTo(cx - 135f, cy - 10f)
                                        lineTo(cx + 135f, cy - 10f)
                                        close()
                                    }
                                    drawPath(roofPath, color = roofCol)
                                    drawPath(roofPath, color = outlineColor, style = outlineStroke)

                                    drawRect(
                                        color = doorCol,
                                        topLeft = Offset(cx - 30f, cy + 50f),
                                        size = Size(60f, 90f)
                                    )
                                    drawRect(
                                        color = outlineColor,
                                        topLeft = Offset(cx - 30f, cy + 50f),
                                        size = Size(60f, 90f),
                                        style = outlineStroke
                                    )
                                }

                                else -> {
                                    // Flower: 0..3 = Petals, 4 = Center
                                    val p0 = regionColors[0] ?: Color.White
                                    val p1 = regionColors[1] ?: Color.White
                                    val p2 = regionColors[2] ?: Color.White
                                    val p3 = regionColors[3] ?: Color.White
                                    val cCol = regionColors[4] ?: Color.White

                                    val d = h * 0.22f
                                    val pr = h * 0.16f
                                    drawCircle(p0, pr, Offset(cx, cy - d))
                                    drawCircle(outlineColor, pr, Offset(cx, cy - d), style = outlineStroke)

                                    drawCircle(p1, pr, Offset(cx, cy + d))
                                    drawCircle(outlineColor, pr, Offset(cx, cy + d), style = outlineStroke)

                                    drawCircle(p2, pr, Offset(cx - d, cy))
                                    drawCircle(outlineColor, pr, Offset(cx - d, cy), style = outlineStroke)

                                    drawCircle(p3, pr, Offset(cx + d, cy))
                                    drawCircle(outlineColor, pr, Offset(cx + d, cy), style = outlineStroke)

                                    drawCircle(cCol, h * 0.16f, Offset(cx, cy))
                                    drawCircle(outlineColor, h * 0.16f, Offset(cx, cy), style = outlineStroke)
                                }
                            }

                            // 2. Freehand Brush Strokes
                            strokes.forEach { stroke ->
                                if (stroke.points.size == 1) {
                                    drawCircle(
                                        color = stroke.color,
                                        radius = 18f,
                                        center = stroke.points.first()
                                    )
                                } else if (stroke.points.size > 1) {
                                    val path = Path().apply {
                                        moveTo(stroke.points.first().x, stroke.points.first().y)
                                        for (i in 1 until stroke.points.size) {
                                            lineTo(stroke.points[i].x, stroke.points[i].y)
                                        }
                                    }
                                    drawPath(
                                        path = path,
                                        color = stroke.color,
                                        style = Stroke(width = 32f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                    )
                                }
                            }
                        }
                    }

                    // 10-Color Touch Palette
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        allColors.forEach { item ->
                            val isSelected = !isEraserActive && selectedColor.id == item.id
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.15f else 1f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "palette_scale_${item.id}"
                            )
                            Surface(
                                modifier = Modifier
                                    .size(48.dp)
                                    .scale(scale)
                                    .testTag("coloring_palette_${item.id}")
                                    .clickable {
                                        isEraserActive = false
                                        selectedColor = item
                                        onSpeakLulu(item.name(appLanguage), appLanguage)
                                    },
                                shape = CircleShape,
                                color = item.colorValue,
                                border = BorderStroke(
                                    width = if (isSelected) 3.5.dp else 2.dp,
                                    color = if (isSelected) DeepNavyText else Color(0xFF90A4AE)
                                ),
                                shadowElevation = if (isSelected) 6.dp else 2.dp
                            ) {}
                        }
                    }

                    // Eraser, Clear & Reset Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Bouncy3DButton(
                            text = if (isArabic) "ممحاة" else "Eraser",
                            onClick = { isEraserActive = !isEraserActive },
                            modifier = Modifier.weight(1f),
                            containerColor = if (isEraserActive) SunnyYellow else Color(0xFFECEFF1),
                            shadowColor = if (isEraserActive) SunnyYellowDark else Color(0xFF90A4AE),
                            contentColor = DeepNavyText,
                            icon = Icons.Filled.AutoFixNormal,
                            fontSize = 14.sp,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                            testTagName = "coloring_eraser_btn"
                        )

                        Bouncy3DButton(
                            text = if (isArabic) "مسح" else "Clear",
                            onClick = {
                                strokes = emptyList()
                                regionColors.clear()
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = Color(0xFFFFECEF),
                            shadowColor = LuluPinkDark,
                            contentColor = DeepNavyText,
                            icon = Icons.Filled.DeleteSweep,
                            fontSize = 14.sp,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                            testTagName = "coloring_clear_btn"
                        )

                        Bouncy3DButton(
                            text = if (isArabic) "إعادة ضبط" else "Reset",
                            onClick = {
                                strokes = emptyList()
                                regionColors.clear()
                                isEraserActive = false
                                selectedColor = allColors.first()
                            },
                            modifier = Modifier.weight(1f),
                            containerColor = MeadowGreen,
                            shadowColor = MeadowGreenDark,
                            contentColor = Color.White,
                            icon = Icons.Filled.Refresh,
                            fontSize = 14.sp,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                            testTagName = "coloring_reset_btn"
                        )
                    }
                }
            }
        }
    }
}

private fun findColoringRegion(templateIdx: Int, pt: Offset, size: Size): Int? {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val h = size.height
    return when (templateIdx) {
        0 -> {
            when {
                hypot(pt.x - cx, pt.y - cy) <= 35f -> 4
                hypot(pt.x - (cx - h * 0.26f), pt.y - (cy - h * 0.14f)) <= h * 0.24f -> 0
                hypot(pt.x - (cx + h * 0.26f), pt.y - (cy - h * 0.14f)) <= h * 0.24f -> 1
                hypot(pt.x - (cx - h * 0.20f), pt.y - (cy + h * 0.20f)) <= h * 0.18f -> 2
                hypot(pt.x - (cx + h * 0.20f), pt.y - (cy + h * 0.20f)) <= h * 0.18f -> 3
                else -> null
            }
        }
        1 -> {
            when {
                hypot(pt.x - (size.width * 0.18f), pt.y - (h * 0.22f)) <= 45f -> 3
                pt.y in (cy - 115f)..(cy - 10f) && pt.x in (cx - 135f)..(cx + 135f) -> 0
                pt.x in (cx - 30f)..(cx + 30f) && pt.y in (cy + 50f)..(cy + 140f) -> 2
                pt.x in (cx - 110f)..(cx + 110f) && pt.y in (cy - 10f)..(cy + 140f) -> 1
                else -> null
            }
        }
        else -> {
            val d = h * 0.22f
            val pr = h * 0.18f
            when {
                hypot(pt.x - cx, pt.y - cy) <= h * 0.16f -> 4
                hypot(pt.x - cx, pt.y - (cy - d)) <= pr -> 0
                hypot(pt.x - cx, pt.y - (cy + d)) <= pr -> 1
                hypot(pt.x - (cx - d), pt.y - cy) <= pr -> 2
                hypot(pt.x - (cx + d), pt.y - cy) <= pr -> 3
                else -> null
            }
        }
    }
}
